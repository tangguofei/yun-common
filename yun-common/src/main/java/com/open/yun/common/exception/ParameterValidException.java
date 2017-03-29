package com.open.yun.common.exception;


/**
 * 业务异常
 * @author tang
 */
public class ParameterValidException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private String code;
	private String msg;

	public static final String SEPARATOR = "#####";
	
	public ParameterValidException(String errorMsg){
		super(errorMsg);
		String[] errors = errorMsg.split(SEPARATOR);
		if(errors.length >= 2){
			this.code = errors[0];
			this.msg = errors[1];
		}else{
			throw new SystemException(SystemExceptionEnum.PARAMETER_VALID);
		}
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
