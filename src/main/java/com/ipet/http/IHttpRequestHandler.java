package com.ipet.http;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/5 15:52
 */
public interface IHttpRequestHandler extends InvocationHandler,AopRequest {
    Object run(Method method, Object[] args) throws Exception;
}
