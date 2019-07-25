package com.bjtu.testmanageplatform.controller;

import com.bjtu.testmanageplatform.beans.FileUploadResponse;
import com.bjtu.testmanageplatform.service.FileService;
import com.bjtu.testmanageplatform.util.JLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/v1/file")
public class FileController {
    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }


    /**
     * 上传文件
     */
    @RequestMapping(value = "/upload")
    public FileUploadResponse upload(MultipartFile file, HttpServletRequest request) {
        JLog.info(String.format("upload file:%s", file.getOriginalFilename()));
        FileUploadResponse fileUploadResponse = new FileUploadResponse();
        String BackUri = fileService.upload(file);

        //上传失败返回BackUri是null
        if (BackUri == null) {
            fileUploadResponse.setErr_no(102500001);
            fileUploadResponse.setErr_msg("Failed to upload file");
            return fileUploadResponse;
        }

        FileUploadResponse.FileUploadResData fileUploadResData =
                new FileUploadResponse.FileUploadResData();
        fileUploadResData.setFile_url(BackUri);
        fileUploadResponse.setData(fileUploadResData);
        return fileUploadResponse;
    }

}
