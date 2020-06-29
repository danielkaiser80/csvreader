package com.danielkaiser.csv_reader.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.IntStream;

import lombok.extern.log4j.Log4j2;

/**
 * {@link InputStream}, which replaces data read by replacements "on the fly" while reading.
 */
@Log4j2
public class ReplacingInputStream extends FilterInputStream {

    final LinkedList<Integer> inQueue = new LinkedList<>();
    final LinkedList<Integer> outQueue = new LinkedList<>();
    final byte[] search;
    final byte[] replacement;

    public ReplacingInputStream(InputStream in, byte[] search, byte[] replacement) {
        super(in);
        this.search = search;
        this.replacement = replacement;
    }

    /**
     * Does the actual reading of the next byte, if necessary doing the actual replacement.
     *
     * @return the read byte (as int)
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public int read() throws IOException {
        // Next byte already determined.
        if (outQueue.isEmpty()) {
            readAhead();

            if (isMatchFound()) {
                IntStream.range(0, search.length).forEach(i -> inQueue.remove());

                IntStream.range(0, replacement.length).forEach(i -> {
                    byte b = replacement[i];
                    outQueue.offer((int) b);
                });
            } else outQueue.add(inQueue.remove());
        }

        return outQueue.remove();
    }

    private boolean isMatchFound() {
        Iterator<Integer> inIter = inQueue.iterator();
        for (byte b : search) {
            if (!inIter.hasNext() || b != inIter.next()) {
                return false;
            }
        }
        return true;
    }

    private void readAhead() throws IOException {
        // Work up some look-ahead.
        while (inQueue.size() < search.length) {
            int next = super.read();
            inQueue.offer(next);
            if (next == -1) {
                break;
            }
        }
    }

    /**
     * Code taken from original {@link InputStream}, as the {@link FilterInputStream} actually delegates to the original {@link InputStream} and so our
     * override of read() method is not called.
     *
     * @param b   the buffer into which the data is read.
     * @param off the start offset in the destination array {@code b}
     * @param len the maximum number of bytes read.
     * @return the total number of bytes read into the buffer, or
     * {@code -1} if there is no more data because the end of
     * the stream has been reached.
     * @throws NullPointerException      If {@code b} is {@code null}.
     * @throws IndexOutOfBoundsException If {@code off} is negative,
     *                                   {@code len} is negative, or {@code len} is greater than
     *                                   {@code b.length - off}
     * @throws IOException               if an I/O error occurs.
     * @see java.io.FilterInputStream#in
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        Objects.checkFromIndexSize(off, len, b.length);
        if (len == 0) {
            return 0;
        }

        int c = read();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte) c;

        int i = 1;
        try {
            for (; i < len; i++) {
                c = read();
                if (c == -1) {
                    break;
                }
                b[off + i] = (byte) c;
            }
        } catch (final IOException ex) {
            log.warn("Exception occured while filtering InputStream, continue reading.", ex);
        }
        return i;
    }
}
