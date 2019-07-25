package com.bjtu.testmanageplatform.service;

import com.bjtu.testmanageplatform.InitConfig;
import com.bjtu.testmanageplatform.util.Conversion;
import com.bjtu.testmanageplatform.util.JLog;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * @Date: 2019-07-19
 */
@Service
public class FileService {

    //文件储存地址
    private String address = InitConfig.Const.staticAddress;
    //版本号
    private String versionNumber = "v1";
    //识别字符是上传的静态变量
    private String idenChr = "upload_success";

    //可设置上传文件到服务器地址，现在为项目绝对地址
    private String Des_file;

    /**
     * 上传文件,成功返回URI，不成功返回NULL
     */
    public String upload(MultipartFile file) {

        //最终返回的URL
        String BackUri;

        //获取客户端文件名称
        String originFilename = file.getOriginalFilename();
        //获取文件后缀
        String suffix = originFilename.substring(originFilename.lastIndexOf(".") + 1);

        //现在设置上传文件名称为上传时间 + 随机数 + 客户端文件名称
        Random random = new Random();
        Integer randomFileName = random.nextInt(1000);

        //添加时间戳
        Long currentTime = System.currentTimeMillis();

        //生成文件名字与文件地址
        String fileName = currentTime + randomFileName + originFilename;
        String fileNameAfterMd5 = Conversion.getMD5(fileName) + '.' + suffix;
        String fileAddress = address + File.separator + fileNameAfterMd5;

        //新建文件
        File targetFile = new File(fileAddress);
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(targetFile);
            IOUtils.copy(file.getInputStream(), fileOutputStream);
            BackUri = getCompleteUri(fileNameAfterMd5);
            JLog.info(String.format("file:{} uploaded successfully", originFilename));
        } catch (IOException e) {
            BackUri = null;
            JLog.info(String.format("file:{} uploaded failed", originFilename));
            return BackUri;
        }
        return BackUri;
    }

    /**
     * 获取完整uri
     */
    public String getCompleteUri(String fileNameAfterMd5) {
        StringBuffer backUrl = new StringBuffer();
        backUrl.append(versionNumber).append(File.separator).append(idenChr).append(File.separator)
                .append(fileNameAfterMd5);
        return backUrl.toString();
    }

}
