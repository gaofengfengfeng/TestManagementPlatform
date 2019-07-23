package com.bjtu.testmanageplatform.beans;

import com.bjtu.testmanageplatform.beans.base.JResponse;
import lombok.Data;

/**
 * @Date: 2019-07-19
 */
@Data
public class FileUploadResponse extends JResponse {

    private FileUploadResData data;

    @Data
    public static class FileUploadResData{
        private String file_url;
    }
}
