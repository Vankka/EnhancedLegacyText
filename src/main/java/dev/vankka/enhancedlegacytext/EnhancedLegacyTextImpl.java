/*
 * MIT License
 *
 * Copyright (c) 2021-2024 Vankka
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

import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class EnhancedLegacyTextImpl implements EnhancedLegacyText {

    static final EnhancedLegacyTextImpl INSTANCE = new EnhancedLegacyTextImpl(new BuilderImpl());

    private final char colorChar;
    private final boolean colorResets;
    private final boolean legacy;
    private final boolean adventureHex;

    EnhancedLegacyTextImpl(Builder builder) {
        this(
                builder.getColorCharacter(),
                builder.isColorResets(),
                builder.isUsingLegacy(),
                builder.isAdventureHex()
        );
    }

    EnhancedLegacyTextImpl(char colorChar, boolean colorResets, boolean legacy, boolean adventureHex) {
        this.colorChar = colorChar;
        this.colorResets = colorResets;
        this.legacy = legacy;
        this.adventureHex = adventureHex;
    }

    @Override
    public EnhancedComponentBuilder buildComponent(String input) {
        return new EnhancedComponentBuilder(this, input);
    }

    @Override
    public @NotNull Component parse(
            @NotNull String input,
            @NotNull List<Pair<Pattern, Function<Matcher, Object>>> replacements,
            @NotNull RecursiveReplacement recursiveReplacement
    ) {
        return EnhancedLegacyTextParser.PARSERS.get()
                .parseToComponent(
                        colorChar,
                        colorResets,
                        legacy,
                        adventureHex,
                        input,
                        replacements,
                        recursiveReplacement
                );
    }

    static class BuilderImpl implements Builder {

        private char colorChar = '&';
        private boolean colorResets = false;
        private boolean legacy = true;
        private boolean adventureHex = true;

        @Override
        public Builder colorCharacter(char colorChar) {
            this.colorChar = colorChar;
            return this;
        }

        public char getColorCharacter() {
            return colorChar;
        }

        @Override
        public Builder colorResets(boolean colorResets) {
            this.colorResets = colorResets;
            return this;
        }

        @Override
        public boolean isColorResets() {
            return colorResets;
        }

        @Override
        public Builder useLegacy(boolean legacy) {
            this.legacy = legacy;
            return this;
        }

        @Override
        public boolean isUsingLegacy() {
            return legacy;
        }

        @Override
        public Builder adventureHex(boolean adventureHex) {
            this.adventureHex = adventureHex;
            return this;
        }

        @Override
        public boolean isAdventureHex() {
            return adventureHex;
        }

        @Override
        public EnhancedLegacyTextImpl build() {
            return new EnhancedLegacyTextImpl(this);
        }
    }
}
