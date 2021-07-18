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

import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@SuppressWarnings("unused") // API
public class EnhancedComponentBuilder {

    private final EnhancedLegacyText enhancedLegacyText;
    private final String input;
    private final Map<Pattern, Supplier<Object>> replacements;

    protected EnhancedComponentBuilder(EnhancedLegacyText enhancedLegacyText, String input) {
        this.enhancedLegacyText = enhancedLegacyText;
        this.input = input;
        this.replacements = new HashMap<>();
    }

    /**
     * Adds a replacement to the component.
     *
     * @param target the literal text to replace
     * @param replacement the replacement
     * @return this builder instance - useful for chaining
     */
    public EnhancedComponentBuilder replace(String target, Object replacement) {
        return replaceAll(Pattern.compile(target, Pattern.LITERAL), replacement);
    }

    /**
     * Adds a replacement to the component.
     *
     * @param target the regex pattern for the replacement
     * @param replacement the replacement
     * @return this builder instance - useful for chaining
     */
    public EnhancedComponentBuilder replaceAll(String target, Object replacement) {
        return replaceAll(Pattern.compile(target), replacement);
    }

    /**
     * Adds a replacement to the component.
     *
     * @param target the regex pattern for the replacement
     * @param replacement the replacement
     * @return this builder instance - useful for chaining
     */
    public EnhancedComponentBuilder replaceAll(Pattern target, Object replacement) {
        return replaceAll(target, () -> replacement);
    }

    /**
     * Adds a replacement to the component.
     *
     * @param target the literal text to replace
     * @param replacement the replacement
     * @return this builder instance - useful for chaining
     */
    public EnhancedComponentBuilder replace(String target, Supplier<Object> replacement) {
        return replaceAll(Pattern.compile(target, Pattern.LITERAL), replacement);
    }

    /**
     * Adds a replacement to the component.
     *
     * @param target the regex pattern for the replacement
     * @param replacement the replacement
     * @return this builder instance - useful for chaining
     */
    public EnhancedComponentBuilder replaceAll(String target, Supplier<Object> replacement) {
        return replaceAll(Pattern.compile(target), replacement);
    }

    /**
     * Adds a replacement to the component.
     *
     * The replacement may be any of the following:<br/>
     * -
     *
     * @param target the regex pattern for the replacement
     * @param replacement the replacement (see possible types above)
     * @return this builder instance - useful for chaining
     */
    public EnhancedComponentBuilder replaceAll(Pattern target, Supplier<Object> replacement) {
        replacements.put(target, replacement);
        return this;
    }

    /**
     * Getter for the input text.
     * @return the input text
     */
    public String getInput() {
        return input;
    }

    /**
     * Getter for the replacements.
     * @return the replacements
     */
    public Map<Pattern, Supplier<Object>> getReplacements() {
        return replacements;
    }

    /**
     * Creates a {@link Component} from the provided input and replacements.
     * @return a new {@link Component}
     */
    public Component build() {
        return enhancedLegacyText.parse(input, replacements);
    }

}
