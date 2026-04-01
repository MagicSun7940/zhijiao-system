-- ============================================================
-- 支教管理系统 数据库初始化脚本
-- 数据库版本：MySQL 8.0+
-- 字符集：utf8mb4
-- ============================================================

CREATE DATABASE IF NOT EXISTS zhijiao_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE zhijiao_db;

-- ============================================================
-- 1. 用户表
-- ============================================================
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username`    VARCHAR(50)  NOT NULL                COMMENT '用户名（登录账号）',
  `password`    VARCHAR(100) NOT NULL                COMMENT '密码（MD5加密）',
  `real_name`   VARCHAR(50)  NOT NULL                COMMENT '真实姓名',
  `role`        TINYINT      NOT NULL DEFAULT 3      COMMENT '角色：1-管理员 2-学校 3-教师 4-学生',
  `phone`       VARCHAR(20)                          COMMENT '手机号',
  `email`       VARCHAR(100)                         COMMENT '邮箱',
  `avatar`      VARCHAR(500)                         COMMENT '头像URL',
  `school_id`   BIGINT                               COMMENT '所属学校ID（学校用户/学生使用）',
  `status`      TINYINT      NOT NULL DEFAULT 1      COMMENT '状态：0-禁用 1-启用',
  `deleted`     TINYINT      NOT NULL DEFAULT 0      COMMENT '逻辑删除：0-正常 1-已删除',
  `create_time` DATETIME     NOT NULL                COMMENT '创建时间',
  `update_time` DATETIME     NOT NULL                COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ============================================================
-- 2. 岗位类别表
-- ============================================================
DROP TABLE IF EXISTS `post_category`;
CREATE TABLE `post_category` (
  `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name`        VARCHAR(50) NOT NULL               COMMENT '类别名称',
  `sort`        INT         NOT NULL DEFAULT 0     COMMENT '排序',
  `deleted`     TINYINT     NOT NULL DEFAULT 0     COMMENT '逻辑删除',
  `create_time` DATETIME    NOT NULL               COMMENT '创建时间',
  `update_time` DATETIME    NOT NULL               COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位类别表';

-- ============================================================
-- 3. 支教岗位表
-- ============================================================
DROP TABLE IF EXISTS `post_info`;
CREATE TABLE `post_info` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title`         VARCHAR(200) NOT NULL               COMMENT '岗位标题',
  `school_id`     BIGINT       NOT NULL               COMMENT '发布学校用户ID',
  `school_name`   VARCHAR(100) NOT NULL               COMMENT '学校名称（冗余）',
  `category_id`   BIGINT                              COMMENT '岗位类别ID',
  `course_type`   VARCHAR(50)  NOT NULL               COMMENT '课程类型',
  `headcount`     INT          NOT NULL DEFAULT 1     COMMENT '招募人数',
  `start_date`    DATE         NOT NULL               COMMENT '开始日期',
  `end_date`      DATE         NOT NULL               COMMENT '结束日期',
  `description`   TEXT                                COMMENT '岗位描述',
  `address`       VARCHAR(200)                        COMMENT '支教地点',
  `status`        TINYINT      NOT NULL DEFAULT 0     COMMENT '状态：0-待审核 1-已发布 2-已关闭 3-已拒绝',
  `applied_count` INT          NOT NULL DEFAULT 0     COMMENT '已申请人数',
  `deleted`       TINYINT      NOT NULL DEFAULT 0     COMMENT '逻辑删除',
  `create_time`   DATETIME     NOT NULL               COMMENT '创建时间',
  `update_time`   DATETIME     NOT NULL               COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_school_id` (`school_id`),
  KEY `idx_status` (`status`),
  KEY `idx_course_type` (`course_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支教岗位表';

