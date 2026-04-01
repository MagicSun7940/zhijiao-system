package com.zhijiao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类（管理员/学校/教师/学生共用）
 */
@Data
@TableName("sys_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户名（登录账号） */
    private String username;

    /** 密码（MD5加密） */
    private String password;

    /** 真实姓名 */
    private String realName;

    /** 角色：1-管理员 2-学校用户 3-志愿教师 4-学生 */
    private Integer role;

    /** 手机号 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 头像URL */
    private String avatar;

    /** 所属学校ID（学校用户/学生使用） */
    private Long schoolId;

    /** 状态：0-禁用 1-启用 */
    private Integer status;

    /** 逻辑删除 */
    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
