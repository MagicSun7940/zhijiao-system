package com.zhijiao.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 教学分享发布 DTO
 */
@Data
public class ShareDTO {

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "内容不能为空")
    private String content;

    /** 附件URL列表，JSON数组字符串，由前端上传OSS后传入URL */
    private String attachments;
}
