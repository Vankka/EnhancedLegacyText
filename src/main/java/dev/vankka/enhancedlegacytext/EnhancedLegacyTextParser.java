/*
 * MIT License
 *
 * Copyright (c) 2021-2023 Vankka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vankka.enhancedlegacytext;

import net.kyori.adventure.text.*;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.*;

import java.awt.Color;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dev.vankka.enhancedlegacytext.ParseContext.SquareBracketStatus.*;

/**
 * The parser, not thread safe.
 */
public class EnhancedLegacyTextParser {

    protected static final ThreadLocal<EnhancedLegacyTextParser> PARSERS = ThreadLocal.withInitial(EnhancedLegacyTextParser::new);

    private static final char ESCAPE = '\\';
    private static final char SQUARE_BRACKET_START = '[';
    private static final char SQUARE_BRACKET_DELIMITER = ':';
    private static final char SQUARE_BRACKET_END = ']';
    private static final char GRADIENT_START = '{';
    private static final char GRADIENT_DELIMITER = ',';
    private static final char GRADIENT_END = '}';
    private static final char HEX = '#';

    // Hover event type
    private static final String SHOW_TEXT = "show_text";

    // Color namespaces
    private static final String NAMESPACE_MINECRAFT = "minecraft";
    private static final String NAMESPACE_CSS = "css";
    private static final String NAMESPACE_HEX = "hex";

    // Square brackets prefixes
    private static final List<Pair<String, Consumer<ParseContext>>> STATUS_TRANSITIONS = new ArrayList<>();

    // Formats
    private static final Map<String, TextDecoration> DECORATIONS = new HashMap<>(7);

    // Click events
    private static final List<Pair<String, ClickEvent.Action>> ACCEPTABLE_CLICK_EVENTS = new ArrayList<>();

    static {
        STATUS_TRANSITIONS.add(new Pair<>("color", ctx -> ctx.squareBracketStatus = COLOR));
        STATUS_TRANSITIONS.add(new Pair<>("click", ctx -> ctx.squareBracketStatus = CLICK_TYPE));
        STATUS_TRANSITIONS.add(new Pair<>("hover", ctx -> ctx.squareBracketStatus = HOVER_TYPE));

        for (TextDecoration value : TextDecoration.values()) {
            DECORATIONS.put(value.name().toLowerCase(Locale.ROOT), value);
        }
        DECORATIONS.put("italics", TextDecoration.ITALIC);
        DECORATIONS.put("underline", TextDecoration.UNDERLINED);

        for (Map.Entry<String, TextDecoration> decoration : DECORATIONS.entrySet()) {
            STATUS_TRANSITIONS.add(new Pair<>(decoration.getKey(), ctx -> {
                ctx.squareBracketStatus = DECORATION;
                ctx.squareBracketContext[0].append(decoration.getKey());
            }));
        }

        for (ClickEvent.Action value : ClickEvent.Action.values()) {
            if (value == ClickEvent.Action.OPEN_FILE) {
                // Client side only
                continue;
            }
            ACCEPTABLE_CLICK_EVENTS.add(new Pair<>(value.name().toLowerCase(), value));
        }
    }

    private char colorChar;
    private boolean colorResets;
    private boolean legacy;
    private boolean adventureHex;
    private RecursiveReplacement recursiveReplacement;
    private ParseContext ctx;
    private ParseContext contextCopy;

    private EnhancedLegacyTextParser() {}

    public Component parseToComponent(
            char colorChar,
            boolean colorResets,
            boolean legacy,
            boolean adventureHex,
            String input,
            List<Pair<Pattern, Function<Matcher, Object>>> replacements,
            RecursiveReplacement recursiveReplacement
    ) {
        this.colorChar = colorChar;
        this.colorResets = colorResets;
        this.legacy = legacy;
        this.adventureHex = adventureHex;
        this.recursiveReplacement = recursiveReplacement;

        ParseContext contextBeforeParse = ctx;
        ParseContext contextCopyBeforeParse = contextCopy;
        this.ctx = new ParseContext();
        this.contextCopy = null;

        processPlaceholders(input, replacements);
        Component output = out(false);

        ctx = contextBeforeParse;
        contextCopy = contextCopyBeforeParse;
        return output;
    }

