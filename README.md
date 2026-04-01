# 支教管理系统

基于 **Spring Boot 3 + Vue 3** 的支教管理平台，实现管理员、学校用户、志愿教师、学生四类角色的完整业务闭环。

## 技术栈

| 层次 | 技术 |
|------|------|
| 后端框架 | Spring Boot 3.2 + MyBatis Plus 3.5 |
| 数据库 | MySQL 8.0 + Redis |
| 认证方式 | JWT Token |
| 文件存储 | 阿里云 OSS |
| 前端框架 | Vue 3 + Vite 5 |
| UI 组件库 | Element Plus 2.x |
| 状态管理 | Pinia |
| 路由 | Vue Router 4 |

## 项目结构

```
zhijiao-system/
├── zhijiao-backend/          # 后端（Spring Boot）
│   ├── src/main/java/com/zhijiao/
│   │   ├── config/           # 配置类（JWT、Redis、跨域、异常处理等）
│   │   ├── controller/       # 控制层
│   │   ├── service/          # 业务逻辑层
│   │   │   └── impl/
│   │   ├── mapper/           # 数据访问层
│   │   ├── entity/           # 实体类
│   │   ├── dto/              # 请求数据传输对象
│   │   ├── vo/               # 响应视图对象
│   │   └── utils/            # 工具类（JWT、OSS）
│   └── src/main/resources/
│       ├── application.yml   # 主配置文件
│       └── mapper/           # MyBatis XML 映射文件
│
├── zhijiao-frontend/         # 前端（Vue 3）
│   ├── src/
│   │   ├── api/              # 接口封装
│   │   ├── components/       # 公共组件
│   │   ├── router/           # 路由配置
│   │   ├── store/            # Pinia 状态管理
│   │   ├── utils/            # 工具（axios封装）
│   │   └── views/            # 页面视图
│   │       ├── admin/        # 管理员端
│   │       ├── school/       # 学校端
│   │       ├── teacher/      # 教师端
│   │       ├── student/      # 学生端
│   │       └── common/       # 公共页（登录/注册）
│   └── vite.config.js
│
└── zhijiao_db.sql            # 数据库初始化脚本
```

## 角色与功能

| 角色 | 主要功能 |
|------|---------|
| 管理员 | 用户管理、岗位审核、分享内容审核、数据概览 |
| 学校用户 | 发布/编辑/删除岗位、查看并审核教师申请 |
| 志愿教师 | 浏览岗位、提交申请（含简历上传）、发布教学分享 |
| 学生 | 查看课程安排、浏览教学资源与分享内容 |

## 快速启动

### 环境要求

- JDK 17+
- Maven 3.8+
- Node.js 18+
- MySQL 8.0+
- Redis 6+

---

### 第一步：初始化数据库

```sql
-- 在 MySQL 中执行
source zhijiao_db.sql;
```

默认账号（密码均为 `123456`）：

| 账号 | 角色 |
|------|------|
| admin | 管理员 |
| school1 | 学校用户 |
| teacher1 | 志愿教师 |
| student1 | 学生 |

---

### 第二步：启动后端

1. 打开 `zhijiao-backend/src/main/resources/application.yml`
2. 修改以下配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/zhijiao_db?...
    username: root
    password: 你的MySQL密码

aliyun:
  oss:
    endpoint: oss-cn-hangzhou.aliyuncs.com  # 你的地域
    access-key-id: 你的AccessKeyId
    access-key-secret: 你的AccessKeySecret
    bucket-name: 你的Bucket名称
    url-prefix: https://你的bucket.oss-cn-hangzhou.aliyuncs.com/
```

3. 用 IntelliJ IDEA 打开 `zhijiao-backend` 目录，运行 `ZhijiaoApplication.java`

```bash
# 或使用 Maven 命令行
cd zhijiao-backend
mvn spring-boot:run
```

后端启动后访问：http://localhost:8080

---

### 第三步：启动前端

```bash
cd zhijiao-frontend

# 安装依赖
npm install

# 开发模式启动
npm run dev
```

前端访问地址：http://localhost:5173

---

### 第四步：构建生产包（可选）

```bash
# 前端打包
cd zhijiao-frontend
npm run build
# 生成 dist/ 目录，可部署到 Nginx

# 后端打包
cd zhijiao-backend
mvn clean package -DskipTests
# 生成 target/zhijiao-backend-1.0.0.jar
java -jar target/zhijiao-backend-1.0.0.jar
```

## 主要接口说明

| 接口路径 | 方法 | 说明 |
|---------|------|------|
| /api/auth/login | POST | 用户登录 |
| /api/auth/register | POST | 用户注册 |
| /api/post/list | GET | 岗位列表（分页） |
| /api/post/publish | POST | 发布岗位（学校） |
| /api/post/audit/{id} | PUT | 审核岗位（管理员） |
| /api/application/apply | POST | 申请岗位（教师） |
| /api/application/my | GET | 我的申请记录 |
| /api/application/review/{id} | PUT | 审核申请（学校） |
| /api/share/list | GET | 分享列表 |
| /api/share/publish | POST | 发布分享（教师） |
| /api/share/audit/{id} | PUT | 审核分享（管理员） |

## 注意事项

1. **阿里云 OSS**：需要在阿里云控制台创建 Bucket，并配置跨域规则，允许前端域名访问。
2. **Redis**：本地开发可使用无密码的 Redis，生产环境请设置密码并修改 `application.yml`。
3. **JWT 密钥**：生产环境请替换 `application.yml` 中的 `jwt.secret` 为更复杂的随机字符串。
4. **文件上传大小**：默认限制单文件 50MB，可在 `application.yml` 的 `multipart` 配置中修改。

## 开发工具推荐

| 工具 | 用途 |
|------|------|
| IntelliJ IDEA | 后端开发 |
| VS Code | 前端开发（推荐安装 Volar、ESLint 插件） |
| Apifox / Postman | 接口测试 |
| Navicat / DBeaver | 数据库管理 |
| Another Redis Desktop Manager | Redis 可视化 |
