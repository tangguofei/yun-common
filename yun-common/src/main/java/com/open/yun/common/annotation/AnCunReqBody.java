package com.open.yun.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

/**
 * 保全中心请求体{"request"：{"content" : {} }}格式指定
 *
 * @Created on 2015年10月15日
 * @author 摇光
 * @version 1.0
 * @Copyright:杭州安存网络科技有限公司 Copyright (c) 2015
 */
@Target({ TYPE, METHOD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface AnCunReqBody {
}
