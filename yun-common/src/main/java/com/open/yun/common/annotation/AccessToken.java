package com.open.yun.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Access签名
 * @author Administrator
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessToken {
	SignType sign() default SignType.HMACSHA;
	AccessType[] access();
	/**
	 * 是否需要检查ACCESSID,KEY。。
	 * 动态ACCESSID,KEY的时候登录是不需要检查的
	 * @return
	 */
	boolean checkAccess() default true;
}
