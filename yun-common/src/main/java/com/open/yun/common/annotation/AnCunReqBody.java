package com.open.yun.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

@Target({ TYPE, METHOD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface AnCunReqBody {
}
