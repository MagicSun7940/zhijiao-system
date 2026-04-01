package com.zhijiao.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhijiao.dto.LoginDTO;
import com.zhijiao.dto.RegisterDTO;
import com.zhijiao.entity.User;
import com.zhijiao.mapper.UserMapper;
import com.zhijiao.service.UserService;
import com.zhijiao.utils.JwtUtils;
import com.zhijiao.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 用户 Service 实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final JwtUtils jwtUtils;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 查找用户
        User user = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, loginDTO.getUsername())
                .eq(User::getDeleted, 0));

        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        if (!DigestUtil.md5Hex(loginDTO.getPassword()).equals(user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用，请联系管理员");
        }
        if (loginDTO.getRole() != null && !loginDTO.getRole().equals(user.getRole())) {
            throw new RuntimeException("角色不匹配，请选择正确的登录入口");
        }

        // 生成 Token
        String token = jwtUtils.generateToken(user.getId(), user.getRole());

        // 构建响应
        LoginVO vo = new LoginVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setRole(user.getRole());
        vo.setAvatar(user.getAvatar());
        vo.setToken(token);
        return vo;
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        // 禁止通过注册接口创建管理员账号
        if (registerDTO.getRole() == null || registerDTO.getRole() == 1) {
            throw new RuntimeException("非法注册角色");
        }
        // 只允许注册教师(3)和学生(4)
        if (registerDTO.getRole() != 3 && registerDTO.getRole() != 4) {
            throw new RuntimeException("角色参数不合法");
        }

        // 检查用户名是否已存在（MyBatis Plus 逻辑删除会自动过滤 deleted=1）
        long count = this.count(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, registerDTO.getUsername()));
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(DigestUtil.md5Hex(registerDTO.getPassword()));
        user.setRealName(registerDTO.getRealName());
        user.setRole(registerDTO.getRole());
        user.setPhone(registerDTO.getPhone());
        user.setEmail(registerDTO.getEmail());
        user.setSchoolId(registerDTO.getSchoolId());
        user.setStatus(1);
        user.setDeleted(0);
        this.save(user);
    }

    @Override
    public void logout(String token) {
        // 将 Token 加入 Redis 黑名单，设置过期时间与 Token 一致（24小时）
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        redisTemplate.opsForValue().set(
                TOKEN_BLACKLIST_PREFIX + token,
                "1",
                24,
                TimeUnit.HOURS
        );
    }
}
