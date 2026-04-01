package com.zhijiao.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 岗位发布/修改 DTO
 */
@Data
public class PostDTO {

    @NotBlank(message = "岗位标题不能为空")
    private String title;

    @NotNull(message = "岗位类别不能为空")
    private Long categoryId;

    @NotBlank(message = "课程类型不能为空")
    private String courseType;

    @NotNull(message = "招募人数不能为空")
    @Min(value = 1, message = "招募人数至少为1")
    private Integer headcount;

    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;

    @NotNull(message = "结束日期不能为空")
    private LocalDate endDate;

    @NotBlank(message = "岗位描述不能为空")
    private String description;

    private String address;
}
