package com.zuma.interceptor;

import com.zuma.annotation.Logined;
import com.zuma.config.Config;
import com.zuma.enums.error.ErrorEnum;
import com.zuma.exception.UnifiedLoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 16:52
 * 登录拦截器
 */

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果访问的不是Controller层的方法
        if (!handler.getClass().isAssignableFrom(HandlerMethod.class))
           return true;
        //获取注解
        Logined logined = ((HandlerMethod) handler).getMethodAnnotation(Logined.class);
        //如果为空
        if (logined == null)
            return true;
        //尝试获取session中的用户信息,
        if(request.getSession().getAttribute(Config.SESSION_USER_ATTRIBUTE_NAME) != null)
            return true;
        //拦截
        log.info("【登录拦截】用户未登录。访问路径={}",request.getRequestURI());
        throw new UnifiedLoginException(ErrorEnum.USER_UNLOGIN);
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
