package com.ipet.http.request;

import com.ipet.http.enums.BodyType;
import com.ipet.http.executor.HttpRequestsExecutor;
import com.ipet.http.metadata.HeaderMetaData;
import com.ipet.http.utils.HttpUri;
import com.ipet.http.utils.HttpUtils;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/5 17:43
 */
public class OptionsHttpRequestHandler extends AbstractHttpRequestHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public Object run(Method method, Object[] args) throws Exception {
        // http options
        HttpUri httpUri = HttpUtils.fromUrl(getRequestMethodMetadata().getRequestMetadata().getUrl());
        BasicHttpEntityEnclosingRequest basicHttpRequest = new BasicHttpEntityEnclosingRequest(getRequestMethodMetadata().getRequestMetadata().getMethod().ofValue(), httpUri.getTarget());
        //设置headers
        for (HeaderMetaData headerMetadata : getRequestMethodMetadata().getRequestMetadata().getHeadersMetaData().getHeaderMetaDataList()) {
            basicHttpRequest.addHeader(headerMetadata.getKey(),headerMetadata.getValue());
        }
        if(getRequestMethodMetadata().getRequestMetadata().getBodyType() != BodyType.DEFAULT)
            basicHttpRequest.setHeader(BodyType.KEY,getRequestMethodMetadata().getRequestMetadata().getBodyType().ofValue());
        //设置content
        basicHttpRequest.setEntity(HttpUtils.createEntity(getRequestMethodMetadata().getRequestMetadata().getBodyType(),
                getRequestMethodMetadata().getRequestParamMetadataList(), getRequestMethodMetadata().getRequestMetadata().getCharset() ,
                getRequestMethodMetadata().getRequestMetadata().isAsync()));

        if(getRequestMethodMetadata().getRequestMetadata().isAsync()){
            return HttpRequestsExecutor.ExcetorBuilder.INSTANCE.create().asyncDoRequest(basicHttpRequest,httpUri,
                    getRequestMethodMetadata().getRequestMetadata().getCertification(),getConnectionConfig(),
                    getRequestMethodMetadata().getReturnClassType());
        }else{
            return HttpRequestsExecutor.ExcetorBuilder.INSTANCE.create().doRequest(basicHttpRequest,httpUri,
                    getRequestMethodMetadata().getRequestMetadata().getCertification(),getConnectionConfig(),
                    getRequestMethodMetadata().getReturnClassType());
        }
    }
}
