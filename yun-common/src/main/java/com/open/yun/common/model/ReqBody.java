package com.open.yun.common.model;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("request")
public class ReqBody<T> {
	private T content;
  
	public ReqBody() {
	}

	public ReqBody(T content) {
		this.content = content;
	}
	
	public T getContent() {
		return content;
	}
	public void setContent(T content) {
		this.content = content;
	}
	
}
