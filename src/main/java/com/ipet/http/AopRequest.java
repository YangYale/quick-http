package com.ipet.http;

import com.ipet.http.metadata.RequestMethodMetadata;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/6 15:44
 */
public interface AopRequest {
    void setRequestMethodMetadata(RequestMethodMetadata requestMethodMetadata);

    RequestMethodMetadata getRequestMethodMetadata();
}
