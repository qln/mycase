package com.qln.cases.shiro;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.google.common.collect.Maps;
import com.qln.cases.shiro.credentials.CustomCredentialsMatcher;

@Configuration
public class ShiroConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ShiroConfiguration.class);

    private static int interval = 30; // 30分钟
    private static Map<String, String> filterChainDefinitionMap = Maps.newLinkedHashMap();

    /**
     * 注册DelegatingFilterProxy（Shiro）
     *
     * @param dispatcherServlet
     * @return
     * @author SHANHY
     * @create 2016年1月13日
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        // 该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean() {
        logger.error("shiroFilter init....");
        String loginUrl = "/login";
        String unauthorizedUrl = "/login";

        Map<String, String> filterChianMap = Maps.newHashMap();
        filterChianMap.put("/admin/**", "authc");
        filterChianMap.put("/**", "anon");

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(getDefaultWebSecurityManager());
        shiroFilterFactoryBean.setLoginUrl(loginUrl);
        shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);

        filterChainDefinitionMap.putAll(filterChianMap);

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        shiroFilterFactoryBean.getFilters().put("anon", getClientAnonFilter());
        return shiroFilterFactoryBean;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager() {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(getShiroRealm());
        dwsm.setSessionManager(getSessionManager(getRedisSessionDAO()));
        dwsm.setCacheManager(getRedisCacheManager());
        return dwsm;
    }

    @Bean(name="shiroRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public MyRealm getShiroRealm() {
        MyRealm shiroRealm = new MyRealm();
        shiroRealm.setCredentialsMatcher(getCustomCredentialsMatcher());
        return shiroRealm;
    }

    @Bean
    public CustomCredentialsMatcher getCustomCredentialsMatcher() {
        return new CustomCredentialsMatcher();
    }

    @Bean(name = "clientAnonFilter")
    public ClientAnonFilter getClientAnonFilter() {
        return new ClientAnonFilter();
    }

    @Bean
    public RedisCacheManager getRedisCacheManager() {
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setRedisManager(getRedisManager());
        return cacheManager;
    }

    @Bean
    public RedisManager getRedisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setExpire(30*60);
        redisManager.setTimeout(0);
        return redisManager;
    }

    @Bean(name = "redisSessionDAO")
    public RedisSessionDAO getRedisSessionDAO() {
        RedisSessionDAO sessionDao = new RedisSessionDAO();
        sessionDao.setRedisManager(getRedisManager());
        return sessionDao;
    }

    @Bean(name = "sessionManager")
    public SessionManager getSessionManager(RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO);
        sessionManager.setGlobalSessionTimeout(interval * 60 * 1000);
        sessionManager.setDeleteInvalidSessions(true);

        // 是否启用/禁用Session Id Cookie，
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookieEnabled(true);

        // 定时去redis校验session
        ExecutorServiceSessionValidationScheduler sessionValidationScheduler = getSessionValidationScheduler();
        sessionValidationScheduler.setSessionManager(sessionManager);

        sessionManager.setSessionValidationScheduler(sessionValidationScheduler);

        sessionManager.setSessionIdCookie(sessionIdCookie());

        // session 监听
        sessionManager.setSessionListeners(getShiroSessionListeners());

        return sessionManager;
    }

    @Bean
    public ExecutorServiceSessionValidationScheduler getSessionValidationScheduler() {
        ExecutorServiceSessionValidationScheduler sessionValidationScheduler = new ExecutorServiceSessionValidationScheduler();
        sessionValidationScheduler.setInterval(interval * 30 * 1000);
        return sessionValidationScheduler;
    }

    /**
     * cookie对象;
     * 
     * @return
     */
    @Bean
    public SimpleCookie sessionIdCookie() {
        String shiroDomain = "case.com";
        SimpleCookie simpleCookie = new SimpleCookie("wid");

        simpleCookie.setDomain(shiroDomain);
        simpleCookie.setName("wid");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        simpleCookie.setMaxAge(interval * 60);
        return simpleCookie;
    }

    @Bean
    public List<SessionListener> getShiroSessionListeners() {
        List<SessionListener> list = new ArrayList<SessionListener>();
        list.add(new ShiroSessionListener());
        return list;
    }

    /**
     * LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，
     * 负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
     * 主要是AuthorizingRealm类的子类，以及EhCacheManager类。
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * AuthorizationAttributeSourceAdvisor，shiro里实现的Advisor类，
     * 内部使用AopAllianceAnnotationsAuthorizingMethodInterceptor来拦截用以下注解的方法。
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(getDefaultWebSecurityManager());
        return new AuthorizationAttributeSourceAdvisor();
    }

    /**
     * DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }
}
