package com.open.yun.common.model;

/**
 * 公共信息
 * @author tang
 */
public class BasePojo {
	/**
     * 权限ID
     */
    private String accessId;

    private String ip; // 用户ip编号

	public String getAccessId() {
		return accessId;
	}

	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
