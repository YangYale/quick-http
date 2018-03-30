package com.ipet.http.utils;

import com.alibaba.fastjson.JSONObject;
import com.ipet.http.enums.BodyType;
import com.ipet.http.metadata.RequestParamMetadata;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.apache.http.nio.entity.NFileEntity;
import org.apache.http.nio.entity.NStringEntity;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/7 14:44
 */
public class HttpUtils {

    private static final String LEFT_SLASH = "/";

    public static HttpUri fromUrl(String url) throws Exception{
        if(StringUtils.isBlank(url)){
            throw new Exception("HttpUtils.fromUrl The url is blank.");
        }
        if(url.indexOf("?") > 0){
            url = url.substring(0,url.indexOf("?"));
        }
        int sId = url.indexOf("://");
        String schema = url.substring(0,sId);
        String target = HttpUri.DEFAULT_TARGET;
        if(url.indexOf(":") == url.lastIndexOf(":")){
            url = url.replaceFirst(schema + "://","");
            int tId = url.indexOf("/");
            if(tId > 0){
                target = url.substring(tId);
                url = url.replace(target,"");
            }
            return new HttpUri(url,schema,target);
        }else{
            url = url.replaceFirst(schema + "://","");
            int tId = url.indexOf("/");
            if(tId > 0){
                target = url.substring(tId);
                url = url.replace(target,"");
            }
            String[] array = url.split(":");
            return new HttpUri(array[0],schema,Integer.parseInt(array[1]),target);
        }
    }

    public static String urlRewrite(String url, Map<String,String> paramMap){
        final StringBuilder urlBuilder = new StringBuilder(url);
        if(!url.contains("?")){
            urlBuilder.append("?");
            paramMap.entrySet().stream().forEach(et -> urlBuilder.append(et.getKey()).append("=").append(StringUtils.isBlank(et.getValue()) ? "" : et.getValue()).append("&"));
        }else{
            paramMap.entrySet().stream().forEach(et -> urlBuilder.append("&").append(et.getKey()).append("=").append(StringUtils.isBlank(et.getValue()) ? "" : et.getValue()));
        }
        return urlBuilder.toString();
    }

    public static HttpEntity createEntity(BodyType bodyType, List<RequestParamMetadata> requestParamMetadataList, Charset charset, boolean async) throws Exception{
        switch (bodyType){
            case RAW_JSON:
                JSONObject jsonObject = new JSONObject();
                for (RequestParamMetadata requestParamMetadata : requestParamMetadataList) {
                    jsonObject.put(requestParamMetadata.getParamName(),requestParamMetadata.getParamValue());
                }
                return async ? new NStringEntity(jsonObject.toJSONString(),bodyType.toContentType(charset)) :
                        new StringEntity(jsonObject.toJSONString(),bodyType.toContentType(charset));
            case RAW_TEXT_PLAIN:
            case RAW_XML:
            case RAW_XML_APPLICATION:
            case RAW_JAVASCRIPT:
            case RAW_HTML:
                if(requestParamMetadataList.size() != 1){
                    throw new Exception("Stream Http Request Entity Set Error, is not only one parameter,please check your interface.");
                }
                if(requestParamMetadataList.get(0).getParamValue() instanceof String){
                    return async ? new NStringEntity(requestParamMetadataList.get(0).getParamValue().toString(),bodyType.toContentType(charset)) :
                            new StringEntity(requestParamMetadataList.get(0).getParamValue().toString(),bodyType.toContentType(charset));
                }else{
                    throw new Exception("String Http Request Entity Set Error, is not a String,please check your interface.");
                }
            case BINARY:
                if(requestParamMetadataList.size() != 1){
                    throw new Exception("Stream Http Request Entity Set Error, is not only one parameter,please check your interface.");
                }

                if(requestParamMetadataList.get(0).getParamValue() instanceof InputStream){
                    return async ? new NByteArrayEntity(IOUtils.toByteArray((InputStream)requestParamMetadataList.get(0).getParamValue()),bodyType.toContentType(charset)) :
                            new InputStreamEntity((InputStream) requestParamMetadataList.get(0).getParamValue(),bodyType.toContentType(charset));
                }else if(requestParamMetadataList.get(0).getParamValue() instanceof File){
                    return async ? new NFileEntity((File) requestParamMetadataList.get(0).getParamValue(), bodyType.toContentType(charset)) :
                            new FileEntity((File) requestParamMetadataList.get(0).getParamValue(),bodyType.toContentType(charset));
                }
                else{
                    throw new Exception("Stream Http Request Entity Set Error, is not a stream,please check your interface.");
                }
            case FORM_URL_ENCODEED:
            case DEFAULT:
                return new UrlEncodedFormEntity(requestParamMetadataList.stream().map(v->new BasicNameValuePair(v.getParamName(),v.getParamValue().toString())).collect(Collectors.toList()),charset);
        }
        return null;
    }

    public static String concatHostWithPath(String host,String path){
        if(host.endsWith(LEFT_SLASH)&&path.startsWith(LEFT_SLASH)){
            return host + path.substring(1);
        }else if(!host.endsWith(LEFT_SLASH) && !path.startsWith(LEFT_SLASH)){
            return host + LEFT_SLASH + path;
        }else{
            return host + path;
        }
    }
}
