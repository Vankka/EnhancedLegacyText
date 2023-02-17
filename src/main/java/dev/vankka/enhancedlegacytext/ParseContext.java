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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

class ParseContext {

    final StringBuilder rollbackBuffer = new StringBuilder();
    boolean escape = false;

    SquareBracketStatus squareBracketStatus = SquareBracketStatus.NONE;
    final StringBuilder squareBracketPrefix = new StringBuilder();
    final StringBuilder[] squareBracketContext = new StringBuilder[] { new StringBuilder(), new StringBuilder() };

    boolean color = false;
    boolean hexColor = false;
    final char[] hex = new char[6];

    boolean gradient = false;
    boolean gradientDelimiter = false;
    final List<TextColor> gradientColors = new ArrayList<>();

    final StringBuilder content = new StringBuilder();

    ClickEvent clickEvent;
    HoverEvent<?> hoverEvent;

    final TextComponent.Builder rootBuilder = Component.text();
    final List<ComponentBuilder<?, ?>> builders = new ArrayList<>();
    TextComponent.Builder current = Component.text();
    final AtomicBoolean newChild = new AtomicBoolean(false);

    enum SquareBracketStatus {

        NONE,
        PREFIX,

        CLICK_TYPE(true),
        CLICK_VALUE(true),

        HOVER_TYPE(true),
        HOVER_VALUE(true),

        DECORATION,

        COLOR,
        COLOR_NAMESPACED;

        private final boolean event;

        SquareBracketStatus() {
            this(false);
        }

        SquareBracketStatus(boolean event) {
            this.event = event;
        }

        public boolean isEvent() {
            return event;
        }
    }

}
