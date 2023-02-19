/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-04-16
*/
package com.rzico.core.controller;

import com.rzico.base.BaseController;
import com.rzico.base.CommResult;
import com.rzico.core.entity.SysMch;
import com.rzico.core.service.SysMchService;
import com.rzico.exception.CustomException;
import com.rzico.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * 二维码相关服务
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2020-04-16
 */
@Api(description = "二维码相关服务")
@RestController
@RequestMapping("/zxing")
public class ZxingController extends BaseController {

    @Value("${rzico.filePath.windows}")
    private String windowsFilePath;

    @Value("${rzico.filePath.linux}")
    private String linuxFilePath;

    @Autowired
    private SysMchService sysMchService;

    @ApiOperation("生成二维码图片")
    @GetMapping("/createImage")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "data", value = "数据", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mchId", value = "mchId", dataType = "String", paramType = "query")
    })
    public void createImage(String data,String mchId,HttpServletResponse response) {
        try {

            response.reset();
            response.setContentType("image/jpeg;charset=utf-8");
            ServletOutputStream output = response.getOutputStream();// 得到输出流
            try {
                if (mchId!=null) {

                    //根据reportName下载文件
                    String filePath = "";
                    if (System.getProperty("os.name").contains("Windows")) {
                        filePath = windowsFilePath;
                    } else if (System.getProperty("os.name").contains("Linux")) {
                        filePath = linuxFilePath;
                    }

                    SysMch sysMch = sysMchService.selectByPrimaryKey(mchId);
                    if (sysMch.getLogo()==null || "".equals(sysMch.getLogo())) {
                        sysMch.setLogo("http://cdnx.rzico.com/images/fraudLogo.png");
                    }

                    String fileName = MD5Utils.getMD5Str(sysMch.getLogo())+".jpg";

                    File file = new File(filePath + File.separator + fileName);
                    if (!file.exists()) {
                        try {
                            FileUtils.UrlDownLoad(ImageUtils.cropImage(sysMch.getLogo(),80,80),filePath, fileName);
                        } catch (Exception e) {
                            logger.info("打印模板下载失败");
                            throw new CustomException("打印模板下载失败");
                        }
                    }

                    QRCodeUtil.encode(data, filePath + File.separator + fileName, output, true);

                } else {
                    QRCodeUtil.encode(data, output);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @ApiOperation("图片代理服务")
    @GetMapping("/proxyImage")
    @ApiImplicitParams({@ApiImplicitParam(name = "url", value = "图片地址", dataType = "String", paramType = "query")
    })
    public void proxyImage(String url,HttpServletResponse response) {
        try {
            response.reset();
            response.setContentType("image/jpeg;charset=utf-8");
            ServletOutputStream output = response.getOutputStream();// 得到输出流
            try {
                FileOutputStream fileOut = null;
                HttpURLConnection conn = null;
                InputStream inputStream = null;
                try{
                    URL httpUrl=new URL(url);
                    conn=(HttpURLConnection) httpUrl.openConnection();
                    //以Post方式提交表单，默认get方式
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    // post方式不能使用缓存
                    conn.setUseCaches(false);
                    conn.connect();
                    //获取网络输入流
                    inputStream = conn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    //判断文件的保存路径后面是否以/结尾

                    byte[] buf = new byte[4096];
                    int length = bis.read(buf);
                    //保存文件
                    while(length != -1){
                        output.write(buf, 0, length);
                        length = bis.read(buf);
                    }
                    bis.close();
                    conn.disconnect();
                } catch (Exception e){
                    e.printStackTrace();
                    System.out.println("抛出异常！！");
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取LocalIp
     * @return
     * @throws IOException
     */
    @ApiOperation("获取本地IP地址")
    @GetMapping(value = "/getLocalIp")

    public CommResult getLocalIp() throws IOException {
        return CommResult.success(IPUtils.getLocalIp());
    }
}
