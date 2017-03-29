package com.open.yun.common.exception;


/**
 * 业务异常
 * @author tang
 */
public class BussinessException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private ExceptionEnum exceptionEnum;
	
	/**
	 * 错误详细信息
	 */
	private String msg;

	public BussinessException(ExceptionEnum exceptionEnum){
		super(exceptionEnum.getMsg());
		this.exceptionEnum = exceptionEnum;
	}
	
	public BussinessException(ExceptionEnum exceptionEnum, String msg){
		super(msg);
		this.exceptionEnum = exceptionEnum;
		this.msg = msg;
	}
	
	public BussinessException(ExceptionEnum exceptionEnum, Exception e){
		super(e);
		this.exceptionEnum = exceptionEnum;
	}

	public ExceptionEnum getExceptionEnum() {
		return exceptionEnum;
	}

	public void setExceptionEnum(ExceptionEnum exceptionEnum) {
		this.exceptionEnum = exceptionEnum;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
