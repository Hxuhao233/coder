package org.flysky.coder.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.flysky.coder.entity.User;
import org.flysky.coder.vo.user.Code;
import org.flysky.coder.vo.user.LoginData;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by hxuhao233 on 2018/4/15.
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
                                     ServletResponse response) throws Exception {
        //获取已登录的用户信息
        User user = (User) subject.getPrincipal();
        //获取session
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpSession session = httpServletRequest.getSession();
        LoginData loginData = new LoginData();

        if (user.getActivated()==0) {
            loginData.setCode(Code.NOT_ACTIVATED);
        } else {
            loginData.setCode(Code.SUCCEED);
            loginData.setUsername(user.getUsername());
            loginData.setType(user.getType());
            session.setAttribute("user", user);
        }


        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println(objectMapper.writeValueAsString(loginData));
        out.flush();
        out.close();

        return false;
    }


    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        response.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        LoginData loginData = new LoginData();
        loginData.setCode(Code.WRONG_EMAIL_OR_PASSWORD);

        try {
            out.println(objectMapper.writeValueAsString(loginData));
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }
        out.flush();
        out.close();

        return false;
    }



    @Bean
    public FilterRegistrationBean registration(CustomFormAuthenticationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

}
