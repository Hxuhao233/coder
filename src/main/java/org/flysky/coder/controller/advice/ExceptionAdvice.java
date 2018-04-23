package org.flysky.coder.controller.advice;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.flysky.coder.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        result.setCode(500);
        result.setInfo("boom");
        return result;
    }


    /**
     * 权限异常
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({ UnauthorizedException.class, AuthorizationException.class })
    @ResponseBody
    public Result authorizationException(HttpServletRequest request, HttpServletResponse response) {
        Result result = new Result();
        result.setCode(3);
        result.setInfo("无权访问，老铁");
        return result;
    }


}
