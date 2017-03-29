package com.open.yun.common.model;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("info")
public class RespInfo {
	private int code;
	private String msg;
	
	public RespInfo() {
		this.code = 100000;
		this.msg = "成功";
	}
	
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
