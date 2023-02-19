/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-07
*/
package com.rzico.core.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.rzico.annotation.Log;
import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.core.entity.SysFile;
import com.rzico.core.entity.SysMenu;
import com.rzico.core.entity.SysPlugin;
import com.rzico.core.entity.SysUser;
import com.rzico.core.plugin.StoragePlugin;
import com.rzico.core.service.SysFileService;
import com.rzico.core.service.SysPluginService;
import com.rzico.core.service.SysUserService;
import com.rzico.entity.PageResult;
import com.rzico.entity.Pageable;
import com.rzico.util.CodeGenerator;
import com.rzico.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件表控制层
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2020-02-07
 */
@Api(description = "文件表接口")
@RestController
@RequestMapping("/file")
public class FileController extends BaseController {

    @Autowired
    private SysFileService sysFileService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPluginService sysPluginService;

    /**
     *
     * 查询分页记录
     *
     * @return
    **/
    @ApiOperation("查询分页记录")
    @GetMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "orderId", value = "关联订单", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "orderType", value = "单据类型", dataType = "String", paramType = "query")
    })
    public CommResult<SysFile> list(Long orderId, String orderType, Pageable pageable) {
        Map<String, Object> params = new HashMap<String, Object>();
        params = buildSortField(params, pageable);
        if (StringUtils.isNotEmpty(orderType)){
            params.put("orderType", orderType);
        }
        if (orderId!=null){
            params.put("orderId", orderId);
        }

        Page<Object> startPage = PageHelper.startPage(pageable.getPageNum(), pageable.getPageSize());
        List<SysFile> list = sysFileService.selectList(params);
        PageResult<SysFile> pageResult = new PageResult<SysFile>(list, startPage.getTotal(), pageable);
        return CommResult.success(pageResult);
    }

    /**
     * 保存记录
     *
     * @param sysFiles
     * @return
     */
    @ApiOperation("保存记录")
    @PostMapping("/add")
    public CommResult<SysFile> add(@RequestBody List<SysFile> sysFiles) {
        SysUser sysUser = sysUserService.getCurrent();
        if (sysUser==null) {
            return CommResult.error("请重新登录");
        }
        for (SysFile sysFile:sysFiles) {
            sysFile.setId(CodeGenerator.getUUID());
            sysFile.setMchId(sysUser.getMchId());
            sysFile.setCreateDate(new Date());
            if (sysFile.getPluginId() == null) {
                sysFile.setPluginId("ossPlugin");
                sysFile.setPluginName("阿里云oss");
            }
            sysFile.setDelFlag(false);
            sysFile.setStatus(0);
            int affectCount = sysFileService.insert(sysFile);
            if (affectCount <= 0) {
                return CommResult.error();
            }
         }
        return CommResult.success();
    }

    /**
     * 上传
     */
    @Log(desc = "上传", type = Log.LOG_TYPE.ADD)
    @ApiOperation("上传")
    @PostMapping("/upload")
    public CommResult<SysFile> upload(Long orderId, String orderType,MultipartFile file) {
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
            sysFile.setOrderId(orderId);
            sysFile.setOrderType(orderType);

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
