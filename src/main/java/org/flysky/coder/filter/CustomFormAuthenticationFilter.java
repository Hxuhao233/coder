package org.flysky.coder.filter;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.flysky.coder.entity.User;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * Created by hxuhao233 on 2018/4/15.
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
                                     ServletResponse response) throws Exception {
        //获取已登录的用户信息
        User activeUser = (User) subject.getPrincipal();
        //获取session
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpSession session = httpServletRequest.getSession();
        //把用户信息保存到session
        session.setAttribute("user", activeUser);
        //return super.onLoginSuccess(token, subject, request, response);
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("{success:true,msg:'登录成功'}");
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
