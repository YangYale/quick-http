package com.ipet.http;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/5 16:03
 */
public interface IHttpRequestWrapper {
    <T> T getInstance(Class<T> clz) throws Exception;
}
