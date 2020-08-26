package com.evertrend.tiger.common.utils;

public class ConnectConfig {

    private String ip;
    private int port;
    private int readBufferSize; //缓存大小
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

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public static class Builder{
        private String ip = "";
        private int port = 28700;
        private int readBufferSize = 10240; //缓存大小
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

        public ConnectConfig bulid(){
            ConnectConfig connectConfig = new ConnectConfig();
            connectConfig.connectionTimeout = this.connectionTimeout;
            connectConfig.ip = this.ip;
            connectConfig.port = this.port;
            connectConfig.readBufferSize = this.readBufferSize;
            return  connectConfig;
        }
    }
}
