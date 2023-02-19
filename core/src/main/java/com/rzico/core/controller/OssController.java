package com.rzico.core.controller;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.rzico.annotation.Log;
import com.rzico.base.CommResult;
import com.rzico.core.entity.SysMch;
import com.rzico.core.entity.SysPlugin;
import com.rzico.core.entity.SysUser;
import com.rzico.core.model.OssCallbackParam;
import com.rzico.core.model.OssCallbackResult;
import com.rzico.core.model.OssPolicyResult;
import com.rzico.core.service.SysMchService;
import com.rzico.core.service.SysPluginService;
import com.rzico.core.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Oss相关操作接口
 * Created by macro on 2018/4/26.
 */
@Controller
@Api(tags = "OssController", description = "Oss管理")
@RequestMapping("/aliyun/oss")
public class OssController {

    private OSSClient ossClient;

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysPluginService sysPluginService;

    @Autowired
    private SysMchService sysMchService;

    // 目前只有"cn-hangzhou"这个region可用, 不要使用填写其他region的值
    public static final String REGION_CN_HANGZHOU = "cn-hangzhou";
    // 当前 STS API 版本
    public static final String STS_API_VERSION = "2015-04-01";

    static AssumeRoleResponse assumeRole(String accessKeyId, String accessKeySecret,
                                         String roleArn, String roleSessionName, String policy,
                                         ProtocolType protocolType) throws ClientException {
        try {
            // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
            IClientProfile profile = DefaultProfile.getProfile(REGION_CN_HANGZHOU, accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            // 创建一个 AssumeRoleRequest 并设置请求参数
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setVersion(STS_API_VERSION);
            request.setMethod(MethodType.POST);
            request.setProtocol(protocolType);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy);
            // 发起请求，并得到response
            final AssumeRoleResponse response = client.getAcsResponse(request);
            return response;
        } catch (ClientException e) {
            throw e;
        }
    }


    /**
     * 授权获取token
     *
     * @return
     * @throws IOException
     */
    @Log(desc = "授权获取token", type = Log.LOG_TYPE.ATHOR)
    @ApiOperation("授权获取token")
    @GetMapping(value = "/access-token")
    public CommResult accessToken(String mchId) throws IOException {
        if (mchId==null) {
            SysUser sysUser = sysUserService.getCurrent();
            if (sysUser!=null) {
                mchId = sysUser.getMchId();
            } else {
                mchId = "10200";
            }
        }
        SysPlugin sysPlugin = sysPluginService.findByPlugin(mchId, "ossPlugin");
        if (sysPlugin==null) {
            SysUser sysUser = sysUserService.findByUsername("admin");
            mchId = sysUser.getMchId();
            sysPlugin = sysPluginService.findByPlugin(mchId, "ossPlugin");
        }
        if (sysPlugin != null) {
            return CommResult.error("插件没安装");
        }
        // 只有 RAM用户（子账号）才能调用 AssumeRole 接口
        // 阿里云主账号的AccessKeys不能用于发起AssumeRole请求
        // 请首先在RAM控制台创建一个RAM用户，并为这个用户创建AccessKeys
        String accessKeyId = sysPlugin.getAttribute("x-sts-accessKey");
        String accessKeySecret = sysPlugin.getAttribute("x-sts-accessSecret");
        // AssumeRole API 请求参数: RoleArn, RoleSessionName, Policy, and DurationSeconds
        // RoleArn 需要在 RAM 控制台上获取
        String roleArn = sysPlugin.getAttribute("x-sts-roleArn");
        // RoleSessionName 是临时Token的会话名称，自己指定用于标识你的用户，主要用于审计，或者用于区分Token颁发给谁
        // 但是注意RoleSessionName的长度和规则，不要有空格，只能有'-' '_' 字母和数字等字符
        // 具体规则请参考API文档中的格式要求
        String roleSessionName = "rzico-boot";
        // 如何定制你的policy?
        String policy = "{\n" +
                "    \"Version\": \"1\", \n" +
                "    \"Statement\": [\n" +
                "        {\n" +
                "            \"Action\": [oss:*], \n" +
                "            \"Resource\": [\n" +
                "                \"acs:oss:*:*:*\"\n" +
                "            ], \n" +
                "            \"Effect\": \"Allow\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        // 此处必须为 HTTPS
        ProtocolType protocolType = ProtocolType.HTTPS;
        Map<String,String> model = new HashMap<String,String>();
        try {
            final AssumeRoleResponse response = assumeRole(accessKeyId, accessKeySecret,
                    roleArn, roleSessionName, null, protocolType);
            model.put("Expiration",response.getCredentials().getExpiration());
            model.put("AccessKeyId", response.getCredentials().getAccessKeyId());
            model.put("AccessKeySecret",response.getCredentials().getAccessKeySecret());
            model.put("SecurityToken",response.getCredentials().getSecurityToken());
            return CommResult.success(model);
        } catch (ClientException e) {
            return CommResult.error("获取令牌失败");
        }

    }


