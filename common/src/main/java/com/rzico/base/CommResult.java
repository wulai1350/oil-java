package com.rzico.base;



import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


/**
 * @author zhongzm
 * 查询接口返回值专用object,用于查询返还结果集
 * @param <T>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommResult<T> {

    public final static String SUCCESS_MSG = "成功";

    public final static String FAIL_MSG = "失败";

    public enum Type {
        /** 成功 */
        success,
        /** 警告 */
        warn,
        /** 错误 */
        error,
        /** 注销 */
        logout,
    }
    /** 类型 */
    private Type type = Type.success;

    /** 说明 */
    private String content = SUCCESS_MSG;

    /** 数据 */
    private Object data = null;

    /** md5加密串 */
    private String md5Str;

    public CommResult(){
    }

    public CommResult(Type type, String content){
        this.type = type;
        this.content = content;
    }

    public CommResult(Type type, String content,Object data){
        this.type = type;
        this.content = content;
        this.data = data;
    }

    public static CommResult success(){
        return new CommResult(Type.success,SUCCESS_MSG);
    }

    public static CommResult success(Object data){
        return new CommResult(Type.success,SUCCESS_MSG,data);
    }

    public static CommResult success(String msg){
        return new CommResult(Type.success,msg);
    }

    public static CommResult error(){
        return new CommResult(Type.error,FAIL_MSG);
    }

    public static CommResult error(String msg){
        return new CommResult(Type.error,msg);
    }

    public static CommResult logout() {
        return new CommResult(Type.logout,"令牌无效");
    }
    
}
