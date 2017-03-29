package com.open.yun.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.open.yun.common.config.SecurityConfig;
import com.open.yun.common.constants.Constants;
import com.open.yun.common.exception.SystemException;
import com.open.yun.common.exception.SystemExceptionEnum;
import com.open.yun.common.utils.IpUtils;

/**
 * ip白名单
 * 签名
 * @author tang
 */
@Component
public class SecurityApiHandler extends HandlerInterceptorAdapter{
	private static final Logger log = LoggerFactory.getLogger(SecurityApiHandler.class);
	
	@Autowired
	private SecurityConfig securityConfig;
	
	private String encoding = "UTF-8";
	
	private static final String defaultIptables = "0.0.0.0";
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//ip检查
		String iptables = securityConfig.getIptables();
		String ip = IpUtils.getIP(request);
		log.info("请求IP:{}", ip);
		if(StringUtils.isNotBlank(iptables) && !defaultIptables.equals(iptables)){
			if(!iptables.contains(ip)){
				throw new SystemException(SystemExceptionEnum.IPLIMIT);
			}
		}
		
		//请求必须为POST
		String methodName = request.getMethod();
		if (!"POST".equalsIgnoreCase(methodName)) {
			throw new SystemException(SystemExceptionEnum.HTTP_METHOD_MUST_BE_POST);
		}
		
//		//签名
//		String sign = WebUtils.getHeaderDecode(request, Constants.SIGN, encoding);
//		request.setAttribute(Constants.REQUEST_SIGN, sign);
//		log.debug("sign:" + sign);
		
		//获取请求体
		String fileupload = request.getHeader(Constants.FILE_UPLOAD);
        if(!"1".equals(fileupload)){
        	//获取请求体
        	String content = IOUtils.toString(request.getInputStream(), encoding);
    		log.info("打印请求体{}", content);
    		request.setAttribute(Constants.REQUEST_CONTENT_BODY, content);
    		
    		String reqLength = request.getHeader(Constants.REQ_LENGTH);
    		//验证请求内容长度
            if(content.length() != Integer.valueOf(reqLength)){
            	throw new SystemException(SystemExceptionEnum.REQUEST_LENGTH_NOT_MATCH);
            }
            log.debug("请求体内容长度认证成功");
        }
		
		return true;
	}

}
