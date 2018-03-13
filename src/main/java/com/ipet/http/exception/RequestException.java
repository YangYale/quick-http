package com.ipet.http.exception;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/12 10:05
 */
public class RequestException extends Exception {
    private int code = -1;

    private static final String MSG_TEMPLATE = "Http Request Error, Error Code : [%s]";

    public RequestException() {
    }

    public RequestException(int code) {
        super(String.format(MSG_TEMPLATE,code));
        this.code = code;
    }

    public RequestException(Throwable cause, int code) {
        super(String.format(MSG_TEMPLATE,code), cause);
        this.code = code;
    }

    @Override
    public String toString() {
        return "RequestException{" +
                "code=" + code +
                "} " + super.toString();
    }
}
