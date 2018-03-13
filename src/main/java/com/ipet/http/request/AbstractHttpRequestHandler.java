package com.ipet.http.request;

import com.ipet.http.IHttpRequestHandler;
import com.ipet.http.metadata.RequestMethodMetadata;
import org.apache.http.config.ConnectionConfig;

import java.lang.reflect.Method;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/5 17:39
 */
public abstract class AbstractHttpRequestHandler implements IHttpRequestHandler {
    private ThreadLocal<RequestMethodMetadata> requestMethodMetadata = new ThreadLocal<>();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getDeclaringClass().isInterface()){
            return run(method,args);
        }else{
            return method.invoke(this,args);
        }
    }

    @Override
    public void setRequestMethodMetadata(RequestMethodMetadata requestMethodMetadata) {
        this.requestMethodMetadata.set(requestMethodMetadata);
    }

    @Override
    public RequestMethodMetadata getRequestMethodMetadata() {
        return this.requestMethodMetadata.get();
    }

    protected ConnectionConfig getConnectionConfig(){
        return ConnectionConfig.custom().setBufferSize(getRequestMethodMetadata().getRequestMetadata().getBufferSize())
                .setCharset(getRequestMethodMetadata().getRequestMetadata().getCharset()).build();
    }
}
