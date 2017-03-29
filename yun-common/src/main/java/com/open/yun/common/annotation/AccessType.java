package com.open.yun.common.annotation;

import org.apache.commons.lang3.StringUtils;

import com.open.yun.common.exception.SystemException;
import com.open.yun.common.exception.SystemExceptionEnum;


/**
 * 使用哪种类似的access加密
 * 
 * @author Administrator
 */
public enum AccessType {
	WEB("3"), OPEN("5");
	
	private String requestType;
	
	private AccessType(String requestType){
		this.requestType = requestType;
	}
	
	public String getRequestType() {
		return requestType;
	}

	public static AccessType getAccessTypeByRequestType(String requestType){
		if(StringUtils.isBlank(requestType)){
			//默认方式
			return WEB;
		}
		AccessType[] types = AccessType.values();
		for(AccessType type : types){
			if(type.getRequestType().equals(requestType)){
				return type;
			}
		}
		/**
		 * 接口对应AccessType不支持
		 */
		throw new SystemException(SystemExceptionEnum.REQUEST_TYPE_NOT_SUPPORT);
	}
	
	/**
	 * 检查accesstype是否合法
	 * @param ordinal
	 * @param types
	 * @return
	 * @throws EduException
	 */
	public static AccessType checkAccessType(String name, AccessType[] types) throws SystemException{
		if(null == name){
			name = "";
		}
		for(AccessType type : types){
			if(name.equalsIgnoreCase(type.name())){
				return type;
			}
		}
		/**
		 * 接口对应AccessType不支持
		 */
		throw new SystemException(SystemExceptionEnum.REQUEST_TYPE_NOT_SUPPORT);
	}
	
}
