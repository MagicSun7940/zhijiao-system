package com.zhijiao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhijiao.entity.Application;
import com.zhijiao.entity.Post;
import com.zhijiao.mapper.ApplicationMapper;
import com.zhijiao.service.ApplicationService;
import com.zhijiao.service.PostService;
import com.zhijiao.utils.OssUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 岗位申请 Service 实现
 */
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application>
        implements ApplicationService {

    private final PostService postService;
    private final OssUtils ossUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void apply(Long postId, String applyReason, MultipartFile resume, Long teacherId) {
        Post post = postService.getById(postId);
        if (post == null || post.getStatus() != 1) {
            throw new RuntimeException("岗位不存在或已关闭");
        }
        if (post.getAppliedCount() >= post.getHeadcount()) {
            throw new RuntimeException("该岗位招募名额已满");
        }
        long count = this.count(new LambdaQueryWrapper<Application>()
                .eq(Application::getPostId, postId)
                .eq(Application::getTeacherId, teacherId)
                .eq(Application::getDeleted, 0));
        if (count > 0) {
            throw new RuntimeException("您已申请过该岗位，请勿重复申请");
        }
        String resumeUrl = null;
        if (resume != null && !resume.isEmpty()) {
            try {
                resumeUrl = ossUtils.upload(resume, "resume");
            } catch (IOException e) {
                throw new RuntimeException("简历上传失败，请重试");
            }
        }
        Application application = new Application();
        application.setPostId(postId);
        application.setTeacherId(teacherId);
        application.setApplyReason(applyReason);
        application.setResumeUrl(resumeUrl);
        application.setStatus(0);
        application.setDeleted(0);
        this.save(application);
        post.setAppliedCount(post.getAppliedCount() + 1);
        postService.updateById(post);
    }

    @Override
    public Page<Application> myList(Integer page, Integer size, Long teacherId) {
        Page<Application> pageParam = new Page<>(page, size);
        return this.page(pageParam, new LambdaQueryWrapper<Application>()
                .eq(Application::getTeacherId, teacherId)
                .eq(Application::getDeleted, 0)
                .orderByDesc(Application::getCreateTime));
    }

    @Override
    public Page<Application> listByPost(Integer page, Integer size, Long postId) {
        Page<Application> pageParam = new Page<>(page, size);
        return this.page(pageParam, new LambdaQueryWrapper<Application>()
                .eq(Application::getPostId, postId)
                .eq(Application::getDeleted, 0)
                .orderByDesc(Application::getCreateTime));
    }

    @Override
    public void review(Long id, Integer status, String feedback, Long schoolId) {
        Application application = this.getById(id);
        if (application == null) {
            throw new RuntimeException("申请记录不存在");
        }
        Post post = postService.getById(application.getPostId());
        if (post == null || !post.getSchoolId().equals(schoolId)) {
            throw new RuntimeException("无权审核该申请");
        }
        application.setStatus(status);
        application.setFeedback(feedback);
        this.updateById(application);
    }
}
