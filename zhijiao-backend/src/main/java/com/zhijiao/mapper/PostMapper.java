package com.zhijiao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhijiao.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 岗位 Mapper
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {

    /** 公开查询：已发布岗位（教师/学生浏览） */
    Page<Post> selectPageWithCondition(
            Page<Post> page,
            @Param("courseType") String courseType,
            @Param("keyword") String keyword
    );

    /** 学校查询：本学校所有状态的岗位 */
    Page<Post> selectPageBySchool(
            Page<Post> page,
            @Param("schoolId") Long schoolId
    );

    /** 管理员查询：所有岗位，可按状态筛选 */
    Page<Post> selectPageForAdmin(
            Page<Post> page,
            @Param("status") Integer status
    );
}
