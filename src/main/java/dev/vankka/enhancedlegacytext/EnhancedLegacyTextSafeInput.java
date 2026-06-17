package dev.vankka.enhancedlegacytext;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A string which is safe, and can have it's content parsed by the EnhancedLegacyText parser, regardless of {@link EnhancedComponentBuilder#isAllPlaceholderOutputIsSafeInput()}.
 */
public class EnhancedLegacyTextSafeInput implements CharSequence {

    @Contract("null -> null; !null -> !null")
    public static EnhancedLegacyTextSafeInput of(CharSequence stringContent) {
        return stringContent != null ? new EnhancedLegacyTextSafeInput(stringContent.toString()) : null;
    }

    private final String safeInput;

    private EnhancedLegacyTextSafeInput(String safeInput) {
        this.safeInput = safeInput;
    }

    @Override
    public int length() {
        return safeInput.length();
    }

    @Override
    public char charAt(int i) {
        return safeInput.charAt(i);
    }

    @Override
    public @NotNull CharSequence subSequence(int i, int i1) {
        return safeInput.subSequence(i, i1);
    }

    @Override
    public @NotNull String toString() {
        return safeInput;
    }
}
