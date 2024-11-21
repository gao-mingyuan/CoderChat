package com.gmy.coder.chat.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.gmy.coder.chat.auth.exception.AuthErrorCode;
import com.gmy.coder.chat.auth.exception.AuthException;
import com.gmy.coder.chat.cache.utils.RedisUtils;
import com.gmy.coder.chat.web.vo.Result;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.gmy.coder.chat.cache.constant.CacheConstant.CACHE_KEY_SEPARATOR;

/**
 * @author gaomingyuan
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("token")
public class TokenController {

    private static final String TOKEN_PREFIX = "token:";

    @GetMapping("/get")
    public Result<String> get(@NotBlank String scene) {
        if (StpUtil.isLogin()) {
            String token = UUID.randomUUID().toString();
            String tokenKey = TOKEN_PREFIX + scene + CACHE_KEY_SEPARATOR + token;
            RedisUtils.set(tokenKey, token, 30, TimeUnit.MINUTES);
            return Result.success(tokenKey);
        }
        throw new AuthException(AuthErrorCode.USER_NOT_LOGIN);
    }
}
