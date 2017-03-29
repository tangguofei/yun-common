package com.open.yun.common.exception;

/**
 * 系统异常
 * @author tang
 */
public class SystemException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private SystemExceptionEnum exceptionEnum;

	public SystemException(SystemExceptionEnum exceptionEnum){
		super(exceptionEnum.getMsg());
		this.exceptionEnum = exceptionEnum;
	}
	
	public SystemException(SystemExceptionEnum exceptionEnum, Exception e){
		super(e);
		this.exceptionEnum = exceptionEnum;
	}

	public SystemExceptionEnum getExceptionEnum() {
		return exceptionEnum;
	}

	public void setExceptionEnum(SystemExceptionEnum exceptionEnum) {
		this.exceptionEnum = exceptionEnum;
	}
}
