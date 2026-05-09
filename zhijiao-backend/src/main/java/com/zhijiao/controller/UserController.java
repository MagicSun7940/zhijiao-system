package com.zhijiao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhijiao.common.result.Result;
import com.zhijiao.entity.User;
import com.zhijiao.service.ApplicationService;
import com.zhijiao.service.PostService;
import com.zhijiao.service.ShareService;
import com.zhijiao.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理接口（仅管理员）
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final ApplicationService applicationService;
    private final ShareService shareService;

    /**
     * 管理员数据概览统计
     */
    @GetMapping("/stats")
    public Result<Map<String, Long>> stats(HttpServletRequest request) {
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role != 1) return Result.error(403, "仅管理员可访问");
        Map<String, Long> map = new HashMap<>();
        map.put("userCount", userService.count(new LambdaQueryWrapper<User>().ne(User::getRole, 1)));
        map.put("postCount", postService.count());
        map.put("applicationCount", applicationService.count());
        map.put("shareCount", shareService.count());
        return Result.success(map);
    }

    /**
     * 分页查询用户列表（仅管理员）
     */
    @GetMapping("/list")
    public Result<Page<User>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer role,
            HttpServletRequest request) {
        Integer currentRole = (Integer) request.getAttribute("role");
        if (currentRole == null || currentRole != 1) {
            return Result.error(403, "仅管理员可访问");
        }
        Page<User> pageParam = new Page<>(page, size);
        // MyBatisPlus 全局逻辑删除已自动过滤 deleted=1，无需手动加条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .ne(User::getRole, 1)  // 不展示管理员账号
                .orderByDesc(User::getCreateTime);
        if (role != null) {
            wrapper.eq(User::getRole, role);
        }
        Page<User> result = userService.page(pageParam, wrapper);
        // 不返回密码字段
        result.getRecords().forEach(u -> u.setPassword(null));
        return Result.success(result);
    }

    /**
     * 启用/禁用用户（仅管理员）
     */
    @PutMapping("/status/{id}")
    public Result<String> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status,
            HttpServletRequest request) {
        Integer currentRole = (Integer) request.getAttribute("role");
        if (currentRole == null || currentRole != 1) {
            return Result.error(403, "仅管理员可操作");
        }
        User user = userService.getById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        // 禁止操作其他管理员
        if (user.getRole() == 1) {
            throw new RuntimeException("不能修改管理员账号状态");
        }
        user.setStatus(status);
        userService.updateById(user);
        return Result.success(status == 1 ? "已启用" : "已禁用");
    }
}

