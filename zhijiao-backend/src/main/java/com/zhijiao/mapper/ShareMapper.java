package com.zhijiao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhijiao.entity.Share;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 教学分享 Mapper
 */
@Mapper
public interface ShareMapper extends BaseMapper<Share> {

    /**
     * 浏览量 +1
     */
    @Update("UPDATE share_content SET view_count = view_count + 1 WHERE id = #{id}")
    void incrementViewCount(Long id);
}
