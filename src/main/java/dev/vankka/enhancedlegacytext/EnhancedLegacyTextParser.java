/*
 * MIT License
 *
 * Copyright (c) 2021 Vankka
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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.*;

import java.awt.Color;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class EnhancedLegacyTextParser {

    static final EnhancedLegacyTextParser INSTANCE = new EnhancedLegacyTextParser();
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

    private EnhancedLegacyTextParser() {}

    @SuppressWarnings("unchecked")
    Component parse(String input, Map<Pattern, Function<Matcher, Object>> replacements,
                    char colorChar, boolean colorResets,
                    char gradientStart, char gradientDelimiterChar, char gradientEnd,
                    char eventStart, char eventDelimiterChar, char eventEnd) {
        TextComponent.Builder rootBuilder = Component.text();

        // Colors
        boolean format = false;
        boolean hex = false;
        char[] colorChars = new char[6];
        int currentChar = 0;

        // Gradient
        boolean gradient = false;
        boolean gradientDelimiter = false;
        List<TextColor> gradientColors = new ArrayList<>();

        // Events
        boolean event = false;
        boolean eventDelimiter = false;
        int eventType = 0; // 0 = none, 1 = click, 2 = hover
        StringBuilder eventTypeBuffer = new StringBuilder();
        StringBuilder eventActionBuffer = new StringBuilder();
        boolean eventActionFinalized = false;
        StringBuilder eventValueBuffer = new StringBuilder();
        ClickEvent clickEvent = null;
        HoverEvent<?> hoverEvent = null;

        // newChild = Does current have any text
        AtomicBoolean newChild = new AtomicBoolean(true);

        StringBuilder contentBuilder = new StringBuilder();
        List<TextComponent.Builder> builders = new ArrayList<>();
        TextComponent.Builder current = Component.text();

        for (char c : input.toCharArray()) {
            // Events
            if (c == eventStart && !event && !format && !gradient) {
                event = true;
                continue;
            }
            if (event && eventType == 0) {
                eventTypeBuffer.append(c);

                String currentBuffer = eventTypeBuffer.toString();
                if (currentBuffer.equals("click")) {
                    eventType = 1;
                } else if (currentBuffer.equals("hover")) {
                    eventType = 2;
                } else if (!"click".startsWith(currentBuffer) && !"hover".startsWith(currentBuffer)) {
                    // Not a valid event type, add as content
                    event = false;
                    contentBuilder.append(eventStart).append(currentBuffer);
                }
                continue;
            }
            if (c == eventEnd && event && eventDelimiter && eventValueBuffer.length() > 0) {
                if (!newChild.get()) {
                    // Clear up the existing text buffer first
                    current = appendContent(
                            colorResets,
                            current,
                            contentBuilder,
                            builders,
                            rootBuilder,
                            replacements,
                            newChild,
                            gradientColors,
                            null,
                            null,
                            false
                    );
                }

                String actionKey = eventActionBuffer.toString().toLowerCase(Locale.ROOT);
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
                    hoverEvent = HoverEvent.hoverEvent(action,
                            parse(value, replacements, colorChar, colorResets,
                                    gradientStart, gradientDelimiterChar, gradientEnd,
                                    // Events are not permitted here
                                    Character.MIN_VALUE, Character.MIN_VALUE, Character.MIN_VALUE));
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
                } else {
                    eventValueBuffer.append(c);
                }
                continue;
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
                    current = appendContent(
                            colorResets,
                            current,
                            contentBuilder,
                            builders,
                            rootBuilder,
                            replacements,
                            newChild,
                            Collections.emptyList(),
                            clickEvent,
                            hoverEvent,
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
                    int index = HEX_CHARACTERS.indexOf(c);
                    if (index != -1) {
                        colorChars[currentChar] = c;
                        if (++currentChar == 6) {
                            TextColor color = TextColor.fromHexString("#" + new String(colorChars));
                            if (gradient) {
                                gradientColors.add(color);
                                gradientDelimiter = false;
                            } else {
                                current = colorize(
                                        colorResets,
                                        color,
                                        current,
                                        contentBuilder,
                                        builders,
                                        rootBuilder,
                                        replacements,
                                        newChild,
                                        gradientColors,
                                        clickEvent,
                                        hoverEvent
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
                            current = appendContent(
                                    colorResets,
                                    current,
                                    contentBuilder,
                                    builders,
                                    rootBuilder,
                                    replacements,
                                    newChild,
                                    gradientColors,
                                    clickEvent,
                                    hoverEvent,
                                    true
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
                                    current = colorize(
                                            colorResets,
                                            color,
                                            current,
                                            contentBuilder,
                                            builders,
                                            rootBuilder,
                                            replacements,
                                            newChild,
                                            gradientColors,
                                            clickEvent,
                                            hoverEvent
                                    );
                                }
                            } else if (textFormat instanceof TextDecoration) {
                                if (newChild.get()) {
                                    current.decorate((TextDecoration) textFormat);
                                } else {
                                    gradientColors.clear();
                                    current = appendContent(
                                            colorResets,
                                            current,
                                            contentBuilder,
                                            builders,
                                            rootBuilder,
                                            replacements,
                                            newChild,
                                            gradientColors,
                                            clickEvent,
                                            hoverEvent,
                                            false
                                    ).decorate((TextDecoration) textFormat);
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

        // Append remaining content
        appendContent(
                colorResets,
                current,
                contentBuilder,
                builders,
                rootBuilder,
                replacements,
                newChild,
                gradientColors,
                clickEvent,
                hoverEvent,
                true
        );

        // Simplify the output component if possible
        List<Component> rootChildren = rootBuilder.children();
        return rootChildren.size() == 1 ? rootChildren.get(0) : rootBuilder.build();
    }

    private TextComponent.Builder colorize(
            boolean colorResets,
            TextColor textColor,
            TextComponent.Builder current,
            StringBuilder textBuilder,
            List<TextComponent.Builder> builders,
            TextComponent.Builder rootBuilder,
            Map<Pattern, Function<Matcher, Object>> replacements,
            AtomicBoolean newChild,
            List<TextColor> gradientColors,
            ClickEvent clickEvent,
            HoverEvent<?> hoverEvent
    ) {
        if (colorResets) {
            current = appendContent(
                    true,
                    current,
                    textBuilder,
                    builders,
                    rootBuilder,
                    replacements,
                    newChild,
                    gradientColors,
                    clickEvent,
                    hoverEvent,
                    true
            );
            newChild.set(true);
        } else if (!newChild.get()) {
            current = appendContent(
                    false,
                    current,
                    textBuilder,
                    builders,
                    rootBuilder,
                    replacements,
                    newChild,
                    gradientColors,
                    clickEvent,
                    hoverEvent,
                    false
            );
            newChild.set(true);
        }
        return current.color(textColor);
    }

    private TextComponent.Builder appendContent(
            boolean colorResets,
            TextComponent.Builder current,
            StringBuilder contentBuilder,
            List<TextComponent.Builder> builders,
            TextComponent.Builder rootBuilder,
            Map<Pattern, Function<Matcher, Object>> replacements,
            AtomicBoolean newChild,
            List<TextColor> gradientColors,
            ClickEvent clickEvent,
            HoverEvent<?> hoverEvent,
            boolean toRoot
    ) {
        String input = contentBuilder.toString();

        String suffix = null;
        boolean anyMatch = false;

        for (Map.Entry<Pattern, Function<Matcher, Object>> replacementEntry : replacements.entrySet()) {
            Pattern pattern = replacementEntry.getKey();

            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                contentBuilder.setLength(0);
                int start = matcher.start();
                int end = matcher.end();

                String prefix = start == 0 ? null : input.substring(0, start);
                suffix = end == input.length() ? null : input.substring(end);
                if (prefix != null) {
                    current = appendContent(
                            colorResets,
                            current,
                            new StringBuilder().append(prefix),
                            builders,
                            rootBuilder,
                            replacements,
                            newChild,
                            gradientColors,
                            clickEvent,
                            hoverEvent,
                            false
                    );
                }

                Object replacement = replacementEntry.getValue().apply(matcher);
                if (replacement instanceof Color) {
                    // Convert java.awt.Color to TextColor
                    Color color = (Color) replacement;
                    replacement = TextColor.color(color.getRed(), color.getGreen(), color.getBlue());
                }

                if (replacement instanceof TextComponent && ((TextComponent) replacement).children().isEmpty()) {
                    TextComponent textComponent = (TextComponent) replacement;
                    Set<TextDecoration> decorations = new HashSet<>();
                    for (Map.Entry<TextDecoration, TextDecoration.State> entry : textComponent.decorations().entrySet()) {
                        if (entry.getValue() == TextDecoration.State.TRUE) {
                            decorations.add(entry.getKey());
                        }
                    }
                    addIfNotEmpty(current, builders);
                    current = Component.text(textComponent.content(), textComponent.color(), decorations).toBuilder();

                    anyMatch = true;
                } else if (replacement instanceof TextComponent.Builder && ((TextComponent.Builder) replacement).children().isEmpty()) {
                    current = (TextComponent.Builder) replacement;
                    anyMatch = true;
                } else if (replacement instanceof Component) {
                    builders.add(Component.text().append((Component) replacement));
                    newChild.set(false);

                    anyMatch = true;
                } else if (replacement instanceof ComponentBuilder) {
                    builders.add(Component.text().append((ComponentBuilder<?, ?>) replacement));
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
                    current = appendContent(
                            colorResets,
                            current,
                            new StringBuilder().append(replaceWith).append(suffix != null ? suffix : ""),
                            builders,
                            rootBuilder,
                            Collections.emptyMap(),
                            newChild,
                            gradientColors,
                            clickEvent,
                            hoverEvent,
                            false
                    );
                    suffix = null;

                    anyMatch = true;
                }
                break;
            }
        }

        if (!anyMatch) {
            if (gradientColors.size() > 1 && contentBuilder.length() > 0) {
                addIfNotEmpty(current, builders);
                current = Component.text();

                Gradient gradient = new Gradient(gradientColors, contentBuilder.length());
                char[] inputText = contentBuilder.toString().toCharArray();
                int index = 0;
                for (TextColor color : gradient.getColors()) {
                    char character = inputText[index++];
                    current.append(Component.text(character).color(color));
                }
                gradientColors.clear();
            } else {
                current.content(current.content() + contentBuilder);
            }
            contentBuilder.setLength(0);
        } else if (suffix != null) {
            current = appendContent(
                    colorResets,
                    current,
                    new StringBuilder().append(suffix),
                    builders,
                    rootBuilder,
                    replacements,
                    newChild,
                    gradientColors,
                    clickEvent,
                    hoverEvent,
                    false
            );
        }

        if (hoverEvent != null) {
            current.hoverEvent(hoverEvent);
        }
        if (clickEvent != null) {
            current.clickEvent(clickEvent);
        }

        addIfNotEmpty(current, builders);
        if (toRoot) {
            rootBuilder.append(collapse(builders));
            builders.clear();

        }
        return Component.text();
    }

    private void addIfNotEmpty(TextComponent.Builder current, List<TextComponent.Builder> builders) {
        if (current.build().equals(Component.empty())) { // Easiest way to tell
            return;
        }
        builders.add(current);
    }

    private TextComponent.Builder collapse(List<TextComponent.Builder> builders) {
        Collections.reverse(builders);

        TextComponent.Builder last = null;
        for (TextComponent.Builder current : builders) {
            if (last != null) {
                current.append(last);
            }
            last = current;
        }
        return last != null ? last : Component.text();
    }
}
