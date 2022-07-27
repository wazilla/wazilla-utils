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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public final class Dates {

    private Dates() {
        // Utility class
    }

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(String value, String pattern) throws ParseException {
        if (value == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(Objects.requireNonNull(pattern));
        return sdf.parse(value);
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

}
