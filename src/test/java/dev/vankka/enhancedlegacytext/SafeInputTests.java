package dev.vankka.enhancedlegacytext;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SafeInputTests {

    @Test
    public void testUnsafeInput() {
        Component reference =
                Component.text()
                        .content("[color:#ff00ff]test")
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("%placeholder%test")
                .replace("%placeholder%", "[color:#ff00ff]")
                .build();

        Assertions.assertEquals(reference, component);
    }

    @Test
    public void testSafeInput() {
        Component reference =
                Component.text()
                        .color(TextColor.color(0xff00ff))
                        .content("test")
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("%placeholder%test")
                .replace("%placeholder%", EnhancedLegacyTextSafeInput.of("[color:#ff00ff]"))
                .build();

        Assertions.assertEquals(reference, component);
    }
}
