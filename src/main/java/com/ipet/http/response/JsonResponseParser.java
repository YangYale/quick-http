package com.ipet.http.response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/8 18:45
 */
public class JsonResponseParser<T> implements IResponseParser<T> {
    @Override
    public T parse(HttpEntity httpEntity, Charset charset, Type returnType) throws Exception {
        // json格式数据转化
        return JSON.parseObject(EntityUtils.toString(httpEntity,charset),new TypeReference<T>(){});
    }
}
