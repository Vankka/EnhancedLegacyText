package dev.vankka.enhancedlegacytext;

public enum RecursiveReplacement {

    /**
     * Replaced content doesn't go through replacements.
     */
    NO,

    /**
     * Only replacements that come after the replacement will process the replaced content.
     * To prevent user input from causing unwanted replacements, user input should be placed after any placeholders.
     */
    ONLY_FOLLOWING,

    /**
     * Replaced content is processed through all patterns until none match.
     * When this is used care needs to be taken to avoid infinite recursion and potential user input.
     */
    YES
}
