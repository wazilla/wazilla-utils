package de.hzd.commons;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Die Klasse Streams bietet Methoden zum Umgang mit Byte-Streams (vor allen
 * {@link InputStream} und {@link OutputStream}) an. Die größe des internen Puffers
 * kann durch setzen der System-Property "de.hzd.commons.Streams.bufferSize"
 * angepasst werden.
 *
 * @author Ralf Lang
 */
public final class Streams {

    private static final int BUFFER_SIZE = Integer.getInteger(Streams.class.getName() + ".bufferSize", 4096);

    private Streams() {
        // Utilityclass!
    }

    /**
     * Schliesst den übergebenen Stream, sollte dabei eine {@link IOException}
     * auftreten, so wird diese ignoriert.
     *
     * @param closeable der Stream der geschlossen werden soll.
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ex) {
                // Ignore
            }
        }
    }

    /**
     * Kopiert den {@link InputStream} in den {@link OutputStream}.
     *
     * @param in  der {@link InputStream} von dem gelesen wird
     * @param out der {@link OutputStream} in den geschrieben wird
     * @throws IOException wenn beim Kopieren ein Fehler auftritt
     */
    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int len = -1;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        close(out);
        close(in);
    }

    /**
     * Liest den übergebenen {@link InputStream} in ein byte[] ein.
     *
     * @param in der {@link InputStream} der gelesen werden soll.
     * @return ein byte[] mit dem Inhalt des Streams
     * @throws IOException wenn der Stream nicht gelesen werden konnte
     */
    public static byte[] read(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copy(in, out);
        return out.toByteArray();
    }
}
