package com.open.yun.common.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public class IpUtils {
	private static boolean noAvailableIp(String ip){
		return StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip);
	}
	public static String getIP(HttpServletRequest request) {
		String ip = request.getHeader("x-real-ip");
		if (noAvailableIp(ip)) {
			ip = request.getHeader("x-forwarded-for");
		} 
		if (noAvailableIp(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (noAvailableIp(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (noAvailableIp(ip)) {
			ip = request.getRemoteAddr();
		}
		//多个路由时，取第一个非unknown的ip
		String[] arr = ip.split(",");
		for(String str : arr){
			if(!noAvailableIp(str)){
				ip = str;
				break;
			}
		}		 
		return ip;
	}
}
