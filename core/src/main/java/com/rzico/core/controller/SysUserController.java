package com.rzico.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.core.entity.*;
import com.rzico.core.service.SysMchService;
import com.rzico.core.service.SysRoleUserService;
import com.rzico.core.service.SysUserDataScopeService;
import com.rzico.core.service.SysUserService;
import com.rzico.entity.PageResult;
import com.rzico.entity.Pageable;
import com.rzico.jwt.JwtTokenUtil;
import com.rzico.util.*;
import com.rzico.util.StringUtils;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
*
<pre>
 * 系统用户控制层
 * </pre>
*
* @author Rzico Boot
* @version 1.0
*/
@Api(description = "用户信息")
@RestController
@RequestMapping("/admin/sysUser")
public class SysUserController extends BaseController {

    private Logger log = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMchService sysMchService;

    @Autowired
    private SysUserDataScopeService sysUserDataScopeService;

    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

    /**
     * 查询记录
     *
     * @return
     */
    @ApiOperation(value = "查询用户记录")
    @GetMapping(value = "/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "用户账号", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "username", value = "用户账号", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "status", value = "状态", dataType = "Integer", paramType = "query") ,
            @ApiImplicitParam(name = "nickname", value = "用户名", dataType = "String", paramType = "query")
        })
    public CommResult<SysUser> list(String username, String mobile, Integer status,
                                    String nickname,Pageable pageable) {

        CommResult<SysUser> commResult = new CommResult<SysUser>();
        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);

        SysMch sysMch = sysMchService.getCurrent();
        params.put("mchId", sysMch.getId());

        if (StringUtils.isNotEmpty(username)){
            params.put("username", username);
        }
        if (StringUtils.isNotEmpty(mobile)){
            params.put("mobile", mobile);
        }
        if (null != status){
            params.put("status", status);
        }
        if (StringUtils.isNotEmpty(nickname)){
            params.put("nickname", nickname);
        }
        params.put("userType", 1);
        params.put("delFlag", false);
        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<SysUser> list = sysUserService.selectList(params);
        for (SysUser u:list) {
            u.getRoleList();
        }
        PageResult<SysUser> pageResult = new PageResult<SysUser>(list, startPage.getTotal(), pageable);
        commResult.setData(pageResult);

        return commResult;
    }

    @ApiOperation("查询单条记录")
    @GetMapping(value = "/find/{id}")
    public CommResult<SysUser> find(@ApiParam(name="id",value="用户ID",required=true)@PathVariable String id) {
        SysUser result = sysUserService.findById(id);
        Map<String,Object> data = new HashMap<>();
        data.put("sysUser",result);
        data.put("dataScopeList",sysUserDataScopeService.getItemList(result.getId()));
        data.put("roleList",result.getRoleList());
        return CommResult.success(data);
    }


    /**
     * 用户中心发起镜像同步
     *
     * @param userInfo
     * @return
     */
    @ApiOperation("用户中心发起镜像同步")
    @Log(desc = "用户中心发起镜像同步")
    @PostMapping("/mirror")
    public CommResult<SysUser> mirror(@RequestBody SysUser userInfo,String mchId,String enPassword,Integer userType) {

        Boolean isNew = false;

        SysUser sysUser = sysUserService.findByUsername(userInfo.getUsername());
        if (sysUser==null) {
            sysUser = new SysUser();
            isNew = true;
//        } else {
//            if (!sysUser.getId().equals(userInfo.getId())) {
//                return CommResult.error("手机号已经使用");
//            }
        }

        sysUser.setId(userInfo.getId());
        sysUser.setCreateDate(new Date());
        sysUser.setUsername(userInfo.getUsername());
        sysUser.setNickname(userInfo.getNickname());
        sysUser.setAvatar(userInfo.getAvatar());
        sysUser.setMobile(userInfo.getMobile());
        sysUser.setEmail(userInfo.getEmail());

        sysUser.setDelFlag(false);
        sysUser.setStatus(SysUser.STATUS_ENABLED);
        sysUser.setUserType(userType);

        sysUser.setMchId(mchId);
        sysUser.setPassword(enPassword);

        if (isNew) {
            int affectCount = sysUserService.insert(sysUser);
            if (affectCount <= 0) {
                return CommResult.error();
            }
        } else {
            int affectCount = sysUserService.updateByPrimaryKeySelective(sysUser);
            if (affectCount <= 0) {
                return CommResult.error();
            }
        }
        return CommResult.success();

    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除记录,ids用逗号拼接")
    @PostMapping("/del/{ids}")
    public CommResult<SysUser> del(@PathVariable String ids) {
        CommResult<SysUser> commResult = new CommResult<SysUser>();
        sysUserService.deleteByIds(ids.split(","));
        return commResult;
    }

    /**
     * 获取授权
     *
     * @param userId
     * @return
     *
     */
    @ApiOperation(value = "/getScopes", httpMethod = "GET", notes = "获取授权")
    @Log(desc = "获取授权", type = Log.LOG_TYPE.SELECT)
    @GetMapping(value = "/getScopes")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required=true, dataType = "String", paramType = "query")})
    public CommResult<SysUser> getScopes(String userId) throws Exception {
        Map<String,Object> data = new HashMap<>();
        SysUser sysUser = sysUserService.findById(userId);
        data.put("roles",sysUser.getRoleList());
        data.put("dataScopeList",sysUserDataScopeService.getItemList(sysUser.getId()));
        return CommResult.success(data);
    }

    /**
     * 用户授权
     *
     * @param userId
     * @param roles
     * @param officeList
     * @return
     */
    @ApiOperation(value = "/updateScopes", httpMethod = "POST", notes = "用户授权")
    @Log(desc = "用户授权", type = Log.LOG_TYPE.UPDATE)
    @PostMapping(value = "/updateScopes")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required=true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "roles", value = "角色id(可传多个)", required=false, dataType = "String", paramType = "query")
    })
    public CommResult<SysUser> updateScopes(String userId, String roles[], @RequestBody List<SysUserDataScope> officeList) throws Exception {

        SysUser oldUser = sysUserService.selectByPrimaryKey(userId);

        int affectCount = sysUserService.updateRoles(oldUser, roles, officeList);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        return CommResult.success();

    }

    /**
     * 更新记录
     *
     * @param sysUser
     * @return
     */
    @ApiOperation("更新记录")
    @PostMapping("/update")
    public CommResult<SysUser> update(@RequestBody SysUser sysUser) {
        int affectCount = sysUserService.updateByPrimaryKeySelective(sysUser);
        if (affectCount <= 0){
            return CommResult.error(CommResult.FAIL_MSG);
        }
        SysUser result = sysUserService.findById(sysUser.getId());
        return CommResult.success(result);
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
     * 重置密码
     *
     * @param userId
     * @return
     */
    @ApiOperation("重置密码")
    @PostMapping("/resetPwd")
    @ApiImplicitParams({@ApiImplicitParam(name = "userId", value = "用户Id", dataType = "String", allowMultiple = true, paramType = "query")})
    public CommResult<SysMchMenu> resetPwd(String userId) {

        SysUser sysUser = sysUserService.findById(userId);
        if (sysUser==null) {
            return CommResult.error("没有设置管理员");
        }

        String password = MD5Utils.getMD5Str("123456");
        sysUser.setPassword(MD5Utils.getMD5Str(password.trim() + sysUser.getUsername().trim()));

        sysUserService.update(sysUser);

        return CommResult.success();

    }


}
