package com.ipet.http.request;

import com.ipet.http.AfterRequest;
import com.ipet.http.IHttpRequestHandler;
import com.ipet.http.PreRequest;
import com.ipet.http.annotation.HttpComponent;
import com.ipet.http.annotation.HttpRequest;
import com.ipet.http.metadata.RequestMethodMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/6 14:55
 */
public final class HttpRequestFactoryHandler extends AbstractHttpRequestHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object run(Method method, Object[] args) throws Exception {
        if(method.getDeclaringClass().isAnnotationPresent(HttpComponent.class) && method.isAnnotationPresent(HttpRequest.class)){
            //暂时先不做缓存
            RequestMethodMetadata requestMethodMetadata = new RequestMethodMetadata(method,args);
            if(requestMethodMetadata.getRequestMetadata().getPreScript() != null){
                PreRequest preRequest = requestMethodMetadata.getRequestMetadata().getPreScript().newInstance();
                //执行请求前操作
                preRequest.setRequestMethodMetadata(requestMethodMetadata);
                preRequest.doScript();
            }
            IHttpRequestHandler httpRequestHandler = HttpRequestHandlerBuilder.create(requestMethodMetadata.getRequestMetadata().getMethod());
            httpRequestHandler.setRequestMethodMetadata(requestMethodMetadata);
            Object result = httpRequestHandler.run(method,args);
            if(requestMethodMetadata.getRequestMetadata().getAfterScript() != null){
                AfterRequest afterRequest = requestMethodMetadata.getRequestMetadata().getAfterScript().newInstance();
                //执行请求后操作
                afterRequest.setRequestMethodMetadata(requestMethodMetadata);
                afterRequest.setResponse(result);
                afterRequest.doScript();
            }
            return result;
        }
        return null;
    }

    public enum HttpRequestFactoryBuilder {
        INSTANCE;
        private IHttpRequestHandler httpRequestHandler;
        HttpRequestFactoryBuilder(){
            httpRequestHandler = new HttpRequestFactoryHandler();
        }
        public IHttpRequestHandler create(){
            return httpRequestHandler;
        }
    }
}
