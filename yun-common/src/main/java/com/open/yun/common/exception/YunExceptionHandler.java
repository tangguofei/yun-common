package com.open.yun.common.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.open.yun.common.model.RespBody;

/**
 * 异常处理
 * @author tang
 */
@ControllerAdvice
public class YunExceptionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(YunExceptionHandler.class);
	
	@ResponseBody
	@ExceptionHandler(value = BussinessException.class)
	public RespBody<String> resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, BussinessException ex) {
		RespBody<String> result = new RespBody<String>();
		result.getInfo().setCode(ex.getExceptionEnum().getCode());
		String msg = ex.getExceptionEnum().getMsg();
		if(StringUtils.isNotBlank(ex.getMsg())){
			msg += ", 错误详细信息:" + ex.getMsg();
		}
		result.getInfo().setMsg(msg);
		log.error("业务异常:{}", JSON.toJSONString(result), ex);
		return result;
	}
	
	@ResponseBody
	@ExceptionHandler(value = ParameterValidException.class)
	public RespBody<String> resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, ParameterValidException ex) {
		RespBody<String> result = new RespBody<String>();
		result.getInfo().setCode(Integer.valueOf(ex.getCode()));
		String msg = ex.getMsg();
		result.getInfo().setMsg(msg);
		log.error("参数检查异常:{}", JSON.toJSONString(result), ex);
		return result;
	}

	@ResponseBody
	@ExceptionHandler(value = Exception.class)
    public RespBody<String> resolveException(HttpServletRequest request,  
            HttpServletResponse response, Object handler, Exception ex) {  
		RespBody<String> result = new RespBody<String>();
		if(ex instanceof SystemException){
			SystemException sex = (SystemException)ex;
			result.getInfo().setCode(sex.getExceptionEnum().getCode());
			result.getInfo().setMsg(sex.getExceptionEnum().getMsg());
		}else{
			result.getInfo().setCode(SystemExceptionEnum.SYSTEM_ERROR.getCode());
			result.getInfo().setMsg(SystemExceptionEnum.SYSTEM_ERROR.getMsg());
		}
		log.error("系统异常:{}", JSON.toJSONString(result), ex);
		return result;
    }

}
