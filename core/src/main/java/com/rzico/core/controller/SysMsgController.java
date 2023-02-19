/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-09
*/
package com.rzico.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.core.entity.*;
import com.rzico.core.plugin.MsgPlugin;
import com.rzico.core.service.*;
import com.rzico.entity.Filter;
import com.rzico.entity.Pageable;
import com.rzico.entity.PageResult;
import com.rzico.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.Api;

import java.util.*;

/**
 * 文件表控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2020-02-09
 */
@Api(description = "文件表接口")
@RestController
@RequestMapping("/admin/sysMsg")
public class SysMsgController extends BaseController {

    @Autowired
    private SysMsgService sysMsgService;

    @Autowired
    private SysTemplateService sysTemplateService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMchService sysMchService;

    @Autowired
    private SysPluginService sysPluginService;

    @Autowired
    private RedisHandler redisHandler;

    /**
     * 查询分页记录
     *
     * @return
    */
    @ApiOperation("查询分页记录")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query")
    })
    public CommResult<SysMsg> list(String startDate, String endDate, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);
        if (StringUtils.isNotEmpty(startDate)){
            params.put("startDate", startDate);
        }
        if (StringUtils.isNotEmpty(endDate)){
            params.put("endDate", endDate);
        }

        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<SysMsg> list = sysMsgService.selectList(params);
        PageResult<SysMsg> pageResult = new PageResult<SysMsg>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 查询单条记录
     *
     * @return
    */
    @ApiOperation("查询单条记录")
    @GetMapping("/find/{id}")
    public CommResult<SysMsg> find(@PathVariable String id) {
        SysMsg result = sysMsgService.findById(id);
        Map<String,Object> data = new HashMap<>();
        data.put("sysMsg",result);
        return CommResult.success(data);
    }

    /**
     * 保存记录
     *
     * @param sysMsg
     * @return
     */
    @ApiOperation("保存记录")
    @PostMapping("/save")
    public CommResult<SysMsg> save(@RequestBody SysMsg sysMsg) {
        int affectCount = sysMsgService.insert(sysMsg);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        SysMsg result = sysMsgService.findById(sysMsg.getId());
        return CommResult.success(result);
    }


    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Log(desc = "批量删除", type = Log.LOG_TYPE.DEL)
    @ApiOperation("批量删除记录,ids用逗号拼接")
    @PostMapping("/del/{ids}")
    public CommResult<SysMsg> del(@PathVariable String ids) {
        sysMsgService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新记录
     *
     * @param sysMsg
     * @return
     */
    @Log(desc = "更新记录", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新记录")
    @PostMapping("/update")
    public CommResult<SysMsg> update(@RequestBody SysMsg sysMsg) {
        int affectCount = sysMsgService.updateByPrimaryKeySelective(sysMsg);
        if (affectCount <= 0){
            return CommResult.error();
        }
        SysMsg result = sysMsgService.findById(sysMsg.getId());
        return CommResult.success(result);
    }

    /**
     * 发送消息
     *
     * @param sysMsg
     * @return
     */
    @Log(desc = "发送消息", type = Log.LOG_TYPE.ADD)
    @ApiOperation("发送消息")
    @PostMapping("/sendMsg")
    public CommResult<SysMsg> sendMsg (@RequestBody SysMsg sysMsg, String[]officeIds, String[]postIds){
        SysMsgPush sysMsgPush = new SysMsgPush();
        SysUser sysUser = sysUserService.getCurrent();
        sysMsgPush.setMchId(sysUser.getMchId());//商号
        sysMsgPush.setIsAttac(sysMsg.getIsAttac());//附件
        sysMsgPush.setMsgTitle(sysMsg.getMsgTitle());//标题
        sysMsgPush.setMsgLevel(sysMsg.getMsgLevel());//级别
        sysMsgPush.setMsgType(sysMsg.getMsgType());//类型
        sysMsgPush.setMsgContent(sysMsg.getMsgContent());//内容
        sysMsgPush.setIsPush(true);//是否推送
        sysMsgPush.setSendDate(new Date());//发送时间
        sysMsgPush.setSendUserId(sysUser.getId());//发送者id
        sysMsgPush.setSendUserName(sysUser.getUsername());//发送者名
        sysMsgService.save(sysMsgPush, officeIds, postIds);
        return CommResult.success();
    }


}
