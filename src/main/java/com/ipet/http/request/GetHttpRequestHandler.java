package com.ipet.http.request;

import com.ipet.http.executor.HttpRequestsExecutor;
import com.ipet.http.metadata.HeaderMetaData;
import com.ipet.http.utils.HttpUri;
import com.ipet.http.utils.HttpUtils;
import org.apache.http.message.BasicHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.stream.Collectors;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/5 17:24
 */
public class GetHttpRequestHandler extends AbstractHttpRequestHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object run(Method method, Object[] args) throws Exception {
        //装载get请求参数
        HttpUri httpUri = HttpUtils.fromUrl(HttpUtils.urlRewrite(getRequestMethodMetadata().getRequestMetadata().getUrl(),
                            getRequestMethodMetadata().getRequestParamMetadataList().stream().
                            collect(Collectors.toMap(v->v.getParamName(),v->v.getParamValue().toString()))));

        BasicHttpRequest basicHttpRequest = new BasicHttpRequest(getRequestMethodMetadata().getRequestMetadata().getMethod().ofValue(),
                httpUri.getTarget());
        for (HeaderMetaData headerMetadata : getRequestMethodMetadata().getRequestMetadata().getHeadersMetaData().getHeaderMetaDataList()) {
            basicHttpRequest.addHeader(headerMetadata.getKey(),headerMetadata.getValue());
        }

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
