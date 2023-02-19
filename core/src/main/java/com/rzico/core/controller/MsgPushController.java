/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-06-11
*/
package com.rzico.core.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.core.entity.SysMsgPush;
import com.rzico.core.entity.SysUser;
import com.rzico.core.service.SysMsgPushService;
import com.rzico.core.service.SysUserService;
import com.rzico.entity.PageResult;
import com.rzico.entity.Pageable;
import com.rzico.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息通讯控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2020-06-11
 */
@Api(description = "消息通讯接口")
@RestController
@RequestMapping("/msgPush")
public class MsgPushController extends BaseController {

    @Autowired
    private SysMsgPushService sysMsgPushService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 分页查询我的消息
     *
     * @return
    */
    @ApiOperation("分页查询我的消息")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "msgType", value = "消息类型", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "msgKey", value = "业务类型", dataType = "String", paramType = "query")
    })
    public CommResult<SysMsgPush> list(Integer msgType,String msgKey, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("sortField","send_date");
        params.put("sortType","desc");

        if (msgType!=null){
            params.put("msgType", msgType);
        }

        if (msgKey!=null){
            params.put("msgKey", msgKey);
        }

        SysUser sysUser = sysUserService.getCurrent();
        if (sysUser!=null) {
            params.put("receiveUserId", sysUser.getId());
        } else {
            params.put("receiveUserId", "#");
        }

        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<SysMsgPush> list = sysMsgPushService.selectList(params);
        PageResult<SysMsgPush> pageResult = new PageResult<SysMsgPush>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);

    }

    /**
     * 我的消息会话
     *
     * @return
     */
    @ApiOperation("我的消息会话")
    @GetMapping("/conversation")
    public CommResult<SysMsgPush> conversation(Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();

        SysUser sysUser = sysUserService.getCurrent();
        if (sysUser!=null) {
            params.put("receiveUserId", sysUser.getId());
        } else {
            params.put("receiveUserId", "#");
        }

        List<SysMsgPush> list = sysMsgPushService.conversation(params);
        return CommResult.success(list);

    }

    /**
     * 查询单条消息通讯
     *
     * @return
    */
    @ApiOperation("查询单条消息通讯")
    @GetMapping("/find/{id}")
    public CommResult<SysMsgPush> find(@PathVariable String id) {
        SysMsgPush result = sysMsgPushService.findById(id);

        Map<String,Object> data = new HashMap<>();
        data.put("sysMsgPush",result);
        return CommResult.success(data);

    }

    /**
     * 批量删除消息通讯
     *
     * @param ids
     * @return
     */
    @Log(desc = "批量删除消息通讯", type = Log.LOG_TYPE.DEL)
    @ApiOperation("批量删除消息通讯,ids用逗号拼接")
    @PostMapping("/del/{ids}")
    public CommResult<SysMsgPush> del(@PathVariable String ids) {

        sysMsgPushService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新消息通讯
     *
     * @param sysMsgPush
     * @return
     */
    @Log(desc = "更新消息通讯", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新消息通讯")
    @PostMapping("/update")
    public CommResult<SysMsgPush> update(@RequestBody SysMsgPush sysMsgPush) {
        int affectCount = sysMsgPushService.updateByPrimaryKeySelective(sysMsgPush);
        if (affectCount <= 0){
            return CommResult.error();
        }
        SysMsgPush result = sysMsgPushService.findById(sysMsgPush.getId());
        return CommResult.success(result);
    }


    /**
     * 未读消息数
     *
     * @return
     */
    @Log(desc = "未读消息数", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("未读消息数")
    @PostMapping("/unReadCount")
    public CommResult<SysMsgPush> unReadCount() {
        Map<String,Object> params = new HashMap<>();
        params.put("readStatus",'2');
        SysUser sysUser = sysUserService.getCurrent();
        if (sysUser!=null) {
            params.put("receiveUserId", sysUser.getId());
        } else {
            params.put("receiveUserId", "#");
        }
        List<Map> data = sysMsgPushService.unReadCount(params);

        return CommResult.success(data);
    }

    /**
     * 批量标记为已读
     *
     * @param msgKey
     * @return
     */
    @Log(desc = "更新消息通讯", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新消息通讯")
    @PostMapping("/markAsRead")
    public CommResult<SysMsgPush> markAsRead(String msgKey) {
        Map<String,Object> params = new HashMap<>();
        params.put("msgKey",msgKey);
        params.put("readStatus",'2');

        SysUser sysUser = sysUserService.getCurrent();
        if (sysUser!=null) {
            params.put("receiveUserId", sysUser.getId());
        } else {
            params.put("receiveUserId", "#");
        }

        int affectCount = sysMsgPushService.markAsRead(params);
        return CommResult.success();
    }


}
