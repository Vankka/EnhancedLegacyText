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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ColorTests {

    @Test
    public void invalidReset() {
        Component reference =
                Component.text()
                        .content("[color]test")
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[color]test").build();

        Assertions.assertEquals(reference, component);
    }


    @Test
    public void invalidHexPosition() {
        Component reference =
                Component.text()
                        .content("[color:ff#00ff]test")
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[color:ff#00ff]test").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void colorRemoveTest() {
        Component reference =
                Component.text()
                        .append(
                                Component.text()
                                        .content("red ")
                                        .color(NamedTextColor.RED)
                        )
                        .append(
                                Component.text()
                                        .content("normal")
                        )
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[color:red]red [color]normal").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void colorCSSTest() {
        Component reference =
                Component.text()
                        .content("red")
                        .color(Colors.CSS.get("red"))
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[color:css:red]red").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void colorMinecraftTest() {
        Component reference =
                Component.text()
                        .content("red")
                        .color(NamedTextColor.RED)
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[color:minecraft:red]red").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void colorSquareBracketHex6() {
        Component reference =
                Component.text()
                        .content("darkred")
                        .color(TextColor.color(0xFF0000))
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[color:hex:ff0000]darkred").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void colorSquareBracketHex6hash() {
        Component reference =
                Component.text()
                        .content("darkred")
                        .color(TextColor.color(0xFF0000))
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[color:hex:#ff0000]darkred").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void colorSquareBracketHex3() {
        Component reference =
                Component.text()
                        .content("darkred")
                        .color(TextColor.color(0xFF0000))
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[color:hex:f00]darkred").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void colorSquareBracketHex3hash() {
        Component reference =
                Component.text()
                        .content("darkred")
                        .color(TextColor.color(0xFF0000))
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[color:hex:#f00]darkred").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void colorOrderTest1() {
        Component reference =
                Component.text()
                        .content("red")
                        .color(NamedTextColor.RED)
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[color:red]red").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void colorOrderTest2() {
        Component reference =
                Component.text()
                        .content("aliceblue")
                        .color(Colors.CSS.get("aliceblue"))
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[color:aliceblue]aliceblue").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void colorOrderTest3() {
        Component reference =
                Component.text()
                        .content("darkred")
                        .color(TextColor.color(0xFF0000))
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[color:ff0000]darkred").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void colorInvalidHexRollbackTest() {
        Component reference =
                Component.text()
                        .content("[color:hex:iopnmk]white")
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[color:hex:iopnmk]white").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void colorInvalidHexRollbackTestDoubleHash1() {
        Component reference =
                Component.text()
                        .content("[color:hex:##iopnmk]white")
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[color:hex:##iopnmk]white").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void colorInvalidHexRollbackTestDoubleHash2() {
        Component reference =
                Component.text()
                        .content("[color:##iopnmk]white")
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[color:##iopnmk]white").build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void colorInvalidNamespaceRollbackTest() {
        Component reference =
                Component.text()
                        .content("[color:invalid:red]white")
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[color:invalid:red]white").build();

        Assertions.assertEquals(reference, component);
    }
}
