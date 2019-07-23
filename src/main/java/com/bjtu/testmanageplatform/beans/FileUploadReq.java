package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JRequest;
import lombok.Data;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Date: 2019-07-19
 */
@Data
public class FileUploadReq extends JRequest {

    private FileUploadData data;

    @Data
    public static class FileUploadData {
        @NonNull
        private MultipartFile upload_file;
    }

}
