package com.xie.common.result;

import lombok.Getter;

/**
 * ClassName: ResultCodeEnum
 * Package: com.jerry.common.result
 * Description:
 *
 * @Author jerry_jy
 * @Create 2023-03-01 9:50
 * @Version 1.0
 */

@Getter
public enum ResultEnum {
    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    LOGIN_MOBLE_ERROR(208,"认证失败");

    private Integer code;
    private String message;

    private ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
