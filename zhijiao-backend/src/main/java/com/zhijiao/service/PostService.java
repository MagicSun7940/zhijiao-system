package com.zhijiao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhijiao.dto.PostDTO;
import com.zhijiao.entity.Post;

/**
 * 岗位 Service 接口
 */
public interface PostService extends IService<Post> {

    /** 公开分页（已发布，教师/学生浏览） */
    Page<Post> pageList(Integer page, Integer size, String courseType, String keyword);

    /** 学校查看自己发布的岗位（所有状态） */
    Page<Post> pageBySchool(Integer page, Integer size, Long schoolId);

    /** 管理员查看所有岗位（可按状态筛选） */
    Page<Post> pageForAdmin(Integer page, Integer size, Integer status);

    void publish(PostDTO postDTO, Long schoolId);

    void updatePost(Long id, PostDTO postDTO, Long schoolId);

    void deletePost(Long id, Long userId, Integer role);

    void audit(Long id, Integer status, String reason);
}
