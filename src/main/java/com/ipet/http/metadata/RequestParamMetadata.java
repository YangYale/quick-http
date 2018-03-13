package com.ipet.http.metadata;

import com.ipet.http.annotation.Param;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Parameter;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/6 13:51
 */
public class RequestParamMetadata {
    private String paramName;
    private Object paramValue;
    private boolean required;
    private Parameter parameter;

    public RequestParamMetadata(Parameter parameter) {
        this.parameter = parameter;
        if(this.parameter.isAnnotationPresent(Param.class)){
            Param param = this.parameter.getAnnotation(Param.class);
            this.required = param.required();
            this.paramName = StringUtils.isNotBlank(param.value()) ? param.value() : this.parameter.getName();
        }else{
            this.required = false;
            this.paramName = this.parameter.getName();
        }
    }

    public RequestParamMetadata(Object paramValue, Parameter parameter) {
        this.paramValue = paramValue;
        this.parameter = parameter;
        if(this.parameter.isAnnotationPresent(Param.class)){
            Param param = this.parameter.getAnnotation(Param.class);
            this.required = param.required();
            this.paramName = StringUtils.isNotBlank(param.value()) ? param.value() : this.parameter.getName();
        }else{
            this.required = false;
            this.paramName = this.parameter.getName();
        }
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Object getParamValue() {
        return paramValue;
    }

    public void setParamValue(Object paramValue) {
        this.paramValue = paramValue;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
