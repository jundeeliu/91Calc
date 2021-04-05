package xyz.luckin.web.controller.miniapp.annotation;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {
    boolean required() default true;
}