    private Component out(boolean skipStatusCheck) {
        if (!skipStatusCheck) {
            if (contextCopy != null) {
                ctx = contextCopy;
                contextCopy = null;
                rollback();
            } else if (ctx.squareBracketStatus != NONE) {
                rollback();
            }
        }

        // Append remaining content
        appendContent(
                true,
                false,
                false
        );

        // Simplify the output component if possible
        List<Component> rootChildren = ctx.rootBuilder.children();
        return rootChildren.size() == 1 ? rootChildren.get(0) : ctx.rootBuilder.build();
    }

    private void bufferForRollback(char c) {
        ctx.rollbackBuffer.append(c);
    }

    private void rollback() {
        if (ctx.gradient) {
            ctx.gradientColors.clear();
        }
        reset();

        if (ctx.rollbackBuffer.length() == 0) {
            return;
        }

        // First character as normal content
        ctx.content.append(ctx.rollbackBuffer.charAt(0));

        String end = ctx.rollbackBuffer.substring(1);
        ctx.rollbackBuffer.setLength(0);

        // Rest parsed one by one
        for (char c : end.toCharArray()) {
            parseCharacter(c);
        }
    }

    private void reset() {
        ctx.squareBracketStatus = NONE;
        ctx.squareBracketPrefix.setLength(0);
        for (int i = 0; i < ctx.squareBracketContext.length; i++) {
            ctx.squareBracketContext[i].setLength(0);
        }

        ctx.color = false;
        ctx.hexColor = false;
        Arrays.fill(ctx.hex, Character.MIN_VALUE);

        ctx.gradient = false;
        ctx.gradientDelimiter = false;
    }

    private void parse(String parseIn) {
        for (char c : parseIn.toCharArray()) {
            parseCharacter(c);
        }

        if (ctx.content.length() > 0 && ctx.squareBracketStatus == NONE) {
            appendContent(false, true, false);
        }
    }

