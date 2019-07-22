package com.bjtu.testmanageplatform.service;

import com.bjtu.testmanageplatform.InitConfig;
import com.bjtu.testmanageplatform.util.Conversion;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * @Date: 2019-07-19
 */
@Slf4j
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
    public String upload(MultipartFile file , HttpServletRequest request){

        //获取文件存储地址
//        String address = getProDesFile();

        //最终返回的URL
        String BackUri;

        //获取客户端文件名称
        String suffix = file.getOriginalFilename();
        //获取文件后缀
        String prefix = suffix.substring(suffix.lastIndexOf(".") + 1);

        //现在设置上传文件名称为上传时间 + 随机数 + 客户端文件名称
        Random random = new Random();
        Integer randomFileName = random.nextInt(1000);

        //添加时间戳
        long currentTime= System.currentTimeMillis();
        String dateFileName = String.valueOf(currentTime);

        //生成文件名字与文件地址
        String fileName = dateFileName + randomFileName + suffix;
        String fileAddress = address + File.separator + fileName;

        //新建文件
        File targetFile = new File(fileAddress);
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(targetFile);
            IOUtils.copy(file.getInputStream(), fileOutputStream);
            //String ServerIPPort = getServerIPPort(request);
            BackUri = getCompleteUri(fileAddress);
            BackUri = Conversion.getMD5(BackUri);
            log.info("file:{} uploaded successfully",suffix);
        } catch (IOException e) {
            BackUri = null;
            log.info("file:{} uploaded failed",suffix);
            return BackUri;
        }

        return BackUri;
    }

    /**
     * 获取完整uri
     */
    public String getCompleteUri(String fileAddress){
        StringBuffer backUrl = new StringBuffer();
        backUrl.append(versionNumber).append("/").append(idenChr).append(fileAddress);
        return backUrl.toString();
    }

    /**
     * 在开发测试模式时，得到的地址为：{项目跟目录}/target/static/upload/
     * 在打包成jar正式发布时，得到的地址为：{发布jar包目录}/static/upload/
     * @return
     */
    public String getProDesFile(){

        //String Des_file;
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        File upload = new File(path,"static/upload/");
        if(!upload.exists()) upload.mkdirs();
        System.out.println("upload url:"+upload.getAbsolutePath());
        this.Des_file = upload.getAbsolutePath();
        return Des_file;
    }


    /**
     * 获取服务端信息
     * @param request
     * @return
     */
    public String getServerIPPort(HttpServletRequest request) {
        //+ ":" + request.getServerPort()
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

    /**
     * 获取完整URL
     * @param serverName
     * @param fileAddress
     * @return
     */
    public String getBackUrl(String serverName, String fileAddress){
        StringBuffer backUrl = new StringBuffer();
        backUrl.append(serverName).append("/").append(fileAddress);
        return backUrl.toString();
    }

}