-- ============================================================
-- 4. 岗位申请表
-- ============================================================
DROP TABLE IF EXISTS `post_application`;
CREATE TABLE `post_application` (
  `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `post_id`      BIGINT       NOT NULL               COMMENT '岗位ID',
  `teacher_id`   BIGINT       NOT NULL               COMMENT '申请教师ID',
  `resume_url`   VARCHAR(500)                        COMMENT '简历文件URL（OSS）',
  `apply_reason` TEXT                                COMMENT '申请说明',
  `status`       TINYINT      NOT NULL DEFAULT 0     COMMENT '状态：0-待审核 1-通过 2-拒绝',
  `feedback`     VARCHAR(500)                        COMMENT '学校反馈意见',
  `deleted`      TINYINT      NOT NULL DEFAULT 0     COMMENT '逻辑删除',
  `create_time`  DATETIME     NOT NULL               COMMENT '创建时间',
  `update_time`  DATETIME     NOT NULL               COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_teacher_id` (`teacher_id`),
  UNIQUE KEY `uk_post_teacher` (`post_id`, `teacher_id`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位申请表';

-- ============================================================
-- 5. 教学分享表
-- ============================================================
DROP TABLE IF EXISTS `share_content`;
CREATE TABLE `share_content` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `teacher_id`  BIGINT       NOT NULL               COMMENT '发布教师ID',
  `title`       VARCHAR(200) NOT NULL               COMMENT '标题',
  `content`     TEXT         NOT NULL               COMMENT '正文内容',
  `attachments` TEXT                                COMMENT '附件URL列表（JSON数组）',
  `status`      TINYINT      NOT NULL DEFAULT 0     COMMENT '状态：0-待审核 1-已发布 2-已拒绝',
  `view_count`  INT          NOT NULL DEFAULT 0     COMMENT '浏览量',
  `deleted`     TINYINT      NOT NULL DEFAULT 0     COMMENT '逻辑删除',
  `create_time` DATETIME     NOT NULL               COMMENT '创建时间',
  `update_time` DATETIME     NOT NULL               COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_teacher_id` (`teacher_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教学分享表';

-- ============================================================
-- 初始化数据
-- ============================================================

-- 岗位类别
INSERT INTO `post_category` (`name`, `sort`, `deleted`, `create_time`, `update_time`) VALUES
('文化课辅导', 1, 0, NOW(), NOW()),
('艺术教育',   2, 0, NOW(), NOW()),
('体育教育',   3, 0, NOW(), NOW()),
('信息技术',   4, 0, NOW(), NOW());

-- 默认管理员账号：admin / 123456（MD5：e10adc3949ba59abbe56e057f20f883e）
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `role`, `phone`, `status`, `deleted`, `create_time`, `update_time`) VALUES
('admin',   'e10adc3949ba59abbe56e057f20f883e', '系统管理员', 1, '13800000001', 1, 0, NOW(), NOW()),
('school1', 'e10adc3949ba59abbe56e057f20f883e', '希望小学',   2, '13800000002', 1, 0, NOW(), NOW()),
('teacher1','e10adc3949ba59abbe56e057f20f883e', '张老师',     3, '13800000003', 1, 0, NOW(), NOW()),
('student1','e10adc3949ba59abbe56e057f20f883e', '小明',       4, '13800000004', 1, 0, NOW(), NOW());

-- 示例岗位（已发布）
INSERT INTO `post_info` (`title`, `school_id`, `school_name`, `category_id`, `course_type`, `headcount`, `start_date`, `end_date`, `description`, `address`, `status`, `applied_count`, `deleted`, `create_time`, `update_time`) VALUES
('2024年暑期数学支教志愿者招募', 2, '希望小学', 1, '数学', 3, '2024-07-01', '2024-08-31',
 '面向农村小学开展暑期数学辅导，帮助学生打好数学基础，要求志愿者具备基本数学教学能力。', '云南省昆明市某区', 1, 1, 0, NOW(), NOW()),
('英语口语支教项目', 2, '希望小学', 1, '英语', 2, '2024-09-01', '2024-12-31',
 '招募英语口语能力较强的志愿者，为山区学生提供英语口语训练，提升学生英语表达能力。', '贵州省某山区小学', 1, 0, 0, NOW(), NOW());
