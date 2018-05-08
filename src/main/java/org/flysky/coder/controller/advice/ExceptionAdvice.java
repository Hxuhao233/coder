package org.flysky.coder.controller.advice;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.flysky.coder.constant.ResponseCode;
import org.flysky.coder.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by hxuhao233 on 2018/4/15.
 */
@ControllerAdvice
public class ExceptionAdvice {

    /**
     * 运行时异常
     * @param runtimeException
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result runtimeExceptionHandler(RuntimeException runtimeException) {
        System.out.println(runtimeException);
        Result result = new Result();
        result.setCode(ResponseCode.UNKNOWN_ERROR);
        result.setInfo("boom");
        return result;
    }


    /**
     * 权限异常
     * @return
     */
    @ExceptionHandler({ UnauthorizedException.class, AuthorizationException.class })
    @ResponseBody
    public Result authorizationException(Exception exception) {
        System.out.println(exception);
        Result result = new Result();
        result.setCode(ResponseCode.FORBIDDEN);
        result.setInfo("无权访问");
        return result;
    }


}
