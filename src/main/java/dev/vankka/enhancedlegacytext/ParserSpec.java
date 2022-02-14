/*
 * MIT License
 *
 * Copyright (c) 2021-2022 Vankka
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

class ParserSpec {

    protected TextComponent.Builder rootBuilder = Component.text();

    // Colors
    protected boolean format = false;
    protected boolean hex = false;
    protected char[] colorChars = new char[6];
    protected int currentChar = 0;

    // Gradient
    protected boolean gradient = false;
    protected boolean gradientDelimiter = false;
    protected List<TextColor> gradientColors = new ArrayList<>();

    // Events
    protected boolean event = false;
    protected boolean eventDelimiter = false;
    protected int eventType = 0; // 0 = none, 1 = click, 2 = hover
    protected StringBuilder eventTypeBuffer = new StringBuilder();
    protected StringBuilder eventActionBuffer = new StringBuilder();
    protected boolean eventActionFinalized = false;
    protected StringBuilder eventValueBuffer = new StringBuilder();
    protected ClickEvent clickEvent = null;
    protected HoverEvent<?> hoverEvent = null;

    // newChild = Does current have any text
    protected AtomicBoolean newChild = new AtomicBoolean(true);

    protected StringBuilder contentBuilder = new StringBuilder();
    protected List<ComponentBuilder<?, ?>> builders = new ArrayList<>();
    protected TextComponent.Builder current = Component.text();

    public void clear() {
        new Impl().copyTo(this);
    }

    public void copyTo(ParserSpec spec) {
        spec.rootBuilder = rootBuilder;
        spec.format = format;
        spec.hex = hex;
        spec.colorChars = colorChars;
        spec.currentChar = currentChar;
        spec.event = event;
        spec.eventType = eventType;
        spec.eventTypeBuffer = eventTypeBuffer;
        spec.eventActionBuffer = eventActionBuffer;
        spec.eventActionFinalized = eventActionFinalized;
        spec.eventValueBuffer = eventValueBuffer;
        spec.clickEvent = clickEvent;
        spec.hoverEvent = hoverEvent;
        spec.newChild = newChild;
        spec.contentBuilder = contentBuilder;
        spec.builders = builders;
        spec.current = current;
    }

    static class Impl extends ParserSpec {}
}
