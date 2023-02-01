/*
 * MIT License
 *
 * Copyright (c) 2021-2022 Vankka
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

package dev.vankka.enhancedlegacytext.gradient;

import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.List;

public class Gradient {

    private final List<TextColor> colors;
    private final int length;

    public Gradient(List<TextColor> colors, int length) {
        this.colors = colors;
        this.length = length;
    }

    public List<TextColor> colors() {
        int regions = colors.size() - 1;

        float perRegion = (length - 1) / (float) regions;

        List<TextColor> textColors = new ArrayList<>(length);
        for (float i = 0; i < length; i++) {
            int region = (int) Math.floor(i / perRegion);
            if (region == regions) {
                // final color
                textColors.add(colors.get(region));
                continue;
            }

            float percentage = (i - (perRegion * (float) region)) / perRegion;

            TextColor start = colors.get(region);
            TextColor end = colors.get(region + 1);

            int r = (int) (start.red() * (1f - percentage) + end.red() * percentage);
            int g = (int) (start.green() * (1f - percentage) + end.green() * percentage);
            int b = (int) (start.blue() * (1f - percentage) + end.blue() * percentage);

            textColors.add(TextColor.color(r, g, b));
        }

        return textColors;
    }

}
