
package com.rzico.core.plugin.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.rzico.core.entity.SysPlugin;
import com.rzico.core.model.PluginAttribute;
import com.rzico.core.plugin.StoragePlugin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import org.springframework.web.multipart.MultipartFile;

/**
 * Plugin - 阿里云存储
 * 
 * @author RZICO.BOOT
 * @version 3.0
 */
@Component("ossPlugin")
public class OssPlugin extends StoragePlugin {

	@Override
	public String getName() {
		return "阿里云OSS存储";
	}

	@Override
	public String getVersion() {
		return "3.8.0";
	}

	@Override
	public List<PluginAttribute> getAttributeKeys() {
		List<PluginAttribute> data = new ArrayList<>();
		data.add(new PluginAttribute("accessId","accessId",PluginAttribute.STRING_VALUE));
		data.add(new PluginAttribute("accessKey","accessKey",PluginAttribute.STRING_VALUE));
		data.add(new PluginAttribute("bucketName","bucketName",PluginAttribute.STRING_VALUE));
		data.add(new PluginAttribute("endpoint","endpoint",PluginAttribute.STRING_VALUE));
		data.add(new PluginAttribute("urlPrefix","urlPrefix",PluginAttribute.STRING_VALUE));

		return data;
	}

	@Override
	public void upload(SysPlugin sysPlugin,String path, MultipartFile file, String contentType) throws IOException {
		if (sysPlugin != null) {
			String accessId = sysPlugin.getAttribute("accessId");
			String accessKey = sysPlugin.getAttribute("accessKey");
			String bucketName = sysPlugin.getAttribute("bucketName");
			String endpoint = sysPlugin.getAttribute("endpoint");
			InputStream inputStream = file.getInputStream();
			try {
				OSSClient ossClient = new OSSClient(endpoint,accessId, accessKey);
				ObjectMetadata objectMetadata = new ObjectMetadata();
				objectMetadata.setContentType(contentType);
				objectMetadata.setContentLength(file.getSize());
				ossClient.putObject(bucketName, StringUtils.removeStart(path, "/"), inputStream, objectMetadata);
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			} finally {
				inputStream.close();
			}
		} else {
			throw new IOException("无效插件");
		}
	}

}