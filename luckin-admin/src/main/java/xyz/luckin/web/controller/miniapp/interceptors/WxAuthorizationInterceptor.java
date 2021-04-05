package xyz.luckin.web.controller.miniapp.interceptors;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.luckin.common.exception.CustomException;
import xyz.luckin.web.controller.miniapp.annotation.Authorize;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


public class WxAuthorizationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                              HttpServletResponse httpServletResponse, Object object) throws Exception{
        if(!(object instanceof HandlerMethod)){
            return true;
        }

        HandlerMethod handlerMethod=(HandlerMethod)  object;
        Method method=handlerMethod.getMethod();

        if(method.isAnnotationPresent(Authorize.class)){
            Authorize wxUserToken=method.getAnnotation(Authorize.class);
            if(wxUserToken.required()){
                String token=httpServletRequest.getHeader("token");

                if(token==null){
                    throw new CustomException("Token无效，请重新登录");
                }
            }
        }

        return true;
    }

    @Override
    public  void postHandle(HttpServletRequest httpServletRequest,
                            HttpServletResponse httpServletResponse,
                            Object o, ModelAndView modelAndView) throws Exception{

    }

    @Override
    public void  afterCompletion(HttpServletRequest httpServletRequest,
                                 HttpServletResponse httpServletResponse,
                                 Object o, Exception e) throws Exception{

    }
}
