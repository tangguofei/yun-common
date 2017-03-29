package com.open.yun.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.open.yun.common.annotation.AccessType;

/**
 * iptables+sign
 * @author tang
 */
@Configuration
@ConfigurationProperties(prefix = SecurityConfig.SECURITY_PREFIX)
public class SecurityConfig {
	public static final String SECURITY_PREFIX = "security";
	//运行访问的IP控制, 多个ip用逗号(,)分割
	private String iptables;
	
	private String accessIdWeb;
	private String accessKeyWeb;
	private String accessIdOpen;
	private String accessKeyOpen;

	public String getIptables() {
		return iptables;
	}

	public void setIptables(String iptables) {
		this.iptables = iptables;
	}

	public String getAccessIdWeb() {
		return accessIdWeb;
	}

	public void setAccessIdWeb(String accessIdWeb) {
		this.accessIdWeb = accessIdWeb;
	}

	public String getAccessKeyWeb() {
		return accessKeyWeb;
	}

	public void setAccessKeyWeb(String accessKeyWeb) {
		this.accessKeyWeb = accessKeyWeb;
	}

	public String getAccessIdOpen() {
		return accessIdOpen;
	}

	public void setAccessIdOpen(String accessIdOpen) {
		this.accessIdOpen = accessIdOpen;
	}

	public String getAccessKeyOpen() {
		return accessKeyOpen;
	}

	public void setAccessKeyOpen(String accessKeyOpen) {
		this.accessKeyOpen = accessKeyOpen;
	}

	/**
	 * 根据AccessType获取对应的AccessId
	 * @param type
	 * @return
	 */
	public String getAccessId(AccessType type){
		switch(type){
			case WEB : return this.getAccessIdWeb();
			case OPEN : return this.getAccessIdOpen();
			default : return this.getAccessIdWeb();
		}
	}
	
	/**
	 * 根据AccessType获取对应的AccessKey
	 * @param type
	 * @return
	 */
	public String getAccessKey(AccessType type){
		switch(type){
			case WEB : return this.getAccessKeyWeb();
			case OPEN : return this.getAccessKeyOpen();
			default : return this.getAccessKeyWeb();
		}
	}
}
