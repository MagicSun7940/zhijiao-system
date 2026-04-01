package com.zhijiao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 支教岗位实体类
 */
@Data
@TableName("post_info")
public class Post {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 岗位标题 */
    private String title;

    /** 发布学校ID */
    private Long schoolId;

    /** 学校名称（冗余字段，方便查询） */
    private String schoolName;

    /** 岗位类别ID */
    private Long categoryId;

    /** 课程类型，如：数学、语文、英语 */
    private String courseType;

    /** 招募人数 */
    private Integer headcount;

    /** 开始日期 */
    private LocalDate startDate;

    /** 结束日期 */
    private LocalDate endDate;

    /** 岗位详情描述 */
    private String description;

    /** 岗位地址 */
    private String address;

    /** 状态：0-待审核 1-已发布 2-已关闭 3-审核拒绝 */
    private Integer status;

    /** 已申请人数 */
    private Integer appliedCount;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
