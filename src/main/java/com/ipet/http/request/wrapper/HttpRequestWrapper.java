package com.ipet.http.request.wrapper;

import com.ipet.http.IHttpRequestWrapper;
import com.ipet.http.request.HttpRequestFactoryHandler;

import java.lang.reflect.Proxy;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/6 15:16
 */
public enum HttpRequestWrapper implements IHttpRequestWrapper {
    INSTANCE;

    @Override
    public <T> T getInstance(Class<T> clz) throws Exception {
        return (T) Proxy.newProxyInstance(clz.getClassLoader(),new Class[]{clz}, HttpRequestFactoryHandler.HttpRequestFactoryBuilder.INSTANCE.create());
    }
}
