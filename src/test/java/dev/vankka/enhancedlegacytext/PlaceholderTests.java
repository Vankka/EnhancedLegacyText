/*
 * MIT License
 *
 * Copyright (c) 2021-2025 Vankka
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
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlaceholderTests {

    @Test
    public void colorPlaceholderTest() {
        Component reference =
                Component.text()
                        .content("red")
                        .color(NamedTextColor.RED)
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("%color%red")
                .replace("%color%", NamedTextColor.RED).build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void colorNotResetPlaceholderTest() {
        Component reference =
                Component.text()
                        .decorate(TextDecoration.BOLD)
                        .append(
                                Component.text()
                                        .content("red")
                                        .color(NamedTextColor.RED)
                        )
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("&l%color%red")
                .replace("%color%", NamedTextColor.RED).build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void colorResetPlaceholderTest() {
        Component reference =
                Component.text()
                        .append(
                                Component.text()
                                        .decorate(TextDecoration.BOLD)
                                        .append(
                                                Component.text()
                                                        .color(NamedTextColor.RED)
                                        )
                        )
                        .append(
                                Component.text()
                                        .content("red")
                                        .color(NamedTextColor.RED)
                        )
                        .build();

        Component component = EnhancedLegacyText.builder().colorResets(true).build()
                .buildComponent("&l%color%red")
                .replace("%color%", NamedTextColor.RED).build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void formattingPlaceholderTest() {
        Component reference =
                Component.text()
                        .content("bold")
                        .decorate(TextDecoration.BOLD)
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("%format%bold")
                .replace("%format%", TextDecoration.BOLD).build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void stylePlaceholderTest() {
        Component reference =
                Component.text()
                        .content("red")
                        .color(NamedTextColor.RED)
                        .decorate(TextDecoration.BOLD)
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("%style%red")
                .replace("%style%", Style.style(NamedTextColor.RED, TextDecoration.BOLD)).build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void builderPlaceholderTest() {
        TextComponent.Builder child = Component.text().content("test");

        Component reference =
                Component.text()
                        .append(child)
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("%builder%")
                .replace("%builder%", child).build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void placeholderTest() {
        Component reference =
                Component.text()
                        .append(Component.text("test"))
                        .append(
                                Component.text()
                                        .content(": ")
                                        .color(NamedTextColor.GREEN)
                                        .append(Component.text("hello"))
                        )
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("%a&a: %b")
                .replace("%a", Component.text("test"))
                .replace("%b", Component.text("hello"))
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
    public void recursiveReplacementNoRecursiveTest() {
        Assertions.assertEquals("b", recursive2(RecursiveReplacement.NO));
    }

    @Test
    public void recursiveReplacementForwardTest() {
        Assertions.assertEquals("d", recursive2(RecursiveReplacement.ONLY_FOLLOWING));
    }

    @Test
    public void recursiveReplacementForwardRecursiveTest() {
        Assertions.assertEquals("c", recursive2(RecursiveReplacement.YES));
    }

    @Test
    public void colorComponentPlaceholder() {
        Component reference =
                Component.text()
                        .color(NamedTextColor.GREEN)
                        .append(
                                Component.text("test")
                        )
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("&a%a")
                .replace("%a", Component.text("test"))
                .build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void hoverComponentPlaceholder() {
        Component reference =
                Component.text()
                        .hoverEvent(HoverEvent.showText(Component.text("hi")))
                        .append(
                                Component.text("test")
                        )
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[hover:show_text:hi]%a")
                .replace("%a", Component.text("test"))
                .build();

        Assertions.assertEquals(reference, component);
    }
}
