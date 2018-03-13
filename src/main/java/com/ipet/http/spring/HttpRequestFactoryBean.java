package com.ipet.http.spring;

import com.ipet.http.request.wrapper.HttpRequestWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/5 17:35
 */
public class HttpRequestFactoryBean<T> implements FactoryBean<T> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String interfaceClassName;
    @Override
    public T getObject() throws Exception {
        Class clz = Class.forName(this.interfaceClassName);
        if(clz.isInterface()){
            return HttpRequestWrapper.INSTANCE.getInstance((Class<T>) clz);
        }else{
            throw new Exception("The Class [" + this.interfaceClassName + "] is not a interface, please check your code.");
        }
    }

    @Override
    public Class<?> getObjectType() {
        try{
            if(StringUtils.isBlank(interfaceClassName)){
                return null;
            }
            return Class.forName(interfaceClassName);
        }catch (Exception e){
            logger.warn("Class [" + this.interfaceClassName + "] is not found.",e);
            return null;
        }
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setInterfaceClassName(String interfaceClassName) {
        this.interfaceClassName = interfaceClassName;
    }
}
