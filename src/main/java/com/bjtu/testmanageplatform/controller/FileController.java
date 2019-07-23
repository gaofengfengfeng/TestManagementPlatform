package com.bjtu.testmanageplatform.controller;

import com.bjtu.testmanageplatform.beans.FileUploadReq;
import com.bjtu.testmanageplatform.beans.FileUploadResponse;
import com.bjtu.testmanageplatform.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/v1/file")
public class FileController {
    private FileService fileService;

    @Autowired
    public FileController(FileService fileService){ this.fileService = fileService; }


    /**
     * 上传文件
     *
     */
    @RequestMapping(value = "/upload")
    public FileUploadResponse upload(MultipartFile file , HttpServletRequest request) {
        log.info("upload file:{}",file.getOriginalFilename());
        FileUploadResponse fileUploadResponse = new FileUploadResponse();
        String BackUri = fileService.upload(file, request);

        //上传失败返回BackUri是null
        if(BackUri == null){
            fileUploadResponse.setErr_no(102500001);
            fileUploadResponse.setErr_msg("Failed to upload file");
            return fileUploadResponse;
        }

        FileUploadResponse.FileUploadResData fileUploadResData = new FileUploadResponse.FileUploadResData();
        fileUploadResData.setFile_url(BackUri);
        fileUploadResponse.setData(fileUploadResData);
        return fileUploadResponse;
    }


    /**
     * 可以直接接受实体类，觉得不行可以删掉
     * @param fileUploadReq
     * @param request
     * @return
     */
//    @RequestMapping(value = "/upload1")
//    public FileUploadResponse saveRule(@ModelAttribute FileUploadReq fileUploadReq, HttpServletRequest request) {
//        //String；
//        FileUploadReq.FileUploadData fileUploadData = fileUploadReq.getData();
//        log.info("upload file:={}",fileUploadData.getUpload_file().getOriginalFilename());
//        FileUploadResponse fileUploadResponse = new FileUploadResponse();
//        String BackUri = fileService.upload(fileUploadData.getUpload_file(), request);
//
//        //上传失败返回BackUri是null
//        if(BackUri == null){
//            fileUploadResponse.setErr_no(102500001);
//            fileUploadResponse.setErr_msg("Failed to upload file");
//            return fileUploadResponse;
//        }
//
//        FileUploadResponse.FileUploadResData fileUploadResData = new FileUploadResponse.FileUploadResData();
//        fileUploadResData.setFile_url(BackUri);
//        fileUploadResponse.setData(fileUploadResData);
//        return fileUploadResponse;
//    }
}
