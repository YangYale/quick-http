package com.ipet.http.request;

import com.ipet.http.IHttpRequestHandler;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/5 17:47
 */
public enum HttpRequestHandlers {
    GET_HANDLER,
    POST_HANDLER{
        @Override
        public IHttpRequestHandler create() {
            return new PostHttpRequestHandler();
        }
    },
    PUT_HANDLER{
        @Override
        public IHttpRequestHandler create() {
            return new PutHttpRequestHandler();
        }
    },
    DELETE_HANDLER{
        @Override
        public IHttpRequestHandler create() {
            return new DeleteHttpRequestHandler();
        }
    },
    HEAD_HANDLER{
        @Override
        public IHttpRequestHandler create() {
            return new HeadHttpRequestHandler();
        }
    },
    OPTIONS_HANDLER{
        @Override
        public IHttpRequestHandler create() {
            return new OptionsHttpRequestHandler();
        }
    };

    public IHttpRequestHandler create(){
        return new GetHttpRequestHandler();
    }

}
