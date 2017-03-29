package com.open.yun.common.interceptor;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.open.yun.common.annotation.AccessToken;
import com.open.yun.common.annotation.AccessType;
import com.open.yun.common.annotation.AnCunReqBody;
import com.open.yun.common.annotation.SignType;
import com.open.yun.common.config.SecurityConfig;
import com.open.yun.common.constants.Constants;
import com.open.yun.common.exception.ParameterValidException;
import com.open.yun.common.exception.SystemException;
import com.open.yun.common.exception.SystemExceptionEnum;
import com.open.yun.common.model.ReqBody;

@Component
public class AnCunHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource
	private SecurityConfig securityConfig;
	
	/**
	 * 是否允许拦截
	 *
	 * @param parameter	参数信息
	 * @return		true;允许/false:不允许
     */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// 取得参数索引
		int parameterIndex = parameter.getParameterIndex();

		// 获取AnCunReqBody注解
		// 从该方法所属类中取得注解
		AnCunReqBody annotation = parameter.getDeclaringClass().getAnnotation(AnCunReqBody.class);
		if ( parameterIndex == 0 && annotation != null) {
			return true;
		}
		// 从该方法中取得注解
		annotation = parameter.getMethodAnnotation(AnCunReqBody.class);
		if (parameterIndex == 0 && annotation != null) {
			return true;
		}
		// 从该方法的参数中取得注解
		annotation = parameter.getParameterAnnotation(AnCunReqBody.class);
		if (annotation != null) {
			return true;
		}

		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
	        ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
	        WebDataBinderFactory binderFactory) throws Exception {

		log.info("当前线程Id: {}", Thread.currentThread().getId());

		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		try {
			// 如果不是泛型时则选择指定为AnCunReqBody注解标注的参数类,
			// 如果没有AnCunReqBody注解标注参数类，则取得第一个
			Class<?> clazz = parameter.getParameterType();
			AnCunReqBody anCunReqBody = parameter.getParameterAnnotation(AnCunReqBody.class);
			if (anCunReqBody != null ) {
				clazz = parameter.getParameterType();
			}

			// 获取请求体
			String content = (String) request.getAttribute(Constants.REQUEST_CONTENT_BODY);

			// 将请求体转换为ReqBody对象
			ReqBody<?> req = convertToJsonReqBody(content, clazz);
			
			// 获取被请求方法的AccessToken注解
			AccessToken annotation = parameter.getMethodAnnotation(AccessToken.class);
			if (annotation == null) {
				throw new SystemException(SystemExceptionEnum.INTERFACE_UNSET_ACCESSTOKEN);
			}
			
			// 获取注解中指定的签名加密方式
			SignType signType = annotation.sign();
			log.debug("请求加密方式：" + signType.name());
			
			// 获取注解中指定的访问类型
			AccessType[] accessTypes = annotation.access();
			// 是否需要检查，校验ACCESSID,ACCESSKEY
			boolean checkAccess = annotation.checkAccess();
			
			WebDataBinder binder = binderFactory.createBinder(webRequest, req.getContent(), clazz.getName());
			binder.addValidators(new DefaultValidator(request, securityConfig, accessTypes, signType, content, checkAccess));
			binder.validate(new Object[] { req.getContent() });
			if (binder.getBindingResult().hasErrors()) {
				throw new BindException(binder.getBindingResult());
			}
			return req.getContent();
		} catch (BindException be) {
			log.error("参数校验：" + be.getMessage());
			String errorInfo = be.getBindingResult().getFieldError().getDefaultMessage();
			throw new ParameterValidException(errorInfo);
		}catch(SystemException se){
			throw se;
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new SystemException(SystemExceptionEnum.SYSTEM_ERROR);
		}
	}

	private <T> ReqBody<T> convertToJsonReqBody(String json, Class<T> clazz) {
		json = getJsonFromKey(json, "request");
		@SuppressWarnings("unchecked")
		ReqBody<T> obj = JSON.parseObject(json, ReqBody.class);
		JSONObject jsonObj = (JSONObject) obj.getContent();
		T t = JSON.parseObject(jsonObj.toJSONString(), clazz);
		obj.setContent(t);
		return obj;
	}

	private String getJsonFromKey(String json, String key) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) JSON.parse(json);
		return map.get(key).toString();
	}
	
}
