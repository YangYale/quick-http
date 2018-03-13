package com.ipet.http.request;

import com.ipet.http.IHttpRequestHandler;
import com.ipet.http.enums.HttpMethod;
import com.ipet.http.metadata.RequestMethodMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/6 15:27
 */
public class HttpRequestHandlerBuilder {

    private static Logger logger = LoggerFactory.getLogger(HttpRequestHandlerBuilder.class);

    public static IHttpRequestHandler create(HttpMethod httpMethod) throws Exception{
        switch (httpMethod){
            case POST: return HttpRequestHandlers.POST_HANDLER.create();
            case GET: return HttpRequestHandlers.GET_HANDLER.create();
            case OPTIONS: return HttpRequestHandlers.OPTIONS_HANDLER.create();
            case DELETE: return HttpRequestHandlers.DELETE_HANDLER.create();
            case HEAD: return HttpRequestHandlers.HEAD_HANDLER.create();
            case PUT: return HttpRequestHandlers.PUT_HANDLER.create();
        }
        throw new Exception("RequestMethodMetadata Data Error,please check your http configuration.");
    }
}
