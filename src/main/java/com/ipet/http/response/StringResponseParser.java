package com.ipet.http.response;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/8 18:44
 */
public class StringResponseParser implements IResponseParser<String> {

    @Override
    public String parse(HttpEntity httpEntity, Charset charset, Class<?> returnType) throws Exception {
        //字符串数据转化
        if(!String.class.equals(returnType)){
            throw new Exception("Http Response Parse Error, return type is not a 'String' , please check your interface defination.");
        }
        return EntityUtils.toString(httpEntity,charset);
    }
}
