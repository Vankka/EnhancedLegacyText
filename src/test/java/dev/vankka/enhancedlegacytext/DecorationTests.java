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
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DecorationTests {

    @Test
    public void boldTest() {
        Component reference = Component.text()
                .content("bold ")
                .decorate(TextDecoration.BOLD)
                .append(
                        Component.text()
                                .content("normal")
                                .decoration(TextDecoration.BOLD, false)
                )
                .build();

        Component component = EnhancedLegacyText.get().buildComponent("[bold]bold [bold]normal").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void italicTest() {
        Component reference = Component.text()
                .content("italic")
                .decorate(TextDecoration.ITALIC)
                .build();

        Component component = EnhancedLegacyText.get().buildComponent("[italic]italic").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void italicsTest() {
        Component reference = Component.text()
                .content("italics")
                .decorate(TextDecoration.ITALIC)
                .build();

        Component component = EnhancedLegacyText.get().buildComponent("[italics]italics").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void strikethroughTest() {
        Component reference = Component.text()
                .content("strikethrough")
                .decorate(TextDecoration.STRIKETHROUGH)
                .build();

        Component component = EnhancedLegacyText.get().buildComponent("[strikethrough]strikethrough").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void obfuscatedTest() {
        Component reference = Component.text()
                .content("obfuscated")
                .decorate(TextDecoration.OBFUSCATED)
                .build();

        Component component = EnhancedLegacyText.get().buildComponent("[obfuscated]obfuscated").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void obfuscatedTest2() {
        Component reference = Component.text()
                .content("obfuscated ")
                .decorate(TextDecoration.OBFUSCATED)
                .append(
                        Component.text()
                                .content("normal")
                                .decoration(TextDecoration.OBFUSCATED, false)
                )
                .build();

        Component component = EnhancedLegacyText.get().buildComponent("[obfuscated]obfuscated [obfuscated]normal").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void underlinedTest() {
        Component reference = Component.text()
                .content("underlined")
                .decorate(TextDecoration.UNDERLINED)
                .build();

        Component component = EnhancedLegacyText.get().buildComponent("[underlined]underlined").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void underlineTest() {
        Component reference = Component.text()
                .content("underline")
                .decorate(TextDecoration.UNDERLINED)
                .build();

        Component component = EnhancedLegacyText.get().buildComponent("[underline]underline").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void underlinedTest2() {
        Component reference = Component.text()
                .content("underlined")
                .decorate(TextDecoration.UNDERLINED)
                .build();

        Component component = EnhancedLegacyText.get().buildComponent("[decoration:underlined]underlined").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void invalid() {
        Component reference = Component.text()
                .content("[decoration:invalid]invalid")
                .build();

        Component component = EnhancedLegacyText.get().buildComponent("[decoration:invalid]invalid").build();

        Assertions.assertEquals(reference, component);
    }
}
