package dev.vankka.enhancedlegacytext;

import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InsertionTests {

    @Test
    public void insertion() {
        Component reference =
                Component.text()
                        .append(Component.text().content("test ").insertion("a"))
                        .append(Component.text().content("hi "))
                        .append(Component.text().content("bye").insertion("b"))
                        .build();

        Component component = EnhancedLegacyText.get().buildComponent("[insert:a]test [insert]hi [insert:b]bye").build();

        Assertions.assertEquals(reference, component);
    }
}
