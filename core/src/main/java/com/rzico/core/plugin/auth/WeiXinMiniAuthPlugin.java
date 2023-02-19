
package com.rzico.core.plugin.auth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rzico.core.entity.SysPlugin;
import com.rzico.core.model.PluginAttribute;
import com.rzico.core.plugin.AuthPlugin;
import com.rzico.exception.CustomException;
import com.rzico.util.RedisHandler;
import com.rzico.util.Sha1Util;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;
import java.util.*;

/**
 * Plugin - 微信小程序授权
 * 
 * @author RZICO.BOOT
 * @version 3.0
 */
@Component("weixinMiniAuthPlugin")
public class WeiXinMiniAuthPlugin extends AuthPlugin {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RedisHandler redisHandler;

	// 获取access_token的接口地址（GET） 限200（次/天） 
	private static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	// 获取oauth2 access_token的接口地址（GET） 限200（次/天） 
	private static String oauth2_access_token_url = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=CODE&grant_type=authorization_code";

	@Override
	public String getName() {
		return "微信小程序";
	}

	@Override
	public String getVersion() {
		return "3.8.0";
	}

	// 算法名
	public static final String KEY_NAME = "AES";

	// 加解密算法/模式/填充方式
	// ECB模式只用密钥即可对数据进行加密解密，CBC模式需要添加一个iv
	public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

	/**
	 * 微信 数据解密<br/>
	 * 对称解密使用的算法为 AES-128-CBC，数据采用PKCS#7填充<br/>
	 * 对称解密的目标密文:encrypted=Base64_Decode(encryptData)<br/>
	 * 对称解密秘钥:key = Base64_Decode(session_key),aeskey是16字节<br/>
	 * 对称解密算法初始向量:iv = Base64_Decode(iv),同样是16字节<br/>
	 *
	 * @param encrypted 目标密文
	 * @param session_key 会话ID
	 * @param iv 加密算法的初始向量
	 */
	public static String wxDecrypt(String encrypted, String session_key, String iv) {
		String json = null;
		byte[] encrypted64 = org.apache.commons.codec.binary.Base64.decodeBase64(encrypted);
		byte[] key64 = org.apache.commons.codec.binary.Base64.decodeBase64(session_key);
		byte[] iv64 = org.apache.commons.codec.binary.Base64.decodeBase64(iv);
		byte[] data;
		try {
			init();
			json = new String(decrypt(encrypted64, key64, generateIV(iv64)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 初始化密钥
	 */
	public static void init() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		KeyGenerator.getInstance(KEY_NAME).init(128);
	}

	/**
	 * 生成iv
	 */
	public static AlgorithmParameters generateIV(byte[] iv) throws Exception {
		// iv 为一个 16 字节的数组，这里采用和 iOS 端一样的构造方法，数据全为0
		// Arrays.fill(iv, (byte) 0x00);
		AlgorithmParameters params = AlgorithmParameters.getInstance(KEY_NAME);
		params.init(new IvParameterSpec(iv));
		return params;
	}

	/**
	 * 生成解密
	 */
	public static byte[] decrypt(byte[] encryptedData, byte[] keyBytes, AlgorithmParameters iv)
			throws Exception {
		Key key = new SecretKeySpec(keyBytes, KEY_NAME);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		// 设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		return cipher.doFinal(encryptedData);
	}


	@Override
	public List<PluginAttribute> getAttributeKeys() {
		List<PluginAttribute> data = new ArrayList<>();
		data.add(new PluginAttribute("appid","appid",PluginAttribute.STRING_VALUE));
		data.add(new PluginAttribute("appSecret","appSecret",PluginAttribute.STRING_VALUE));
		return data;
	}

	@Override
	public Map<String,String> getOauth2AccessToken(SysPlugin sysPlugin,String auth_code) throws Exception {

		String requestUrl = oauth2_access_token_url.replace("APPID", sysPlugin.getAttribute("appid")).replace("SECRET", sysPlugin.getAttribute("appSecret")).replace("CODE", auth_code);
		String resultJson = get(requestUrl,null);

		JSONObject jsonObject = JSON.parseObject(resultJson);
		Map<String,String> data = new HashMap<>();
		data.put("token",jsonObject.getString("access_token"));
		data.put("userId",jsonObject.getString("openid"));
		if (jsonObject.containsKey("unionid")) {
			data.put("unionid", jsonObject.getString("unionid"));
		}
		data.put("session_key",jsonObject.getString("session_key"));
		return data;

	}

	@Override
	public Map<String,String> getUserInfoByToken(SysPlugin sysPlugin,String token,String userId,String encryptData,String iv,String sessionKey) throws Exception {
		String jsonStr = wxDecrypt(encryptData,sessionKey, iv);
		JSONObject jsonData = JSON.parseObject(jsonStr);
		if (jsonData==null) {
			throw new CustomException("解密失败");
		}
		Map<String,String> data = new HashMap<>();
		data.put("mobile",jsonData.getString("phoneNumber"));
		return data;
	}

}