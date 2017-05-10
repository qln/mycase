package com.qln.cases.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qln.cases.common.util.SpringContextUtil;

/**
 * 客户端权限过滤，用于保存host and requestURI
 * 
 * @author lvyulin
 *
 */
public class ClientAnonFilter extends AuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(ClientAnonFilter.class);

    RedisSessionDAO redisSessionDAO;

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        logger.error("anon filter...");
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        String loginSessionId = null;

        javax.servlet.http.Cookie[] cookiess = httpRequest.getCookies();
        if (null != cookiess && cookiess.length > 0) {
            for (javax.servlet.http.Cookie c : cookiess) {
                if (c.getName().equals("wid")) {
                    String csid = c.getValue();
                    if (null != csid) {
                        redisSessionDAO = SpringContextUtil.getBean("redisSessionDAO");
                        Session ses = redisSessionDAO.readSession(c.getValue());
                        if (null != ses && null != ses.getId()) {
                            logger.error("ClientAnonFilter sid:{}", ses.getId());
                            loginSessionId = (String) ses.getId();
                        } else {
                        }
                    }
                }
            }
        }

        if (null != loginSessionId) {
            if (null != cookiess && cookiess.length > 0) {
                for (javax.servlet.http.Cookie c : cookiess) {
                    if (c.getName().equals("wid")) {
                        SimpleCookie simpleCookie = new SimpleCookie("wid");
                        // <!-- 记住我cookie生效时间30天 ,单位秒;-->
                        simpleCookie.setDomain("case.com");
                        simpleCookie.setValue(c.getValue());
                        simpleCookie.setMaxAge(30 * 60);
                        simpleCookie.setHttpOnly(true);
                        simpleCookie.setPath("/");
                        String csid = c.getValue();
                        if (null != csid) {
                            simpleCookie.setMaxAge(0);
                            simpleCookie.removeFrom(httpRequest, httpResponse);
                        }
                    }
                }
            }

            SimpleCookie simpleCookie = new SimpleCookie("wid");
            // <!-- 记住我cookie生效时间30天 ,单位秒;-->
            simpleCookie.setDomain("case.com");
            simpleCookie.setValue(loginSessionId);
            simpleCookie.setMaxAge(30 * 60);
            simpleCookie.setHttpOnly(true);
            simpleCookie.setPath("/");
            DefaultWebSessionManager sessionManager = SpringContextUtil.getBean("sessionManager");

            sessionManager.setSessionIdCookie(simpleCookie);

            simpleCookie.saveTo(httpRequest, httpResponse);

        }

        return true;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return false;
    }

    /*
     * 是否是Ajax异步请求
     * 
     * @param request
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {

        String accept = request.getHeader("accept");
        String xRequestedWith = request.getHeader("X-Requested-With");

        // 如果是异步请求或是手机端，则直接返回信息
        return ((accept != null && accept.indexOf("application/json") != -1 || (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1)));
    }

    private String getDefaultBackUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String domain = request.getServerName();
        int port = request.getServerPort();
        String contextPath = request.getContextPath();
        StringBuilder backUrl = new StringBuilder(scheme);
        backUrl.append("://");
        backUrl.append(domain);
        if ("http".equalsIgnoreCase(scheme) && port != 80) {
            backUrl.append(":").append(String.valueOf(port));
        } else if ("https".equalsIgnoreCase(scheme) && port != 443) {
            backUrl.append(":").append(String.valueOf(port));
        }
        backUrl.append(contextPath);
        return backUrl.toString();
    }

}