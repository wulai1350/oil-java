package com.rzico.exception;


/**
 * 自定义异常
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2019-12-02
 */
public class CustomException extends RuntimeException{

    public String message;
    public Object data;

    public CustomException(String message) {
        super(message);
        this.message = message;
    }

    public CustomException(String message,Object data) {
        super(message);
        this.message = message;
        this.data = data;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

}
