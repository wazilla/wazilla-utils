package de.wazilla.utils;

public final class Strings {

    private static final String DEFAULT_FILLER = System.getProperty(String.class.getName() + ".defaultFiller", " ");
    private static final String NUMERIC_CHARS = "0123456789";
    private static final String ALPHA_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHANUMERIC_CHARS = NUMERIC_CHARS + ALPHA_CHARS;

    private Strings() {
        // Utility class
    }

    public static boolean isNotNullOrBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isNotNullOrEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    /**
     * Füllt den übergebenen String links (also am Anfang) bis zur angeg. Länge auf. Als Füllzeichen wird der Default (Blank)
     * verwendet, sofern über die entsprechende System-Property kein abweichender Wert gesetzt wurde.
     *
     * @param value der aufzufüllende String
     * @param len   die Länge, die erreicht werden soll
     * @return den aufgefüllten String
     */
    public static String leftPad(String value, int len) {
        return leftPad(value, len, DEFAULT_FILLER);
    }

    /**
     * Füllt den übergebenen String links (also am Anfang) bis zur angeg. Länge auf mit dem angegebenen Füllzeichen auf.
     *
     * @param value  der aufzufüllende String
     * @param len    die Länge, die erreicht werden soll
     * @param filler das Füllzeichen
     * @return den aufgefüllten String
     */
    public static String leftPad(String value, int len, char filler) {
        return leftPad(value, len, Character.toString(filler));
    }

    /**
     * Füllt den übergebenen String links (also am Anfang) bis zur angeg. Länge auf mit dem angegebenen Füll-String auf.
     * Wenn der Füll-String nicht mehr genau zur angeg. Länge eingefügt werden kann, dann wird er am Ende abgeschnitten.
     *
     * @param value  der aufzufüllende String
     * @param len    die Länge, die erreicht werden soll
     * @param filler der Füll-String
     * @return den aufgefüllten String
     */
    public static String leftPad(String value, int len, String filler) {
        if (value == null) return null;
        if (len < 0) throw new IllegalArgumentException("len < 0!");
        if (filler == null || filler.isEmpty()) throw new IllegalArgumentException("filler='" + filler + "'");
        StringBuilder sb = new StringBuilder(value);
        while (sb.length() < len) {
            // Nur so viel vom Filler einsetzen, dass die max. Laenge nicht ueberschritten wird.
            int max = sb.length() + filler.length() > len ? len - sb.length() : filler.length();
            sb.insert(0, filler.substring(0, max));
        }
        return sb.toString();
    }

    /**
     * Füllt den übergebenen String rechts (also am Ende) bis zur angeg. Länge auf. Als Füllzeichen wird der Default (Blank)
     * verwendet, sofern über die entsprechende System-Property kein abweichender Wert gesetzt wurde.
     *
     * @param value der aufzufüllende String
     * @param len   die Länge, die erreicht werden soll
     * @return den aufgefüllten String
     */
    public static String rightPad(String value, int len) {
        return rightPad(value, len, DEFAULT_FILLER);
    }

    /**
     * Füllt den übergebenen String rechts (also am Ende) bis zur angeg. Länge auf mit dem angegebenen Füllzeichen auf.
     *
     * @param value  der aufzufüllende String
     * @param len    die Länge, die erreicht werden soll
     * @param filler das Füllzeichen
     * @return den aufgefüllten String
     */
    public static String rightPad(String value, int len, char filler) {
        return rightPad(value, len, Character.toString(filler));
    }

    /**
     * Füllt den übergebenen String rechts (also am Ende) bis zur angeg. Länge auf mit dem angegebenen Füll-String auf.
     * Wenn der Füll-String nicht mehr genau zur angeg. Länge eingefügt werden kann, dann wird er am Ende abgeschnitten.
     *
     * @param value  der aufzufüllende String
     * @param len    die Länge, die erreicht werden soll
     * @param filler der Füll-String
     * @return den aufgefüllten String
     */
    public static String rightPad(String value, int len, String filler) {
        if (value == null) return null;
        if (len < 0) throw new IllegalArgumentException("len < 0!");
        if (filler == null || filler.isEmpty()) throw new IllegalArgumentException("filler='" + filler + "'");
        StringBuilder sb = new StringBuilder(value);
        while (sb.length() < len) {
            if (sb.length() + filler.length() <= len) {
                sb.append(filler);
            } else {
                sb.append(filler, 0, len - sb.length());
            }
        }
        return sb.toString();
    }

    /**
     * Erzeugt eine Zeichenketten mit zufaelligen Buchstaben
     *
     * @param len Länge (Anzahl Zeichen) der zu erzeugenden Zeichenkette
     * @return einen String mit zufälligen Buchstaben in der angeg. Länge
     */
    public static String randomAlpha(int len) {
        return random(len, ALPHA_CHARS);
    }

    /**
     * Erzeugt eine Zeichenketten mit zufaelligen Buchstaben und Zahlen
     *
     * @param len Länge (Anzahl Zeichen) der zu erzeugenden Zeichenkette
     * @return einen String mit zufälligen Buchstaben und Zahlen in der angeg. Länge
     */
    public static String randomAlphaNumeric(int len) {
        return random(len, ALPHANUMERIC_CHARS);
    }

    /**
     * Erzeugt eine Zeichenketten mit zufaelligen Zahlen
     *
     * @param len Länge (Anzahl Zeichen) der zu erzeugenden Zeichenkette
     * @return einen String mit zufälligen Zahlen in der angeg. Länge
     */
    public static String randomNumeric(int len) {
        return random(len, NUMERIC_CHARS);
    }

    /**
     * Erzeugt eine Zeichenketten mit zufaelligen Zeichen aus dem angeg. Zeichenumfang
     *
     * @param len Länge (Anzahl Zeichen) der zu erzeugenden Zeichenkette
     * @param set einen String aus welchem die Zeichen ausgewählt werden
     * @return einen String mit zufälligen Zeichen in der angeg. Länge
     */
    public static String random(int len, String set) {
        if (len < 0) throw new IllegalArgumentException("len < 0!");
        if (set == null || set.isEmpty()) throw new IllegalArgumentException("set=" + set);
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            double random = Math.random() * set.length();
            int index = (int) Math.floor(random);
            char c = set.charAt(index);
            sb.append(c);
        }
        return sb.toString();
    }

    // TODO Javadoc
    public static String repeat(char filler, int len) {
        if (len <= 0) throw new IllegalArgumentException("len <= 0!");
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++) {
            sb.append(filler);
        }
        return sb.toString();
    }

}
