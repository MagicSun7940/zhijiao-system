package com.zhijiao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 岗位申请实体类
 */
@Data
@TableName("post_application")
public class Application {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 申请的岗位ID */
    private Long postId;

    /** 申请人（教师）ID */
    private Long teacherId;

    /** 简历文件URL（OSS地址） */
    private String resumeUrl;

    /** 申请说明 */
    private String applyReason;

    /** 状态：0-待审核 1-通过 2-拒绝 */
    private Integer status;

    /** 学校反馈意见 */
    private String feedback;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
