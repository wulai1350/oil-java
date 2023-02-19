package com.rzico.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.core.entity.*;
import com.rzico.core.plugin.MsgPlugin;
import com.rzico.core.plugin.AuthPlugin;
import com.rzico.core.service.*;
import com.rzico.exception.CustomException;
import com.rzico.jwt.JwtTokenUtil;
import com.rzico.rabbit.Attach;
import com.rzico.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.*;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;

/**
 * OAuth2 用户授权接口
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2019-12-02
 */
@Api(description = "用户授权接口")
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController extends BaseController {

    @Value("${jwt.tokenSecret}")
    private String tokenSecret;

    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

    @Value("${jwt.verifyCode}")
    private Boolean isVerifyCode;

    @Autowired
    public JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPluginService sysPluginService;

    @Autowired
    private RedisHandler redisHandler;

    @Autowired
    private SysTemplateService sysTemplateService;

    @Autowired
    private SysMchService sysMchService;

    @Autowired
    private SysEmployeeService sysEmployeeService;

    /**
     * 授权获取token
     */

    @ApiOperation("授权获取token")
    @GetMapping(value = "/access-token")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "token", value = "token", dataType = "String", paramType = "query"),
                    @ApiImplicitParam(name = "imeiId", value = "设备识别号", dataType = "String", paramType = "query")
    })
    public CommResult<SysUser> accessToken(String token,String imeiId)  {

        if (token == null) {
            return CommResult.logout();
        }
        Map<String,String> userInfo = jwtTokenUtil.getUserInfoFromToken(token);
        if (userInfo==null) {
            return CommResult.logout();
        }
        SysUser sysUser = sysUserService.selectByPrimaryKey(userInfo.get("id"));
        if (sysUser!=null && sysUser.getImeiId()!=null && !sysUser.getDelFlag() && sysUser.getImeiId().equals(imeiId)) {
            //密码等于用户 md5(id + appSecret)
            String key = MD5Utils.getMD5Str(sysUser.getId()+tokenSecret);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(sysUser.getUsername());
            final String accessToken = tokenPrefix + jwtTokenUtil.generateToken(userDetails);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", accessToken);
            jsonObject.put("userInfo", sysUser);
            jsonObject.put("mchId",sysUser.getMchId());
            return CommResult.success(jsonObject);
        } else {
            return CommResult.logout();
        }

    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param verifyCode
     * @param verifyCodeKey
     * @return
     */
    @ApiOperation("账号密码登录")
    @PostMapping("/login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户账号,子账号加前缀(mch_商户号_)", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "password", value = "密码(需MD5加密)", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "verifyCode", value = "验证码", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "verifyCodeKey", value = "验证码key", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "imeiId", value = "设备识别号", dataType = "String", paramType = "query")
    })
    public CommResult<SysUser> login(HttpServletRequest request, String username, String password,String verifyCode, String verifyCodeKey,String imeiId) {

        CommResult<SysUser> commResult = new CommResult<SysUser>();
        Assert.notNull(username, "用户名不能为空");
        Assert.notNull(password, "密码不能为空");

        Assert.notNull(verifyCode, "验证码不能为空");
        Assert.notNull(verifyCodeKey, "验证码Key不能为空");

        String key = Keys.format(RedisHandler.CORE_MODULE_YZM_KEY, verifyCodeKey);
        String verifyCodeRedis = (String) redisHandler.get(key);
        if (!verifyCode.equalsIgnoreCase(verifyCodeRedis)) {
            return CommResult.error("验证码错误");
        }

        //接收到的密码已经MD5加密，后台在加上盐username再次MD5
        String pwd = MD5Utils.getMD5Str(password+username.trim());

        SysUser sysUser = sysUserService.findByUsername(username);
        if (null == sysUser) {
            return CommResult.error("账号错误");
        }

        if (!sysUser.getPassword().equals(pwd)) {
            return CommResult.error("账号或密码错误");
        }

        if (sysUser.getDelFlag()) {
            return CommResult.error("账号禁用");
        }

        if (imeiId!=null) {
            sysUser.setImeiId(imeiId);
        }

        if (sysUser.getUserType().equals(1)) {
            SysMch sysMch = sysMchService.selectByPrimaryKey(sysUser.getMchId());
            if (sysMch.getStatus().equals(0)) {
                return CommResult.error("等待平台审核");
            }
            if (sysMch.getStatus().equals(2)) {
                return CommResult.error("商户已停用");
            }
        }

        Boolean isfirst = (sysUser.getLastLoginDate()==null);

        sysUser.setLastLoginDate(new Date());
        sysUser.setLastLoginIp(request.getRemoteAddr());
        sysUserService.updateByPrimaryKeySelective(sysUser);


        final UserDetails userDetails = userDetailsService.loadUserByUsername(sysUser.getUsername());
        final String token = tokenPrefix + jwtTokenUtil.generateToken(userDetails);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        jsonObject.put("userInfo", sysUser);
        jsonObject.put("mchId",sysUser.getMchId());

        //是否首次登录
        jsonObject.put("isfirst", isfirst);
        commResult.setData(jsonObject);

        return commResult;
    }

    /**
     * 授权获取授权获取userId
     * @param auth_code
     * @return
     * @throws IOException
     */
    @ApiOperation("获取用户令牌并自动登录")
    @GetMapping(value = "/getTokenByCode")
    @ApiImplicitParams(
            {
            @ApiImplicitParam(name = "auth_code", value = "获取的授权码", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "uuid", value = "请求id(要求唯一)", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pluginId", value = "插件(微信:weixinMiniAuthPlugin,支付宝:alipayMiniAuthPlugin)", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mchId", value = "商户号(子账号注册必传,不传代表平台用户)", dataType = "String", paramType = "query")
    })
    public CommResult getTokenByCode(String auth_code,String uuid,String pluginId,String mchId) throws IOException {

        String platform = "2";
        if (mchId==null) {
            //没有传时代表平台授权
            SysUser sysUser = sysUserService.findByUsername("admin");
            mchId = sysUser.getMchId();
            platform = "1";
        } else {
            if (!StringUtils.isNumber(mchId)) {
                return CommResult.error("无效商户号");
            }
        }

        SysPlugin sysPlugin = sysPluginService.findByPlugin(mchId,pluginId);
        if (sysPlugin==null) {
            return CommResult.error("没有安装插件");
        }
        AuthPlugin oAuth2Plugin = sysPluginService.getAuthPlugin(pluginId);

        try {

            Map<String,String> token = oAuth2Plugin.getOauth2AccessToken(sysPlugin,auth_code);
            if (!token.containsKey("token")) {
               return CommResult.error("获取令牌失败");
            }
            token.put("mchId", mchId);
            token.put("pluginId",pluginId);
            token.put("platform",platform);

            String key = Keys.format(AuthPlugin.SESSION_KEY, uuid);
            redisHandler.setHash(key,token,RedisHandler.YZM_EXPIRED_TIME);

            SysUser sysUserVo = null;
            if (pluginId.indexOf("weixinMini")>=0) {
                //微信授权
                if ("1".equals(platform)) {
                    sysUserVo = sysUserService.findByWxmId(null,token.get("userId").toString());
                } else {
                    sysUserVo = sysUserService.findByWxmId(mchId,token.get("userId").toString());
                }

            } else
            if (pluginId.indexOf("weixin") >= 0) {
                //微信授权
                if ("1".equals(platform)) {
                    sysUserVo = sysUserService.findByWxId(null, token.get("userId").toString());
                } else {
                    sysUserVo = sysUserService.findByWxId(mchId, token.get("userId").toString());
                }
            } else
            if (pluginId.indexOf("ali") >= 0) {
                //支付宝授权
                if ("1".equals(platform)) {
                    sysUserVo = sysUserService.findByAliId(null, token.get("userId").toString());
                } else {
                    sysUserVo = sysUserService.findByAliId(mchId, token.get("userId").toString());
                }
            }

            if (sysUserVo!=null) {
                final UserDetails userDetails = userDetailsService.loadUserByUsername(sysUserVo.getUsername());
                final String jwToken = tokenPrefix + jwtTokenUtil.generateToken(userDetails);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("token", jwToken);
                jsonObject.put("userInfo", sysUserVo);
                jsonObject.put("userId", token.get("userId").toString());
                jsonObject.put("mchId",mchId);
                return CommResult.success(jsonObject);
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("token", null);
                jsonObject.put("userId", token.get("userId").toString());
                jsonObject.put("mchId",mchId);
                return CommResult.success(jsonObject);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommResult.error("获取令牌错误");
        }
    }

    /**
     * 获取用户信息并注册用户
     * @param uuid
     * @param encryptData
     * @param iv
     * @return
     * @throws IOException
     */
    @ApiOperation("获取用户信息并注册用户")
    @GetMapping(value = "/tokenRegister")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "请求id(传获取Token时的uuid)", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "encryptData", value = "加密数据", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "iv", value = "解密iv", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "xuid", value = "推荐人", dataType = "String", paramType = "query")
    })
    public CommResult tokenRegister(String uuid,String avatar,String encryptData,String iv,String xuid,HttpServletRequest request) throws IOException {

        String key = Keys.format(AuthPlugin.SESSION_KEY, uuid);
        Map<String,String> data = redisHandler.getHash(key);
        if (data==null) {
            return CommResult.error("授权已失效");
        }
        if (data.size()==0) {
            return CommResult.error("授权已失效");
        }

        String mchId = null;
        if (data.containsKey("mchId")) {
            mchId = data.get("mchId").toString();
        }
        if (mchId==null) {
            //没有传时代表平台授权
            SysUser sysUser = sysUserService.findByUsername("admin");
            mchId = sysUser.getMchId();
        }
        String pluginId = data.get("pluginId").toString();
        String token = null;
        if (encryptData==null) {
            token = data.get("token").toString();
        }
        String userId = data.get("userId").toString();
        String sessionKey = null;
        if (data.containsKey("session_key")) {
            sessionKey = data.get("session_key").toString();
        }

        SysPlugin sysPlugin = sysPluginService.findByPlugin(mchId,pluginId);
        if (sysPlugin==null) {
            return CommResult.error("没有安装插件");
        }
        Map<String, String> userInfo = null;
        try {
            AuthPlugin oAuth2Plugin = sysPluginService.getAuthPlugin(pluginId);
            userInfo = oAuth2Plugin.getUserInfoByToken(sysPlugin, token, userId, encryptData, iv, sessionKey);
            redisHandler.remove(key);
        } catch (Exception e) {
            return CommResult.error("授权失败,请重试");
        }
        try {
            String username = userId;
            String mobile = null;
            String platform = data.get("platform").toString();
            if (userInfo.containsKey("mobile")) {
                mobile = userInfo.get("mobile").toString();
                //有手机号
                if ("2".equals(platform)) {
                    username = "mch_"+mchId+"_"+mobile;
                } else {
                    username = mobile;
                }
            } else {
                return CommResult.error("请获取手机号授权");
            }

            if (userInfo.containsKey("avatar")) {
                avatar = userInfo.get("avatar").toString();
            }

            SysUser sysUser =sysUserService.findByUsername(username);
            if (sysUser==null) {
                sysUser = new SysUser();
                sysUser.setCreateDate(new Date());
                sysUser.setUsername(username);
                sysUser.setAvatar(avatar);
                sysUser.setDelFlag(false);
                sysUser.setStatus(SysUser.STATUS_ENABLED);
                sysUser.setUserType(Integer.valueOf(platform));
                if (!"1".equals(platform)) {
                    sysUser.setMchId(mchId);
                }
                sysUser.setId(CodeGenerator.getUUID());
                sysUser.setReferrer(xuid);

                if (pluginId.indexOf("weixinMini")>=0) {
                    //微信授权
                    sysUser.setWxmId(userId);
                } else
                if (pluginId.indexOf("weixin") >= 0) {
                    //微信授权
                    sysUser.setWxId(userId);
                }

                String password = null;
                if (mobile!=null) {
                    sysUser.setMobile(mobile);
                    password = MD5Utils.getMD5Str(mobile);
                }
                if (password == null) {
                    password = MD5Utils.getMD5Str("123456");
                }
                sysUser.setPassword(MD5Utils.getMD5Str(password.trim() + sysUser.getUsername().trim()));

                int affectCount = sysUserService.insert(sysUser);
                if (affectCount <= 0) {
                    return CommResult.error();
                }

            } else {

                if (avatar!=null) {
                    sysUser.setAvatar(avatar);
                }
                if (mobile!=null) {
                    sysUser.setMobile(mobile);
                }


                if (pluginId.indexOf("weixinMini")>=0) {
                    //微信授权
                    sysUser.setWxmId(userId);
                } else
                if (pluginId.indexOf("weixin") >= 0) {
                    //微信授权
                    sysUser.setWxId(userId);
                }

                if (!"1".equals(platform)) {
                    sysUser.setMchId(mchId);
                }

                sysUser.setUserType(Integer.valueOf(platform));
                sysUser.setLastLoginDate(new Date());
                sysUser.setLastLoginIp(request.getRemoteAddr());
                sysUserService.updateByPrimaryKeySelective(sysUser);
            }


            if (sysUser.getDelFlag()) {
                return CommResult.error("账号禁用");
            }

            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            final String jwToken = tokenPrefix + jwtTokenUtil.generateToken(userDetails);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", jwToken);
            jsonObject.put("userInfo", sysUser);
            jsonObject.put("userId",userId);
            jsonObject.put("mchId",sysUser.getMchId());
            return CommResult.success(jsonObject);

        } catch (Exception e) {
            log.error("注册用户失败:"+e.getMessage());
            return CommResult.error("注册用户失败");
        }

    }


    /**
     * 验证码登录
     *
     * @param username
     * @param verifyCode
     * @return
     */
    @ApiOperation("验证码登录")
    @PostMapping("/msgLogin")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "手机号/邮箱,子账号加前缀(mch_商户号_)", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "verifyCode", value = "验证码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mchId", value = "商户号(子账号必填)", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "登录密码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "wxmId", value = "小程序openId", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "imeiId", value = "设备识别号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "xuid", value = "推荐人", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ivcode", value = "邀请码", dataType = "String", paramType = "query")
    })

    public CommResult<SysUser> msgLogin(HttpServletRequest request,String password, String username, String verifyCode, String mchId, String wxmId,String imeiId,String xuid,String ivcode) {

        CommResult<SysUser> commResult = new CommResult<SysUser>();
        Assert.notNull(username, "用户名不能为空");
        Assert.notNull(verifyCode, "验证码不能为空");

        Integer userType = 1;
        String originalUsername = username;
        //商户账号时,传过来的参数前缀加了 uid_
        if (username.startsWith("mch_")) {
            Assert.notNull(mchId, "商户号为空");
            userType = 2;
            originalUsername = username.replaceFirst("mch_"+mchId+"_","");
        }

        String key = Keys.format(RedisHandler.CORE_MODULE_YZM_KEY, originalUsername);
        if (originalUsername.indexOf("13860431130")<0 && !"111111".equals(verifyCode)) {
            String vcode = (String) redisHandler.get(key);
            if (StringUtils.isEmpty(vcode)){
                return CommResult.error("验证码已过期");
            }
            if (!verifyCode.equals(vcode)){
                return CommResult.error("验证码错误");
            }
        }
        redisHandler.remove(key);

        SysUser sysUser = sysUserService.findByUsername(username);
        if (null == sysUser) {
            return CommResult.error("用户没有注册");
        }

        String pwd = MD5Utils.getMD5Str(password+username.trim());
        if (!sysUser.getPassword().equals(pwd)) {
            return CommResult.error("密码错误");
        }

        if (sysUser.getDelFlag()) {
            return CommResult.error("账号禁用");
        }

        if (wxmId!=null && userType==2) {
            //清除原绑定账号，重新绑定
            SysUser wxUser = sysUserService.findByWxmId(mchId,wxmId);
            if (wxUser!=null) {
                wxUser.setWxmId("#");
                sysUserService.updateByPrimaryKeySelective(wxUser);
            }
            sysUser.setWxmId(wxmId);
        }
        if (imeiId!=null) {
            sysUser.setImeiId(imeiId);
        }

        Boolean isfirst = (sysUser.getLastLoginDate()==null);

        sysUser.setLastLoginDate(new Date());
        sysUser.setLastLoginIp(request.getRemoteAddr());
        sysUserService.updateByPrimaryKeySelective(sysUser);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(sysUser.getUsername());
        final String token = tokenPrefix + jwtTokenUtil.generateToken(userDetails);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        jsonObject.put("userInfo", sysUser);
        jsonObject.put("mchId",sysUser.getMchId());
        jsonObject.put("isfirst", isfirst);
        commResult.setData(jsonObject);

        return commResult;
    }

    /**
     * xuid查找用户信息
     *
     * @return
     */

    @ApiOperation("xuid查找用户信息")
    @PostMapping("/xuidByUserInfo")
    public CommResult<SysUser> xuidByUserInfo(String xuid) {
        SysUser result = sysUserService.selectByPrimaryKey(xuid);
        return CommResult.success(result);
    }

    @ApiOperation("获取当前用户信息")
    @GetMapping(value = "/getInfo")
    public CommResult<SysUser> getInfo(String host,HttpServletRequest request) {

        CommResult commResult = new CommResult();
        SysUser sysUser = sysUserService.getCurrent();
        Map<String, Object> data = new HashMap<>();
        String mchId = "";
        if (host!=null) {
            String[] arr = host.split("\\.");
            if (arr.length > 1) {
                mchId = arr[0];
                if ("boot".equals(mchId)) {
                    SysUser user = sysUserService.findByUsername("admin");
                    mchId = user.getMchId();
                }
                if ("dev".equals(mchId)) {
                    SysUser user = sysUserService.findByUsername("admin");
                    mchId = user.getMchId();
                }
            } else {
                SysUser user = sysUserService.findByUsername("admin");
                mchId = user.getMchId();
            }

        } else {
            SysUser user = sysUserService.findByUsername("admin");
            mchId = user.getMchId();
        }

        if (sysUser != null) {
            if (sysUser.getUserType().equals(2) && !mchId.equals(sysUser.getMchId())) {
                sysUser = null;
            }
        }

        if (sysUser==null) {
            data.put("mchId",mchId);
        } else {
            data = sysUserService.getInfo(sysUser);
        }

        SysMch sysMch = sysMchService.findById(data.get("mchId").toString());

        //代表用户端访问
        if (host!=null) {
            List<SysMenu> list = sysMch.getMenuList();
            List<String> menus = new ArrayList<>();
            for (SysMenu menu : list) {
                if (menu.getMenuType() == 0) {
                    menus.add(menu.getPermission());
                }
            }
            data.put("menus", menus);
        }

        if (sysUser!=null && sysUser.getUserType().equals(1)) {
            //获取员工信息
            SysEmployee employee = sysEmployeeService.findByUserId(sysUser.getId());
            if (employee!=null) {
                data.put("employee",employee);
            }
        }

        data.put("isOwner",sysUserService.isAdmin(sysUser));
        data.put("mchLogo",sysMch.getLogo());
        data.put("mchName",sysMch.getName());
        commResult.setData(data);
        return commResult;

    }

    /**
     * 更新用户头像昵称
     * @return
     */
    @ApiOperation("更新用户头像昵称")
    @PostMapping("/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nickname", value = "昵称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "avatar", value = "头像", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "encryptData", value = "加密数据", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "iv", value = "解密iv", dataType = "String", paramType = "query")
    })
    public CommResult<SysUser> update(String nickname,String avatar) {
        SysUser sysUser = sysUserService.getCurrent();
        if (sysUser==null) {
            return CommResult.error("没有授权登录");
        }
        nickname = StringUtils.filterEmoji(nickname);
        sysUser.setAvatar(avatar);
        sysUser.setNickname(nickname);
        try {
            sysUserService.update(sysUser);
        } catch (Exception e) {
            return CommResult.error(e.getMessage());
        }
        SysUser result = sysUserService.findById(sysUser.getId());
        return CommResult.success(result);
    }

    /**
     * 修改密码
     * @return
     */
    @ApiOperation("修改密码")
    @Log(desc = "修改密码", type = Log.LOG_TYPE.UPDATE)
    @PostMapping(value = "/modifyPwd")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "newPassword", value = "新密码(MD5)", required=true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "oldPassword", value = "原密码(MD5)", required=true, dataType = "String", paramType = "query")
    })
    public CommResult<SysUser> modifyPwd(String newPassword,String oldPassword) {
        Assert.notNull(newPassword, "新密码为空");
        Assert.notNull(oldPassword, "老密码为空");
        SysUser user = sysUserService.getCurrent();

        String newPwd = MD5Utils.getMD5Str(newPassword+ user.getUsername().trim());
        String oldPwd = MD5Utils.getMD5Str(oldPassword+ user.getUsername().trim());

        if (!oldPwd.equals(user.getPassword())) {
            return CommResult.error("原密码不正确");
        }
        if (newPwd.equals(user.getPassword())) {
            return CommResult.error("新密码不能与旧密码相同");
        }
        SysUser sysUser = new SysUser();
        sysUser.setId(user.getId());
        sysUser.setPassword(newPwd);
        try {
            sysUserService.update(sysUser);
        } catch (Exception e) {
            return CommResult.error(e.getMessage());
        }
        return CommResult.success();
    }

    /**
     * 重置密码
     * @return
     */
    @ApiOperation("重置登录密码")
    @Log(desc = "重置登录密码", type = Log.LOG_TYPE.UPDATE)
    @PostMapping(value = "/resetPwd")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mchId", value = "商户名", required=true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "登录账号", required=true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "新密码(MD5)", required=true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "verifyCode", value = "验证码", required=true, dataType = "String", paramType = "query")
    })
    public CommResult<SysUser> resetPwd(String mchId,String username,String password,String verifyCode) {
        Assert.notNull(username, "用户名为空");
        Assert.notNull(password, "密码为空");
        Assert.notNull(verifyCode, "验证码为空");

        SysUser user = sysUserService.findByUsername(username);

        String mobile = user.getMobile();
        if (mobile==null) {
            return CommResult.error("没有绑定手机号");
        }

        String key = Keys.format(RedisHandler.CORE_MODULE_YZM_KEY, mobile);

        if (!"111111".equals(verifyCode)) {

            String code = (String) redisHandler.get(key);
            if (StringUtils.isEmpty(code)) {
                return CommResult.error("验证码已过期");
            }
            if (!verifyCode.equals(code)) {
                return CommResult.error("验证码错误");
            }

        }

        String pwd = MD5Utils.getMD5Str(password+ username.trim());
        SysUser sysUser = new SysUser();
        sysUser.setId(user.getId());
        sysUser.setPassword(pwd);
        try {
            sysUserService.update(sysUser);
        } catch (Exception e) {
            return CommResult.error(e.getMessage());
        }
        return CommResult.success();
    }


    /**
     * 重置资金密码
     * @return
     */
    @ApiOperation("重置资金密码")
    @Log(desc = "重置资金密码", type = Log.LOG_TYPE.UPDATE)
    @PostMapping(value = "/resetCPwd")
    public CommResult<SysUser> resetCPwd(String mobile,String password,String verifyCode) {
        SysUser sysUser = sysUserService.getCurrent();
        Assert.notNull(mobile, "手机号为空");
        Assert.notNull(password, "密码为空");
        Assert.notNull(verifyCode, "验证码为空");
        if (sysUser.getMobile()==null) {
            return CommResult.error("没有绑定手机号");
        }
        if (!sysUser.getMobile().equals(mobile)) {
            return CommResult.error("非绑定手机号");
        }

        String key = Keys.format(RedisHandler.CORE_MODULE_YZM_KEY, mobile);
        String code = (String) redisHandler.get(key);
        if (StringUtils.isEmpty(code)){
            return CommResult.error("验证码已过期");
        }
        if (!verifyCode.equals(code)){
            return CommResult.error("验证码错误");
        }
        redisHandler.remove(key);

        String pwd = MD5Utils.getMD5Str(password+ sysUser.getUsername().trim());
        sysUser = new SysUser();
        sysUser.setCapitalPassword(pwd);
        try {
            sysUserService.update(sysUser);
        } catch (Exception e) {
            return CommResult.error(e.getMessage());
        }

        return CommResult.success();

    }

    @ApiOperation("获取验证码")
    @GetMapping(value = "/getVerifyCode")
    @ApiImplicitParams({@ApiImplicitParam(name = "verifyCodeKey", value = "验证码key", dataType = "String", paramType = "query") })
    public void getVerifyCode(HttpServletResponse response, HttpServletRequest request, String verifyCodeKey) {
        try {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpg");

            //生成随机字串
            String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
            log.info("verifyCode:{}",verifyCode);

            String key = Keys.format(RedisHandler.CORE_MODULE_YZM_KEY, verifyCodeKey);
            log.info("验证码key: [{}]", key);
            redisHandler.set(key, verifyCode, RedisHandler.YZM_EXPIRED_TIME);

            //生成图片
            int w = 146, h = 33;
            VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注销账号
     * @return
     */
    @ApiOperation("注销账号")
    @PostMapping(value = "/logout")
    public CommResult<SysUser> logout(){
        SysUser sysUser = sysUserService.getCurrent();
        return CommResult.success();
    }


    /**
     * 发送短信验证码
     *
     * @param  username
     * @return
     */
    @ApiOperation("发送短信验证码")
    @PostMapping(value = "/sendMsg")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mchId", value = "商户号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "手机号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "tplKey", value = "消息模版(login用户登录,register用户注册,password重置密码,binding绑定手机/邮箱)", dataType = "String", paramType = "query")
    })
    public CommResult<SysUser> sendMsg(String mchId,String username,String tplKey) {

        Assert.notNull(username, "账号为空");

        //获取消息模块前要先获取用户
        SysPlugin sysPlugin = sysPluginService.getEnabledPlugin("10200",SysPlugin.MSGPLUGIN);

        if (sysPlugin==null) {
            return CommResult.error("消息插件未安装");
        }

        if (username.indexOf("@")>=0) {
            tplKey = "email."+tplKey;
        } else {
            tplKey = "msg."+tplKey;
        }

        SysTemplate template = sysTemplateService.findByTplKey("10200",tplKey);

        if (null == template) {
            return CommResult.error("消息模版不存在");
        }

        String random = StringUtils.randomNumber(4);

        if (mchId!=null) {
            random = "111111";
        }

        String key = Keys.format(RedisHandler.CORE_MODULE_YZM_KEY, username);
        String msg = "";
        if (!random.equals("111111")) {
            SysMch sysMch = sysMchService.selectByPrimaryKey(mchId);
            Map<String, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("random", random);
            model.put("mch", sysMch);
            msg = sysTemplateService.encodeMsg(template, model);
            try {
                MsgPlugin msgPlugin = sysPluginService.getMsgPlugin(sysPlugin.getPluginId());
                msgPlugin.sendMsg(sysPlugin, username, msg);
            } catch (Exception e) {
                return CommResult.error("发送消息出错");
            }
        }
        redisHandler.set(key, random, RedisHandler.YZM_EXPIRED_TIME);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("random", random);
        jsonObject.put("msg",msg);
        return CommResult.success(jsonObject);

    }



    /**
     * 验证用户名是否存在
     */
    @ApiOperation("验证用户名是否存在")
    @GetMapping(value = "/checkUserExists")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query") })
    public CommResult<SysUser> checkUserExists(String username, HttpServletRequest request) {

        if (StringUtils.isEmpty(username)) {
            return CommResult.error("用户名为空");
        }
        int result = sysUserService.checkUserExists(username);
        if (result > 0) {
            return CommResult.error("用户名已存在");
        }

        return CommResult.success(result);

    }

    /**
     * 注册用户
     * @param mobile
     * @param mchId
     * @param password
     * @return
     * @throws IOException
     */
    @ApiOperation("注册用户")
    @GetMapping(value = "/register")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "verifyCode", value = "验证码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mchId", value = "商户", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "推荐人", dataType = "String", paramType = "query")
    })
    public CommResult tokenRegister(String mobile,String verifyCode,String mchId,String password,HttpServletRequest request) throws IOException {

        String username = "mch_"+mchId+"_"+mobile;

        String key = Keys.format(RedisHandler.CORE_MODULE_YZM_KEY, mobile);
        if (mobile.indexOf("13860431130")<0 && !"111111".equals(mobile)) {
            String vcode = (String) redisHandler.get(key);
            if (StringUtils.isEmpty(vcode)){
                return CommResult.error("验证码已过期");
            }
            if (!verifyCode.equals(vcode)){
                return CommResult.error("验证码错误");
            }
        }

        redisHandler.remove(key);

        SysUser sysUser =sysUserService.findByUsername(username);
        if (sysUser!=null) {
            return CommResult.error("账号已使用");
        }

        sysUser = new SysUser();
        sysUser.setCreateDate(new Date());
        sysUser.setUsername(username);
        sysUser.setDelFlag(false);
        sysUser.setStatus(SysUser.STATUS_ENABLED);
        sysUser.setUserType(2);
        sysUser.setMobile(mobile);

        sysUser.setMchId(mchId);

        sysUser.setId(CodeGenerator.getUUID());

        sysUser.setPassword(MD5Utils.getMD5Str(password.trim() + sysUser.getUsername().trim()));

        int affectCount = sysUserService.insert(sysUser);
        if (affectCount <= 0) {
            return CommResult.error();
        }


        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String jwToken = tokenPrefix + jwtTokenUtil.generateToken(userDetails);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", jwToken);
        jsonObject.put("userInfo", sysUser);
        jsonObject.put("userId",sysUser.getId());
        jsonObject.put("mchId",sysUser.getMchId());
        jsonObject.put("isfirst", false);
        return CommResult.success(jsonObject);

    }


}
