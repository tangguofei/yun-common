package com.open.yun.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("response")
@JsonInclude(Include.NON_NULL) 
public class RespBody<T> {
	private RespInfo info;
	private T content;
	public RespBody(){
		super();
		this.info = new RespInfo();
	};
	public RespBody(T content) {
		super(); 
		this.info = new RespInfo();
		this.content = content;
	}
	public RespBody(RespInfo info, T content) {
		super();
		this.info = info;
		this.content = content;
	}
	public RespInfo getInfo() {
		return info;
	}
	public void setInfo(RespInfo info) {
		this.info = info;
	}
	public T getContent() {
		return content;
	}
	public void setContent(T content) {
		this.content = content;
	}
}
