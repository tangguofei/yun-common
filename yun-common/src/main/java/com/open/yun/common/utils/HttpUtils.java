package com.open.yun.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;

public class HttpUtils {
	
	private static final String charsetName = "UTF-8";
	
	public static String doApiPost(String url, String reqcontent, Map<String, String> headers){
		HttpClient client = HttpClientProvider.get();
		HttpPost post = new HttpPost(url);
		//1. 封装请求header和body
		//1.1 request header
		Set<String> keys = headers.keySet();
        for(String key : keys){
        	post.addHeader(key, headers.get(key).toString());
        }
		post.addHeader("reqlength", String.valueOf(reqcontent.length()));
        //1.2 request body
		StringEntity entity = new StringEntity(reqcontent, charsetName);
		post.setEntity(entity);
		
		//2. 执行请求
		HttpResponse response;
		try {
			response = client.execute(post);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		//3. 处理返回结果
		return doResponse(response);
	}

	public static String doApiPostFile(String url, String filePath, Map<String, String> headers){
		HttpResponse response;
		try {
			HttpClient client = HttpClientProvider.get();
			HttpPost post = new HttpPost(url);
			File file = new File(filePath);
			// 把文件转换成流对象FileBody
//			FileBody bin = new FileBody(file);
			InputStreamEntity bin = new InputStreamEntity(new FileInputStream(file));
			// 1. 封装请求header和body
			// 1.1 request header
			Set<String> keys = headers.keySet();
			for(String key : keys){
				post.addHeader(key, headers.get(key).toString());
			}
			String reqlength = String.valueOf(file.length());
			System.out.println(reqlength);
			post.addHeader("reqlength", reqlength);
			// 1.2 request body
			// 以浏览器兼容模式运行，防止文件名乱码。
//			MultipartEntityBuilder reqEntityBuilder = MultipartEntityBuilder.create()
//					.setMode(HttpMultipartMode.BROWSER_COMPATIBLE).addPart("file", bin);
//			post.setEntity(reqEntityBuilder.setCharset(CharsetUtils.get(charsetName)).build());
			post.setEntity(bin);

			// 2. 执行请求
			response = client.execute(post);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		//3. 处理返回结果
		return doResponse(response);
	}
	
	public static String doGet(String url){
		HttpClient client = HttpClientProvider.get();
		HttpGet httpGet=new HttpGet(url);
		try{
			HttpResponse response;
			try {
				response = client.execute(httpGet);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return doResponse(response);
		}finally{
			httpGet.abort();
		}
	}
	
	private static String doResponse(HttpResponse response){
		int resultCode = response.getStatusLine().getStatusCode();
		if(resultCode == 200){
			HttpEntity entity = response.getEntity();
			return readHttpEntity(entity);
		}
		return null;
	}
	
	private static String readHttpEntity(HttpEntity entity){
		try{
			if (entity != null) {              
	            InputStream is = entity.getContent();
	            BufferedReader in = new BufferedReader(new InputStreamReader(is));
	            StringBuffer buffer = new StringBuffer();   
	            String line = "";  
	            while ((line = in.readLine()) != null) {  
	                buffer.append(line);  
	            }  
	            return buffer.toString();
	        }
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		return null;
	}
}
