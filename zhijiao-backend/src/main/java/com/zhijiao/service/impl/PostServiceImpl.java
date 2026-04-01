package com.zhijiao.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhijiao.dto.PostDTO;
import com.zhijiao.entity.Post;
import com.zhijiao.entity.User;
import com.zhijiao.mapper.PostMapper;
import com.zhijiao.mapper.UserMapper;
import com.zhijiao.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 岗位 Service 实现
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    // 直接注入 UserMapper，避免与 UserService 产生循环依赖
    private final UserMapper userMapper;

    @Override
    public Page<Post> pageList(Integer page, Integer size, String courseType, String keyword) {
        Page<Post> pageParam = new Page<>(page, size);
        return baseMapper.selectPageWithCondition(pageParam, courseType, keyword);
    }

    @Override
    public Page<Post> pageBySchool(Integer page, Integer size, Long schoolId) {
        Page<Post> pageParam = new Page<>(page, size);
        return baseMapper.selectPageBySchool(pageParam, schoolId);
    }

    @Override
    public Page<Post> pageForAdmin(Integer page, Integer size, Integer status) {
        Page<Post> pageParam = new Page<>(page, size);
        return baseMapper.selectPageForAdmin(pageParam, status);
    }

    @Override
    public void publish(PostDTO postDTO, Long schoolId) {
        User school = userMapper.selectById(schoolId);
        if (school == null) {
            throw new RuntimeException("学校用户不存在");
        }

        Post post = new Post();
        BeanUtils.copyProperties(postDTO, post);
        post.setSchoolId(schoolId);
        post.setSchoolName(school.getRealName());
        post.setStatus(0);  // 待审核
        post.setAppliedCount(0);
        post.setDeleted(0);
        this.save(post);
    }

    @Override
    public void updatePost(Long id, PostDTO postDTO, Long schoolId) {
        Post post = this.getById(id);
        if (post == null) {
            throw new RuntimeException("岗位不存在");
        }
        if (!post.getSchoolId().equals(schoolId)) {
            throw new RuntimeException("无权修改此岗位");
        }
        BeanUtils.copyProperties(postDTO, post);
        post.setStatus(0); // 修改后重新审核
        this.updateById(post);
    }

    @Override
    public void deletePost(Long id, Long userId, Integer role) {
        Post post = this.getById(id);
        if (post == null) {
            throw new RuntimeException("岗位不存在");
        }
        // 管理员可删任意岗位，学校只能删自己的
        if (role != 1 && !post.getSchoolId().equals(userId)) {
            throw new RuntimeException("无权删除该岗位");
        }
        this.removeById(id);
    }

    @Override
    public void audit(Long id, Integer status, String reason) {
        Post post = this.getById(id);
        if (post == null) {
            throw new RuntimeException("岗位不存在");
        }
        post.setStatus(status);
        this.updateById(post);
    }
}