    @ApiOperation(value = "oss上传签名生成")
    @RequestMapping(value = "/policy", method = RequestMethod.GET)
    @ResponseBody
    public CommResult<OssPolicyResult> policy(String mchId) {

        if (mchId==null) {
            SysUser sysUser = sysUserService.getCurrent();
            if (sysUser!=null) {
                mchId = sysUser.getMchId();
            } else {
                mchId = "10200";
            }
        }

        SysMch sysMch = sysMchService.selectByPrimaryKey(mchId);

        // 存储目录
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dir = mchId + "/upload/"+sdf.format(new Date());

        SysPlugin sysPlugin = sysPluginService.findByPlugin(mchId, "ossPlugin");
        if (sysPlugin==null) {
            SysUser sysUser = sysUserService.findByUsername("admin");
            mchId = sysUser.getMchId();
            sysPlugin = sysPluginService.findByPlugin(mchId, "ossPlugin");
        }
        if (sysPlugin == null) {
            return CommResult.error("插件没安装");
        }

        ossClient = new OSSClient(sysPlugin.getAttribute("endpoint"),sysPlugin.getAttribute("accessId"),sysPlugin.getAttribute("accessKey"));

        String ALIYUN_OSS_DIR_PREFIX = sysPlugin.getAttribute("urlPrefix");
        int ALIYUN_OSS_EXPIRE = 300;
        int ALIYUN_OSS_MAX_SIZE = 1000;
        String ALIYUN_OSS_BUCKET_NAME = sysPlugin.getAttribute("bucketName");
        String ALIYUN_OSS_ENDPOINT = sysPlugin.getAttribute("endpoint");
        //String ALIYUN_OSS_CALLBACK = "";
        OssPolicyResult result = new OssPolicyResult();
        // 签名有效期
        long expireEndTime = System.currentTimeMillis() + ALIYUN_OSS_EXPIRE * 1000;
        Date expiration = new Date(expireEndTime);
        // 文件大小
        long maxSize = ALIYUN_OSS_MAX_SIZE * 1024 * 1024;
        // 回调
        //OssCallbackParam callback = new OssCallbackParam();
        //callback.setCallbackUrl(ALIYUN_OSS_CALLBACK);
        //callback.setCallbackBody("filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
        //callback.setCallbackBodyType("application/x-www-form-urlencoded");
        // 提交节点
        String action = "https://" + ALIYUN_OSS_BUCKET_NAME + "." + ALIYUN_OSS_ENDPOINT;
        try {
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxSize);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String policy = BinaryUtil.toBase64String(binaryData);
            String signature = ossClient.calculatePostSignature(postPolicy);
            //String callbackData = BinaryUtil.toBase64String(JSON.toJSONString(callback).getBytes("utf-8"));
            // 返回结果
            result.setAccessKeyId(ossClient.getCredentialsProvider().getCredentials().getAccessKeyId());
            result.setPolicy(policy);
            result.setSignature(signature);
            result.setDir(dir);
            result.setAction(action);
            result.setHost(ALIYUN_OSS_DIR_PREFIX);
        } catch (Exception e) {
            CommResult.error("签名生成失败");
        }
        return CommResult.success(result);
    }

}
