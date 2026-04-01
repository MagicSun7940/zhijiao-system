package com.zhijiao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhijiao.entity.Application;
import org.springframework.web.multipart.MultipartFile;

/**
 * 岗位申请 Service 接口
 */
public interface ApplicationService extends IService<Application> {

    void apply(Long postId, String applyReason, MultipartFile resume, Long teacherId);

    Page<Application> myList(Integer page, Integer size, Long teacherId);

    Page<Application> listByPost(Integer page, Integer size, Long postId);

    void review(Long id, Integer status, String feedback, Long schoolId);
}
