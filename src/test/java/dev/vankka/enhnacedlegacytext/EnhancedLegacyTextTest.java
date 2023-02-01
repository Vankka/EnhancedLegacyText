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

package dev.vankka.enhnacedlegacytext;

import dev.vankka.enhancedlegacytext.EnhancedLegacyText;
import dev.vankka.enhancedlegacytext.RecursiveReplacement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EnhancedLegacyTextTest {

    @Test
    public void hoverShowTextTest() {
        Component reference =
                Component.text()
                        .content("a")
                        .append(
                                Component.text()
                                        .content("b")
                                        .hoverEvent(
                                                HoverEvent.showText(
                                                        Component.text("hi")
                                                                .decorate(TextDecoration.BOLD)
                                                )
                                        )
                        ).build();

        Component component = EnhancedLegacyText.get().buildComponent("a[hover:show_text:&lhi]b").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void placeholderTest() {
        Component reference =
                Component.text()
                        .content("test")
                        .append(
                                Component.text(": ")
                                        .append(
                                                Component.text("hello")
                                        )
                        )
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("%a: %b")
                .replace("%a", Component.text("test"))
                .replace("%b", Component.text("hello"))
                .build();

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

    private String recursive(RecursiveReplacement recursiveReplacement) {
        Component component = EnhancedLegacyText.get().buildComponent("a")
                .replace("b", "c") // b -> c
                .replace("a", "b") // a -> b
                .setRecursiveReplacement(recursiveReplacement)
                .build();

        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    @Test
    public void recursiveReplacementTest() {
        Assertions.assertEquals("b", recursive(RecursiveReplacement.ONLY_FOLLOWING));
    }

    @Test
    public void recursiveReplacementEnabledTest() {
        Assertions.assertEquals("c", recursive(RecursiveReplacement.YES));
    }

    private String recursive2(RecursiveReplacement recursiveReplacement) {
        Component component = EnhancedLegacyText.get().buildComponent("a")
                .replace("b", "c") // b -> c
                .replace("a", "b") // a -> b
                .replace("b", "d") // b -> d
                .setRecursiveReplacement(recursiveReplacement)
                .build();

        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    @Test
    public void recursiveReplacementForwardTest() {
        Assertions.assertEquals("d", recursive2(RecursiveReplacement.ONLY_FOLLOWING));
    }

    @Test
    public void recursiveReplacementForwardRecursiveTest() {
        Assertions.assertEquals("c", recursive2(RecursiveReplacement.YES));
    }
}
