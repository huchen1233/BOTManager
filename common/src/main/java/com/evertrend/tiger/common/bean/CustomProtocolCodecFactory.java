package com.evertrend.tiger.common.bean;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.textline.TextLineEncoder;

import java.nio.charset.Charset;

public class CustomProtocolCodecFactory implements ProtocolCodecFactory {

    private final ProtocolEncoder encoder;
    private final ProtocolDecoder decoder;

    public CustomProtocolCodecFactory() {
        this(Charset.forName("UTF-8"));
    }

    // 构造方法注入编解码器
    public CustomProtocolCodecFactory(Charset charset) {
        this.encoder = new CustomProtocolEncoder(charset);
        this.decoder = new CustomProtocolDecoder(charset);
//        this.decoder = new BigDataProtocolDecoder();
    }

    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }

}
