package com.evertrend.tiger.common.utils;

import com.evertrend.tiger.common.bean.CustomProtocolCodecFactory;
import com.evertrend.tiger.common.bean.event.ServerMsgEvent;
import com.evertrend.tiger.common.utils.general.LogUtil;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.greenrobot.eventbus.EventBus;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

public class ConnectManager {
    public static final String TAG = ConnectManager.class.getCanonicalName();
    private ConnectConfig mConfig;//配置文件
    private NioSocketConnector mConnection;
    private IoSession mSessioin;
    private InetSocketAddress mAddress;

    public ConnectManager(ConnectConfig mConfig) {
        this.mConfig = mConfig;
        init();
    }

    private void init() {
        mAddress = new InetSocketAddress(mConfig.getIp(),mConfig.getPort());
        //创建连接对象
        mConnection = new NioSocketConnector();
        //设置连接地址
        mConnection.setDefaultRemoteAddress(mAddress);
        mConnection.getSessionConfig().setReadBufferSize(mConfig.getReadBufferSize());
//        mConnection.getSessionConfig().setReceiveBufferSize(20*1024*1024);
        //设置过滤
        mConnection.getFilterChain().addLast("logger",new LoggingFilter());
//        TextLineCodecFactory factory = new TextLineCodecFactory(Charset.forName("UTF-8"));
        CustomProtocolCodecFactory factory = new CustomProtocolCodecFactory(Charset.forName("UTF-8"));
        //设置解码器长度，解决大数据无法接收问题
//        factory.setDecoderMaxLineLength(2*1024*1024);
//        factory.setEncoderMaxLineLength(2*1024*1024);
        mConnection.getFilterChain().addLast("codec", new ProtocolCodecFilter(factory));
//        mConnection.getFilterChain().addLast("exceutor", new ExecutorFilter(Executors.newCachedThreadPool()));
        //设置连接监听
        mConnection.setHandler(new DefaultHandler());
    }

    private static class DefaultHandler extends IoHandlerAdapter {

        @Override
        public void sessionCreated(IoSession session) throws Exception {
            LogUtil.d(TAG, "connect success: "+session);
            SessionManager.getmInstance().setIoSession(session);
//            super.sessionCreated(session);
        }

        /**
         * 连接成功时回调的方法
         * @param session
         * @throws Exception
         */
        @Override
        public void sessionOpened(IoSession session) throws Exception {
            //当与服务器连接成功时,将我们的session保存到我们的sesscionmanager类中,从而可以发送消息到服务器
//            LogUtil.d(TAG, "connect success: "+session);
//            SessionManager.getmInstance().setIoSession(session);
        }

        @Override
        public void messageSent(IoSession session, Object message) throws Exception {
            LogUtil.d(TAG, "messageSent");
            super.messageSent(session, message);
        }

        /**
         * 接收到消息时回调的方法
         * @param session
         * @param message
         * @throws Exception
         */
        @Override
        public void messageReceived(IoSession session, Object message) throws Exception {
//            LogUtil.d(TAG, "message Received: "+message.toString());
            EventBus.getDefault().post(new ServerMsgEvent(message.toString()));
        }

        @Override
        public void sessionClosed(IoSession session) throws Exception {
            LogUtil.d(TAG, "connect closed: "+session);
            SessionManager.getmInstance().setIoSession(null);
            super.sessionClosed(session);
        }
    }

    /**
     * 与服务器连接的方法
     * @return
     */
    public boolean connect(){
        try{
            ConnectFuture future = mConnection.connect();
            future.awaitUninterruptibly();
            mSessioin = future.getSession();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return mSessioin==null?false:true;
    }

    /**
     * 断开连接的方法
     */
    public void disConnect(){
        mConnection.dispose();
        mConnection=null;
        mSessioin=null;
        mAddress=null;
    }
}
