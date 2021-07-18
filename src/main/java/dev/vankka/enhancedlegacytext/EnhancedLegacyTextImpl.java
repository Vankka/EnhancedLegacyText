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

import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;

class EnhancedLegacyTextImpl implements EnhancedLegacyText {

    static final EnhancedLegacyTextImpl INSTANCE = new EnhancedLegacyTextImpl(new BuilderImpl());

    private final char colorChar;
    private final boolean colorResets;
    private final char gradientStart;
    private final char gradientDelimiter;
    private final char gradientEnd;
    private final char eventStart;
    private final char eventDelimiter;
    private final char eventEnd;

    EnhancedLegacyTextImpl(Builder builder) {
        this(
                builder.getColorCharacter(),
                builder.isColorResets(),
                builder.getGradientStart(),
                builder.getGradientDelimiter(),
                builder.getGradientEnd(),
                builder.getEventStart(),
                builder.getEventDelimiter(),
                builder.getEventEnd()
        );
    }

    EnhancedLegacyTextImpl(char colorChar, boolean colorResets,
                                  char gradientStart, char gradientDelimiter, char gradientEnd,
                                  char eventStart, char eventDelimiter, char eventEnd) {
        this.colorChar = colorChar;
        this.colorResets = colorResets;
        this.gradientStart = gradientStart;
        this.gradientDelimiter = gradientDelimiter;
        this.gradientEnd = gradientEnd;
        this.eventStart = eventStart;
        this.eventDelimiter = eventDelimiter;
        this.eventEnd = eventEnd;
    }

    @Override
    public EnhancedComponentBuilder buildComponent(String input) {
        return new EnhancedComponentBuilder(this, input);
    }

    @Override
    public Component parse(String input, Map<Pattern, Supplier<Object>> replacements) {
        return EnhancedLegacyTextParser.INSTANCE.parse(
                input,
                replacements,
                colorChar,
                colorResets,
                gradientStart,
                gradientDelimiter,
                gradientEnd,
                eventStart,
                eventDelimiter,
                eventEnd
        );
    }

    static class BuilderImpl implements Builder {

        private char colorChar = '&';
        private boolean colorResets = true;
        private char gradientStart = '{';
        private char gradientDelimiter = ',';
        private char gradientEnd = '}';
        private char eventStart = '[';
        private char eventDelimiter = ':';
        private char eventEnd = ']';

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
        public Builder gradientStart(char gradientStart) {
            this.gradientStart = gradientStart;
            return this;
        }

        @Override
        public char getGradientStart() {
            return gradientStart;
        }

        @Override
        public Builder gradientDelimiter(char gradientDelimiter) {
            this.gradientDelimiter = gradientDelimiter;
            return this;
        }

        @Override
        public char getGradientDelimiter() {
            return gradientDelimiter;
        }

        @Override
        public Builder gradientEnd(char gradientEnd) {
            this.gradientEnd = gradientEnd;
            return this;
        }

        @Override
        public char getGradientEnd() {
            return gradientEnd;
        }

        @Override
        public Builder eventStart(char eventStart) {
            this.eventStart = eventStart;
            return this;
        }

        @Override
        public char getEventStart() {
            return eventStart;
        }

        @Override
        public Builder eventDelimiter(char eventDelimiter) {
            this.eventDelimiter = eventDelimiter;
            return this;
        }

        @Override
        public char getEventDelimiter() {
            return eventDelimiter;
        }

        @Override
        public Builder eventEnd(char eventEnd) {
            this.eventEnd = eventEnd;
            return this;
        }

        @Override
        public char getEventEnd() {
            return eventEnd;
        }

        @Override
        public EnhancedLegacyTextImpl build() {
            return new EnhancedLegacyTextImpl(this);
        }
    }
}
