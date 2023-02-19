package com.rzico.util;

import java.security.MessageDigest;

/*
 '============================================================================
 'api璇存槑锛�
 'createSHA1Sign鍒涘缓绛惧悕SHA1
 'getSha1()Sha1绛惧悕
 '============================================================================
 '*/
public class Sha1Util {

	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 灏嗗瓧鑺傝浆鎹负鍗佸叚杩涘埗瀛楃涓�
	 * @param mByte
	 * @return
	 */
	private static String byteToHexStr(byte mByte) {
		char[] Digit = HEX_DIGITS;
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];

		String s = new String(tempArr);
		return s;
	}

	/*
	 * * 灏嗗瓧鑺傛暟缁勮浆鎹负鍗佸叚杩涘埗瀛楃涓�
	 * @param byteArray
	 * @return
	 */
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

	public static String encode(String str) {
		if (str == null) {
			return null;
		}
		MessageDigest md = null;
		String tmpStr = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			// 灏嗕笁涓弬鏁板瓧绗︿覆鎷兼帴鎴愪竴涓瓧绗︿覆杩涜sha1鍔犲瘑
			byte[] digest = md.digest(str.getBytes());
			tmpStr = byteToStr(digest);
			return tmpStr;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
