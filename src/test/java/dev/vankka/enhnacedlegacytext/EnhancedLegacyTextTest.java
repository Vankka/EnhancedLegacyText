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

package dev.vankka.enhnacedlegacytext;

import dev.vankka.enhancedlegacytext.EnhancedLegacyText;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EnhancedLegacyTextTest {

    @Test
    public void placeholderTest() {
        Component reference =
                Component.text()
                        .content("test: ")
                        .append(
                                Component.text()
                                        .content("hello")
                        )
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("%a: %b")
                .replace("%a", Component.text("test"))
                .replace("%b", Component.text("hello"))
                .build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void complexTest() {
        Component reference =
                Component.text()
                        .content("Aqua ")
                        .color(NamedTextColor.AQUA)
                        .append(
                                Component.text()
                                        .content("bold")
                                        .decorate(TextDecoration.BOLD)
                        )
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("&bAqua &lbold")
                .build();

        Assertions.assertEquals(reference, component);
    }
}
