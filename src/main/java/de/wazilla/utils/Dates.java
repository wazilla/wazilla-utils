package de.wazilla.utils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public final class Dates {

    private Dates() {
        // Utility class
    }

    /**
     * Prüft, ob es sich bei dem übergebenen Datum um einen gütltigen Wert entsprechend dem angegebenen Pattern handelt.
     *
     * @param date    das zu prüfende Dateum
     * @param pattern das Pattern
     * @return true bei validen Datum, sonst false
     */
    public static boolean isValid(String date, String pattern) {
        return isValid(date, pattern, null);
    }

    /**
     * Prüft, ob es sich bei dem übergebenen Datum um einen gütltigen Wert entsprechend dem angegebenen Pattern handelt.
     * Für eine umfassendere Prüfung kann zusätzlich ein regulärer Ausdruck mitgegeben werden.
     *
     * @param date    das zu prüfende Dateum
     * @param pattern das Pattern
     * @param regex   regulärerer Ausdruck für umfassendere Prüfung.
     * @return true bei validen Datum, sonst false
     */
    public static boolean isValid(String date, String pattern, String regex) {
        if (date == null) return false;
        if (regex != null && !date.matches(regex)) return false;
        try {
            parse(date, pattern);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static Date toDate(LocalDate localDate) {
        if (localDate == null) return null;
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(String value, String pattern) throws ParseException {
        if (value == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(Objects.requireNonNull(pattern));
        sdf.setLenient(false);
        return sdf.parse(value);
    }

    /**
     * Wandelt einen {@link XMLGregorianCalendar} in ein {@link Date} um.
     *
     * @param xmlGregorianCalendar der {@link XMLGregorianCalendar}
     * @return ein {@link Date} oder null
     */
    public static Date toDate(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, xmlGregorianCalendar.getYear());
        calendar.set(Calendar.MONTH, xmlGregorianCalendar.getMonth() - 1);
        calendar.set(Calendar.DATE, xmlGregorianCalendar.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, xmlGregorianCalendar.getHour());
        calendar.set(Calendar.MINUTE, xmlGregorianCalendar.getMinute());
        calendar.set(Calendar.SECOND, xmlGregorianCalendar.getSecond());
        calendar.set(Calendar.MILLISECOND, xmlGregorianCalendar.getMillisecond());
        return calendar.getTime();
    }

    /**
     * Wandelt das {@link Date} in ein {@link LocalDate} um.
     *
     * @param date das Datum als {@link Date}
     * @return ein {@link LocalDate} oder null
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Wandelt eine Zeichenkette (z.B. ein {@link String}) in ein {@link LocalDate} um.
     *
     * @param cs      die Zeichenkette
     * @param pattern das Pattern für das Datumsformat
     * @return ein {@link LocalDate} oder null
     */
    public static LocalDate toLocalDate(CharSequence cs, String pattern) {
        if (cs == null) return null;
        if (pattern == null) throw new IllegalArgumentException("pattern==null");
        return LocalDate.parse(cs, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Wandelt einen {@link XMLGregorianCalendar} in ein LocalDate um.
     *
     * @param xmlGregorianCalendar der {@link XMLGregorianCalendar}
     * @return ein {@link LocalDate} oder null
     */
    public static LocalDate toLocalDate(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) return null;
        return toLocalDate(toDate(xmlGregorianCalendar));
    }

    /**
     * Wandelt ein {@link Date} in ein {@link LocalDateTime} um.
     *
     * @param date Das Date
     * @return ein {@link LocalDateTime} oder null.
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Wandelt eine Zeichenkette (z.B. einen {@link String}) in ein {@link LocalDateTime} um.
     *
     * @param cs      die Zeichenkette
     * @param pattern das Datumsformat
     * @return ein {@link LocalDateTime} oder null
     * @throws IllegalArgumentException wenn das pattern null ist
     * @throws DateTimeParseException   wenn das Datum nicht geparst werden konnte
     */
    public static LocalDateTime toLocalDateTime(CharSequence cs, String pattern) {
        if (cs == null) return null;
        if (pattern == null) throw new IllegalArgumentException("pattern==null");
        return LocalDateTime.parse(cs, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Wandelt einen {@link XMLGregorianCalendar} in ein {@link LocalDateTime} um.
     *
     * @param xmlGregorianCalendar der {@link XMLGregorianCalendar}
     * @return ein {@link LocalDateTime} oder null
     */
    public static LocalDateTime toLocalDateTime(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) return null;
        return toLocalDateTime(toDate(xmlGregorianCalendar));
    }


    public static String toString(Date date, String pattern) {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(Objects.requireNonNull(pattern));
        return sdf.format(date);
    }

    public static String toString(LocalDate localDate, String pattern) {
        if (localDate == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDate.format(formatter);
    }

    public static String toString(LocalDateTime localDateTime, String pattern) {
        if (localDateTime == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);
    }

    public static String toString(XMLGregorianCalendar xmlGregorianCalendar, String pattern) {
        return toString(toLocalDateTime(xmlGregorianCalendar), pattern);
    }

    public static XMLGregorianCalendar toXmlGregorianCalendar(Date date) throws DatatypeConfigurationException {
        if (date == null) return null;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        xmlGregorianCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
        return xmlGregorianCalendar;
    }

    public static XMLGregorianCalendar toXmlGregorianCalendar(LocalDate localDate) throws DatatypeConfigurationException {
        return toXmlGregorianCalendar(toDate(localDate));
    }

    public static XMLGregorianCalendar toXmlGregorianCalendar(LocalDateTime localDateTime) throws DatatypeConfigurationException {
        return toXmlGregorianCalendar(toDate(localDateTime));
    }

    private static Date parse(String value, String pattern) throws ParseException {
        if (pattern == null) throw new IllegalArgumentException("pattern==null");
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(false);
        return sdf.parse(value);
    }

}
