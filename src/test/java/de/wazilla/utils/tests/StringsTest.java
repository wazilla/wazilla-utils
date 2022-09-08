package de.wazilla.utils.tests;

import de.wazilla.utils.Strings;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringsTest {

    @Test
    void equals_NullAndNullGiven_ShouldNotBeEquals() {
        assertFalse(Strings.equals(null, null));
    }

    @Test
    void equals_NullAndValueGiven_ShouldNotBeEquals() {
        assertFalse(Strings.equals(null, "bar"));
    }

    @Test
    void equals_ValueAndNullGiven_ShouldNotBeEquals() {
        assertFalse(Strings.equals("foo", null));
    }

    @Test
    void equals_SameValueGiven_ShouldBeEquals() {
        assertTrue(Strings.equals("foo", "foo"));
    }

    @Test
    void equals_DifferentValuesGiven_ShouldNotBeEquals() {
        assertFalse(Strings.equals("foo", "bar"));
    }

    @Test
    void equalsIgnoringCase_NullAndNullGiven_ShouldNotBeEquals() {
        assertFalse(Strings.equalsIgnoringCase(null, null));
    }

    @Test
    void equalsIgnoringCase_NullAndValueGiven_ShouldNotBeEquals() {
        assertFalse(Strings.equalsIgnoringCase(null, "bar"));
    }

    @Test
    void equalsIgnoringCase_ValueAndNullGiven_ShouldNotBeEquals() {
        assertFalse(Strings.equalsIgnoringCase("foo", null));
    }

    @Test
    void equalsIgnoringCase_SameValueGiven_ShouldBeEquals() {
        assertTrue(Strings.equalsIgnoringCase("foo", "foo"));
    }

    @Test
    void equalsIgnoringCase_SameValueWithDifferentCaseGiven_ShouldBeEquals() {
        assertTrue(Strings.equalsIgnoringCase("FoO", "foo"));
    }

    @Test
    void equalsIgnoringCase_DifferentValuesGiven_ShouldNotBeEquals() {
        assertFalse(Strings.equalsIgnoringCase("foo", "bar"));
    }


    @Test
    void isNullOrBlank_NullGiven_ShouldBeTrue() {
        assertTrue(Strings.isNullOrBlank(null));
    }

    @Test
    void isNullOrBlank_EmptyStringGiven_ShouldBeTrue() {
        assertTrue(Strings.isNullOrBlank(""));
    }

    @Test
    void isNullOrBlank_BlankStringGiven_ShouldBeTrue() {
        assertTrue(Strings.isNullOrBlank(" "));
    }

    @Test
    void isNullOrBlank_NonBlankStringGiven_ShouldBeFalse() {
        assertFalse(Strings.isNullOrBlank("foo"));
    }

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
    public void repeat_NullGiven_ShouldReturnNull() {
        assertNull(Strings.repeat(null, 69));
    }

    @Test
    public void repeat_StringAndLenLesserThanOneGiven_ShouldReturnUnmodifiedString() {
        assertEquals("foo", Strings.repeat("foo", 0));
        assertEquals("foo", Strings.repeat("foo", -1));
    }

    @Test
    public void repeat_CharAndLenGiven_ShouldReturnRepeatedChar() {
        assertEquals("aaa", Strings.repeat('a', 3));
    }

    @Test
    public void repeat_StringAndLenGiven_ShouldReturnRepeatedString() {
        assertEquals("foofoo", Strings.repeat("foo", 2));
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

    @Test
    public void split_NullGiven_ShouldReturnEmptyList() {
        assertTrue(Strings.split(null, ',').isEmpty());
    }

    @Test
    public void split_EmptyStringGiven_ShouldReturnListWithOneEmptyEntry() {
        List<String> parts = Strings.split("", ',');
        assertEquals(1, parts.size());
        assertTrue(parts.get(0).isEmpty());
    }

    @Test
    public void split_StringGiven_ShouldReturnListOfStrings() {
        List<String> parts = Strings.split("foo,bar", ',');
        assertEquals(2, parts.size());
        assertEquals("foo", parts.get(0));
        assertEquals("bar", parts.get(1));
    }

}
