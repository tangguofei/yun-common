package com.open.yun.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HmacSha1Utils {
	private static final String HMAC_SHA1 = "HmacSHA1";
	private static final Logger log = LoggerFactory.getLogger(HmacSha1Utils.class);
	private static final String ALGORITHM = "HmacSHA1";

	public static byte[] signature(String data, String key)
			throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] keyBytes = key.getBytes();
		SecretKeySpec signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1);
		Mac mac = Mac.getInstance(HMAC_SHA1);
		mac.init(signingKey);
		return mac.doFinal(data.getBytes());
	}

	public static byte[] signature(String data, String key, String charsetName)
			throws NoSuchAlgorithmException, InvalidKeyException,
			IllegalStateException, UnsupportedEncodingException {
		byte[] keyBytes = key.getBytes();
		SecretKeySpec signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1);
		Mac mac = Mac.getInstance(HMAC_SHA1);
		mac.init(signingKey);
		return mac.doFinal(data.getBytes(charsetName));
	}

	public static byte[] sign(String data, String key, String charsetName)
			throws NoSuchAlgorithmException, InvalidKeyException,
			IllegalStateException, UnsupportedEncodingException {

		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(charsetName),
				ALGORITHM);
		Mac mac = Mac.getInstance(ALGORITHM);
		mac.init(signingKey);

		return mac.doFinal(data.getBytes(charsetName));
	}

	public static String signToString(String data, String key,
			String charsetName) throws InvalidKeyException,
			NoSuchAlgorithmException, IllegalStateException,
			UnsupportedEncodingException {

		return StringUtils.trimToEmpty(Base64.encodeBase64String(sign(data, key,
				charsetName)));
	}

	public static boolean signCheck(String data, String key, String sign, String charsetName) {
		String sSign = "";
		try {
			sSign = signToString(data, key, charsetName);
			return sSign.equals(sign);
		} catch (Exception e) {
			log.error("签名错误", e);
			return false;
		}
		
	}

}
