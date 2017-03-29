package com.open.yun.common.exception;

/**
 * 系统异常枚举
 * @author tang
 */
public enum SystemExceptionEnum implements ExceptionEnum{
	SYSTEM_ERROR(100001, "系统异常"),
	IPLIMIT(100002, "ip受限"),
	HTTP_METHOD_MUST_BE_POST(100003, "请求的HTTP METHOD不支持，必须为POST"),
	PARAMETER_VALID(100004, "参数校验失败"),
	REQUEST_TYPE_NOT_SUPPORT(100005, "请求类型不支持"),
	INTERFACE_UNSET_ACCESSTOKEN(100006, "接口未指定AccessToken注解"),
	ACCESSID_NOT_VALID(100007, "accessId不匹配"),
	SIGN_NAME_NOT_MATCH(100008, "签名不匹配"),
	REQUEST_LENGTH_NOT_MATCH(100009, "请求体长度不匹配");
	
	private SystemExceptionEnum(int code, String msg){
		this.code = code;
		this.msg = msg;
	}
	
	private int code;
	private String msg;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
