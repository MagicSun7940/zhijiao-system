package com.zhijiao.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhijiao.common.result.Result;
import com.zhijiao.entity.Application;
import com.zhijiao.service.ApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 岗位申请接口
 */
@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    /**
     * 教师申请岗位（仅教师角色）
     */
    @PostMapping("/apply")
    public Result<String> apply(
            @RequestParam Long postId,
            @RequestParam String applyReason,
            @RequestParam(required = false) MultipartFile resume,
            HttpServletRequest request) {
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role != 3) {
            return Result.error(403, "仅志愿教师可申请岗位");
        }
        Long teacherId = (Long) request.getAttribute("userId");
        applicationService.apply(postId, applyReason, resume, teacherId);
        return Result.success("申请成功");
    }

    /**
     * 教师查看自己的申请记录（仅教师角色）
     */
    @GetMapping("/my")
    public Result<Page<Application>> myApplications(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role != 3) {
            return Result.error(403, "仅志愿教师可查看申请记录");
        }
        Long teacherId = (Long) request.getAttribute("userId");
        return Result.success(applicationService.myList(page, size, teacherId));
    }

    /**
     * 学校查看某岗位的申请列表（仅学校角色）
     */
    @GetMapping("/post/{postId}")
    public Result<Page<Application>> postApplications(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role != 2) {
            return Result.error(403, "仅学校用户可查看申请列表");
        }
        return Result.success(applicationService.listByPost(page, size, postId));
    }

    /**
     * 学校审核申请（仅学校角色）
     */
    @PutMapping("/review/{id}")
    public Result<String> review(
            @PathVariable Long id,
            @RequestParam Integer status,
            @RequestParam(required = false) String feedback,
            HttpServletRequest request) {
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role != 2) {
            return Result.error(403, "仅学校用户可审核申请");
        }
        Long schoolId = (Long) request.getAttribute("userId");
        applicationService.review(id, status, feedback, schoolId);
        return Result.success("审核完成");
    }
}

