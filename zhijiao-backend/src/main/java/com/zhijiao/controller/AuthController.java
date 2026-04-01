package com.zhijiao.controller;

import com.zhijiao.common.result.Result;
import com.zhijiao.dto.LoginDTO;
import com.zhijiao.dto.RegisterDTO;
import com.zhijiao.service.UserService;
import com.zhijiao.vo.LoginVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证接口：登录、注册
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = userService.login(loginDTO);
        return Result.success("登录成功", loginVO);
    }

    /**
     * 用户注册（教师/学生自助注册）
     */
    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO);
        return Result.success();
    }

    /**
     * 退出登录（前端清除Token即可，后端可加入黑名单）
     */
    @PostMapping("/logout")
    public Result<String> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        return Result.success("退出成功");
    }
}