    private void parseCharacter(char c) {
        if (contextCopy != null) {
            contextCopy.rollbackBuffer.append(c);
        }

        boolean escape = ctx.escape;
        if (escape) {
            ctx.escape = false;
        } else if (c == ESCAPE) {
            ctx.escape = true;
            return;
        }

        // Square brackets
        ParseContext.SquareBracketStatus squareBracketStatus = ctx.squareBracketStatus;
        if (squareBracketStatus != NONE) {
            bufferForRollback(c);

            if (c == SQUARE_BRACKET_END && !escape && (squareBracketStatus == HOVER_TYPE || squareBracketStatus == CLICK_TYPE)) {
                rollback();
                return;
            }

            // Undo hover/click/color
            if (c == SQUARE_BRACKET_END && !escape && squareBracketStatus == PREFIX) {
                String buffer = ctx.squareBracketPrefix.toString();
                switch (buffer) {
                    case "hover": {
                        if (!ctx.newChild.get()) {
                            appendContent(true, true, false);
                        }
                        ctx.hoverEvent = null;
                        break;
                    }
                    case "click": {
                        if (!ctx.newChild.get()) {
                            appendContent(true, true, false);
                        }
                        ctx.clickEvent = null;
                        break;
                    }
                    case "color": {
                        if (!ctx.newChild.get()) {
                            appendContent(true, true, false);
                        }
                        colorize(null);
                        break;
                    }
                    default: {
                        TextColor color = parseColor(null, buffer);
                        if (color != null) {
                            applyColor(color);
                            return;
                        }

                        TextDecoration decoration = DECORATIONS.get(buffer);
                        if (decoration != null) {
                            decorate(decoration, false);
                            reset();
                            return;
                        }

                        rollback();
                        return;
                    }
                }

                reset();
                ctx.rollbackBuffer.setLength(0);
                return;
            }

            if (squareBracketStatus == PREFIX) {
                String buffer = ctx.squareBracketPrefix.toString();
                if (c == SQUARE_BRACKET_DELIMITER && !escape) {
                    for (Pair<String, Consumer<ParseContext>> transition : STATUS_TRANSITIONS) {
                        String key = transition.getKey();
                        if (contextCopy != null && (key.equals("click") || key.equals("hover"))) {
                            continue;
                        }

                        if (buffer.equals(key)) {
                            ctx.squareBracketPrefix.setLength(0);
                            transition.getValue().accept(ctx);
                            return;
                        }
                    }

                    rollback();
                    return;
                }

                ctx.squareBracketPrefix.append(c);
                return;
            }

            boolean hover;
            if ((hover = squareBracketStatus == HOVER_TYPE) || squareBracketStatus == CLICK_TYPE) {
                if (c == SQUARE_BRACKET_DELIMITER && !escape) {
                    String buffer = ctx.squareBracketContext[0].toString();

                    if (hover) {
                        if (buffer.equals(SHOW_TEXT)) {
                            clearExistingContent();

                            ctx.squareBracketStatus = HOVER_VALUE;
                            contextCopy = ctx;

                            ctx = new ParseContext();
                            return;
                        }
                    } else /* click */ {
                        for (Pair<String, ClickEvent.Action> event : ACCEPTABLE_CLICK_EVENTS) {
                            String match = event.getKey();
                            if (match.equals(buffer)) {
                                ctx.squareBracketStatus = CLICK_VALUE;
                                return;
                            }
                        }
                    }

                    rollback();
                    return;
                }

                ctx.squareBracketContext[0].append(c);
                return;
            }

            if ((hover = squareBracketStatus == HOVER_VALUE) || squareBracketStatus == CLICK_VALUE) {
                if (c == SQUARE_BRACKET_END && !escape) {
                    String type = ctx.squareBracketContext[0].toString();
                    String valueBuffer = ctx.squareBracketContext[1].toString();

                    if (hover) {
                        throw new IllegalStateException("Impossible hover type: " + type);
                    } else /* click */ {
                        ClickEvent.Action action = null;
                        for (Pair<String, ClickEvent.Action> event : ACCEPTABLE_CLICK_EVENTS) {
                            if (event.getKey().equals(type)) {
                                action = event.getValue();
                                break;
                            }
                        }
                        if (action == null) {
                            throw new IllegalStateException("Impossible click type: " + type);
                        }

                        if (!ctx.newChild.get()) {
                            // Clear up the existing text buffer first
                            appendContent(false, true, true);
                        }

                        ctx.clickEvent = ClickEvent.clickEvent(action, valueBuffer);
                    }

                    reset();
                    ctx.rollbackBuffer.setLength(0);
                    return;
                }

                ctx.squareBracketContext[1].append(c);
                return;
            }

            boolean namespaced;
            if ((namespaced = squareBracketStatus == COLOR_NAMESPACED) || squareBracketStatus == COLOR) {
                if (!namespaced && c == SQUARE_BRACKET_DELIMITER && !escape) {
                    String buffer = ctx.squareBracketContext[0].toString();
                    if (!buffer.equals(NAMESPACE_MINECRAFT) && !buffer.equals(NAMESPACE_CSS) && !buffer.equals(NAMESPACE_HEX)) {
                        rollback();
                        return;
                    }

                    ctx.squareBracketStatus = COLOR_NAMESPACED;
                    return;
                }
                if (!namespaced && c == HEX && !escape) {
                    if (ctx.squareBracketContext[0].length() > 0) {
                        rollback();
                        return;
                    }

                    ctx.squareBracketContext[0].append(NAMESPACE_HEX);
                    ctx.squareBracketStatus = COLOR_NAMESPACED;
                    return;
                }

                if (c == SQUARE_BRACKET_END && !escape) {
                    String namespace = namespaced ? ctx.squareBracketContext[0].toString() : null;
                    String name = ctx.squareBracketContext[namespaced ? 1 : 0].toString();

                    TextColor color = parseColor(namespace, name);
                    if (color == null) {
                        rollback();
                        return;
                    }

                    applyColor(color);
                    return;
                }

                ctx.squareBracketContext[namespaced ? 1 : 0].append(c);
                return;
            }

            if (squareBracketStatus == DECORATION) {
                if (c == SQUARE_BRACKET_END && !escape) {
                    String buffer = ctx.squareBracketContext[0].toString();
                    TextDecoration decoration = DECORATIONS.get(buffer);
                    if (decoration == null) {
                        throw new IllegalStateException("Impossible decoration: " + buffer);
                    }

                    boolean booleanValue;
                    String value = ctx.squareBracketContext[1].toString();
                    switch (value) {
                        case "true":
                        case "on":
                            booleanValue = true;
                            break;
                        case "false":
                        case "off":
                            booleanValue = false;
                            break;
                        default:
                            rollback();
                            return;
                    }

                    decorate(decoration, booleanValue);
                    reset();
                    return;
                }

                ctx.squareBracketContext[1].append(c);
                return;
            }

            throw new IllegalStateException("Unexpected SquareBracketStatus: " + squareBracketStatus);
        } else if (c == SQUARE_BRACKET_START && !escape) {
            bufferForRollback(c);
            ctx.squareBracketStatus = PREFIX;
            return;
        }

        if (c == SQUARE_BRACKET_END && contextCopy != null) {
            Component component = out(true);

            ctx = contextCopy;
            contextCopy = null;

            ctx.hoverEvent = HoverEvent.showText(component);
            reset();
            ctx.rollbackBuffer.setLength(0);
            return;
        }

        if (ctx.color) {
            bufferForRollback(c);
            if (c == HEX && !escape && adventureHex) {
                ctx.hexColor = true;
                return;
            }

            if (ctx.hexColor) {
                for (int i = 0; i < 6; i++) {
                    if (ctx.hex[i] == Character.MIN_VALUE) {
                        char character = Character.toLowerCase(c);
                        if (!Colors.HEX_CHARACTERS.contains(Character.toString(character))) {
                            rollback();
                            return;
                        }

                        ctx.hex[i] = character;
                        if (i != 5) {
                            return;
                        }
                    }
                }

                TextColor color = TextColor.fromHexString("#" + new String(ctx.hex));
                ctx.color = false;
                ctx.hexColor = false;

                if (ctx.gradient) {
                    if (!ctx.newChild.get()) {
                        // Clear up the existing text buffer first
                        appendContent(false, true, false);
                    }
                    ctx.gradientColors.add(color);
                    ctx.gradientDelimiter = true;
                    Arrays.fill(ctx.hex, Character.MIN_VALUE);
                } else {
                    colorize(color);
                    reset();
                }
            } else {
                if (!legacy) {
                    rollback();
                    return;
                }

                TextFormat legacy = Colors.LEGACY.get(c);
                if (legacy == null || (ctx.gradient && !(legacy instanceof TextColor))) {
                    rollback();
                    return;
                }

                if (legacy == Colors.RESET) {
                    appendContent(
                            true,
                            true,
                            false
                    );
                    ctx.gradientColors.clear();
                    ctx.clickEvent = null;
                    ctx.hoverEvent = null;
                } else if (legacy instanceof TextColor) {
                    TextColor color = (TextColor) legacy;
                    if (ctx.gradient) {
                        ctx.gradientColors.add(color);
                        ctx.gradientDelimiter = true;
                        ctx.color = false;
                        return;
                    } else {
                        ctx.gradientColors.clear();
                        colorize(color);
                        reset();
                        ctx.rollbackBuffer.setLength(0);
                    }
                } else if (legacy instanceof TextDecoration) {
                    decorate((TextDecoration) legacy, true);
                } else {
                    throw new IllegalStateException(legacy.getClass().getName() + " is not a known TextFormat");
                }
                reset();
                ctx.rollbackBuffer.setLength(0);
            }
            return;
        }
        if (!ctx.gradientDelimiter && c == colorChar && !escape) {
            bufferForRollback(c);
            ctx.color = true;
            return;
        }
        if (ctx.gradient && ctx.gradientDelimiter && c == GRADIENT_END && !escape) {
            ctx.gradient = false;
            ctx.rollbackBuffer.setLength(0);
            return;
        }
        if (ctx.gradientDelimiter) {
            bufferForRollback(c);
            ctx.gradientDelimiter = false;
            if (c != GRADIENT_DELIMITER || escape) {
                rollback();
            }
            return;
        }
        if (c == GRADIENT_START && !escape) {
            bufferForRollback(c);
            ctx.gradient = true;
            return;
        }

        ctx.content.append(c);
        ctx.newChild.set(false);
    }

