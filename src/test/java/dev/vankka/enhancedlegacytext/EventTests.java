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
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventTests {

    @Test
    public void invalidHoverTypeTest() {
        Component reference =
                Component.text()
                        .content("[hover:]test")
                        .build();

        Component component = EnhancedLegacyText.get()
                .buildComponent("[hover:]test")
                .build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void invalidClickTypeTest() {
        Component reference =
                Component.text()
                        .content("[click:]test")
                        .build();

        Component component = EnhancedLegacyText.get()
                .buildComponent("[click:]test")
                .build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void noHoverTypeTest() {
        Component reference =
                Component.text()
                        .content("[hover]test")
                        .build();

        Component component = EnhancedLegacyText.get()
                .buildComponent("[hover]test")
                .build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void noClickTypeTest() {
        Component reference =
                Component.text()
                        .content("[click]test")
                        .build();

        Component component = EnhancedLegacyText.get()
                .buildComponent("[click]test")
                .build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void invalidTypeTest() {
        Component reference =
                Component.text()
                        .content("[hover:invalid:hello]test")
                        .build();

        Component component = EnhancedLegacyText.get()
                .buildComponent("[hover:invalid:hello]test")
                .build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void eventInvalidRecursionTest() {
        Component reference =
                Component.text()
                        .content("] test")
                        .hoverEvent(HoverEvent.showText(Component.text("[click:suggest_command:invalid recursion")))
                        .build();

        Component component = EnhancedLegacyText.get()
                .buildComponent("[hover:show_text:[click:suggest_command:invalid recursion]] test")
                .build();

        Assertions.assertEquals(reference, component);
    }

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
                        )
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("a[hover:show_text:&lhi]b").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void hoverShowTextEndTest() {
        Component reference =
                Component.text()
                        .append(
                                Component.text()
                                        .content("a")
                                        .append(
                                                Component.text()
                                                        .content("some text")
                                                        .hoverEvent(
                                                                HoverEvent.showText(
                                                                        Component.text("hi")
                                                                                .decorate(TextDecoration.BOLD)
                                                                )
                                                        )
                                        )
                        )
                        .append(
                                Component.text()
                                        .content(" more text")
                        )
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("a[hover:show_text:&lhi]some text[hover] more text").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void clickSuggestCommandTest() {
        Component reference =
                Component.text()
                        .content("a")
                        .append(
                                Component.text()
                                        .content("b")
                                        .clickEvent(
                                                ClickEvent.suggestCommand("/a")
                                        )
                        )
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("a[click:suggest_command:/a]b").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void clickSuggestCommandEndTest() {
        Component reference =
                Component.text()
                        .append(
                                Component.text()
                                        .content("a")
                                        .append(
                                                Component.text()
                                                        .content("b ")
                                                        .clickEvent(
                                                                ClickEvent.suggestCommand("/a")
                                                        )
                                        )
                        )
                        .append(
                                Component.text()
                                        .content("c")
                        )
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("a[click:suggest_command:/a]b [click]c").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void clickRollbackTest() {
        Component reference =
                Component.text()
                        .content("[click:suggest_command:/say hi")
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[click:suggest_command:/say hi").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void hoverRollbackTest() {
        Component reference =
                Component.text()
                        .content("[hover:show_text:a")
                        .append(
                                Component.text()
                                        .content("bold")
                                        .decorate(TextDecoration.BOLD)
                        )
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[hover:show_text:a&lbold").build();

        Assertions.assertEquals(reference, component);
    }
}
