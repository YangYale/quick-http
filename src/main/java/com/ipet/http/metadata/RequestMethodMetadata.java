package com.ipet.http.metadata;

import com.ipet.http.annotation.Header;
import com.ipet.http.annotation.HttpComponent;
import com.ipet.http.annotation.HttpRequest;
import com.ipet.http.annotation.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/6 11:56
 */
public class RequestMethodMetadata {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Method method;
    private Object[] args;
    private List<RequestParamMetadata> requestParamMetadataList;
    private RequestMetadata requestMetadata;
    private Class<?> returnClassType;

    public RequestMethodMetadata(Method method, Object[] args) throws Exception {
        this.method = method;
        this.args = args;
        this.requestParamMetadataList = new ArrayList<>();
        this.initialRequestMetadata(this.method,this.args);
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public RequestMetadata getRequestMetadata() {
        return requestMetadata;
    }

    public Class<?> getReturnClassType() {
        return returnClassType;
    }

    public List<RequestParamMetadata> getRequestParamMetadataList() {
        return requestParamMetadataList;
    }

    private void initialRequestMetadata(Method method, Object[] args) throws Exception{
        if(method.getDeclaringClass().isAnnotationPresent(HttpComponent.class) && method.isAnnotationPresent(HttpRequest.class)){
            HttpRequest httpRequest = method.getDeclaredAnnotation(HttpRequest.class);
            HttpComponent httpComponent = method.getDeclaringClass().getDeclaredAnnotation(HttpComponent.class);
            this.requestMetadata = new RequestMetadata(httpRequest,httpComponent);
            if(method.getReturnType().equals(Future.class)){
                this.requestMetadata.setAsync(true);
                this.returnClassType = Class.forName(((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments()[0].getTypeName());
            }else{
                this.returnClassType = method.getReturnType();
            }
            //参数中附带的header
            for (int i = 0; i < method.getParameters().length; i++) {
                Parameter parameter = method.getParameters()[i];
                if(parameter.isAnnotationPresent(Header.class)){
                    if(!parameter.isAnnotationPresent(Param.class)){
                        //是请求头
                        HeaderMetaData headerMetaData = new HeaderMetaData(parameter.getAnnotation(Header.class));
                        if(headerMetaData.legal() && parameter.getType().equals(String.class)){
                            headerMetaData.setValue((String) args[i]);
                            this.requestMetadata.getHeadersMetaData().addHeaderMetaData(headerMetaData);
                            continue;
                        }
                    }
                    logger.warn("Header Parameter [{}] in Method [{}] Config Error,Without No effects.",parameter.getName(),method.getName());
                    continue;
                }else{
                    //非请求头，是正常请求体内容
                    RequestParamMetadata requestParamMetadata = new RequestParamMetadata(args[i],parameter);
                    if(requestParamMetadata.isRequired() && requestParamMetadata.getParamValue() == null){
                        throw new NullPointerException("Request [" + this.requestMetadata.getUrl() + "] Error, Param [" + requestParamMetadata.getParamName() + "] is required, can not be null.");
                    }
                    this.requestParamMetadataList.add(new RequestParamMetadata(args[i],parameter));
                }
            }
        }else{
            throw new Exception("HttpComponent [" + method.getDeclaringClass().getName() + "." + method.getName() + "] Java Config Error,Please check your configuration.");
        }
    }
}
