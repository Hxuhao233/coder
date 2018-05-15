package org.flysky.coder.listener;


import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hxuhao233 on 2018/5/15.
 */
public class CustomSessionListener implements HttpSessionListener{

    public static ConcurrentHashMap<String, Object> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        sessionMap.put(httpSessionEvent.getSession().getId(),httpSessionEvent.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        sessionMap.remove(httpSessionEvent.getSession());
    }

}
