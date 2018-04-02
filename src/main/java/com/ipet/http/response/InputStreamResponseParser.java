package com.ipet.http.response;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/8 18:46
 */
public class InputStreamResponseParser implements IResponseParser<InputStream> {
    @Override
    public InputStream parse(HttpEntity httpEntity, Charset charset, Type returnType) throws Exception {
        // 文件数据转化
        if(!InputStream.class.equals(returnType)){
            throw new Exception("Http Response Parse Error, return type is not a 'InputStream' , please check your interface defination.");
        }
        return new ByteArrayInputStream(EntityUtils.toByteArray(httpEntity));
    }
}
