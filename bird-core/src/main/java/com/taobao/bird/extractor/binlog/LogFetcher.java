package com.taobao.bird.extractor.binlog;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;


/**
 * @desc 
 * @author junyu 2015��10��3������8:56:32
 * @version 
 **/
public abstract class LogFetcher extends LogBuffer implements Closeable {

    /** Default initial capacity. */
    public static final int   DEFAULT_INITIAL_CAPACITY = 8192;

    /** Default growth factor. */
    public static final float DEFAULT_GROWTH_FACTOR    = 2.0f;

    /** Binlog file header size */
    public static final int   BIN_LOG_HEADER_SIZE      = 4;

    protected final float     factor;

    public LogFetcher()
    {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_GROWTH_FACTOR);
    }

    public LogFetcher(final int initialCapacity)
    {
        this(initialCapacity, DEFAULT_GROWTH_FACTOR);
    }

    public LogFetcher(final int initialCapacity, final float growthFactor)
    {
        this.buffer = new byte[initialCapacity];
        this.factor = growthFactor;
    }

    /**
     * Increases the capacity of this <tt>LogFetcher</tt> instance, if
     * necessary, to ensure that it can hold at least the number of elements
     * specified by the minimum capacity argument.
     * 
     * @param minCapacity the desired minimum capacity
     */
    protected final void ensureCapacity(final int minCapacity)
    {
        final int oldCapacity = buffer.length;

        if (minCapacity > oldCapacity)
        {
            int newCapacity = (int) (oldCapacity * factor);
            if (newCapacity < minCapacity)
                newCapacity = minCapacity;

            buffer = Arrays.copyOf(buffer, newCapacity);
        }
    }

    /**
     * Fetches the next frame of binary-log, and fill it in buffer.
     */
    public abstract boolean fetch() throws IOException;

    /**
     * {@inheritDoc}
     * 
     * @see java.io.Closeable#close()
     */
    public abstract void close() throws IOException;

}
