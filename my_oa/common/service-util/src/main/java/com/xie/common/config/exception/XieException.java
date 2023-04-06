package com.xie.common.config.exception;

import com.xie.common.result.ResultEnum;
import lombok.Data;

//自定义异常
@Data
public class XieException extends RuntimeException{
    //定义状态码和异常信息属性
    private Integer code;
    private String message;

    public XieException(Integer code,String message){
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 接收枚举类型对象
     * @param resultEnum
     */
    public XieException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }

    @Override
    public String toString() {
        return "XieException{" +
                "code=" + code +
                ", msg='" + message + '\'' +
                '}';
    }

}
