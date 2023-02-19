package com.rzico.util;

import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description 请求数据通用处理类
 * @author 澄泓
 * @date 2020年10月22日 下午14:25:31
 * @since JDK1.7
 */
public class Sha256Util {

    /**
     * 不允许外部创建实例
     */
   private Sha256Util(){}

   private static BASE64Encoder encodeBase64= new BASE64Encoder();
    /**
     * 拼接待签名字符串
     * @param method
     * @param accept
     * @param contentMD5
     * @param contentType
     * @param date
     * @param headers
     * @param url
     * @return
     */
    public static String appendSignDataString(String method,String accept,String contentMD5,String contentType,String date,String headers,String url){
        StringBuffer sb = new StringBuffer();
        sb.append(method).append("\n").append(accept).append("\n").append(contentMD5).append("\n")
                .append(contentType).append("\n").append(date).append("\n");
        if ("".equals(headers)) {
            sb.append(headers).append(url);
        } else {
            sb.append(headers).append("\n").append(url);
        }
        return new String(sb);
    }

    /***
     *  Content-MD5的计算方法
     * @param str 待计算的消息
     * @return MD5计算后摘要值的Base64编码(ContentMD5)
     * @throws Exception 加密过程中的异常信息
     */
    public static String doContentMD5(String str) throws Exception {
        byte[] md5Bytes = null;
        MessageDigest md5 = null;
        String contentMD5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md5.update(str.getBytes("UTF-8"));
            // 获取文件MD5的二进制数组（128位）
            md5Bytes = md5.digest();
            // 把MD5摘要后的二进制数组md5Bytes使用Base64进行编码（而不是对32位的16进制字符串进行编码）
            contentMD5 = encodeBase64.encode(md5Bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("不支持此算法",e);

        } catch (UnsupportedEncodingException e) {
            throw new Exception("不支持的字符编码",e);
        }
        return contentMD5;
    }

    /***
     * 计算请求签名值-HmacSHA256摘要
     * @param message 待签名字符串
     * @param secret  密钥APP KEY
     * @return HmacSHA256计算后摘要值的Base64编码
     * @throws Exception 加密过程中的异常信息
     */
    public static String doSignatureBase64(String message, String secret) throws Exception {
        String algorithm = "HmacSHA256";
        Mac hmacSha256;
        String digestBase64 = null;
        try {
            hmacSha256 = Mac.getInstance(algorithm);
            byte[] keyBytes = secret.getBytes("UTF-8");
            byte[] messageBytes = message.getBytes("UTF-8");
            hmacSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, algorithm));
            // 使用HmacSHA256对二进制数据消息Bytes计算摘要
            byte[] digestBytes = hmacSha256.doFinal(messageBytes);
            // 把摘要后的结果digestBytes使用Base64进行编码
            digestBase64 = encodeBase64.encode(digestBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("不支持此算法",e);
        } catch (UnsupportedEncodingException e) {
            throw new Exception("不支持的字符编码",e);
        } catch (InvalidKeyException e) {
            throw new Exception("无效的密钥规范",e);
        }
        return digestBase64;
    }

    /**
     * 获取时间戳
     * @return
     */
    public static String timeStamp() {
        long timeStamp = System.currentTimeMillis();
        return String.valueOf(timeStamp);
    }

    /**
     * byte字节数组转换成字符串
     * @param b
     * @return
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    /**
     * hash散列加密算法
     * @return
     */
    public static String Hmac_SHA256(String message,String key) throws Exception {
        byte[] rawHmac=null;
        try {
            SecretKeySpec sk = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(sk);
            rawHmac = mac.doFinal(message.getBytes());
        }catch (InvalidKeyException e){
            throw new Exception("无效的密钥规范",e);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("不支持此算法",e);
        }catch (Exception e){
            throw new Exception("hash散列加密算法报错",e);
        }finally {
            return byteArrayToHexString(rawHmac);
        }

    }

    /**
     * MD5加密32位
     */
    public static String MD5Digest(String text) throws Exception {
        byte[] digest=null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(text.getBytes());
            digest = md5.digest();
        }catch (NoSuchAlgorithmException e){
            throw new Exception("不支持此算法",e);
        }finally {
            return byteArrayToHexString(digest);
        }

    }
}
