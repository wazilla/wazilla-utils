package de.wazilla.util.tests;

import de.wazilla.utils.Strings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class StringsTest {

    @Test
    void isNotNullOrEmpty_NullGiven_FalseExpected() {
        assertFalse(Strings.isNotNullOrEmpty(null));
    }

    @Test
    void isNotNullOrEmpty_EmptyStringGiven_FalseExpected() {
        assertFalse(Strings.isNotNullOrEmpty(""));
    }
}
