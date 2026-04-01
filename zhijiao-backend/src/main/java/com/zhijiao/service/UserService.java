package com.zhijiao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhijiao.dto.LoginDTO;
import com.zhijiao.dto.RegisterDTO;
import com.zhijiao.entity.User;
import com.zhijiao.vo.LoginVO;

/**
 * 用户 Service 接口
 */
public interface UserService extends IService<User> {

    LoginVO login(LoginDTO loginDTO);

    void register(RegisterDTO registerDTO);

    void logout(String token);
}
