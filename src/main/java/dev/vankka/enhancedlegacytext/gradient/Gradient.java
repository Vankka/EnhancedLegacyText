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

    public List<TextColor> getColors() {
        int regionCount = colors.size() - 1;

        float perRegion = (float) length / (regionCount);
        float over = perRegion - (float) (int) perRegion;

        float currentOver = 0;
        int start;
        int end = 0;

        List<TextColor> colors = new ArrayList<>();
        for (int color = 0; color < regionCount; ++color) {
            start = end;
            currentOver += over;
            end = (int) Math.floor((((int) perRegion) * (color + 1) + currentOver));
            if (currentOver > 1) {
                currentOver = currentOver - 1;
            }

            TextColor one = this.colors.get(color);
            TextColor two = this.colors.get(color + 1);

            int difference = end - start;
            for (int current = 0; current < difference; current++) {
                double percentage = (1f / difference) * (current + 1f);
                int r = (int) (one.red() * (1f - percentage) + two.red() * percentage);
                int g = (int) (one.green() * (1f - percentage) + two.green() * percentage);
                int b = (int) (one.blue() * (1f - percentage) + two.blue() * percentage);

                colors.add(TextColor.color(r, g, b));
            }
        }
        return colors;
    }
}
