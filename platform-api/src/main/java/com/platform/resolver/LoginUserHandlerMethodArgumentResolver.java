/*
 * 项目名称:platform-plus
 * 类名称:LoginUserHandlerMethodArgumentResolver.java
 * 包名称:com.platform.modules.app.resolver
 *
 * 修改履历:
 *      日期                修正者      主要内容
 *      2018/11/21 16:04    tp      初版完成
 *
 * Copyright (c) 2019-2019 tp软件
 */
package com.platform.resolver;

import com.platform.annotation.LoginUser;
import com.platform.interceptor.AuthorizationInterceptor;
import com.platform.modules.register.entity.RegisterUserEntity;
import com.platform.modules.register.service.RegisterUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 有@LoginUser注解的方法参数，注入当前登录用户
 *
 * @author tp
 */
@Component
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private RegisterUserService registerUserService;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(RegisterUserEntity.class)
                && parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) {
        //获取用户ID
        Object object = request.getAttribute(AuthorizationInterceptor.USER_KEY, RequestAttributes.SCOPE_REQUEST);
        if (object == null) {
            return null;
        }

        //获取用户信息
        return registerUserService.getById((String) object);
    }
}
