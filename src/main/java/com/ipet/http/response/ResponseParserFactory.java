package com.ipet.http.response;

import com.ipet.http.enums.BodyType;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/8 18:48
 */
public class ResponseParserFactory {
    private static ResponseParserFactory responseParserFactory = new ResponseParserFactory();
    private ResponseParserFactory(){}

    public <T> IResponseParser<T> create(BodyType bodyType){
        switch (bodyType){
            //数据类型转化
            case DEFAULT:
            case RAW_XML:
            case RAW_HTML:
            case RAW_JAVASCRIPT:
            case RAW_TEXT_PLAIN:
            case RAW_XML_APPLICATION: return (IResponseParser<T>) new StringResponseParser();
            case BINARY: return (IResponseParser<T>) new InputStreamResponseParser();
            case RAW_JSON: return new JsonResponseParser<T>();
        }
        return null;
    }


    public enum Builder {
        INSTANCE;
        public ResponseParserFactory build(){
            return responseParserFactory;
        }
    }
}
