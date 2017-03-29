package com.open.yun.common.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

/**
 * 单例生成http client
 * @author Administrator
 */
public class HttpClientProvider {
	private HttpClientProvider(){}
	
	private static HttpClient uniqueClient = null;
	
	public static HttpClient get() {
		if(null == uniqueClient)
			uniqueClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
		return uniqueClient;
	}
}
