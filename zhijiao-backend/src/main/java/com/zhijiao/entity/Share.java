package com.zhijiao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 教学分享实体类
 */
@Data
@TableName("share_content")
public class Share {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 发布者（教师）ID */
    private Long teacherId;

    /** 标题 */
    private String title;

    /** 正文内容 */
    private String content;

    /** 附件URL列表（JSON数组字符串，存图片/视频） */
    private String attachments;

    /** 状态：0-待审核 1-已发布 2-已拒绝 */
    private Integer status;

    /** 浏览量 */
    private Integer viewCount;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
