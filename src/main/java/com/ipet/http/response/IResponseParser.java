package com.ipet.http.response;

import org.apache.http.HttpEntity;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/8 18:29
 */
public interface IResponseParser<T> {

    /**
     * http响应结果解析
     * @param httpEntity
     * @return
     * @throws Exception
     */
    T parse(HttpEntity httpEntity, Charset charset, Type returnType) throws Exception;
}
