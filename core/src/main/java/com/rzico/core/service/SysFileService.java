/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-07
*/
package com.rzico.core.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysFile;
import com.rzico.core.entity.SysPlugin;
import com.rzico.core.plugin.StoragePlugin;
import com.rzico.util.CodeGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.core.mapper.SysFileMapper;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 * 文件表业务类
 * </pre>
 *
 * @author Rzico Boot
 * @version 1.0
 */
@Service
public class SysFileService extends BaseServiceImpl<SysFile, String> {

    @Autowired
    private SysFileMapper sysFileMapper;

    @Override
    public BaseMapper<SysFile, String> getMapper() {
        return sysFileMapper;
    }

    public List<String> upload(SysPlugin sysPlugin, StoragePlugin oss, HttpServletRequest request) {

        List<String> files = new ArrayList<>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> fileList = multipartRequest.getFiles("file");
        if (fileList == null || fileList.size() == 0) {
            throw new RuntimeException("无效文件");
        } else {
            for (int i = 0; i < fileList.size(); i++) {

                MultipartFile multipartFile = fileList.get(0);
                String originFileName = multipartFile.getOriginalFilename();
                String suffix = oss.getSuffixByFilename(originFileName);
//                originFileName = originFileName.substring(0, originFileName.length() - suffix.length());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String savePath = "/upload/" + sdf.format(new Date()) + "/" + CodeGenerator.getUUID() + suffix;
                try {
                    oss.upload(sysPlugin, savePath, multipartFile, oss.getMineType(suffix));
                    String fileUrl = StringUtils.removeEnd(sysPlugin.getAttribute("host"), "/") + savePath;
                    files.add(fileUrl);

//                    SysFile sysFile = new SysFile();
//                    sysFile.setFileName(originFileName);
//                    sysFile.setFileType(oss.getFileType(suffix));
//                    sysFile.setFileUrl(fileUrl);
//                    sysFile.setStatus(0);
//                    sysFile.setDelFlag(false);
//                    sysFile.setMchId(sysPlugin.getMchId());
//                    sysFile.setPluginId(oss.getId());
//                    sysFile.setPluginName(oss.getName());
//
//                    super.insert(sysFile);

                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }

            }

        }
        return files;
    }

}
