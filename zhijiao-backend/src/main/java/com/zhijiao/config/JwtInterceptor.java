package com.zhijiao.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhijiao.common.result.Result;
import com.zhijiao.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 登录拦截器
 */
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");

        // 提取 Bearer Token
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (!StringUtils.hasText(token)) {
            writeUnauthorized(response, "请先登录");
            return false;
        }

        if (jwtUtils.isTokenExpired(token)) {
            writeUnauthorized(response, "登录已过期，请重新登录");
            return false;
        }

        // 检查 Token 是否在黑名单（已退出登录）
        Boolean isBlacklisted = redisTemplate.hasKey(TOKEN_BLACKLIST_PREFIX + token);
        if (Boolean.TRUE.equals(isBlacklisted)) {
            writeUnauthorized(response, "登录已过期，请重新登录");
            return false;
        }

        // 将用户信息存入请求属性，供 Controller 使用
        try {
            Long userId = jwtUtils.getUserId(token);
            Integer role = jwtUtils.getRole(token);
            request.setAttribute("userId", userId);
            request.setAttribute("role", role);
        } catch (Exception e) {
            writeUnauthorized(response, "Token 无效");
            return false;
        }

        return true;
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(Result.error(401, message)));
    }
}