    private void clearExistingContent() {
        if (!ctx.newChild.get()) {
            // Clear up the existing text buffer first
            appendContent(false, true, true);
        }
    }

    private void applyColor(TextColor color) {
        if (ctx.gradient) {
            if (!ctx.newChild.get()) {
                // Clear up the existing text buffer first
                appendContent(false, true, false);
            }
            ctx.gradientColors.add(color);
            ctx.gradientDelimiter = true;

            ctx.squareBracketStatus = NONE;
            ctx.squareBracketPrefix.setLength(0);
            for (int i = 0; i < ctx.squareBracketContext.length; i++) {
                ctx.squareBracketContext[i].setLength(0);
            }
        } else {
            colorize(color);
            reset();
        }
    }

    private TextColor parseColor(String namespace, String name) {
        boolean namespaced = namespace != null;

        TextColor color = null;
        if (!namespaced || NAMESPACE_MINECRAFT.equals(namespace)) {
            color = NamedTextColor.NAMES.value(name);
        }
        if (color == null && (!namespaced || NAMESPACE_CSS.equals(namespace))) {
            color = Colors.CSS.get(name);
        }

        if (color == null && (!namespaced || NAMESPACE_HEX.equals(namespace))) {
            if (name.startsWith("#")) {
                name = name.substring(1);
            }

            int length;
            if ((length = name.length()) == 3 || name.length() == 6) {
                boolean isHex = true;
                name = name.toLowerCase(Locale.ROOT);
                for (int i = 0; i < name.length(); i++) {
                    if (!Colors.HEX_CHARACTERS.contains(Character.toString(name.charAt(i)))) {
                        isHex = false;
                        break;
                    }
                }
                if (isHex) {
                    if (length == 3) {
                        int c1 = Character.digit(name.charAt(0), 16);
                        int c2 = Character.digit(name.charAt(1), 16);
                        int c3 = Character.digit(name.charAt(2), 16);

                        int rgb = (c3 + (c3 << 4))
                                + ((c2 + (c2 << 4)) << 8)
                                + ((c1 + (c1 << 4)) << 16);
                        color = TextColor.color(rgb);
                    } else {
                        int rgb = Integer.parseInt(name, 16);
                        color = TextColor.color(rgb);
                    }
                }
            }
        }

        return color;
    }

