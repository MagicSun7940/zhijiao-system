package com.zhijiao.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhijiao.common.result.Result;
import com.zhijiao.dto.PostDTO;
import com.zhijiao.entity.Post;
import com.zhijiao.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 支教岗位接口
 */
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 公开岗位列表（已发布，教师/学生浏览用）
     */
    @GetMapping("/list")
    public Result<Page<Post>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String courseType,
            @RequestParam(required = false) String keyword) {
        return Result.success(postService.pageList(page, size, courseType, keyword));
    }

    /**
     * 学校查看自己发布的岗位（所有状态）
     */
    @GetMapping("/my")
    public Result<Page<Post>> myPosts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        Long schoolId = (Long) request.getAttribute("userId");
        return Result.success(postService.pageBySchool(page, size, schoolId));
    }

    /**
     * 管理员查看所有岗位（可按状态筛选）
     */
    @GetMapping("/admin/list")
    public Result<Page<Post>> adminList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status,
            HttpServletRequest request) {
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role != 1) {
            return Result.error(403, "无权限访问");
        }
        return Result.success(postService.pageForAdmin(page, size, status));
    }

    /**
     * 岗位详情
     */
    @GetMapping("/{id}")
    public Result<Post> detail(@PathVariable Long id) {
        return Result.success(postService.getById(id));
    }

    /**
     * 学校发布岗位（仅学校角色）
     */
    @PostMapping("/publish")
    public Result<String> publish(@Valid @RequestBody PostDTO postDTO, HttpServletRequest request) {
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role != 2) {
            return Result.error(403, "仅学校用户可发布岗位");
        }
        Long schoolId = (Long) request.getAttribute("userId");
        postService.publish(postDTO, schoolId);
        return Result.success("发布成功");
    }

    /**
     * 学校修改岗位（仅本人）
     */
    @PutMapping("/{id}")
    public Result<String> update(@PathVariable Long id,
                                 @Valid @RequestBody PostDTO postDTO,
                                 HttpServletRequest request) {
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role != 2) {
            return Result.error(403, "仅学校用户可修改岗位");
        }
        Long schoolId = (Long) request.getAttribute("userId");
        postService.updatePost(id, postDTO, schoolId);
        return Result.success("修改成功");
    }

    /**
     * 删除岗位（学校本人或管理员）
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id, HttpServletRequest request) {
        Integer role = (Integer) request.getAttribute("role");
        Long userId = (Long) request.getAttribute("userId");
        if (role == null || (role != 1 && role != 2)) {
            return Result.error(403, "无权限删除岗位");
        }
        postService.deletePost(id, userId, role);
        return Result.success("删除成功");
    }

    /**
     * 管理员审核岗位
     */
    @PutMapping("/audit/{id}")
    public Result<String> audit(@PathVariable Long id,
                                @RequestParam Integer status,
                                @RequestParam(required = false) String reason,
                                HttpServletRequest request) {
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role != 1) {
            return Result.error(403, "仅管理员可审核岗位");
        }
        postService.audit(id, status, reason);
        return Result.success("审核完成");
    }
}

