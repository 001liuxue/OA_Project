package com.xie.common.config.exception;

import com.xie.common.result.Result;
import com.xie.common.result.ResultEnum;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result globalException(Exception e){
        e.printStackTrace();
        return Result.fail().message("执行全局异常");
    }

    //当有特定异常，先执行特定异常，没有特定异常才会执行全局异常
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result arithmeticException(ArithmeticException e){
        e.printStackTrace();
        return Result.fail().message("执行特定异常");
    }

    //自定义异常
    @ExceptionHandler(XieException.class)
    @ResponseBody
    public Result xieException(XieException e){
        e.printStackTrace();
        return Result.fail().code(e.getCode()).message(e.getMessage());
    }

    /**
     * spring security异常
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result error(AccessDeniedException e) throws AccessDeniedException {
        return Result.fail().code(205).message("没有操作权限");
    }

}
