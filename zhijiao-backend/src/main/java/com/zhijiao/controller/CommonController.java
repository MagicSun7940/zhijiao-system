package com.zhijiao.controller;

import com.zhijiao.common.result.Result;
import com.zhijiao.utils.OssUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 通用接口：文件上传等
 */
@RestController
@RequestMapping("/api/common")
@RequiredArgsConstructor
public class CommonController {

    private final OssUtils ossUtils;

    /**
     * 通用文件上传（需登录）
     * 返回 OSS 访问 URL
     */
    @PostMapping("/upload")
    public Result<String> upload(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        // 必须登录才能上传
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error(401, "请先登录");
        }
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }
        if (file.getSize() > 50 * 1024 * 1024) {
            throw new RuntimeException("文件大小不能超过 50MB");
        }
        try {
            String url = ossUtils.upload(file, "share");
            return Result.success(url);
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败：" + e.getMessage());
        }
    }
}

