/*
 * MIT License
 *
 * Copyright (c) 2021-2024 Vankka
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

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.format.TextFormat;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for color and formatting lookup.
 */
class Colors {

    static final String HEX_CHARACTERS = "1234567890abcdef";
    static final Map<Character, TextFormat> LEGACY = new HashMap<>(22);
    static final Reset RESET = new Reset();
    static final Map<String, TextColor> CSS = new HashMap<>(148); // Level 4

    static {
        LEGACY.put('0', NamedTextColor.BLACK);
        LEGACY.put('1', NamedTextColor.DARK_BLUE);
        LEGACY.put('2', NamedTextColor.DARK_GREEN);
        LEGACY.put('3', NamedTextColor.DARK_AQUA);
        LEGACY.put('4', NamedTextColor.DARK_RED);
        LEGACY.put('5', NamedTextColor.DARK_PURPLE);
        LEGACY.put('6', NamedTextColor.GOLD);
        LEGACY.put('7', NamedTextColor.GRAY);
        LEGACY.put('8', NamedTextColor.DARK_GRAY);
        LEGACY.put('9', NamedTextColor.BLUE);
        LEGACY.put('a', NamedTextColor.GREEN);
        LEGACY.put('b', NamedTextColor.AQUA);
        LEGACY.put('c', NamedTextColor.RED);
        LEGACY.put('d', NamedTextColor.LIGHT_PURPLE);
        LEGACY.put('e', NamedTextColor.YELLOW);
        LEGACY.put('f', NamedTextColor.WHITE);

        LEGACY.put('k', TextDecoration.OBFUSCATED);
        LEGACY.put('l', TextDecoration.BOLD);
        LEGACY.put('m', TextDecoration.STRIKETHROUGH);
        LEGACY.put('n', TextDecoration.UNDERLINED);
        LEGACY.put('o', TextDecoration.ITALIC);
        LEGACY.put('r', RESET);

        CSS.put("aliceblue", TextColor.color(0xf0f8ff));
        CSS.put("antiquewhite", TextColor.color(0xfaebd7));
        CSS.put("aqua", TextColor.color(0x00ffff));
        CSS.put("aquamarine", TextColor.color(0x7fffd4));
        CSS.put("azure", TextColor.color(0xf0ffff));
        CSS.put("beige", TextColor.color(0xf5f5dc));
        CSS.put("bisque", TextColor.color(0xffe4c4));
        CSS.put("black", TextColor.color(0x000000));
        CSS.put("blanchedalmond", TextColor.color(0xffebcd));
        CSS.put("blue", TextColor.color(0x0000ff));
        CSS.put("blueviolet", TextColor.color(0x8a2be2));
        CSS.put("brown", TextColor.color(0xa52a2a));
        CSS.put("burlywood", TextColor.color(0xdeb887));
        CSS.put("cadetblue", TextColor.color(0x5f9ea0));
        CSS.put("chartreuse", TextColor.color(0x7fff00));
        CSS.put("chocolate", TextColor.color(0xd2691e));
        CSS.put("coral", TextColor.color(0xff7f50));
        CSS.put("cornflowerblue", TextColor.color(0x6495ed));
        CSS.put("cornsilk", TextColor.color(0xfff8dc));
        CSS.put("crimson", TextColor.color(0xdc143c));
        CSS.put("cyan", TextColor.color(0x00ffff));
        CSS.put("darkblue", TextColor.color(0x00008b));
        CSS.put("darkcyan", TextColor.color(0x008b8b));
        CSS.put("darkgoldenrod", TextColor.color(0xb8860b));
        CSS.put("darkgray", TextColor.color(0xa9a9a9));
        CSS.put("darkgreen", TextColor.color(0x006400));
        CSS.put("darkgrey", TextColor.color(0xa9a9a9));
        CSS.put("darkkhaki", TextColor.color(0xbdb76b));
        CSS.put("darkmagenta", TextColor.color(0x8b008b));
        CSS.put("darkolivegreen", TextColor.color(0x556b2f));
        CSS.put("darkorange", TextColor.color(0xff8c00));
        CSS.put("darkorchid", TextColor.color(0x9932cc));
        CSS.put("darkred", TextColor.color(0x8b0000));
        CSS.put("darksalmon", TextColor.color(0xe9967a));
        CSS.put("darkseagreen", TextColor.color(0x8fbc8f));
        CSS.put("darkslateblue", TextColor.color(0x483d8b));
        CSS.put("darkslategray", TextColor.color(0x2f4f4f));
        CSS.put("darkslategrey", TextColor.color(0x2f4f4f));
        CSS.put("darkturquoise", TextColor.color(0x00ced1));
        CSS.put("darkviolet", TextColor.color(0x9400d3));
        CSS.put("deeppink", TextColor.color(0xff1493));
        CSS.put("deepskyblue", TextColor.color(0x00bfff));
        CSS.put("dimgray", TextColor.color(0x696969));
        CSS.put("dimgrey", TextColor.color(0x696969));
        CSS.put("dodgerblue", TextColor.color(0x1e90ff));
        CSS.put("firebrick", TextColor.color(0xb22222));
        CSS.put("floralwhite", TextColor.color(0xfffaf0));
        CSS.put("forestgreen", TextColor.color(0x228b22));
        CSS.put("fuchsia", TextColor.color(0xff00ff));
        CSS.put("gainsboro", TextColor.color(0xdcdcdc));
        CSS.put("ghostwhite", TextColor.color(0xf8f8ff));
        CSS.put("goldenrod", TextColor.color(0xdaa520));
        CSS.put("gold", TextColor.color(0xffd700));
        CSS.put("gray", TextColor.color(0x808080));
        CSS.put("green", TextColor.color(0x008000));
        CSS.put("greenyellow", TextColor.color(0xadff2f));
        CSS.put("grey", TextColor.color(0x808080));
        CSS.put("honeydew", TextColor.color(0xf0fff0));
        CSS.put("hotpink", TextColor.color(0xff69b4));
        CSS.put("indianred", TextColor.color(0xcd5c5c));
        CSS.put("indigo", TextColor.color(0x4b0082));
        CSS.put("ivory", TextColor.color(0xfffff0));
        CSS.put("khaki", TextColor.color(0xf0e68c));
        CSS.put("lavenderblush", TextColor.color(0xfff0f5));
        CSS.put("lavender", TextColor.color(0xe6e6fa));
        CSS.put("lawngreen", TextColor.color(0x7cfc00));
        CSS.put("lemonchiffon", TextColor.color(0xfffacd));
        CSS.put("lightblue", TextColor.color(0xadd8e6));
        CSS.put("lightcoral", TextColor.color(0xf08080));
        CSS.put("lightcyan", TextColor.color(0xe0ffff));
        CSS.put("lightgoldenrodyellow", TextColor.color(0xfafad2));
        CSS.put("lightgray", TextColor.color(0xd3d3d3));
        CSS.put("lightgreen", TextColor.color(0x90ee90));
        CSS.put("lightgrey", TextColor.color(0xd3d3d3));
        CSS.put("lightpink", TextColor.color(0xffb6c1));
        CSS.put("lightsalmon", TextColor.color(0xffa07a));
        CSS.put("lightseagreen", TextColor.color(0x20b2aa));
        CSS.put("lightskyblue", TextColor.color(0x87cefa));
        CSS.put("lightslategray", TextColor.color(0x778899));
        CSS.put("lightslategrey", TextColor.color(0x778899));
        CSS.put("lightsteelblue", TextColor.color(0xb0c4de));
        CSS.put("lightyellow", TextColor.color(0xffffe0));
        CSS.put("lime", TextColor.color(0x00ff00));
        CSS.put("limegreen", TextColor.color(0x32cd32));
        CSS.put("linen", TextColor.color(0xfaf0e6));
        CSS.put("magenta", TextColor.color(0xff00ff));
        CSS.put("maroon", TextColor.color(0x800000));
        CSS.put("mediumaquamarine", TextColor.color(0x66cdaa));
        CSS.put("mediumblue", TextColor.color(0x0000cd));
        CSS.put("mediumorchid", TextColor.color(0xba55d3));
        CSS.put("mediumpurple", TextColor.color(0x9370db));
        CSS.put("mediumseagreen", TextColor.color(0x3cb371));
        CSS.put("mediumslateblue", TextColor.color(0x7b68ee));
        CSS.put("mediumspringgreen", TextColor.color(0x00fa9a));
        CSS.put("mediumturquoise", TextColor.color(0x48d1cc));
        CSS.put("mediumvioletred", TextColor.color(0xc71585));
        CSS.put("midnightblue", TextColor.color(0x191970));
        CSS.put("mintcream", TextColor.color(0xf5fffa));
        CSS.put("mistyrose", TextColor.color(0xffe4e1));
        CSS.put("moccasin", TextColor.color(0xffe4b5));
        CSS.put("navajowhite", TextColor.color(0xffdead));
        CSS.put("navy", TextColor.color(0x000080));
        CSS.put("oldlace", TextColor.color(0xfdf5e6));
        CSS.put("olive", TextColor.color(0x808000));
        CSS.put("olivedrab", TextColor.color(0x6b8e23));
        CSS.put("orange", TextColor.color(0xffa500));
        CSS.put("orangered", TextColor.color(0xff4500));
        CSS.put("orchid", TextColor.color(0xda70d6));
        CSS.put("palegoldenrod", TextColor.color(0xeee8aa));
        CSS.put("palegreen", TextColor.color(0x98fb98));
        CSS.put("paleturquoise", TextColor.color(0xafeeee));
        CSS.put("palevioletred", TextColor.color(0xdb7093));
        CSS.put("papayawhip", TextColor.color(0xffefd5));
        CSS.put("peachpuff", TextColor.color(0xffdab9));
        CSS.put("peru", TextColor.color(0xcd853f));
        CSS.put("pink", TextColor.color(0xffc0cb));
        CSS.put("plum", TextColor.color(0xdda0dd));
        CSS.put("powderblue", TextColor.color(0xb0e0e6));
        CSS.put("purple", TextColor.color(0x800080));
        CSS.put("rebeccapurple", TextColor.color(0x663399));
        CSS.put("red", TextColor.color(0xff0000));
        CSS.put("rosybrown", TextColor.color(0xbc8f8f));
        CSS.put("royalblue", TextColor.color(0x4169e1));
        CSS.put("saddlebrown", TextColor.color(0x8b4513));
        CSS.put("salmon", TextColor.color(0xfa8072));
        CSS.put("sandybrown", TextColor.color(0xf4a460));
        CSS.put("seagreen", TextColor.color(0x2e8b57));
        CSS.put("seashell", TextColor.color(0xfff5ee));
        CSS.put("sienna", TextColor.color(0xa0522d));
        CSS.put("silver", TextColor.color(0xc0c0c0));
        CSS.put("skyblue", TextColor.color(0x87ceeb));
        CSS.put("slateblue", TextColor.color(0x6a5acd));
        CSS.put("slategray", TextColor.color(0x708090));
        CSS.put("slategrey", TextColor.color(0x708090));
        CSS.put("snow", TextColor.color(0xfffafa));
        CSS.put("springgreen", TextColor.color(0x00ff7f));
        CSS.put("steelblue", TextColor.color(0x4682b4));
        CSS.put("tan", TextColor.color(0xd2b48c));
        CSS.put("teal", TextColor.color(0x008080));
        CSS.put("thistle", TextColor.color(0xd8bfd8));
        CSS.put("tomato", TextColor.color(0xff6347));
        CSS.put("turquoise", TextColor.color(0x40e0d0));
        CSS.put("violet", TextColor.color(0xee82ee));
        CSS.put("wheat", TextColor.color(0xf5deb3));
        CSS.put("white", TextColor.color(0xffffff));
        CSS.put("whitesmoke", TextColor.color(0xf5f5f5));
        CSS.put("yellow", TextColor.color(0xffff00));
        CSS.put("yellowgreen", TextColor.color(0x9acd32));
    }

    private static class Reset implements TextFormat {}
}
