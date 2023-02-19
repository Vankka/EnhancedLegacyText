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
                .content("bold")
                .decorate(TextDecoration.BOLD)
                .append(
                        Component.text()
                                .content(" normal")
                                .decoration(TextDecoration.BOLD, false)
                )
                .build();

        Component component = EnhancedLegacyText.get().buildComponent("[bold:true]bold[bold:false] normal").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void italicTest() {
        Component reference = Component.text()
                .content("italic")
                .decorate(TextDecoration.ITALIC)
                .build();

        Component component = EnhancedLegacyText.get().buildComponent("[italic:on]italic").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void italicsTest() {
        Component reference = Component.text()
                .content("italics")
                .decorate(TextDecoration.ITALIC)
                .build();

        Component component = EnhancedLegacyText.get().buildComponent("[italics:true]italics").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void strikethroughTest() {
        Component reference = Component.text()
                .content("strikethrough")
                .decorate(TextDecoration.STRIKETHROUGH)
                .build();

        Component component = EnhancedLegacyText.get().buildComponent("[strikethrough:on]strikethrough").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void obfuscatedTest() {
        Component reference = Component.text()
                .content("obfuscated")
                .decorate(TextDecoration.OBFUSCATED)
                .build();

        Component component = EnhancedLegacyText.get().buildComponent("[obfuscated:on]obfuscated").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void obfuscatedTest2() {
        Component reference = Component.text()
                .content("obfuscated")
                .decorate(TextDecoration.OBFUSCATED)
                .append(
                        Component.text()
                                .content(" normal")
                                .decoration(TextDecoration.OBFUSCATED, false)
                )
                .build();

        Component component = EnhancedLegacyText.get().buildComponent("[obfuscated:on]obfuscated[obfuscated] normal").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void underlinedTest() {
        Component reference = Component.text()
                .content("underlined")
                .decorate(TextDecoration.UNDERLINED)
                .build();

        Component component = EnhancedLegacyText.get().buildComponent("[underlined:on]underlined").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void underlineTest() {
        Component reference = Component.text()
                .content("underline")
                .decorate(TextDecoration.UNDERLINED)
                .build();

        Component component = EnhancedLegacyText.get().buildComponent("[underline:true]underline").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void underlineInvalidValueTest() {
        Component reference = Component.text()
                .content("[underline:maybe]underline")
                .build();

        Component component = EnhancedLegacyText.get().buildComponent("[underline:maybe]underline").build();

        Assertions.assertEquals(reference, component);
    }
}
