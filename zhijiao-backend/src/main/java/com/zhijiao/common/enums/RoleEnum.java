package com.zhijiao.common.enums;

import lombok.Getter;

/**
 * 用户角色枚举
 */
@Getter
public enum RoleEnum {
    ADMIN(1, "管理员"),
    SCHOOL(2, "学校用户"),
    TEACHER(3, "志愿教师"),
    STUDENT(4, "学生");

    private final int code;
    private final String desc;

    RoleEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
