package de.wazilla.utils.tests;

import de.wazilla.utils.Strings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringsTest {

    @Test
    void isNotNullOrEmpty_NullGiven_FalseExpected() {
        assertFalse(Strings.isNotNullOrEmpty(null));
    }

    @Test
    void isNotNullOrEmpty_EmptyStringGiven_FalseExpected() {
        assertFalse(Strings.isNotNullOrEmpty(""));
    }

    @Test
    public void randomAlpha_LenGiven_ShouldCreateRandomString() {
        assertTrue(Strings.randomAlpha(5).matches("[A-Z]{5}"));
    }

    @Test
    public void randomAlphaNumeric_LenGiven_ShouldCreateRandomString() {
        assertTrue(Strings.randomAlphaNumeric(5).matches("[A-Z0-9]{5}"));
    }

    @Test
    public void randomNumeric_LenGiven_ShouldCreateRandomString() {
        assertTrue(Strings.randomNumeric(5).matches("[0-9]{5}"));
    }

    @Test
    public void randomAlpha_NegativeLenGiven_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Strings.randomAlpha(-1));
    }

    @Test
    public void randomAlphaNumeric_NegativeLenGiven_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Strings.randomAlphaNumeric(-1));
    }

    @Test
    public void randomNumeric_NegativeLenGiven_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Strings.randomNumeric(-1));
    }

    @Test
    public void random_NegativeLenGiven_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Strings.random(-1, "abc"));
    }

    @Test
    public void random_NullAsSetGiven_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Strings.random(4711, null));
    }

    @Test
    public void random_LenAndSetGiven_ShouldCreateRandomString() {
        String actual = Strings.random(5, "ab");
        assertTrue(actual.matches("[a-b]{5}"));
    }

    @Test
    public void randomAlpha_LenAndSetGiven_ShouldCreateRandomString() {
        String actual = Strings.randomAlpha(9);
        assertTrue(actual.matches("[A-Z]{9}"));
    }

    @Test
    public void randomAlphaNumeric_LenAndSetGiven_ShouldCreateRandomString() {
        String actual = Strings.randomAlpha(7);
        assertTrue(actual.matches("[A-Z0-9]{7}"));
    }

    @Test
    public void randomNumeric_LenAndSetGiven_ShouldCreateRandomString() {
        String actual = Strings.randomNumeric(11);
        assertTrue(actual.matches("[0-9]{11}"));
    }

    @Test
    public void leftPad_NullGiven_ShouldReturnNull() {
        assertNull(Strings.leftPad(null, 1));
    }

    @Test
    public void leftPad_LenGiven_ShouldReturnFilledString() {
        String actual = Strings.leftPad("foo", 5);
        assertEquals("  foo", actual);
    }

    @Test
    public void leftPad_LenAndFillerGiven_ShouldReturnFilledString() {
        String actual = Strings.leftPad("foo", 5, 'x');
        assertEquals("xxfoo", actual);
    }

    @Test
    public void leftPad_NotExcactFillerGiven_ShouldReturnFilledString() {
        String actual = Strings.leftPad("bar", 7, "abc");
        assertEquals("aabcbar", actual);
    }

    @Test
    public void rightPad_NullGiven_ShouldReturnNull() {
        assertNull(Strings.rightPad(null, 1));
    }

    @Test
    public void rightPad_NegativeLenGiven_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Strings.rightPad("", -1));
    }

    @Test
    public void rightPad_NullAsFillerGiven_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Strings.rightPad("", 1, null));
    }

    @Test
    public void rightPad_EmptyFillerGiven_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Strings.rightPad("", 1, ""));
    }

    @Test
    public void rightPad_LenGiven_ShouldReturnFilledString() {
        String actual = Strings.rightPad("bar", 4);
        assertEquals("bar ", actual);
    }

    @Test
    public void rightPad_LenAndFillerGiven_ShouldReturnFilledString() {
        String actual = Strings.rightPad("bar", 5, 'x');
        assertEquals("barxx", actual);
    }

    @Test
    public void rightPad_NotExcactFillerGiven_ShouldReturnFilledString() {
        String actual = Strings.rightPad("bar", 7, "abc");
        assertEquals("barabca", actual);
    }
}