    private void colorize(TextColor textColor) {
        if (colorResets || !ctx.newChild.get()) {
            appendContent(
                    colorResets,
                    true,
                    false
            );
            if (colorResets) {
                ctx.newChild.set(true);
            }
        }
        ctx.current.color(textColor);
    }

    private void decorate(TextDecoration decoration, boolean state) {
        if (ctx.newChild.get()) {
            ctx.current.decoration(decoration, state);
        } else {
            appendContent(false, true, false);
            ctx.current.decoration(decoration, state);
        }
    }

    private void appendContent(
            boolean toRoot,
            boolean allowEmpty,
            boolean noGradients
    ) {
        StringBuilder contentBuilder = ctx.content;
        List<TextColor> gradientColors = noGradients ? Collections.emptyList() : ctx.gradientColors;
        ClickEvent clickEvent = ctx.clickEvent;
        HoverEvent<?> hoverEvent = ctx.hoverEvent;

        if (gradientColors.size() > 1 && contentBuilder.length() > 0) {
            addIfNotEmpty(ctx.current, ctx.builders);
            ctx.current = Component.text();

            Gradient gradient = new Gradient(gradientColors, contentBuilder.length());
            char[] inputText = contentBuilder.toString().toCharArray();
            int index = 0;
            for (TextColor color : gradient.colors()) {
                char character = inputText[index++];
                ctx.current.append(Component.text(character).color(color));
            }
            gradientColors.clear();
        } else {
            ctx.current.content(ctx.current.content() + contentBuilder);
        }
        contentBuilder.setLength(0);

        if (hoverEvent != null) {
            ctx.current.hoverEvent(hoverEvent);
        }
        if (clickEvent != null) {
            ctx.current.clickEvent(clickEvent);
        }

        if (allowEmpty || ctx.current.content().length() > 0 || !ctx.current.children().isEmpty()) {
            addIfNotEmpty(ctx.current, ctx.builders);
        }
        if (toRoot) {
            ctx.rootBuilder.append(collapse(ctx.builders));
            ctx.builders.clear();
        }
        ctx.current = Component.text();
        ctx.newChild.set(true);
    }

