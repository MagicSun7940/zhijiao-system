package com.zhijiao.vo;

import lombok.Data;

/**
 * 登录响应 VO
 */
@Data
public class LoginVO {

    private Long userId;
    private String username;
    private String realName;
    private Integer role;
    private String avatar;
    private String token;
}
