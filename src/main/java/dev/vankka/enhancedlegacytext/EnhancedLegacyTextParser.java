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

import dev.vankka.enhancedlegacytext.gradient.Gradient;
import dev.vankka.enhancedlegacytext.tuple.Pair;
import net.kyori.adventure.text.BuildableComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.*;

import java.awt.Color;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnhancedLegacyTextParser extends ParserSpec {

    private static final String HEX_CHARACTERS = "1234567890abcdef";
    private static final Map<Character, TextFormat> FORMATS = new HashMap<>();
    private static final List<String> CLICK_ACTIONS = new ArrayList<>();
    private static final List<String> HOVER_ACTIONS = new ArrayList<>();

    static {
        FORMATS.put('0', NamedTextColor.BLACK);
        FORMATS.put('1', NamedTextColor.DARK_BLUE);
        FORMATS.put('2', NamedTextColor.DARK_GREEN);
        FORMATS.put('3', NamedTextColor.DARK_AQUA);
        FORMATS.put('4', NamedTextColor.DARK_RED);
        FORMATS.put('5', NamedTextColor.DARK_PURPLE);
        FORMATS.put('6', NamedTextColor.GOLD);
        FORMATS.put('7', NamedTextColor.GRAY);
        FORMATS.put('8', NamedTextColor.DARK_GRAY);
        FORMATS.put('9', NamedTextColor.BLUE);
        FORMATS.put('a', NamedTextColor.GREEN);
        FORMATS.put('b', NamedTextColor.AQUA);
        FORMATS.put('c', NamedTextColor.RED);
        FORMATS.put('d', NamedTextColor.LIGHT_PURPLE);
        FORMATS.put('e', NamedTextColor.YELLOW);
        FORMATS.put('f', NamedTextColor.WHITE);

        FORMATS.put('k', TextDecoration.OBFUSCATED);
        FORMATS.put('l', TextDecoration.BOLD);
        FORMATS.put('m', TextDecoration.STRIKETHROUGH);
        FORMATS.put('n', TextDecoration.UNDERLINED);
        FORMATS.put('o', TextDecoration.ITALIC);
        FORMATS.put('r', null); // Reset

        for (ClickEvent.Action value : ClickEvent.Action.values()) {
            if (value == ClickEvent.Action.OPEN_FILE) {
                // Client side only
                continue;
            }
            CLICK_ACTIONS.add(value.toString());
        }
        HOVER_ACTIONS.add(HoverEvent.Action.SHOW_TEXT.toString());
    }

    private final RecursiveReplacement recursiveReplacement;
    private final char colorChar;
    private final boolean colorResets;
    private final char gradientStart, gradientDelimiterChar, gradientEnd;
    private final char eventStart, eventDelimiterChar, eventEnd;

    private ParserSpec componentCopy;

    protected EnhancedLegacyTextParser(
            String input,
            List<Pair<Pattern, Function<Matcher, Object>>> replacements,
            RecursiveReplacement recursiveReplacement,
            char colorChar, boolean colorResets,
            char gradientStart, char gradientDelimiterChar, char gradientEnd,
            char eventStart, char eventDelimiterChar, char eventEnd
    ) {
        this.recursiveReplacement = recursiveReplacement;
        this.colorChar = colorChar;
        this.colorResets = colorResets;
        this.gradientStart = gradientStart;
        this.gradientDelimiterChar = gradientDelimiterChar;
        this.gradientEnd = gradientEnd;
        this.eventStart = eventStart;
        this.eventDelimiterChar = eventDelimiterChar;
        this.eventEnd = eventEnd;
        processPlaceholders(input, replacements);
    }

    Component out() {
        // Append remaining content
        appendContent(
                contentBuilder,
                true,
                false,
                false,
                false
        );

        // Simplify the output component if possible
        List<Component> rootChildren = rootBuilder.children();
        return rootChildren.size() == 1 ? rootChildren.get(0) : rootBuilder.build();
    }

    @SuppressWarnings("unchecked")
    void parse(String parseIn) {
        for (char c : parseIn.toCharArray()) {
            // Events
            if (c == eventStart && !event && !format && !gradient && componentCopy == null) {
                event = true;
                continue;
            }
            if (event && eventType == 0) {
                String currentBuffer = eventTypeBuffer.toString() + c;
                if ("click".startsWith(currentBuffer) || "hover".startsWith(currentBuffer)) {
                    if (currentBuffer.equals("click")) {
                        eventType = 1;
                        continue;
                    } else if (currentBuffer.equals("hover")) {
                        eventType = 2;
                        continue;
                    } else {
                        eventTypeBuffer.append(c);
                        continue;
                    }
                }

                // Not a valid event type, add as content
                event = false;
                String buffer = eventTypeBuffer.toString();
                eventTypeBuffer.setLength(0);
                contentBuilder.append(eventStart).append(buffer);
            }
            if (c == eventEnd && (event && eventDelimiter && (eventValueBuffer.length() > 0 || componentCopy != null))) {
                if (!newChild.get()) {
                    // Clear up the existing text buffer first
                    appendContent(
                            contentBuilder,
                            false,
                            true,
                            false,
                            true
                    );
                }

                String actionKey = (componentCopy != null ? componentCopy.eventActionBuffer : eventActionBuffer)
                        .toString().toLowerCase(Locale.ROOT);
                String value = eventValueBuffer.toString();
                if (eventType == 1) {
                    // Click
                    ClickEvent.Action action = ClickEvent.Action.valueOf(actionKey.toUpperCase(Locale.ROOT));
                    clickEvent = ClickEvent.clickEvent(action, value);
                } else {
                    // Hover
                    HoverEvent.Action<Component> action = (HoverEvent.Action<Component>) HoverEvent.Action.NAMES.value(actionKey);
                    if (action == null) {
                        throw new IllegalStateException("Unknown hover action: " + actionKey);
                    }

                    if (actionKey.equals(HoverEvent.Action.SHOW_TEXT.toString())) {
                        Component component = out();

                        componentCopy.copyTo(this);
                        componentCopy = null;

                        appendContent(
                                contentBuilder,
                                false,
                                true,
                                false,
                                true
                        );

                        hoverEvent = HoverEvent.hoverEvent(
                                action,
                                component
                        );
                    } else {
                        hoverEvent = HoverEvent.hoverEvent(
                                action,
                                Component.text(value)
                        );
                    }
                }
                event = false;
                eventDelimiter = false;
                eventType = 0;
                eventTypeBuffer.setLength(0);
                eventActionBuffer.setLength(0);
                eventActionFinalized = false;
                eventValueBuffer.setLength(0);
                continue;
            }
            if (event) {
                if (!eventDelimiter) {
                    if (c == eventDelimiterChar) {
                        eventDelimiter = true;
                    } else {
                        contentBuilder.append(eventStart).append(eventTypeBuffer);
                        event = false;
                        eventTypeBuffer.setLength(0);
                        eventActionBuffer.setLength(0);
                    }
                    continue;
                }

                if (!eventActionFinalized) {
                    eventActionBuffer.append(c);

                    String currentBuffer = eventActionBuffer.toString().toLowerCase(Locale.ROOT);
                    List<String> possibleValues = eventType == 1 ? CLICK_ACTIONS : HOVER_ACTIONS;

                    boolean anyStartsWith = false;
                    for (String possibleValue : possibleValues) {
                        if (currentBuffer.equalsIgnoreCase(possibleValue)) {
                            eventActionFinalized = true;
                            eventDelimiter = false;
                            if (eventType == 2 && currentBuffer.equalsIgnoreCase(HoverEvent.Action.SHOW_TEXT.toString())) {
                                componentCopy = new Impl();
                                copyTo(componentCopy);
                                clear();

                                event = componentCopy.event;
                                eventType = componentCopy.eventType;
                                eventActionBuffer = componentCopy.eventActionBuffer;
                                eventDelimiter = componentCopy.eventDelimiter;
                                eventActionFinalized = componentCopy.eventActionFinalized;
                            }
                            break;
                        }
                        if (possibleValue.startsWith(currentBuffer)) {
                            anyStartsWith = true;
                        }
                    }
                    if (eventActionFinalized || anyStartsWith) {
                        continue;
                    }
                    contentBuilder.append(eventStart).append(eventTypeBuffer)
                            .append(eventDelimiterChar).append(eventActionBuffer);
                    event = false;
                    eventTypeBuffer.setLength(0);
                    eventDelimiter = false;
                    eventActionBuffer.setLength(0);
                    continue;
                } else if (componentCopy == null) {
                    eventValueBuffer.append(c);
                    continue;
                }
            }

            // Gradient
            if (c == gradientStart && !gradient && !format) {
                // Begin gradient
                gradient = true;
                gradientDelimiter = true;
                continue;
            }
            if (c == gradientDelimiterChar && gradient && !format && !gradientDelimiter) {
                // Gradient color is changing
                gradientDelimiter = true;
                continue;
            }
            if (c == gradientEnd && gradient && !format && !gradientDelimiter && gradientColors.size() > 1) {
                // Gradient is fully configured now

                if (!newChild.get()) {
                    // Clear up the existing text buffer first
                    appendContent(
                            contentBuilder,
                            false,
                            true,
                            true,
                            false
                    );
                }
                gradient = false;
                continue;
            }

            // Format
            if (c == colorChar && !format && (!gradient || gradientDelimiter || gradientColors.isEmpty())) {
                // Start of a formatting code
                format = true;
                continue;
            }
            if (format) {
                if (c == '#' && !hex) {
                    hex = true;
                    continue;
                }
                if (hex) {
                    int index = HEX_CHARACTERS.indexOf(Character.toLowerCase(c));
                    if (index != -1) {
                        colorChars[currentChar] = c;
                        if (++currentChar == 6) {
                            TextColor color = TextColor.fromHexString("#" + new String(colorChars));
                            if (gradient) {
                                gradientColors.add(color);
                                gradientDelimiter = false;
                            } else {
                                colorize(
                                        colorResets,
                                        color,
                                        contentBuilder
                                );
                            }
                        } else {
                            continue;
                        }
                    } else {
                        // Revert color due to invalid character
                        contentBuilder.append(colorChar).append('#').append(colorChars).append(c);
                        newChild.set(false);
                    }

                    hex = false;
                    colorChars = new char[6];
                    currentChar = 0;
                } else {
                    if (FORMATS.containsKey(c)) {
                        TextFormat textFormat = FORMATS.get(c);
                        if (textFormat == null) {
                            // Reset
                            appendContent(
                                    contentBuilder,
                                    true,
                                    true,
                                    false,
                                    false
                            );
                            gradientColors.clear();
                            clickEvent = null;
                            hoverEvent = null;
                            newChild.set(true);
                        } else {
                            if (textFormat instanceof TextColor) {
                                TextColor color = (TextColor) textFormat;
                                if (gradient) {
                                    gradientColors.add(color);
                                    gradientDelimiter = false;
                                } else {
                                    gradientColors.clear();
                                    colorize(
                                            colorResets,
                                            color,
                                            contentBuilder
                                    );
                                }
                            } else if (textFormat instanceof TextDecoration) {
                                if (newChild.get()) {
                                    current.decorate((TextDecoration) textFormat);
                                } else {
                                    gradientColors.clear();
                                    appendContent(
                                            contentBuilder,
                                            false,
                                            true,
                                            false,
                                            false
                                    );
                                    current.decorate((TextDecoration) textFormat);
                                    newChild.set(true);
                                }
                            } else {
                                throw new IllegalStateException(textFormat.getClass().getName() + " is not a known TextFormat");
                            }
                        }
                    } else {
                        contentBuilder.append(colorChar).append(c);
                        newChild.set(false);
                    }
                }
                format = false;
                continue;
            }

            // Gradient revert
            if (gradient) {
                contentBuilder.append('{');
                List<String> colorsReverted = new ArrayList<>();
                for (TextColor color : gradientColors) {
                    if (color instanceof NamedTextColor) {
                        for (Map.Entry<Character, TextFormat> entry : FORMATS.entrySet()) {
                            if (entry.getValue() == color) {
                                colorsReverted.add(colorChar + String.valueOf(entry.getKey()));
                                break;
                            }
                        }
                    } else {
                        colorsReverted.add(colorChar + color.asHexString());
                    }
                }
                contentBuilder.append(String.join(",", colorsReverted)).append(gradientDelimiter ? "," : "");
                gradient = false;
            }

            contentBuilder.append(c);
            newChild.set(false);
        }

        if (contentBuilder.length() > 0) {
            appendContent(contentBuilder, false, true, false, false);
        }
    }

    private void colorize(
            boolean colorResets,
            TextColor textColor,
            StringBuilder textBuilder
    ) {
        if (colorResets) {
            appendContent(
                    textBuilder,
                    true,
                    true,
                    false,
                    false
            );
            newChild.set(true);
        } else if (!newChild.get()) {
            appendContent(
                    textBuilder,
                    false,
                    true,
                    false,
                    false
            );
            newChild.set(true);
        }
        current.color(textColor);
    }

    private void appendContent(
            StringBuilder contentBuilder,
            boolean toRoot,
            boolean allowEmpty,
            boolean noGradients,
            boolean noEvents
    ) {
        List<TextColor> gradientColors = noGradients ? Collections.emptyList() : this.gradientColors;
        ClickEvent clickEvent = noEvents ? null : this.clickEvent;
        HoverEvent<?> hoverEvent = noEvents ? null : this.hoverEvent;

        if (gradientColors.size() > 1 && contentBuilder.length() > 0) {
            addIfNotEmpty(current, builders);
            current = Component.text();

            Gradient gradient = new Gradient(gradientColors, contentBuilder.length());
            char[] inputText = contentBuilder.toString().toCharArray();
            int index = 0;
            for (TextColor color : gradient.colors()) {
                char character = inputText[index++];
                current.append(Component.text(character).color(color));
            }
            gradientColors.clear();
        } else {
            current.content(current.content() + contentBuilder);
        }
        contentBuilder.setLength(0);

        if (hoverEvent != null) {
            current.hoverEvent(hoverEvent);
        }
        if (clickEvent != null) {
            current.clickEvent(clickEvent);
        }

        if (allowEmpty || current.content().length() > 0 || !current.children().isEmpty()) {
            addIfNotEmpty(current, builders);
        }
        if (toRoot) {
            rootBuilder.append(collapse(builders));
            builders.clear();
        }
        current = Component.text();
    }

    private void processPlaceholders(String input, List<Pair<Pattern, Function<Matcher, Object>>> replacements) {
        boolean anyMatch = false;
        String suffix = null;

        for (int i = 0; i < replacements.size(); i++) {
            Pair<Pattern, Function<Matcher, Object>> replacementEntry = replacements.get(i);
            Pattern pattern = replacementEntry.getKey();

            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                contentBuilder.setLength(0);
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

                if (replacement instanceof Component) {
                    builders.add(replacement instanceof BuildableComponent
                                 ? ((BuildableComponent<?, ?>) replacement).toBuilder()
                                 : Component.text().append((Component) replacement)
                    );
                    newChild.set(false);

                    anyMatch = true;
                } else if (replacement instanceof ComponentBuilder) {
                    builders.add((ComponentBuilder<?, ?>) replacement);
                    newChild.set(false);

                    anyMatch = true;
                } else if (replacement instanceof TextFormat || replacement instanceof Style) {
                    addIfNotEmpty(current, builders);
                    current = Component.text();

                    newChild.set(true);
                    if (replacement instanceof TextColor || replacement instanceof Style) {
                        TextColor color;
                        if (replacement instanceof Style) {
                            Style style = (Style) replacement;
                            current.style(style);
                            color = style.color();
                        } else {
                            color = (TextColor) replacement;
                            current.color(color);
                        }
                        if (color != null && colorResets) {
                            builders.add(current);
                            rootBuilder.append(collapse(builders));
                            builders.clear();
                        }

                        anyMatch = true;
                        break;
                    } else if (replacement instanceof TextDecoration) {
                        current.decorate((TextDecoration) replacement);

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