    private void processPlaceholders(String input, List<Pair<Pattern, Function<Matcher, Object>>> replacements) {
        boolean anyMatch = false;
        String suffix = null;

        for (int i = 0; i < replacements.size(); i++) {
            Pair<Pattern, Function<Matcher, Object>> replacementEntry = replacements.get(i);
            Pattern pattern = replacementEntry.getKey();

            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                ctx.content.setLength(0);
                int start = matcher.start();
                int end = matcher.end();

                String prefix = start == 0 ? null : input.substring(0, start);
                suffix = end == input.length() ? null : input.substring(end);
                if (prefix != null) {
                    processPlaceholders(prefix, replacements);
                }

                Object replacement = replacementEntry.getValue().apply(matcher);
                if (replacement instanceof Color) {
                    // Convert java.awt.Color to TextColor
                    Color color = (Color) replacement;
                    replacement = TextColor.color(color.getRed(), color.getGreen(), color.getBlue());
                }

                if (replacement instanceof ComponentLike) {
                    ctx.builders.add(replacement instanceof BuildableComponent
                                 ? ((BuildableComponent<?, ?>) replacement).toBuilder()
                                 : Component.text().append((ComponentLike) replacement)
                    );
                    ctx.newChild.set(false);

                    anyMatch = true;
                } else if (replacement instanceof TextFormat || replacement instanceof Style) {
                    addIfNotEmpty(ctx.current, ctx.builders);
                    ctx.current = Component.text();
                    ctx.newChild.set(true);

                    if (replacement instanceof TextColor || replacement instanceof Style) {
                        TextColor color;
                        if (replacement instanceof Style) {
                            Style style = (Style) replacement;
                            ctx.current.style(style);
                            color = style.color();
                        } else {
                            color = (TextColor) replacement;
                            ctx.current.color(color);
                        }
                        if (color != null && colorResets) {
                            ctx.builders.add(ctx.current);
                            ctx.rootBuilder.append(collapse(ctx.builders));
                            ctx.builders.clear();
                        }

                        anyMatch = true;
                        break;
                    } else if (replacement instanceof TextDecoration) {
                        ctx.current.decorate((TextDecoration) replacement);

                        anyMatch = true;
                        break;
                    }
                    throw new IllegalStateException("Unknown TextFormat or Style: " + replacement.getClass().getName());
                } else {
                    String replaceWith = String.valueOf(replacement);

                    List<Pair<Pattern, Function<Matcher, Object>>> newReplacements;
                    switch (recursiveReplacement) {
                        default:
                        case NO:
                            newReplacements = Collections.emptyList();
                            break;
                        case YES:
                            newReplacements = replacements;
                            break;
                        case ONLY_FOLLOWING:
                            int size = replacements.size();
                            if (size == i + 1) {
                                newReplacements = Collections.emptyList();
                            } else {
                                newReplacements = replacements.subList(i + 1, size);
                            }
                            break;
                    }

                    processPlaceholders(replaceWith, newReplacements);
                    anyMatch = true;
                }
                break;
            }
        }

        if (!anyMatch) {
            parse(input);
        } else if (suffix != null) {
            processPlaceholders(suffix, replacements);
        }
    }

    private void addIfNotEmpty(TextComponent.Builder current, List<ComponentBuilder<?, ?>> builders) {
        if (current.build().equals(Component.empty())) { // Easiest way to tell
            return;
        }
        builders.add(current);
    }

    private ComponentBuilder<?, ?> collapse(List<ComponentBuilder<?, ?>> builders) {
        Collections.reverse(builders);

        ComponentBuilder<?, ?> last = null;
        for (ComponentBuilder<?, ?> current : builders) {
            if (last != null) {
                current.append(last);
            }
            last = current;
        }
        return last != null ? last : Component.text();
    }
}
