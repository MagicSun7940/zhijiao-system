package com.zhijiao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhijiao.dto.ShareDTO;
import com.zhijiao.entity.Share;

/**
 * 教学分享 Service 接口
 */
public interface ShareService extends IService<Share> {

    /** 管理员查看所有分享（可按状态筛选） */
    Page<Share> pageForAdmin(Integer page, Integer size, Integer status);

    /** 公开已发布的分享（学生/所有人浏览） */
    Page<Share> pagePublished(Integer page, Integer size, String keyword);

    /** 教师查看自己发布的分享（所有状态） */
    Page<Share> pageByTeacher(Integer page, Integer size, Long teacherId);

    Share detail(Long id);

    void publish(ShareDTO shareDTO, Long teacherId);

    void audit(Long id, Integer status);

    void delete(Long id, Long userId);
}
