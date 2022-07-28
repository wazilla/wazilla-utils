package de.hzd.commons;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

public class DatesTest {

    @Test
    void isValid_NullGiven_ShouldReturnFalse() {
        assertFalse(Dates.isValid(null, "dd.MM.yyyy"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.1.2020", "31.12.1999", "5.8.20", "12.12.2999"})
    void isValid_ValidStringGiven_ShouldReturnTrue(String value) {
        assertTrue(Dates.isValid(value, "dd.MM.yyyy"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"01-01-2020", "2020-01-01", "2020/01/02", "30.02.2021", "32.07.2999"})
    void isValid_InvalidStringGiven_ShouldReturnFalse(String value) {
        assertFalse(Dates.isValid(value, "dd.MM.yyyy"), "Der Wert '" + value + "' sollte nicht als gültiges Datum angesehen werden!");
    }

    @Test
    void isValid_InvalidStringAndTestWithRegexGiven_ShouldReturnFalse() {
        assertFalse(Dates.isValid("5.8.20", "dd.MM.yyyy", "[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}"));
    }

    @Test
    void isValid_ValidStringAndTestWithRegexGiven_ShouldReturnTrue() {
        assertTrue(Dates.isValid("05.08.2019", "dd.MM.yyyy", "[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}"));
    }

    @Test
    void toLocalDate_NullGiven_ShouldReturnNull() {
        assertNull(Dates.toLocalDate((Date) null));
        assertNull(Dates.toLocalDate((XMLGregorianCalendar) null));
        assertNull(Dates.toLocalDate(null, "dummyRegex"));
    }

    @Test
    void toLocalDate_NullAsRegexGiven_ShouldThrowEx() {
        assertThrows(IllegalArgumentException.class, () -> Dates.toLocalDate("4.7.11", null));
    }

    @Test
    void toLocalDate_DateGiven_ShouldReturnLocalDate() {
        int year = 2011;
        int month = 7;
        int day = 4;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day); // -1, da hier ab 0 gezaehlt wird
        Date date = calendar.getTime();
        LocalDate localDate = Dates.toLocalDate(date);
        assertNotNull(localDate);
        assertEquals(year, localDate.getYear());
        assertEquals(month, localDate.getMonthValue());
        assertEquals(day, localDate.getDayOfMonth());
    }

    @Test
    void toLocalDate_StringGiven_ShouldReturnLocalDate() {
        String value = "07.04.2011";
        String pattern = "dd.MM.yyyy";
        LocalDate localDate = Dates.toLocalDate(value, pattern);
        assertNotNull(pattern);
        assertEquals(2011, localDate.getYear());
        assertEquals(4, localDate.getMonthValue());
        assertEquals(7, localDate.getDayOfMonth());
    }

    @Test
    void toLocalDate_XmlGregorianCalendarGiven_ShouldReturnLocalDate() throws Exception {
        int year = 2011;
        int month = 4;
        int day = 7;
        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(year, month, day, 0, 0, 0, 0, 0);
        LocalDate localDate = Dates.toLocalDate(xmlGregorianCalendar);
        assertNotNull(localDate);
        assertEquals(year, localDate.getYear());
        assertEquals(month, localDate.getMonthValue());
        assertEquals(day, localDate.getDayOfMonth());
    }

    @Test
    void toLocalDateTime_NullGiven_ShouldReturnNull() {
        assertNull(Dates.toLocalDateTime((Date) null));
        assertNull(Dates.toLocalDateTime((XMLGregorianCalendar) null));
        assertNull(Dates.toLocalDateTime(null, "dummyPattern"));
    }

    @Test
    void toLocalDateTime_NullAsRegexGiven_ShouldThrowEx() {
        assertThrows(IllegalArgumentException.class, () -> Dates.toLocalDateTime("4.7.11", null));
    }

    @Test
    void toLocalDateTime_DateGiven_ShouldReturnLocalDate() {
        int year = 2011;
        int month = 7;
        int day = 4;
        int hour = 14;
        int minute = 15;
        int second = 16;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second); // -1, da hier ab 0 gezaehlt wird
        Date date = calendar.getTime();
        LocalDateTime localDateTime = Dates.toLocalDateTime(date);
        assertNotNull(localDateTime);
        assertEquals(year, localDateTime.getYear());
        assertEquals(month, localDateTime.getMonthValue());
        assertEquals(day, localDateTime.getDayOfMonth());
        assertEquals(hour, localDateTime.getHour());
        assertEquals(minute, localDateTime.getMinute());
        assertEquals(second, localDateTime.getSecond());
    }

    @Test
    void toLocalDateTime_StringGiven_ShouldReturnLocalDate() {
        String value = "07.04.2011 14:15:16";
        String pattern = "dd.MM.yyyy HH:mm:ss";
        LocalDateTime localDate = Dates.toLocalDateTime(value, pattern);
        assertNotNull(pattern);
        assertEquals(2011, localDate.getYear());
        assertEquals(4, localDate.getMonthValue());
        assertEquals(7, localDate.getDayOfMonth());
        assertEquals(14, localDate.getHour());
        assertEquals(15, localDate.getMinute());
        assertEquals(16, localDate.getSecond());
    }

    @Test
    void toLocalDateTime_XmlGregorianCalendarGiven_ShouldReturnLocalDate() throws Exception {
        int year = 2011;
        int month = 4;
        int day = 7;
        int hour = 14;
        int minute = 15;
        int second = 16;
        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(year, month, day, hour, minute, second, 0, 0);
        LocalDateTime localDateTime = Dates.toLocalDateTime(xmlGregorianCalendar);
        assertNotNull(localDateTime);
        assertEquals(year, localDateTime.getYear());
        assertEquals(month, localDateTime.getMonthValue());
        assertEquals(day, localDateTime.getDayOfMonth());
        assertEquals(hour, localDateTime.getHour());
        assertEquals(minute, localDateTime.getMinute());
        assertEquals(second, localDateTime.getSecond());
    }

    @Test
    void toDate_NullGiven_ShouldReturnNull() {
        assertNull(Dates.toDate((LocalDate) null));
        assertNull(Dates.toDate((LocalDateTime) null));
        assertNull(Dates.toDate((XMLGregorianCalendar) null));
        assertNull(Dates.toDate(null, "dummyPattern"));
    }

    @Test
    void toDate_LocalDateGiven_ShouldReturnLocalDate() {
        LocalDate localDate = LocalDate.now();
        Date date = Dates.toDate(localDate);
        assertNotNull(date);
    }

    @Test
    void toDate_LocalDateTimeGiven_ShouldReturnLocalDate() {
        LocalDateTime localDate = LocalDateTime.now();
        Date date = Dates.toDate(localDate);
        assertNotNull(date);
    }

    @Test
    void toDate_ValidStringGiven_ShouldReturnLocalDate() {
        String value = "04.07.2011";
        Date date = Dates.toDate(value, "dd.MM.yyyy");
        assertNotNull(date);
    }

    @Test
    void toDate_InvalidStringGiven_ShouldThrowEx() {
        assertThrows(IllegalArgumentException.class, () -> Dates.toDate("30.2.11", "dd.MM.yyyy"));
    }

    @Test
    void toDate_NullAsPatternGiven_ShouldThrowEx() {
        assertThrows(IllegalArgumentException.class, () -> Dates.toDate("30.2.11", null));
    }

    @Test
    void toString_NullGiven_ShouldReturnNull() {
        assertNull(Dates.toString((Date) null, "dummyPattern"));
        assertNull(Dates.toString((LocalDate) null, "dummyPattern"));
        assertNull(Dates.toString((LocalDateTime) null, "dummyPattern"));
    }

    @Test
    void toString_NullAsPatternGiven_ShouldThrowEx() {
        assertThrows(IllegalArgumentException.class, () -> Dates.toString(new Date(), null));
        assertThrows(IllegalArgumentException.class, () -> Dates.toString(LocalDate.now(), null));
        assertThrows(IllegalArgumentException.class, () -> Dates.toString(LocalDateTime.now(), null));
    }

    @Test
    void toString_DateGiven_ShouldReturnString() {
        Date date = new Date();
        String value = Dates.toString(date, "dd.MM.yyyy");
        assertNotNull(value);
    }

    @Test
    void toString_LocalDateGiven_ShouldReturnString() {
        LocalDate localDate = LocalDate.now();
        String value = Dates.toString(localDate, "dd.MM.yyyy");
        assertNotNull(value);
    }

    @Test
    void toString_LocalDateTimeGiven_ShouldReturnString() {
        LocalDateTime localDateTime = LocalDateTime.now();
        String value = Dates.toString(localDateTime, "dd.MM.yyyy");
        assertNotNull(value);
    }

    @Test
    void toXMLGregorianCalendar_NullGiven_ShouldReturnString() {
        assertNull(Dates.toXMLGregorianCalendar(null));
    }

    @Test
    void toXMLGregorianCalendar_DateGiven_ShouldReturnString() {
        Date date = new Date();
        XMLGregorianCalendar xmlGregorianCalendar = Dates.toXMLGregorianCalendar(date);
        assertNotNull(xmlGregorianCalendar);
    }

    @Disabled
    @Test
    void toXMLGregorianCalendar_ExceptionInDatatypeFactory_ShouldThrowEx() throws Exception {
        // Vollqualifizierte Klassenname einer nicht vorhandenen (Dummy-)Implementierung.
        String nonExistingDatatypeFactoryClassName = "de.hzd.commons.DatesTest$NonExistingDatatypeFactory";
        // Vollqualifizierte Klassenname der Test-Implementierung. Diese wirf eine Exception im Konstruktor
        String throwingDatatypeFactoryClassName = ThrowingDatatypeFactory.class.getName();
        for(String datatypeFactoryClassName : Arrays.asList(nonExistingDatatypeFactoryClassName, throwingDatatypeFactoryClassName)) {
            // Die Dates-Klasse "cached" die DatatypeFactory in einem private static field
            // Damit wir in diesem Test bewusst unsere "falsche" Factory bekommen, müssen wir
            // dies Field zuerst per Reflection auf "null" setzen. Dadurch wird dann anschließend
            // innerhalb der Dates-Klasse die neue (hier nichts existente) Factory geladen.
            // Somit müsste dann die gewünschte Exception geworfen werden.
            Field datatypeFactoryField = Dates.class.getDeclaredField("datatypeFactory");
            datatypeFactoryField.setAccessible(true);
            Object originalDatatypeFactory = datatypeFactoryField.get(null);
            datatypeFactoryField.set(null, null);
            // Jetzt die System-Property auf unsere Dummy-Klasse setzen
            System.setProperty(DatatypeFactory.class.getName(), datatypeFactoryClassName);
            try {
                Dates.toXMLGregorianCalendar(new Date());
            } catch (IllegalStateException ex) {
                Throwable cause = ex.getCause();
                assertTrue(cause instanceof DatatypeConfigurationException, "Cause: " + cause);
            } finally {
                // Damit andere, nachfolgende Tests nicht auf die gleiche Exception laufen,
                // stellen wir am Ende noch den Ursprungszustand wieder her.
                if (originalDatatypeFactory != null) {
                    System.setProperty(DatatypeFactory.class.getName(), originalDatatypeFactory.getClass().getName());
                }
            }

        }
    }

    private static class ThrowingDatatypeFactory extends DatatypeFactory {

        public static final String MESSAGE = "Forcing a " + DatatypeConfigurationException.class.getName();

        public ThrowingDatatypeFactory() {
            throw new RuntimeException(MESSAGE);
        }

        @Override
        public Duration newDuration(String lexicalRepresentation) {
            return null;
        }

        @Override
        public Duration newDuration(long durationInMilliSeconds) {
            return null;
        }

        @Override
        public Duration newDuration(boolean isPositive, BigInteger years, BigInteger months, BigInteger days, BigInteger hours, BigInteger minutes, BigDecimal seconds) {
            return null;
        }

        @Override
        public XMLGregorianCalendar newXMLGregorianCalendar() {
            return null;
        }

        @Override
        public XMLGregorianCalendar newXMLGregorianCalendar(String lexicalRepresentation) {
            return null;
        }

        @Override
        public XMLGregorianCalendar newXMLGregorianCalendar(GregorianCalendar cal) {
            return null;
        }

        @Override
        public XMLGregorianCalendar newXMLGregorianCalendar(BigInteger year, int month, int day, int hour, int minute, int second, BigDecimal fractionalSecond, int timezone) {
            return null;
        }
    }

}
