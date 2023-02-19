
package com.rzico.core.plugin.msg;

import com.alibaba.fastjson.JSON;
import com.rzico.core.entity.SysPlugin;
import com.rzico.core.model.PluginAttribute;
import com.rzico.core.plugin.MsgPlugin;
import com.rzico.exception.CustomException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Plugin -  创蓝短信
 * 
 * @author RZICO.BOOT
 * @version 3.0
 */
@Component("chuangLanPlugin")
public class ChuangLanPlugin extends MsgPlugin {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public String getName() {
		return "创蓝验证码/友盟推送";
	}

	@Override
	public String getVersion() {
		return "1.8.0";
	}


	@Override
	public List<PluginAttribute> getAttributeKeys() {
		List<PluginAttribute> data = new ArrayList<>();
		data.add(new PluginAttribute("appid","appid",PluginAttribute.STRING_VALUE));
		data.add(new PluginAttribute("appkey","appkey",PluginAttribute.STRING_VALUE));
		data.add(new PluginAttribute("umengAndroidAppkey","androidAppkey",PluginAttribute.STRING_VALUE));
		data.add(new PluginAttribute("umengAndroidMasterSecret","androidMasterSecret",PluginAttribute.STRING_VALUE));
		data.add(new PluginAttribute("umengIOSAppkey","IOSAppkey",PluginAttribute.STRING_VALUE));
		data.add(new PluginAttribute("umengIOSMasterSecret","IOSMasterSecret",PluginAttribute.STRING_VALUE));
		return data;
	}

	/**
	 *
	 * @author tianyh
	 * @Description
	 * @param path
	 * @param postContent
	 * @return String
	 * @throws
	 */
	public static String sendSmsByPost(String path, String postContent) {
		URL url = null;
		try {
			url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");// 提交模式
			httpURLConnection.setConnectTimeout(10000);//连接超时 单位毫秒
			httpURLConnection.setReadTimeout(2000);//读取超时 单位毫秒
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type", "application/json");

			httpURLConnection.connect();
			OutputStream os=httpURLConnection.getOutputStream();
			os.write(postContent.getBytes("UTF-8"));
			os.flush();

			StringBuilder sb = new StringBuilder();
			int httpRspCode = httpURLConnection.getResponseCode();
			if (httpRspCode == HttpURLConnection.HTTP_OK) {
				// 开始获取数据
				BufferedReader br = new BufferedReader(
						new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
				return sb.toString();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 发送验证码
	 * @param sysPlugin 配置
	 * @param username 手机
	 * @param content 内容
	 * @return 请求参数
	 */
	@Override
	public void sendMsg(SysPlugin sysPlugin,String username,String content) throws IOException {
		try {
			SmsSendRequest smsSingleRequest = new SmsSendRequest(sysPlugin.getAttribute("appid"), sysPlugin.getAttribute("appkey"), content, username,"true");
			String requestJson = JSON.toJSONString(smsSingleRequest);
			logger.info("短信发送内容:[{}]", requestJson);
			String response = sendSmsByPost("http://smssh1.253.com/msg/send/json", requestJson);
			SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
			if (!smsSingleResponse.getCode().equals("0")) {
				throw new CustomException("发送失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException("发送失败");
		}
	}

}