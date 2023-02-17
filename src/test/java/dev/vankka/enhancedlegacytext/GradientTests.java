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
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GradientTests {

    @Test
    public void gradientInvalidFormatTest() {
        Component reference =
                Component.text()
                        .content("{")
                        .append(
                                Component.text()
                                        .content(",")
                                        .decorate(TextDecoration.BOLD)
                                        .append(
                                                Component.text()
                                                        .content("}white")
                                                        .color(NamedTextColor.RED)
                                        )
                        )
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("{&l,&c}white").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void gradientRollbackTest() {
        Component reference =
                Component.text()
                        .content("{")
                        .append(
                                Component.text()
                                        .content(",")
                                        .color(NamedTextColor.GREEN)
                                        .append(
                                                Component.text()
                                                        .content("red")
                                                        .color(NamedTextColor.RED)
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("{&a,&cred").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void gradientTest() {
        Component reference =
                Component.text()
                        .append(
                                Component.text()
                                        .content("1")
                                        .color(NamedTextColor.GREEN)
                                        .build()
                        )
                        .append(
                                Component.text()
                                        .content("2")
                                        .color(NamedTextColor.RED)
                                        .build()
                        )
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("{&a,&c}12").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void gradientHexTest() {
        Component reference =
                Component.text()
                        .append(
                                Component.text()
                                        .content("1")
                                        .color(TextColor.color(0x0000ff))
                                        .build()
                        )
                        .append(
                                Component.text()
                                        .content("2")
                                        .color(TextColor.color(0xff0000))
                                        .build()
                        )
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("{&#0000ff,&#ff0000}12").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void gradientSquareBracket() {
        Component reference =
                Component.text()
                        .append(
                                Component.text()
                                        .content("1")
                                        .color(TextColor.color(0x0000ff))
                                        .build()
                        )
                        .append(
                                Component.text()
                                        .content("2")
                                        .color(TextColor.color(0xff0000))
                                        .build()
                        )
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("{[#0000ff],[#ff0000]}12").build();

        Assertions.assertEquals(reference, component);
    }
}
