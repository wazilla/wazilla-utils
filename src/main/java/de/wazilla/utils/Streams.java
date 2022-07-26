package de.wazilla.utils;

import java.io.*;

public final class Streams {

    private static final int DEFAULT_BUFFER_SIZE = Integer.getInteger(Streams.class.getName() + ".defaultBufferSize", 4096);

    private Streams() {
        // Utility class
    }

    public static void close(Closeable... closeables) {
        for(Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    public static void copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int len = -1;
        while((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        close(in, out);
    }

    public static byte[] read(InputStream in) throws IOException {
        return read(in, DEFAULT_BUFFER_SIZE);
    }

    public static byte[] read(InputStream in, int bufferSize) throws IOException {
        if (in == null) return new byte[0];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copy(in, out, bufferSize);
        return out.toByteArray();
    }


}
