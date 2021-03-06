package com.joindata.inf.boot.annotation;

import com.joindata.inf.boot.mechanismx.MessageSourceConfiguration;
import com.joindata.inf.boot.mechanismx.NewExceptionController;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 新的异常处理器
 */
@Retention(RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({MessageSourceConfiguration.class,NewExceptionController.class})
public @interface EnableNewExceptionAdvice {

}
