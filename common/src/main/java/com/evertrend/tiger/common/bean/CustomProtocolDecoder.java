/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package com.evertrend.tiger.common.bean;

import com.evertrend.tiger.common.utils.general.LogUtil;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.DecimalFormat;

import org.apache.mina.core.buffer.BufferDataException;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderException;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.RecoverableProtocolDecoderException;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A {@link ProtocolDecoder} which decodes a text line into a string.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
//public class CustomProtocolDecoder extends TextLineDecoder {
public class CustomProtocolDecoder extends CumulativeProtocolDecoder {
    private static final AttributeKey CONTEXT = new AttributeKey(CustomProtocolDecoder.class, "context");
    private static final String TAG = "CustomProtocolDecoder";

    private final Charset charset;
    private boolean isHex = false;
    private int length = 0;
    private StringBuffer totalContent;

    /**
     * The delimiter used to determinate when a line has been fully decoded
     */
    private final LineDelimiter delimiter;

    /**
     * An IoBuffer containing the delimiter
     */
    private IoBuffer delimBuf;

    /**
     * The default maximum Line length. Default to 1024.
     */
    private int maxLineLength = 2 * 1024 * 1024;

    /**
     * The default maximum buffer length. Default to 128 chars.
     */
    private int bufferLength = 128;

    /**
     * Creates a new instance with the current default {@link Charset}
     * and {@link LineDelimiter#AUTO} delimiter.
     */
    public CustomProtocolDecoder() {
        this(LineDelimiter.AUTO);
    }

    /**
     * Creates a new instance with the current default {@link Charset}
     * and the specified <tt>delimiter</tt>.
     *
     * @param delimiter The line delimiter to use
     */
    public CustomProtocolDecoder(String delimiter) {
        this(new LineDelimiter(delimiter));
    }

    /**
     * Creates a new instance with the current default {@link Charset}
     * and the specified <tt>delimiter</tt>.
     *
     * @param delimiter The line delimiter to use
     */
    public CustomProtocolDecoder(LineDelimiter delimiter) {
        this(Charset.defaultCharset(), delimiter);
    }

    /**
     * Creates a new instance with the spcified <tt>charset</tt>
     * and {@link LineDelimiter#AUTO} delimiter.
     *
     * @param charset The {@link Charset} to use
     */
    public CustomProtocolDecoder(Charset charset) {
        this(charset, LineDelimiter.AUTO);
    }

    /**
     * Creates a new instance with the spcified <tt>charset</tt>
     * and the specified <tt>delimiter</tt>.
     *
     * @param charset   The {@link Charset} to use
     * @param delimiter The line delimiter to use
     */
    public CustomProtocolDecoder(Charset charset, String delimiter) {
        this(charset, new LineDelimiter(delimiter));
    }

    /**
     * Creates a new instance with the specified <tt>charset</tt>
     * and the specified <tt>delimiter</tt>.
     *
     * @param charset   The {@link Charset} to use
     * @param delimiter The line delimiter to use
     */
    public CustomProtocolDecoder(Charset charset, LineDelimiter delimiter) {
        if (charset == null) {
            throw new IllegalArgumentException("charset parameter shuld not be null");
        }

        if (delimiter == null) {
            throw new IllegalArgumentException("delimiter parameter should not be null");
        }

        this.charset = charset;
        this.delimiter = delimiter;

        // Convert delimiter to ByteBuffer if not done yet.
        if (delimBuf == null) {
            IoBuffer tmp = IoBuffer.allocate(2).setAutoExpand(true);

            try {
                tmp.putString(delimiter.getValue(), charset.newEncoder());
            } catch (CharacterCodingException cce) {

            }

            tmp.flip();
            delimBuf = tmp;
        }
    }

    /**
     * @return the allowed maximum size of the line to be decoded.
     * If the size of the line to be decoded exceeds this value, the
     * decoder will throw a {@link BufferDataException}.  The default
     * value is <tt>1024</tt> (1KB).
     */
    public int getMaxLineLength() {
        return maxLineLength;
    }

    /**
     * Sets the allowed maximum size of the line to be decoded.
     * If the size of the line to be decoded exceeds this value, the
     * decoder will throw a {@link BufferDataException}.  The default
     * value is <tt>1024</tt> (1KB).
     *
     * @param maxLineLength The maximum line length
     */
    public void setMaxLineLength(int maxLineLength) {
        if (maxLineLength <= 0) {
            throw new IllegalArgumentException("maxLineLength (" + maxLineLength + ") should be a positive value");
        }

        this.maxLineLength = maxLineLength;
    }

