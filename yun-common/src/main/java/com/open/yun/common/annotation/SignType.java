package com.open.yun.common.annotation;

/**
 * 签名方式
 * HMACSHA: 根据accesskey已HMACSHA算法签名
 *     MD5: md5签名
 * @author Administrator
 */
public enum SignType {
	HMACSHA, MD5;
}
