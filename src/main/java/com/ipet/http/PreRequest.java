package com.ipet.http;

import com.ipet.http.metadata.RequestMethodMetadata;

/**
 * http请求前操作
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/5 13:45
 */
public interface PreRequest extends AopRequest{
    void doScript();

    //This method doesn't need to be impl.
    default RequestMethodMetadata getRequestMethodMetadata(){
        return null;
    }
}
