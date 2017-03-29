package com.open.yun.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;


public class WebUtils {
	private static final String CONTENT_ATTRIBUTE = "javax.servlet.content";
	private static final int BUFFERSIZE = 8196;
	
	public static String getRequestContent(HttpServletRequest request) throws IOException {
		String returnV = null;
		Object o = request.getAttribute(CONTENT_ATTRIBUTE);
		if (o == null) {
			StringBuffer sb = new StringBuffer();
			InputStream is = request.getInputStream();

			PushbackInputStream pbis = new PushbackInputStream(is);

			InputStreamReader isr = new InputStreamReader(pbis);
			BufferedReader br = new BufferedReader(isr);
			String s = null;
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			returnV = sb.toString();
			request.setAttribute(CONTENT_ATTRIBUTE, returnV);
		} else {
			returnV = o.toString();
		}
		return returnV;
	}
	
	public static String getLocalHost() {

		try {
			return InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
		}

		return "";
	}
	public static String encode(String str, String enc) {

		String strEncode = "";

		try {
			if (str != null)
				strEncode = URLEncoder.encode(str, enc);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return strEncode;
	}
	public static String decode(String str, String enc) {
		String result = null;
		if(StringUtils.isEmpty(str)){
			result = "";
		}else{
			try {
				result = URLDecoder.decode(str, enc);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static String getHeaderDecode(HttpServletRequest request, String headerName, String encoding) {
		String headerValue = request.getHeader(headerName);
		return decode(StringUtils.defaultIfEmpty(StringUtils.trim(headerValue), ""), StringUtils.defaultString(encoding, "UTF-8"));
	}
	public static String generateContent(InputStream inputStream, String encoding) {

		if (inputStream == null) {
			return null;
		}

		BufferedInputStream bufferedInputStream = null;

		StringBuffer content = new StringBuffer();

		try {
			byte[] buffer = new byte[BUFFERSIZE];
			int count = 0;

			bufferedInputStream = new BufferedInputStream(inputStream, BUFFERSIZE);

			while ((count = bufferedInputStream.read(buffer)) != -1) { // >0
				content.append(new String(buffer, 0, count, encoding));
			}

			buffer = null;

			return content.toString();
		} catch (Exception e) {
			return null;
		} finally {
			if(bufferedInputStream != null) {
				try {
					bufferedInputStream.close();
				} catch (IOException e) {
				}
				bufferedInputStream = null;
			}
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
				inputStream = null;
			}
		}
	}
	
	
	/**
	 * 获取客户端真实ip地址。
	 * 
	 * @param request HttpServletRequest
	 * @return ip地址
	 */
	public static String getClientIP(HttpServletRequest request) {

		String ip = "";
	    String UNKNOWN = "unknown";
		ip = request.getHeader("x-forwarded-for");
		if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
				if (StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
					ip = request.getRemoteAddr();
				}
			}
			
			return ip;
		}
		
		String[] ips = ip.split(",");
		if(ips.length > 1) {
		String tempIP = "";
			for(int i=0; i<ips.length; i++) {
				tempIP = StringUtils.trimToEmpty(ips[i].trim());
				if(StringUtils.isNotEmpty(tempIP) && UNKNOWN.equalsIgnoreCase(tempIP)) {
					ip = tempIP;
					break;
				}
			}
		}

		return ip;
	}
}
