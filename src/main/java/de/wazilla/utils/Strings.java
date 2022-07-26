package de.wazilla.utils;

public final class Strings {

    private Strings() {
        // Utility class
    }

    public static boolean isNotNullOrBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isNotNullOrEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    public static String repeat(char filler, int len) {
        if (len <= 0) throw new IllegalArgumentException("len <= 0!");
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++) {
            sb.append(filler);
        }
        return sb.toString();
    }

}
