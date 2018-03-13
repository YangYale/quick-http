package com.ipet.http.utils;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/7 14:47
 */
public final class HttpUri {

    public static final String SCHEMA_HTTP = "http";
    public static final String SCHEMA_HTTPS = "https";
    public static final String DEFAULT_TARGET = "/";

    private String host;
    private String schema;
    private int port;
    private String target;

    public HttpUri(String host, String schema) throws Exception{
        this.host = host;
        this.schema = schema;
        if(SCHEMA_HTTP.equalsIgnoreCase(this.schema)){
            this.port = 80;
        }else if(SCHEMA_HTTPS.equalsIgnoreCase(this.schema)){
            this.port = 443;
        }else{
            throw new Exception("The Http Schema Error,please use 'http' or 'https'.");
        }
        this.setTarget(DEFAULT_TARGET);
    }

    public HttpUri(String host, String schema, String target) throws Exception {
        this(host, schema);
        this.setTarget(target);
    }

    public HttpUri(String host, String schema, int port) throws Exception {
        this(host,schema);
        this.port = port;
    }

    public HttpUri(String host, String schema, int port, String target) throws Exception {
        this(host,schema,port);
        this.setTarget(target);
    }

    public HttpUri() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) throws Exception {
        this.schema = schema;
        if(!(SCHEMA_HTTP.equalsIgnoreCase(this.schema) || SCHEMA_HTTPS.equalsIgnoreCase(this.schema))){
            throw new Exception("The Http Schema Error,please use 'http' or 'https'.");
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "HttpUri{" +
                "host='" + host + '\'' +
                ", schema='" + schema + '\'' +
                ", port=" + port +
                ", target='" + target + '\'' +
                '}';
    }
}
