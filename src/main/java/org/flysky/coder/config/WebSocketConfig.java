package org.flysky.coder.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 配置消息代理(ChatMessage broker) 设置消息连接请求的各种规范
        config.enableSimpleBroker("/chat","/user");//客户端订阅地址的前缀信息
        config.setApplicationDestinationPrefixes("/app");//客户端给服务端发消息的地址的前缀
        config.setUserDestinationPrefix("/user");//一对一前缀
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册一个名字为"coder" 的endpoint,注册handshake拦截器用于保存HttpSession,指定SockJS协议;
        registry.addEndpoint("/coderRoom").setAllowedOrigins("*").addInterceptors(new HttpSessionHandshakeInterceptor()).withSockJS();
    }
}
