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
