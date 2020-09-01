package com.evertrend.tiger.common.utils;

public class ConnectConfig {

    private String ip;
    private int port;
    private int readBufferSize; //缓存大小
    private int receiveBufferSize; //缓存大小
    private long connectionTimeout;//连接超时时间

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getReadBufferSize() {
        return readBufferSize;
    }

    public int getReceiveBufferSize() {
        return receiveBufferSize;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public static class Builder{
        private String ip = "";
        private int port = 28700;
        private int readBufferSize = 2*1024*1024; //缓存大小
        private int receiveBufferSize = 2*1024*1024; //接收缓存大小
        private long connectionTimeout = 10000;//连接超时时间

        public Builder setConnectionTimeout(long connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setReadBufferSize(int readBufferSize) {
            this.readBufferSize = readBufferSize;
            return this;
        }

        public Builder setReceiveBufferSize(int receiveBufferSize) {
            this.receiveBufferSize = receiveBufferSize;
            return this;
        }

        public ConnectConfig bulid(){
            ConnectConfig connectConfig = new ConnectConfig();
            connectConfig.connectionTimeout = this.connectionTimeout;
            connectConfig.ip = this.ip;
            connectConfig.port = this.port;
            connectConfig.readBufferSize = this.readBufferSize;
            connectConfig.receiveBufferSize = this.receiveBufferSize;
            return  connectConfig;
        }
    }
}
