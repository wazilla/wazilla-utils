package de.wazilla.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

}