    /**
     * Sets the default buffer size. This buffer is used in the Context
     * to store the decoded line.
     *
     * @param bufferLength The default bufer size
     */
    public void setBufferLength(int bufferLength) {
        if (bufferLength <= 0) {
            throw new IllegalArgumentException("bufferLength (" + maxLineLength + ") should be a positive value");

        }

        this.bufferLength = bufferLength;
    }

    /**
     * @return the allowed buffer size used to store the decoded line
     * in the Context instance.
     */
    public int getBufferLength() {
        return bufferLength;
    }

    /**
     * {@inheritDoc}
     */
//    @Override
//    public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
//        Context ctx = getContext(session);
//
//        LogUtil.d(TAG, "delimiter: "+delimiter);
//        if (LineDelimiter.AUTO.equals(delimiter)) {
//            LogUtil.d(TAG, "decodeAuto: ");
//            decodeAuto(ctx, session, in, out);
//        } else {
//            LogUtil.d(TAG, "decodeNormal: ");
//            decodeNormal(ctx, session, in, out);
//        }
//    }
    public float txfloat(long a, int b) {
        LogUtil.d(TAG, "a: "+a);
        DecimalFormat df = new DecimalFormat("0.0000");//设置保留位数
        return Float.valueOf(df.format((float)a / b));
    }

    public JSONObject strToJson(String[] content, String binData) {
        if (check(content)) {
//            LogUtil.d(TAG, "json start");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(RobotAction.CMD_CODE, 0x2A);
                jsonObject.put(RobotAction.RESULT_CODE, 0);
                JSONObject objectData = new JSONObject();
                LogUtil.d(TAG, "ox: "+content[16] + content[17] + content[18] + content[19]);
                LogUtil.d(TAG, "oy: "+content[20] + content[21] + content[22] + content[23]);
//                objectData.put(RobotAction.RESOLUTION, txfloat(Long.valueOf(content[6] + content[7], 16), 1000));
                objectData.put(RobotAction.RESOLUTION, txfloat(new BigInteger(content[6] + content[7], 16).intValue(), 1000));
                objectData.put(RobotAction.WIDTH, Long.valueOf(content[8] + content[9] + content[10] + content[11], 16));
                objectData.put(RobotAction.HEIGHT, Long.valueOf(content[12] + content[13] + content[14] + content[15], 16));
//                objectData.put(RobotAction.ORIGIN_X, txfloat(Long.valueOf(content[16] + content[17] + content[18] + content[19], 16), 10000));
//                objectData.put(RobotAction.ORIGIN_Y, txfloat(Long.valueOf(content[20] + content[21] + content[22] + content[23], 16), 10000));
//                objectData.put(RobotAction.ORIGIN_YAW, txfloat(Long.valueOf(content[24] + content[25] + content[26] + content[27], 16), 100));
                objectData.put(RobotAction.ORIGIN_X, txfloat(new BigInteger(content[16] + content[17] + content[18] + content[19], 16).intValue(), 10000));
                objectData.put(RobotAction.ORIGIN_Y, txfloat(new BigInteger(content[20] + content[21] + content[22] + content[23], 16).intValue(), 10000));
                objectData.put(RobotAction.ORIGIN_YAW, txfloat(new BigInteger(content[24] + content[25] + content[26] + content[27], 16).intValue(), 100));
                int length = content.length;
//                LogUtil.d(TAG, "length: "+length);
//                LogUtil.d(TAG, "str length: "+ binData.length());
                String data = binData.substring(27*2 - 2, length*2 - 2);
//                for (int i = 28; i < length - 1; i++) {
//                    data += content[i];
//                }
                objectData.put(RobotAction.DATA, data);
                jsonObject.put(RobotAction.TIME_STAMP, System.currentTimeMillis() / 1000);
                jsonObject.put(RobotAction.DATA, objectData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            LogUtil.d(TAG, "json end");
            return jsonObject;
        }
        return null;
    }

    private boolean check(String[] content) {
        String xorCode = "";
        int length = content.length;
        for (int i = 0; i < length - 1; i++) {
            if (i == 0) {
                xorCode = xorString(content[i], content[i + 1]);
            } else {
                xorCode = xorString(xorCode, content[i]);
            }
        }
        LogUtil.d(TAG, "xorCode: " + xorCode);
        LogUtil.d(TAG, "checkCode: " + content[length - 1]);
        if (xorCode.equalsIgnoreCase(content[length - 1])) {
            return true;
        }
        return false;
    }

    private String xorString(String strHex_X, String strHex_Y) {
        //将x、y转成二进制形式
        String anotherBinary = Integer.toBinaryString(Integer.valueOf(strHex_X, 16));
        String thisBinary = Integer.toBinaryString(Integer.valueOf(strHex_Y, 16));
        String result = "";
        //判断是否为8位二进制，否则左补零
        if (anotherBinary.length() != 8) {
            for (int i = anotherBinary.length(); i < 8; i++) {
                anotherBinary = "0" + anotherBinary;
            }
        }
        if (thisBinary.length() != 8) {
            for (int i = thisBinary.length(); i < 8; i++) {
                thisBinary = "0" + thisBinary;
            }
        }
        //异或运算
        for (int i = 0; i < anotherBinary.length(); i++) {
            //如果相同位置数相同，则补0，否则补1
            if (thisBinary.charAt(i) == anotherBinary.charAt(i))
                result += "0";
            else {
                result += "1";
            }
        }
        return Integer.toHexString(Integer.parseInt(result, 2));
    }

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
//        LogUtil.d(TAG, "in.remaining(): " + in.remaining());
        if (in.remaining() > 0) {
            in.mark();//标记当前位置，以便reset
            IoBuffer lengthB = in.getSlice(0, 4);
            IoBuffer cmdB = in.getSlice(4, 1);
            IoBuffer resultB = in.getSlice(5, 1);
            LogUtil.d(TAG, "cmd: " + cmdB.getHexDump());
//            LogUtil.d(TAG, "result: " + resultB.getHexDump());
//            in.reset();

            if (("2A".equals(cmdB.getHexDump()) && "00".equals(resultB.getHexDump())) || isHex) {
                isHex = true;
                if (length == 0) {
                    LogUtil.d(TAG, "length hex: " + lengthB.getHexDump());
                    length = Integer.valueOf(lengthB.getHexDump().replace(" ", ""), 16);
                    LogUtil.d(TAG, "length: " + length);
                    totalContent = new StringBuffer(length);
                }
                String content = in.getHexDump();
//                LogUtil.d(TAG, "hex: "+content);
                totalContent.append(content);
                totalContent.append(" ");
                String[] tmp = totalContent.toString().trim().split(" ");
                LogUtil.d(TAG, "tmp length: " + tmp.length);
                if (tmp.length >= length) {
                    JSONObject jsonObject = strToJson(tmp, totalContent.toString().trim().replace(" ", ""));
                    if (jsonObject != null) {
                        out.write(jsonObject);
                    }
                    totalContent = null;
                    isHex = false;
                    length = 0;
                    return true;
                } else {
                    in.reset();
                    return false;
                }
            } else {
                isHex = false;
                length = 0;
                Context ctx = getContext(session);
                return decodeAuto(ctx, session, in, out);
            }
        }
        return true;
    }

