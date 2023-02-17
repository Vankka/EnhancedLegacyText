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
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LegacyTests {

    @Test
    public void disabled() {
        Component reference = Component.text()
                .content("&chi")
                .build();

        Component component = EnhancedLegacyText.builder().useLegacy(false).build()
                .buildComponent("&chi").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void altColorCharTest1() {
        Component reference = Component.text()
                .content("hi")
                .color(NamedTextColor.RED)
                .build();

        Component component = EnhancedLegacyText.builder().colorCharacter('!').build()
                .buildComponent("!chi").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void altColorCharTest2() {
        Component reference = Component.text()
                .content("&chi")
                .build();

        Component component = EnhancedLegacyText.builder().colorCharacter('!').build()
                .buildComponent("&chi").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void colorResetsTest() {
        Component reference =
                Component.text()
                        .append(
                                Component.text()
                                        .decorate(TextDecoration.BOLD)
                        )
                        .append(
                                Component.text()
                                        .content("just red")
                                        .color(NamedTextColor.RED)
                        )
                        .build();

        Component component = EnhancedLegacyText.builder().colorResets(true).build()
                .buildComponent("&l&cjust red").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void noColorResetsTest() {
        Component reference =
                Component.text()
                        .content("just red")
                        .decorate(TextDecoration.BOLD)
                        .color(NamedTextColor.RED)
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("&l&cjust red").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void complexFormattingTest() {
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

    @Test
    public void formattingTest() {
        Component reference =
                Component.text()
                        .content("bold")
                        .decorate(TextDecoration.BOLD)
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("&lbold").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void resetTest() {
        Component reference =
                Component.text()
                        .append(
                                Component.text()
                                        .content("bold red ")
                                        .color(NamedTextColor.RED)
                                        .decorate(TextDecoration.BOLD)
                        )
                        .append(
                                Component.text()
                                        .content("blank")
                        )
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("&c&lbold red &rblank").build();

        Assertions.assertEquals(reference, component);
    }
}
