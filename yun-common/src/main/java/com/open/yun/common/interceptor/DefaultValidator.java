package com.open.yun.common.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.open.yun.common.annotation.AccessType;
import com.open.yun.common.annotation.SignType;
import com.open.yun.common.config.SecurityConfig;
import com.open.yun.common.constants.Constants;
import com.open.yun.common.exception.SystemException;
import com.open.yun.common.exception.SystemExceptionEnum;
import com.open.yun.common.utils.HmacSha1Utils;
import com.open.yun.common.utils.MD5Utils;

/**
 * 系统默认的验证
 * @author tang
 */
public class DefaultValidator implements Validator{
	
	private static final Logger log = LoggerFactory.getLogger(DefaultValidator.class);
	
	private SecurityConfig securityConfig;
	
	private HttpServletRequest request;
	
	private AccessType[] accessTypes;
	
	private boolean checkAccess;
	
	private SignType signType;
	
	private String content;
	
	private void signValidator(AccessType accessType, String sign){
		//accessKey
		String accessKey = securityConfig.getAccessKey(accessType);
		//加密方式
		switch(signType){
		 	case MD5 :  md5Sign(content, sign); break;
		 	case HMACSHA : hmacShaSign(content, sign, accessKey); break;
		 	default : hmacShaSign(content, sign, accessKey); break;
		}
	}

	public DefaultValidator(HttpServletRequest request, SecurityConfig securityConfig, AccessType[] accessTypes, SignType signType, String content, boolean checkAccess){
		this.securityConfig = securityConfig;
		this.request = request;
		this.accessTypes = accessTypes;
		this.checkAccess = checkAccess;
		this.signType = signType;
		this.content = content;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(!checkAccess){
			//不需要检查
			return ;
		}
		AccessType accessType = requestType2AccessType(target);
		//检查requestType是否满足条件
		AccessType.checkAccessType(accessType.name(), accessTypes);
		
		//检查AccessId是否满足条件
		String requestAccessId = getAccessIdFromRequestBody(target);
		String systemAccessId = securityConfig.getAccessId(accessType);
		if(!StringUtils.equals(requestAccessId, systemAccessId)){
			throw new SystemException(SystemExceptionEnum.ACCESSID_NOT_VALID);
		}
		
		//检查签名是否匹配
		String sign = (String)request.getAttribute(Constants.REQUEST_SIGN);
		signValidator(accessType, sign);
	}
	
	/**
	 * 将请求体中的requestType转换为AccessType
	 * @return
	 */
	private AccessType requestType2AccessType(Object target){
		String requestType = "";
		try {
			BeanWrapper jim = new BeanWrapperImpl(target);
			requestType = (String) jim.getPropertyValue("requestType");
		} catch (BeansException e) {
			log.error("从BeanWrapper中获取type失败：" + e.getMessage());
		}
		return AccessType.getAccessTypeByRequestType(requestType);
	}
	
	/**
	 * 从请求体中获取获取AccessId
	 * @param target
	 * @return
	 */
	private String getAccessIdFromRequestBody(Object target){
		String accessId = "";
		try {
			BeanWrapper jim = new BeanWrapperImpl(target);
			accessId = (String) jim.getPropertyValue("accessId");
		} catch (BeansException e) {
			log.error("从BeanWrapper中获取type失败：" + e.getMessage());
		}
		return accessId;
	}
	
	/**
	 * 验证MD5加密的签名
	 *
	 * @param content
	 * @param sign
	 */
	private void md5Sign(String content, String sign) {
		String encryptContent = MD5Utils.md5(content).toLowerCase();
		if (!sign.equals(encryptContent)) {
			throw new SystemException(SystemExceptionEnum.SIGN_NAME_NOT_MATCH);
		}
	}

	/**
	 * 验证HMACSHA加密的签名
	 *
	 * @param backstageType
	 * @param provinceCode
	 * @param sign
	 * @param content
	 * @param encoding
	 * @throws EduException
	 */
	private void hmacShaSign(String content, String sign, String accessKey) {
		try{
			String encryptContent = HmacSha1Utils.signToString(MD5Utils.md5(content).toLowerCase(), accessKey, "UTF-8");
			if (!sign.equals(encryptContent)) {
				throw new SystemException(SystemExceptionEnum.SIGN_NAME_NOT_MATCH);
			}
		}catch(Exception e){
			log.error("签名错误"+e.getMessage(), e);
			throw new SystemException(SystemExceptionEnum.SIGN_NAME_NOT_MATCH);
		}
	}

}
