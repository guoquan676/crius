package com.pbkj.crius.common.constant;

import lombok.Getter;

/**
 * @author GZQ
 **/
@Getter
public enum ErrorCodeEnum {
    /**
     * 系统错误
     */
    SYSTEM_ERROR(500, "系统错误");
    /**
     * 错误类型码
     */
    private Integer code;
    /**
     * 错误类型描述信息
     */
    private String msg;
    ErrorCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsgByCode(int code){
        for(ErrorCodeEnum errorCodeEnum:ErrorCodeEnum.values()){
            if(code==errorCodeEnum.getCode()){
                return errorCodeEnum.getMsg();
            }
        }
        return  null;
    }
}