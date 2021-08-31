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

import dev.vankka.enhancedlegacytext.tuple.Pair;
import net.kyori.adventure.text.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused") // API
public interface EnhancedLegacyText {

    /**
     * Gets the all default {@link EnhancedLegacyText} instance.
     * @return the all default options instance of {@link EnhancedLegacyText}
     */
    static EnhancedLegacyText get() {
        return EnhancedLegacyTextImpl.INSTANCE;
    }

    /**
     * Creates a new {@link EnhancedLegacyText} {@link Builder}.
     * @return a new instance of {@link Builder}
     */
    static Builder builder() {
        return new EnhancedLegacyTextImpl.BuilderImpl();
    }

    /**
     * Creates a new {@link EnhancedComponentBuilder} for creating a new component.
     * @param input the input text
     * @return a new instance of {@link EnhancedComponentBuilder} with the input text
     */
    EnhancedComponentBuilder buildComponent(String input);

    /**
     * Parses the input text into a {@link Component} without replacements.
     *
     * @param input the input text
     * @return the {@link Component} parsed from the input
     */
    default Component parse(String input) {
        return parse(input, Collections.emptyList());
    }

    /**
     * Parses the input text and replacements into a {@link Component}.
     *
     * @param input the input text
     * @param replacements the replacements
     * @return the {@link Component} parsed from the input and replacements
     */
    Component parse(String input, List<Pair<Pattern, Function<Matcher, Object>>> replacements);

    interface Builder {

        /**
         * Changes the color character.
         * The default value is {@code &}.
         *
         * @param colorChar the new color character
         * @return this builder instance, useful for chaining
         */
        Builder colorCharacter(char colorChar);

        /**
         * Gets the color character.
         * @return the color character
         */
        char getColorCharacter();

        /**
         * Chooses if color codes reset all formatting.
         * The default value is {@code true}.
         * @param colorResets if color codes reset all formatting
         * @return this builder instance, useful for chaining
         */
        Builder colorResets(boolean colorResets);

        /**
         * If all formatting is reset on color code change.
         * @return true if formatting is reset when the color code changes.
         */
        boolean isColorResets();

        /**
         * Changes the gradient start character.
         * The default value is {.
         *
         * @param gradientStart the new gradient start character
         * @return this builder instance, useful for chaining
         */
        Builder gradientStart(char gradientStart);

        /**
         * Gets the gradient start character.
         * @return the gradient start character
         */
        char getGradientStart();

        /**
         * Changes the gradient delimiter character.
         * The default value is {@code ,}.
         *
         * @param gradientDelimiter the new gradient delimiter character
         * @return this builder instance, useful for chaining
         */
        Builder gradientDelimiter(char gradientDelimiter);

        /**
         * Gets the gradient delimiter character.
         * @return the gradient delimiter character
         */
        char getGradientDelimiter();

        /**
         * Changes the gradient end character.
         * The default value is }.
         *
         * @param gradientEnd the new gradient end character
         * @return this builder instance, useful for chaining
         */
        Builder gradientEnd(char gradientEnd);

        /**
         * Gets the gradient end character.
         * @return the gradient end character
         */
        char getGradientEnd();

        /**
         * Changes the gradient start character.
         * The default value is {@code [}.
         *
         * @param eventStart the new event start character
         * @return this builder instance, useful for chaining
         */
        Builder eventStart(char eventStart);

        /**
         * Gets the event start character.
         * @return the event start character
         */
        char getEventStart();

        /**
         * Gets the event delimiter character.
         * The default value {@code :}.
         *
         * @param eventDelimiter the new event delimiter character
         * @return this builder instance, useful for chaining
         */
        Builder eventDelimiter(char eventDelimiter);

        /**
         * Gets the event delimiter character.
         * @return the event delimiter character
         */
        char getEventDelimiter();

        /**
         * Changes the event end character.
         * The default value {@code ]}.
         *
         * @param eventEnd the new event end character
         * @return this builder instance, useful for chaining
         */
        Builder eventEnd(char eventEnd);

        /**
         * Gets the event end character.
         * @return the event end character
         */
        char getEventEnd();

        /**
         * Creates a new instance of {@link EnhancedLegacyText}.
         * @return creates a new instance of {@link EnhancedLegacyText}
         */
        EnhancedLegacyText build();

    }
}
