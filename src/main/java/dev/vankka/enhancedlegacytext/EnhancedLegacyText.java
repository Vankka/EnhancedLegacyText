/*
 * MIT License
 *
 * Copyright (c) 2021-2025 Vankka
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

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
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
        return parse(input, Collections.emptyList(), RecursiveReplacement.NO);
    }

    /**
     * Parses the input text and replacements into a {@link Component}.
     *
     * @param input the input text
     * @param replacements the replacements
     * @param recursiveReplacement the recursive replacement policy
     * @return the {@link Component} parsed from the input and replacements
     */
    @NotNull
    Component parse(
            @NotNull String input,
            @NotNull List<Pair<Pattern, Function<Matcher, Object>>> replacements,
            @NotNull RecursiveReplacement recursiveReplacement
    );

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
         * Chooses if color codes reset all formatting and events.
         * The default value is {@code false}.
         * @param colorResets if color codes reset all formatting and events
         * @return this builder instance, useful for chaining
         */
        Builder colorResets(boolean colorResets);

        /**
         * If all formatting and events are reset on color code change.
         * @return true if formatting and events are reset when the color changes.
         */
        boolean isColorResets();

        /**
         * If legacy color codes and formatting is enabled.
         * The default value is {@code true}.
         * @param legacy if legacy is enabled
         * @return this builder instance, useful for chaining
         */
        Builder useLegacy(boolean legacy);

        /**
         * If legacy color codes and formatting is enabled.
         * @return true if legacy is enabled
         */
        boolean isUsingLegacy();

        /**
         * If using the Adventure hex color format is enabled.
         * The default value is {@code true}.
         * @param adventureHex if the adventure hex format is enabled
         * @return this builder instance, useful for chaining
         */
        Builder adventureHex(boolean adventureHex);

        /**
         * If using the Adventure hex color format is enabled.
         * @return true if the adventure hex format is enabled
         */
        boolean isAdventureHex();

        /**
         * Creates a new instance of {@link EnhancedLegacyText}.
         * @return creates a new instance of {@link EnhancedLegacyText}
         */
        EnhancedLegacyText build();

    }
}
