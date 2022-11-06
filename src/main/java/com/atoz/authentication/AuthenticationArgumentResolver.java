package com.atoz.authentication;

import com.atoz.user.SigninDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("AuthenticationArgumentResolver.supportsParameter");

        boolean hasAuthCheckAnnotation = parameter.hasParameterAnnotation(AuthenticationCheck.class);
        boolean hasMemberType = SigninDTO.class.isAssignableFrom(parameter.getParameterType());

        return hasAuthCheckAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("AuthenticationArgumentResolver.resolveArgument");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession();
        if (session == null) {
            return null;
        }

        return session.getAttribute(AuthenticationConst.SIGNIN_MEMBER);
    }
}
