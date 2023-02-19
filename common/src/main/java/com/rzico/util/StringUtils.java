/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package com.rzico.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utils - String
 * 
 * @author rsico Team
 * @version 3.0
 */
public class StringUtils  {
	/**
	 * 树形内容分隔符
	 */
	public static final String TREE_PATH_SEPARATOR = ",";

	/** 中文字符配比 */
	private static final Pattern PATTERN = Pattern.compile("[\\u4e00-\\u9fa5\\ufe30-\\uffa0]+$");

	public static final String EMPTY = "";

	public static boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	public static boolean isNotEmpty(final CharSequence str) {
		return !isEmpty(str);
	}

	public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(final CharSequence cs) {
		return !isBlank(cs);
	}

	public static boolean equals(String str1, String str2) {
		return str1 == null?str2 == null:str1.equals(str2);
	}

	public static boolean equalsIgnoreCase(String str1, String str2) {
		return str1 == null?str2 == null:str1.equalsIgnoreCase(str2);
	}

	public static boolean contains(String[] arr, String searchStr) {
		if (arr == null || searchStr == null) {
			return false;
		}
		for(String str : arr) {
			if(searchStr.equals(str)){
				return true;
			}
		}
		return false;
	}

	/**
	 * StringUtils.capitalize(null)  = null
	 * StringUtils.capitalize("")    = ""
	 * StringUtils.capitalize("cat") = "Cat"
	 * StringUtils.capitalize("cAt") = "CAt"
	 * StringUtils.capitalize("'cat'") = "'cat'"
	 *
	 * @param str
	 * @return
	 */

	public static String capitalize(final String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}

		final char firstChar = str.charAt(0);
		final char newChar = Character.toTitleCase(firstChar);
		if (firstChar == newChar) {
			// already capitalized
			return str;
		}

		char[] newChars = new char[strLen];
		newChars[0] = newChar;
		str.getChars(1,strLen, newChars, 1);
		return String.valueOf(newChars);
	}

	/** 过滤掉表情 */
	public static String filterEmoji(String source) {
		if(source != null)
		{
			Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
			Matcher emojiMatcher = emoji.matcher(source);
			if ( emojiMatcher.find())
			{
				source = emojiMatcher.replaceAll("*");
				return source ;
			}
			return source;
		}
		return source;
	}


	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if (null == str || "".equals(str)){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}


	/**
	 * 随机字母
	 * @param len
	 * @return
	 */
	public static String randomNumber(int len) {
		return randomString("0123456789", len);
	}

	private static String randomString(String charSetStr, int len) {
		int srcStrLen = charSetStr.length();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < len; i++) {
			int maxnum = random.nextInt(1000);
			int result = maxnum % srcStrLen;
			char temp = charSetStr.charAt(result);
			sb.append(temp);
		}

		return sb.toString();
	}

	/**
	 * 判断是否有数字
	 * @param str
	 * @return 是整数返回true,否则返回false
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 验证邮箱
	 *
	 * @param strEmail
	 * @return
	 */
	public static boolean checkEmailValid(String strEmail) {
		String checkemail = "^([a-z0-9A-Z]+[-|\\.|_]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern pattern = Pattern.compile(checkemail);
		Matcher matcher = pattern.matcher(strEmail);
		return matcher.matches();
	}

	/**
	 * 功能描述:
	 * 判断传入的字符串是否为数字
	 *
	 * @param str String
	 * @return boolean
	 */
	public static boolean isNumber(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			char a = str.charAt(i);
			if (!Character.isDigit(a)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 浮点型判断
	 *
	 * @param str
	 */
	public static boolean isDecimal(String str) {
		if (str == null || "".equals(str))
			return false;
		Pattern pattern = Pattern.compile("^\\d*[0-9](|.\\d*[0-9]|,\\d*[0-9])?$");
		return pattern.matcher(str).matches();
	}

	/**
	 * java对象属性转换为数据库字段，如userName-->user_name
	 *
	 * @param property
	 * @return
	 */
	public static String propertyToField(String property) {
		if (null == property) {
			return "";
		}
		char[] chars = property.toCharArray();
		StringBuffer field = new StringBuffer();
		for (char c : chars) {
			if (isUp(c)) {
				field.append("_" + String.valueOf(c).toLowerCase());
			} else {
				field.append(c);
			}
		}
		return field.toString();
	}

	/**
	 * 判断是否是大写字母
	 *
	 * @param c
	 * @return
	 */
	public static Boolean isUp(char c) {
		if (c >= 'A' && c <= 'Z') {
			return true;
		}
		return false;
	}

	/**
	 * 将数据库字段转换为java属性，如user_name-->userName
	 *
	 * @param field 字段名
	 * @return
	 */
	public static String fieldToProperty(String field) {
		if (null == field) {
			return "";
		}
		field = field.toLowerCase(); //modify by zzm 2018-11-18 11:34 为了兼容oracle
		char[] chars = field.toCharArray();
		StringBuffer property = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == '_') {
				int j = i + 1;
				if (j < chars.length) {
					property.append(String.valueOf(chars[j]).toUpperCase());
					i++;
				}
			} else {
				property.append(c);
			}
		}
		return property.toString();
	}

	/**
	 * 将数据库字段转换为java属性，如user_name-->UserName
	 *
	 * @param field 字段名
	 * @return
	 */
	public static String fieldToGsProperty(String field) {
		if (null == field) {
			return "";
		}
		char[] chars = field.toCharArray();
		StringBuffer property = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == '_') {
				int j = i + 1;
				if (j < chars.length) {
					property.append(String.valueOf(chars[j]).toUpperCase());
					i++;
				}
			} else {
				if (i == 0) {
					property.append(String.valueOf(chars[i]).toUpperCase());
				} else {
					property.append(c);
				}
			}
		}
		return property.toString();
	}


	public static String CreateNoncestr() {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		for (int i = 0; i < 16; i++) {
			Random rd = new Random();
			res += chars.charAt(rd.nextInt(chars.length() - 1));
		}
		return res;
	}

	public static String getParamStr(String s, String name) {
		s += ";";
		String [] spt = s.split(";");
		for (int i=0;i<spt.length;i++) {
			String str = spt[i];
			String hd = str.substring(0,name.length()+1);
			if (hd.equals(name+"=")) {
				return str.substring(name.length()+1);
			}
		}

		return null;

	}

	public static String usernameDecode(String s) {
		String name = s.substring(s.indexOf("_") + 1);
		       name = name.substring(name.indexOf("_") + 1);
		return name;

	}

}