package com.evertrend.tiger.common.bean;

import com.evertrend.tiger.common.utils.general.LogUtil;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineEncoder;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class CustomProtocolEncoder extends ProtocolEncoderAdapter {
    private static final AttributeKey ENCODER = new AttributeKey(CustomProtocolEncoder.class, "encoder");

    private final Charset charset;

    private final LineDelimiter delimiter;

    private int maxLineLength = Integer.MAX_VALUE;

    /**
     * Creates a new instance with the current default {@link Charset}
     * and {@link LineDelimiter#UNIX} delimiter.
     */
    public CustomProtocolEncoder() {
        this(Charset.defaultCharset(), LineDelimiter.UNIX);
    }

    /**
     * Creates a new instance with the current default {@link Charset}
     * and the specified <tt>delimiter</tt>.
     *
     * @param delimiter The line delimiter to use
     */
    public CustomProtocolEncoder(String delimiter) {
        this(new LineDelimiter(delimiter));
    }

    /**
     * Creates a new instance with the current default {@link Charset}
     * and the specified <tt>delimiter</tt>.
     *
     * @param delimiter The line delimiter to use
     */
    public CustomProtocolEncoder(LineDelimiter delimiter) {
        this(Charset.defaultCharset(), delimiter);
    }

    /**
     * Creates a new instance with the specified <tt>charset</tt>
     * and {@link LineDelimiter#UNIX} delimiter.
     *
     * @param charset The {@link Charset} to use
     */
    public CustomProtocolEncoder(Charset charset) {
        this(charset, LineDelimiter.UNIX);
    }

    /**
     * Creates a new instance with the specified <tt>charset</tt>
     * and the specified <tt>delimiter</tt>.
     *
     * @param charset The {@link Charset} to use
     * @param delimiter The line delimiter to use
     */
    public CustomProtocolEncoder(Charset charset, String delimiter) {
        this(charset, new LineDelimiter(delimiter));
    }

    /**
     * Creates a new instance with the specified <tt>charset</tt>
     * and the specified <tt>delimiter</tt>.
     *
     * @param charset The {@link Charset} to use
     * @param delimiter The line delimiter to use
     */
    public CustomProtocolEncoder(Charset charset, LineDelimiter delimiter) {
        if (charset == null) {
            throw new IllegalArgumentException("charset");
        }
        if (delimiter == null) {
            throw new IllegalArgumentException("delimiter");
        }
        if (LineDelimiter.AUTO.equals(delimiter)) {
            throw new IllegalArgumentException("AUTO delimiter is not allowed for encoder.");
        }

        this.charset = charset;
        this.delimiter = delimiter;
    }

    /**
     * @return the allowed maximum size of the encoded line.
     * If the size of the encoded line exceeds this value, the encoder
     * will throw a {@link IllegalArgumentException}.  The default value
     * is {@link Integer#MAX_VALUE}.
     */
    public int getMaxLineLength() {
        return maxLineLength;
    }

    /**
     * Sets the allowed maximum size of the encoded line.
     * If the size of the encoded line exceeds this value, the encoder
     * will throw a {@link IllegalArgumentException}.  The default value
     * is {@link Integer#MAX_VALUE}.
     *
     * @param maxLineLength The maximum line length
     */
    public void setMaxLineLength(int maxLineLength) {
        if (maxLineLength <= 0) {
            throw new IllegalArgumentException("maxLineLength: " + maxLineLength);
        }

        this.maxLineLength = maxLineLength;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        CharsetEncoder encoder = (CharsetEncoder) session.getAttribute(ENCODER);

        if (encoder == null) {
            encoder = charset.newEncoder();
            session.setAttribute(ENCODER, encoder);
        }

        String value = message == null ? "" : message.toString()+"\n";
        IoBuffer buf = IoBuffer.allocate(value.length()).setAutoExpand(true);

        buf.putString(value, encoder);//java.nio.BufferUnderflowException

        if (buf.position() > maxLineLength) {
            throw new IllegalArgumentException("Line length: " + buf.position());
        }

//        buf.putString(delimiter.getValue(), encoder);
        buf.flip();
        out.write(buf);
    }

    /**
     * Dispose the encoder
     *
     * @throws Exception If the dispose failed
     */
    public void dispose() throws Exception {
        // Do nothing
    }
}