    /**
     * @param session The session for which we want the context
     * @return the context for this session
     */
    private Context getContext(IoSession session) {
        Context ctx;
        ctx = (Context) session.getAttribute(CONTEXT);

        if (ctx == null) {
            ctx = new Context(bufferLength);
            session.setAttribute(CONTEXT, ctx);
        }

        return ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose(IoSession session) throws Exception {
        Context ctx = (Context) session.getAttribute(CONTEXT);

        if (ctx != null) {
            session.removeAttribute(CONTEXT);
        }
    }

    /**
     * Decode a line using the default delimiter on the current system
     */
    private boolean decodeAuto(Context ctx, IoSession session, IoBuffer in, ProtocolDecoderOutput out)
            throws CharacterCodingException, ProtocolDecoderException {
        int matchCount = ctx.getMatchCount();

        // Try to find a match
        int oldPos = in.position();
        int oldLimit = in.limit();

        while (in.hasRemaining()) {
            byte b = in.get();
            boolean matched = false;

            switch (b) {
                case '\r':
                    // Might be Mac, but we don't auto-detect Mac EOL
                    // to avoid confusion.
                    matchCount++;
                    break;

                case '\n':
                    // UNIX
                    matchCount++;
                    matched = true;
                    break;

                default:
                    matchCount = 0;
            }

            if (matched) {
                // Found a match.
                int pos = in.position();
                in.limit(pos);
                in.position(oldPos);

                ctx.append(in);

                in.limit(oldLimit);
                in.position(pos);

                if (ctx.getOverflowPosition() == 0) {
                    IoBuffer buf = ctx.getBuffer();
                    buf.flip();
                    buf.limit(buf.limit() - matchCount);

                    try {
                        byte[] data = new byte[buf.limit()];
                        buf.get(data);
                        CharsetDecoder decoder = ctx.getDecoder();

                        CharBuffer buffer = decoder.decode(ByteBuffer.wrap(data));
                        String str = buffer.toString();
                        writeText(session, str, out);
                    } finally {
                        buf.clear();
                    }
                } else {
                    int overflowPosition = ctx.getOverflowPosition();
                    ctx.reset();
                    throw new RecoverableProtocolDecoderException("Line is too long: " + overflowPosition);
                }

                oldPos = pos;
                matchCount = 0;
                return true;
            }
        }

        // Put remainder to buf.
        in.position(oldPos);
        ctx.append(in);

        ctx.setMatchCount(matchCount);
        return false;
    }

    /**
     * Decode a line using the delimiter defined by the caller
     */
    private void decodeNormal(Context ctx, IoSession session, IoBuffer in, ProtocolDecoderOutput out)
            throws CharacterCodingException, ProtocolDecoderException {
        int matchCount = ctx.getMatchCount();

        // Try to find a match
        int oldPos = in.position();
        int oldLimit = in.limit();

        while (in.hasRemaining()) {
            byte b = in.get();

            if (delimBuf.get(matchCount) == b) {
                matchCount++;

                if (matchCount == delimBuf.limit()) {
                    // Found a match.
                    int pos = in.position();
                    in.limit(pos);
                    in.position(oldPos);

                    ctx.append(in);

                    in.limit(oldLimit);
                    in.position(pos);

                    if (ctx.getOverflowPosition() == 0) {
                        IoBuffer buf = ctx.getBuffer();
                        buf.flip();
                        buf.limit(buf.limit() - matchCount);

                        try {
                            writeText(session, buf.getString(ctx.getDecoder()), out);
                        } finally {
                            buf.clear();
                        }
                    } else {
                        int overflowPosition = ctx.getOverflowPosition();
                        ctx.reset();
                        throw new RecoverableProtocolDecoderException("Line is too long: " + overflowPosition);
                    }

                    oldPos = pos;
                    matchCount = 0;
                }
            } else {
                // fix for DIRMINA-506 & DIRMINA-536
                in.position(Math.max(0, in.position() - matchCount));
                matchCount = 0;
            }
        }

        // Put remainder to buf.
        in.position(oldPos);
        ctx.append(in);

        ctx.setMatchCount(matchCount);
    }

    /**
     * By default, this method propagates the decoded line of text to
     * {@code ProtocolDecoderOutput#write(Object)}.  You may override this method to modify
     * the default behavior.
     *
     * @param session the {@code IoSession} the received data.
     * @param text    the decoded text
     * @param out     the upstream {@code ProtocolDecoderOutput}.
     */
    protected void writeText(IoSession session, String text, ProtocolDecoderOutput out) {
        out.write(text);
    }

    /**
     * A Context used during the decoding of a lin. It stores the decoder,
     * the temporary buffer containing the decoded line, and other status flags.
     *
     * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
     * @version $Rev$, $Date$
     */
    private class Context {
        /**
         * The decoder
         */
        private final CharsetDecoder decoder;

        /**
         * The temporary buffer containing the decoded line
         */
        private final IoBuffer buf;

        /**
         * The number of lines found so far
         */
        private int matchCount = 0;

        /**
         * A counter to signal that the line is too long
         */
        private int overflowPosition = 0;

        /**
         * Create a new Context object with a default buffer
         */
        private Context(int bufferLength) {
            decoder = charset.newDecoder();
            buf = IoBuffer.allocate(bufferLength).setAutoExpand(true);
        }

        public CharsetDecoder getDecoder() {
            return decoder;
        }

        public IoBuffer getBuffer() {
            return buf;
        }

        public int getOverflowPosition() {
            return overflowPosition;
        }

        public int getMatchCount() {
            return matchCount;
        }

        public void setMatchCount(int matchCount) {
            this.matchCount = matchCount;
        }

        public void reset() {
            overflowPosition = 0;
            matchCount = 0;
            decoder.reset();
        }

        public void append(IoBuffer in) {
            if (overflowPosition != 0) {
                discard(in);
            } else if (buf.position() > maxLineLength - in.remaining()) {
                overflowPosition = buf.position();
                buf.clear();
                discard(in);
            } else {
                getBuffer().put(in);
            }
        }

        private void discard(IoBuffer in) {
            if (Integer.MAX_VALUE - in.remaining() < overflowPosition) {
                overflowPosition = Integer.MAX_VALUE;
            } else {
                overflowPosition += in.remaining();
            }

            in.position(in.limit());
        }
    }
}