package com.open.yun.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.URL;
import java.security.Key;
import org.apache.commons.codec.binary.Base64;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;


public class DESUtils {

	private static final String ALGORITHM = "DES";
	public static final String URL_SUBFIX = "/data/key/deskeykey.data";
	public static final String URL_SUBFIX_TEST = "/data/key/deskeytestkey.data";
	
	// 加密文件路径
	private static final URL url = DESUtils.class.getResource(URL_SUBFIX);
	
	// 加密文件路径
	private static final URL urlTest = DESUtils.class.getResource(URL_SUBFIX_TEST);
       
	public static Key getKey(URL keyFileName) {

		Key key = null;

		InputStream is = null;
		ObjectInputStream ois = null;

		try {
			is = keyFileName.openStream();
			ois = new ObjectInputStream(is);

			key = (Key) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				ois = null;
			}
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				is = null;
			}
		}

		return key;
	}

	public static String encrypt(String str, URL key){
		if(key == null){
			key = url;
		}
		
		return realEncrypt(str, key);
	}

	private static String realEncrypt(String str, URL key) {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, getKey(key));
			byte[] buffer = cipher.doFinal(str.getBytes("UTF-8"));
			return Base64.encodeBase64String(buffer).trim();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 加密-迁移原来老的框架支持的加减密，在测试和服务状态下是不一样的加减密方式
	 * @param src ：源字符串
	 * @param mode：test/dev
	 * @return
	 */
	public static String encryptOldStructure(String src,String mode) {
		String str = null;
		if(null ==mode || mode.equalsIgnoreCase("dev") || mode.equalsIgnoreCase("product")) {
			str = realEncrypt(src, url);
		}else if(mode.equalsIgnoreCase("test")) {
			str = realEncrypt(src, urlTest);
		}
		return str;
	}

	public static String decrypt(String str, URL key){

		Cipher cipher = null;
		if(key == null){
			key = url;
		}
		
		try {
			cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, getKey(key));

			byte[] buffer = cipher.doFinal(Base64.decodeBase64(str));
			return new String(buffer, "UTF-8").trim();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	public static void main(String[] args){
		while(true){
			System.out.println("请选择所要的操作----------------------");
			System.out.println(">>>>> 1.加密======================");
			System.out.println(">>>>> 2.解密======================");
			InputStreamReader ir = new InputStreamReader(System.in);
			BufferedReader buf = new BufferedReader(ir);
			try {
				String sel = buf.readLine();
	            if(sel.equals("1")){
	            	System.out.println("请输入要加密的字符串----------------------");
	            	ir = new InputStreamReader(System.in);
	            	buf = new BufferedReader(ir);
	            	sel = buf.readLine();
	            	System.out.println(encrypt(sel, null));
	            } else if (sel.equals("2")) {
	            	System.out.println("请输入要解密的字符串----------------------");
	            	ir = new InputStreamReader(System.in);
	            	buf = new BufferedReader(ir);
	            	sel = buf.readLine();
	            	System.out.println(decrypt(sel, null));
	            }
			} catch (Exception e) {
	            e.printStackTrace();
	            try {
	                TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e1) {
	                e1.printStackTrace();
                }
	            continue;
            }
		}
	}

}

