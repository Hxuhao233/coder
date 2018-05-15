package org.flysky.coder.filter;

import org.flysky.coder.listener.CustomSessionListener;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by hxuhao233 on 2018/5/15.
 */
public class CustomSessionMangerFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println(servletRequest);
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String sessionId = request.getParameter("sessionId");
        if (sessionId != null) {
            HttpSession session = (HttpSession)CustomSessionListener.sessionMap.get(sessionId);
            request.getSession().setAttribute("user",session.getAttribute("user"));
            request.getSession().setAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_AUTHENTICATED_SESSION_KEY",session.getAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_AUTHENTICATED_SESSION_KEY"));
            request.getSession().setAttribute("org.apache.shiro.web.session.HttpServletSession.HOST_SESSION_KEY",session.getAttribute("org.apache.shiro.web.session.HttpServletSession.HOST_SESSION_KEY"));
            request.getSession().setAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY",session.getAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY"));

        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
