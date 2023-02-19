/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-07
*/
package com.rzico.core.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.core.entity.SysPlugin;
import com.rzico.core.entity.SysUser;
import com.rzico.core.plugin.StoragePlugin;
import com.rzico.core.service.SysPluginService;
import com.rzico.core.service.SysUserService;
import com.rzico.entity.Filter;
import com.rzico.entity.Pageable;
import com.rzico.entity.PageResult;
import com.rzico.core.entity.SysFile;
import com.rzico.core.service.SysFileService;
import com.rzico.util.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.Api;
import com.rzico.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

/**
 * 文件表控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2020-02-07
 */
@Api(description = "文件表接口")
@RestController
@RequestMapping("/admin/sysFile")
public class SysFileController extends BaseController {

    @Autowired
    private SysFileService sysFileService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPluginService sysPluginService;


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
    public CommResult<SysFile> list(String startDate, String endDate, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);
        if (StringUtils.isNotEmpty(startDate)){
            params.put("startDate", startDate);
        }
        if (StringUtils.isNotEmpty(endDate)){
            params.put("endDate", endDate);
        }

        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<SysFile> list = sysFileService.selectList(params);
        PageResult<SysFile> pageResult = new PageResult<SysFile>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 查询单条记录
     *
     * @return
    */
    @ApiOperation("查询单条记录")
    @GetMapping("/find/{id}")
    public CommResult<SysFile> find(@PathVariable String id) {
        SysFile result = sysFileService.findById(id);
        Map<String,Object> data = new HashMap<>();
        data.put("sysFile",result);
        return CommResult.success(data);
    }

    /**
     * 保存记录
     *
     * @param sysFile
     * @return
     */
    @ApiOperation("保存记录")
    @PostMapping("/save")
    public CommResult<SysFile> save(@RequestBody SysFile sysFile) {
        if (sysFile.getId()==null) {
            sysFile.setId(CodeGenerator.getUUID());
        }
        SysUser sysUser = sysUserService.getCurrent();
        sysFile.setCreateDate(new Date());
        sysFile.setMchId(sysUser.getMchId());
        sysFile.setDelFlag(false);
        int affectCount = sysFileService.insert(sysFile);
        if (affectCount <= 0) {
            return CommResult.error();
        }
        SysFile result = sysFileService.findById(sysFile.getId());
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
    public CommResult<SysFile> del(@PathVariable String ids) {
        sysFileService.deleteByIds(ids.split(","));
        return CommResult.success();
    }

    /**
     * 更新记录
     *
     * @param sysFile
     * @return
     */
    @Log(desc = "更新记录", type = Log.LOG_TYPE.UPDATE)
    @ApiOperation("更新记录")
    @PostMapping("/update")
    public CommResult<SysFile> update(@RequestBody SysFile sysFile) {
        int affectCount = sysFileService.updateByPrimaryKeySelective(sysFile);
        if (affectCount <= 0){
            return CommResult.error();
        }
        SysFile result = sysFileService.findById(sysFile.getId());
        return CommResult.success(result);
    }

    /**
     * 上传
     */
    @Log(desc = "上传", type = Log.LOG_TYPE.ADD)
    @ApiOperation("上传")
    @PostMapping("/upload")
    public CommResult<SysFile> upload(MultipartFile file) {
        try {
            if (file==null) {
                return CommResult.error("上传文件为空");
            }
            SysUser sysUser = sysUserService.getCurrent();
            if (sysUser==null) {
                sysUser = sysUserService.findByUsername("admin");
            }

            SysPlugin sysPlugin = sysPluginService.getEnabledPlugin(sysUser.getMchId(),SysPlugin.STORAGEPLUGIN);
            if (sysPlugin==null) {
                return CommResult.error("存储插件没有安装");
            }
            StoragePlugin oss = sysPluginService.getStoragePlugin(sysPlugin.getPluginId());

            String originFileName = file.getOriginalFilename();
            String suffix = oss.getSuffixByFilename(originFileName);

            SysFile sysFile = new SysFile();
            sysFile.setId(CodeGenerator.getUUID());
            sysFile.setFileName(originFileName);
            sysFile.setFileType(oss.getFileType(suffix));
            sysFile.setFileUrl(sysFile.getId());
            sysFile.setStatus(0);
            sysFile.setDelFlag(false);
            sysFile.setMchId(sysPlugin.getMchId());
            sysFile.setPluginId(oss.getId());
            sysFile.setPluginName(oss.getName());
            sysFile.setContent(file.getBytes());
            sysFile.setOrderType("pcks");

            sysFileService.insert(sysFile);

            Map<String,String> data = new HashMap<>();
            data.put("id",sysFile.getId());
            return CommResult.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return CommResult.error(e.getMessage());
        }
    }

}
