package org.flysky.coder.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.flysky.coder.filter.CustomFormAuthenticationFilter;
import org.flysky.coder.security.UserRealm;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by hxuhao233 on 2018/4/13.
 */
//@Configuration
public class ShiroConfig {



    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();//获取filters
        filters.put("authc", customFormAuthenticationFilter());//将自定义 的FormAuthenticationFilter注入shiroFilter中

        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        //  注意过滤器配置顺序 不能颠倒
        //  配置登出url
        filterChainDefinitionMap.put("/test/logout", "logout");
        // 配置不会被拦截的url 顺序判断
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/login.html", "anon");
        filterChainDefinitionMap.put("/hello", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");

        // 配置需要验证的url
        filterChainDefinitionMap.put("/**", "authc");
        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
        shiroFilterFactoryBean.setLoginUrl("/test/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/test/show");
        //未授权界面;
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    public CustomFormAuthenticationFilter customFormAuthenticationFilter(){
        CustomFormAuthenticationFilter filter = new CustomFormAuthenticationFilter();
        return filter;
    }


    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(getRealm());

        return securityManager;
    }



    @Bean
    public UserRealm getRealm() {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return userRealm;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:MD5;
        hashedCredentialsMatcher.setHashIterations(1);//散列的次数;

        return hashedCredentialsMatcher;
    }


    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }


    // aop
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

}
