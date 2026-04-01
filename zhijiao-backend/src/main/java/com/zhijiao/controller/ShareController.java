package com.zhijiao.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhijiao.common.result.Result;
import com.zhijiao.dto.ShareDTO;
import com.zhijiao.entity.Share;
import com.zhijiao.service.ShareService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 教学分享接口
 */
@RestController
@RequestMapping("/api/share")
@RequiredArgsConstructor
public class ShareController {

    private final ShareService shareService;

    /**
     * 管理员查看所有分享（可按状态筛选）
     */
    @GetMapping("/admin/list")
    public Result<Page<Share>> adminList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status,
            HttpServletRequest request) {
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role != 1) {
            return Result.error(403, "仅管理员可访问");
        }
        return Result.success(shareService.pageForAdmin(page, size, status));
    }

    /**
     * 公开分页（已发布，所有人可见）
     */
    @GetMapping("/list")
    public Result<Page<Share>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        return Result.success(shareService.pagePublished(page, size, keyword));
    }

    /**
     * 教师查看自己的分享（所有状态）
     */
    @GetMapping("/my")
    public Result<Page<Share>> myShares(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role != 3) {
            return Result.error(403, "仅志愿教师可查看");
        }
        Long teacherId = (Long) request.getAttribute("userId");
        return Result.success(shareService.pageByTeacher(page, size, teacherId));
    }

    /**
     * 分享详情（浏览量+1）
     */
    @GetMapping("/{id}")
    public Result<Share> detail(@PathVariable Long id) {
        return Result.success(shareService.detail(id));
    }

    /**
     * 教师发布分享（仅教师角色）
     */
    @PostMapping("/publish")
    public Result<String> publish(@Valid @RequestBody ShareDTO shareDTO, HttpServletRequest request) {
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role != 3) {
            return Result.error(403, "仅志愿教师可发布分享");
        }
        Long teacherId = (Long) request.getAttribute("userId");
        shareService.publish(shareDTO, teacherId);
        return Result.success("发布成功，等待审核");
    }

    /**
     * 管理员审核分享内容
     */
    @PutMapping("/audit/{id}")
    public Result<String> audit(@PathVariable Long id,
                                @RequestParam Integer status,
                                HttpServletRequest request) {
        Integer role = (Integer) request.getAttribute("role");
        if (role == null || role != 1) {
            return Result.error(403, "仅管理员可审核");
        }
        shareService.audit(id, status);
        return Result.success("审核完成");
    }

    /**
     * 删除分享（本人或管理员）
     */
    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Long id, HttpServletRequest request) {
        Integer role = (Integer) request.getAttribute("role");
        Long userId = (Long) request.getAttribute("userId");
        if (role == null || (role != 1 && role != 3)) {
            return Result.error(403, "无权限删除");
        }
        shareService.delete(id, userId);
        return Result.success("删除成功");
    }
}

