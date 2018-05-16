package org.flysky.coder.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.ExpiringSession;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by hxuhao233 on 2018/5/15.
 * 拦截请求，通过请求中的sessionid去Redis里面获取Session相关信息。
 */
public class CustomSessionMangerFilter implements Filter{

    @Autowired
    private RedisOperationsSessionRepository sessionRepository;

    public RedisOperationsSessionRepository getSessionRepository() {
        return sessionRepository;
    }

    public void setSessionRepository(RedisOperationsSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println(servletRequest);
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String sessionId = request.getParameter("sessionId");
        if (sessionId != null) {
            ExpiringSession session = sessionRepository.getSession(sessionId);
            if (session != null) {
                request.getSession().setAttribute("user", session.getAttribute("user"));
                request.getSession().setAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_AUTHENTICATED_SESSION_KEY", session.getAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_AUTHENTICATED_SESSION_KEY"));
                request.getSession().setAttribute("org.apache.shiro.web.session.HttpServletSession.HOST_SESSION_KEY", session.getAttribute("org.apache.shiro.web.session.HttpServletSession.HOST_SESSION_KEY"));
                request.getSession().setAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY", session.getAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY"));
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
