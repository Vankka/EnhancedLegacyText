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
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A helper class for creating enhanced legacy components.
 *
 * The replacements may be any of the following:
 * - {@link net.kyori.adventure.text.format.TextColor} (or {@link java.awt.Color})
 * - {@link net.kyori.adventure.text.Component}
 * - {@link net.kyori.adventure.text.ComponentBuilder}
 * - {@link net.kyori.adventure.text.format.TextFormat}
 * - {@link net.kyori.adventure.text.format.Style}
 * - Any other {@link Object}s will be converted to strings
 */
@SuppressWarnings("unused") // API
public class EnhancedComponentBuilder {

    private final EnhancedLegacyText enhancedLegacyText;
    private final String input;
    private final List<Pair<Pattern, Function<Matcher, Object>>> replacements;
    private RecursiveReplacement recursiveReplacement = RecursiveReplacement.ONLY_FOLLOWING;

    protected EnhancedComponentBuilder(EnhancedLegacyText enhancedLegacyText, String input) {
        this.enhancedLegacyText = enhancedLegacyText;
        this.input = input;
        this.replacements = new ArrayList<>();
    }

    /**
     * Adds a replacement to the component.
     *
     * @param target the literal text to replace
     * @param replacement the replacement (see {@link EnhancedComponentBuilder} for possible replacements)
     * @return this builder instance - useful for chaining
     */
    public EnhancedComponentBuilder replace(String target, Object replacement) {
        return replaceAll(Pattern.compile(target, Pattern.LITERAL), replacement);
    }

    /**
     * Adds a replacement to the component.
     *
     * @param regex the regex pattern for the replacement
     * @param replacement the replacement (see {@link EnhancedComponentBuilder} for possible replacements)
     * @return this builder instance - useful for chaining
     */
    public EnhancedComponentBuilder replaceAll(String regex, Object replacement) {
        return replaceAll(Pattern.compile(regex), replacement);
    }

    /**
     * Adds a replacement to the component.
     *
     * @param regex the regex pattern for the replacement
     * @param replacement the replacement (see {@link EnhancedComponentBuilder} for possible replacements)
     * @return this builder instance - useful for chaining
     */
    public EnhancedComponentBuilder replaceAll(Pattern regex, Object replacement) {
        return replaceAll(regex, () -> replacement);
    }

    /**
     * Adds a replacement to the component.
     *
     * @param target the literal text to replace
     * @param replacement the replacement (see {@link EnhancedComponentBuilder} for possible replacements)
     * @return this builder instance - useful for chaining
     */
    public EnhancedComponentBuilder replace(String target, Supplier<Object> replacement) {
        return replaceAll(Pattern.compile(target, Pattern.LITERAL), replacement);
    }

    /**
     * Adds a replacement to the component.
     *
     * @param regex the regex pattern for the replacement
     * @param replacement the replacement (see {@link EnhancedComponentBuilder} for possible replacements)
     * @return this builder instance - useful for chaining
     */
    public EnhancedComponentBuilder replaceAll(String regex, Supplier<Object> replacement) {
        return replaceAll(Pattern.compile(regex), replacement);
    }

    /**
     * Adds a replacement to the component.
     *
     * @param regex the regex pattern for the replacement
     * @param replacement the replacement (see {@link EnhancedComponentBuilder} for possible replacements)
     * @return this builder instance - useful for chaining
     */
    public EnhancedComponentBuilder replaceAll(Pattern regex, Supplier<Object> replacement) {
        return replaceAll(regex, matcher -> replacement.get());
    }

    /**
     * Adds a replacement to the component.
     *
     * @param target the literal text to replace
     * @param replacement the replacement (see {@link EnhancedComponentBuilder} for possible replacements)
     * @return this builder instance - useful for chaining
     */
    @NotNull
    public EnhancedComponentBuilder replace(@NotNull String target, @NotNull Function<Matcher, Object> replacement) {
        return replaceAll(Pattern.compile(target, Pattern.LITERAL), replacement);
    }

    /**
     * Adds a replacement to the component.
     *
     * @param regex the regex pattern for the replacement
     * @param replacement the replacement (see {@link EnhancedComponentBuilder} for possible replacements)
     * @return this builder instance - useful for chaining
     */
    @NotNull
    public EnhancedComponentBuilder replaceAll(@NotNull String regex, @NotNull Function<Matcher, Object> replacement) {
        return replaceAll(Pattern.compile(regex), replacement);
    }

    /**
     * Adds a replacement to the component.
     *
     * @param regex the regex pattern for the replacement
     * @param replacement the replacement (see {@link EnhancedComponentBuilder} for possible replacements)
     * @return this builder instance - useful for chaining
     */
    @NotNull
    public EnhancedComponentBuilder replaceAll(@NotNull Pattern regex, @NotNull Function<Matcher, Object> replacement) {
        replacements.add(new Pair<>(regex, replacement));
        return this;
    }

    /**
     * Getter for the input text.
     * @return the input text
     */
    @NotNull
    public String getInput() {
        return input;
    }

    /**
     * Getter for the replacements.
     * @return the replacements
     */
    @NotNull
    public List<Pair<Pattern, Function<Matcher, Object>>> getReplacements() {
        return replacements;
    }

    /**
     * Sets the recursive replacement policy. The default value is {@link RecursiveReplacement#ONLY_FOLLOWING}.
     * @param replacement the recursive replacement policy
     * @return this builder instance - useful for chaining
     */
    public EnhancedComponentBuilder setRecursiveReplacement(@NotNull RecursiveReplacement replacement) {
        this.recursiveReplacement = Objects.requireNonNull(replacement);
        return this;
    }

    /**
     * Gets the recursive replacement policy.
     * @return the recursive replacement policy
     */
    public RecursiveReplacement getRecursiveReplacement() {
        return recursiveReplacement;
    }

    /**
     * Creates a {@link Component} from the provided input and replacements.
     * @return a new {@link Component}
     */
    public Component build() {
        return enhancedLegacyText.parse(input, replacements, recursiveReplacement);
    }

}
