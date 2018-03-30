package com.ipet.http.metadata;

import com.ipet.http.AfterRequest;
import com.ipet.http.PreRequest;
import com.ipet.http.annotation.HttpComponent;
import com.ipet.http.annotation.HttpRequest;
import com.ipet.http.enums.BodyType;
import com.ipet.http.enums.HttpMethod;
import com.ipet.http.utils.HttpUtils;
import com.ipet.http.utils.UrlPropertyResolveUtil;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/6 10:50
 */
public class RequestMetadata {

    private String url;
    private HttpMethod method;
    private HeadersMetaData headersMetaData;
    private BodyType bodyType;
    private boolean async;
    private String certification;
    private Class<? extends PreRequest> preScript;
    private Class<? extends AfterRequest> afterScript;
    private Charset charset;
    private int bufferSize;

    public RequestMetadata(HttpRequest httpRequest,HttpComponent httpComponent) throws Exception {
        String tagUrl = StringUtils.isNotBlank(httpRequest.value()) ? httpRequest.value() : httpRequest.url();
        if(StringUtils.isNotBlank(tagUrl)){
            this.setUrl(UrlPropertyResolveUtil.resolveProperty(tagUrl));
        }else{
            if(StringUtils.isBlank(httpComponent.value()) && StringUtils.isBlank(httpComponent.host())){
                throw new Exception("Http Interface url is Empty, Http Configuration Error,please check your Http Configuration");
            }
            this.setUrl(HttpUtils.concatHostWithPath(
                    UrlPropertyResolveUtil.resolveProperty(StringUtils.isNotBlank(httpComponent.value()) ? httpComponent.value() : httpComponent.host()),
                    UrlPropertyResolveUtil.resolveProperty(httpRequest.path())
            ));
        }
        this.setMethod(httpRequest.method());
        this.setBodyType(httpRequest.body());
        this.setCertification(httpRequest.certification());
        this.setPreScript(httpRequest.preScript());
        this.setAfterScript(httpRequest.afterScript());
        this.setHeadersMetaData(new HeadersMetaData(httpRequest.headers().value()));
        this.getHeadersMetaData().addHeaderMetaData(new HeaderMetaData(httpRequest.header()));
        this.setCharset(Charset.forName(httpRequest.charset()));
        this.setBufferSize(httpRequest.bufferSize());
        this.setAsync(false);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public HeadersMetaData getHeadersMetaData() {
        return headersMetaData;
    }

    public void setHeadersMetaData(HeadersMetaData headersMetaData) {
        this.headersMetaData = headersMetaData;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public Class<? extends PreRequest> getPreScript() {
        return preScript;
    }

    public void setPreScript(Class<? extends PreRequest> preScript) {
        this.preScript = preScript;
    }

    public Class<? extends AfterRequest> getAfterScript() {
        return afterScript;
    }

    public void setAfterScript(Class<? extends AfterRequest> afterScript) {
        this.afterScript = afterScript;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
}
