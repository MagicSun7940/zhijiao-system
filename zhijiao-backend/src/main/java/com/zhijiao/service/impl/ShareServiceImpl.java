package com.zhijiao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhijiao.dto.ShareDTO;
import com.zhijiao.entity.Share;
import com.zhijiao.mapper.ShareMapper;
import com.zhijiao.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 教学分享 Service 实现
 */
@Service
@RequiredArgsConstructor
public class ShareServiceImpl extends ServiceImpl<ShareMapper, Share> implements ShareService {

    @Override
    public Page<Share> pageForAdmin(Integer page, Integer size, Integer status) {
        Page<Share> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Share> wrapper = new LambdaQueryWrapper<Share>()
                .eq(Share::getDeleted, 0)
                .orderByDesc(Share::getCreateTime);
        if (status != null) {
            wrapper.eq(Share::getStatus, status);
        }
        return this.page(pageParam, wrapper);
    }

    @Override
    public Page<Share> pagePublished(Integer page, Integer size, String keyword) {
        Page<Share> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Share> wrapper = new LambdaQueryWrapper<Share>()
                .eq(Share::getStatus, 1)
                .eq(Share::getDeleted, 0)
                .orderByDesc(Share::getCreateTime);
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Share::getTitle, keyword);
        }
        return this.page(pageParam, wrapper);
    }

    @Override
    public Page<Share> pageByTeacher(Integer page, Integer size, Long teacherId) {
        Page<Share> pageParam = new Page<>(page, size);
        return this.page(pageParam, new LambdaQueryWrapper<Share>()
                .eq(Share::getTeacherId, teacherId)
                .eq(Share::getDeleted, 0)
                .orderByDesc(Share::getCreateTime));
    }

    @Override
    public Share detail(Long id) {
        Share share = this.getById(id);
        if (share == null) {
            throw new RuntimeException("分享内容不存在");
        }
        // 浏览量 +1
        baseMapper.incrementViewCount(id);
        share.setViewCount(share.getViewCount() + 1);
        return share;
    }

    @Override
    public void publish(ShareDTO shareDTO, Long teacherId) {
        Share share = new Share();
        BeanUtils.copyProperties(shareDTO, share);
        share.setTeacherId(teacherId);
        share.setStatus(0); // 待审核
        share.setViewCount(0);
        share.setDeleted(0);
        this.save(share);
    }

    @Override
    public void audit(Long id, Integer status) {
        Share share = this.getById(id);
        if (share == null) {
            throw new RuntimeException("分享内容不存在");
        }
        share.setStatus(status);
        this.updateById(share);
    }

    @Override
    public void delete(Long id, Long userId) {
        Share share = this.getById(id);
        if (share == null) {
            throw new RuntimeException("分享内容不存在");
        }
        this.removeById(id);
    }
}
