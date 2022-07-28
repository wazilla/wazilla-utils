package de.hzd.commons;

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

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Hilfsklasse für Datums- und Zeitklassen wie {@link Date}, {@link LocalDate} und {@link LocalDateTime}.
 *
 * @author Ralf Lang
 */
public final class Dates {

    // volatile is required for lazy init with double checked locking!
    private static volatile DatatypeFactory datatypeFactory;

    private Dates() {
        // Utilityclass
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

    /**
     * Wandelt ein {@link LocalDate} in ein {@link Date} um
     *
     * @param localDate das {@link LocalDate}
     * @return ein {@link Date} oder null
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) return null;
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Wandelt ein {@link LocalDateTime} in ein {@link Date} um
     *
     * @param localDateTime das {@link LocalDateTime}
     * @return ein {@link Date} oder null
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Wandelt einen String in ein {@link Date} um
     *
     * @param date    das Datum als String
     * @param pattern das Datumsformat
     * @return ein {@link Date} oder null
     * @throws IllegalArgumentException wenn das pattern null ist oder das Datum mit dem Datumsformat nicht geparst werden konnte.
     */
    public static Date toDate(String date, String pattern) {
        if (date == null) return null;
        try {
            return parse(date, pattern);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Ungültiges Datum: " + date, ex);
        }
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
     * Wandelt ein Date in einen String um.
     *
     * @param date das {@link Date}
     * @param pattern das Datumsformat
     * @return ein String oder null
     * @throws IllegalArgumentException wenn das pattern null ist
     */
    public static String toString(Date date, String pattern) {
        if (date == null) return null;
        if (pattern == null) throw new IllegalArgumentException("pattern==null");
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * Wandelt ein {@link LocalDate} in einen String um.
     *
     * @param localDate das {@link LocalDate}
     * @param pattern das Datumsformat
     * @return einen {@link String} oder null
     * @throws IllegalArgumentException wenn das pattern null ist
     */
    public static String toString(LocalDate localDate, String pattern) {
        if (localDate == null) return null;
        if (pattern == null) throw new IllegalArgumentException("pattern==null");
        return DateTimeFormatter.ofPattern(pattern).format(localDate);
    }

    /**
     * Wandelt ein {@link LocalDateTime} in einen String um.
     *
     * @param localDateTime das {@link LocalDateTime}
     * @param pattern das Datumsformat
     * @return einen String oder null
     */
    public static String toString(LocalDateTime localDateTime, String pattern) {
        if (localDateTime == null) return null;
        if (pattern == null) throw new IllegalArgumentException("pattern==null");
        return DateTimeFormatter.ofPattern(pattern).format(localDateTime);
    }

    /**
     * Wandelt ein {@link Date} in einen {@link XMLGregorianCalendar} um.
     *
     * @param date das {@link Date}
     * @return einen {@link XMLGregorianCalendar} oder null
     */
    public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
        if (date == null) return null;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        return getDatatypeFactory().newXMLGregorianCalendar(gregorianCalendar);
    }

    private static Date parse(String value, String pattern) throws ParseException {
        if (pattern == null) throw new IllegalArgumentException("pattern==null");
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(false);
        return sdf.parse(value);
    }

    private static DatatypeFactory getDatatypeFactory() {
        if (datatypeFactory == null) {
            synchronized (Dates.class) {
                // double checked locking
                if (datatypeFactory == null) {
                    try {
                        System.out.println("Erzeuge neue DatatypeFactory");
                        datatypeFactory = DatatypeFactory.newInstance();
                        System.out.println("DatatypeFactory=" + datatypeFactory.getClass().getName());
                    } catch (DatatypeConfigurationException ex) {
                        throw new IllegalStateException("Es wurde ein schwerer Konfigurationsfehler bei der verwendeten XML-Implementierung festgestellt!", ex);
                    }
                }
            }
        }
        return datatypeFactory;
    }

}
