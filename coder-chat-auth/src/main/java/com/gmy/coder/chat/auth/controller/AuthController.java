package com.gmy.coder.chat.auth.controller;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.gmy.coder.chat.api.logic.request.user.UserQueryRequest;
import com.gmy.coder.chat.api.logic.request.user.UserRegisterRequest;
import com.gmy.coder.chat.api.logic.response.user.UserInfo;
import com.gmy.coder.chat.api.logic.response.user.UserOperatorResponse;
import com.gmy.coder.chat.api.logic.response.user.UserQueryResponse;
import com.gmy.coder.chat.auth.exception.AuthException;
import com.gmy.coder.chat.auth.param.LoginParam;
import com.gmy.coder.chat.auth.param.RegisterParam;
import com.gmy.coder.chat.auth.vo.LoginVO;
import com.gmy.coder.chat.web.vo.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import static com.gmy.coder.chat.auth.exception.AuthErrorCode.VERIFICATION_CODE_WRONG;


/**
 * 认证相关接口
 *
 * @author gaomingyuan
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
public class AuthController {

    private static final String ROOT_CAPTCHA = "8888";

    /**
     * 默认登录超时时间：7天
     */
    private static final Integer DEFAULT_LOGIN_SESSION_TIMEOUT = 60 * 60 * 24 * 7;

    @PostMapping("/register")
    public Result<Boolean> register(@Valid @RequestBody RegisterParam registerParam) {

        //todo 验证码校验
        String cachedCode = ROOT_CAPTCHA;
        if (!StringUtils.equalsIgnoreCase(cachedCode, registerParam.getCaptcha())) {
            throw new AuthException(VERIFICATION_CODE_WRONG);
        }

        //注册
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setTelephone(registerParam.getTelephone());
        userRegisterRequest.setInviteCode(registerParam.getInviteCode());

        //todo 调用dubbo接口注册
        UserOperatorResponse registerResult = null;
        if (registerResult.getSuccess()) {
            return Result.success(true);
        }
        return Result.error(registerResult.getResponseCode(), registerResult.getResponseMessage());
    }

    /**
     * 登录方法
     *
     * @param loginParam 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginParam loginParam) {
        //todo 验证码校验
        String cachedCode = ROOT_CAPTCHA;
        if (!StringUtils.equalsIgnoreCase(cachedCode, loginParam.getCaptcha())) {
            throw new AuthException(VERIFICATION_CODE_WRONG);
        }

        //判断是注册还是登陆
        //查询用户信息
        UserQueryRequest userQueryRequest = new UserQueryRequest(loginParam.getTelephone());
        UserQueryResponse<UserInfo> userQueryResponse = userFacadeService.query(userQueryRequest);
        UserInfo userInfo = userQueryResponse.getData();
        if (userInfo == null) {
            //需要注册
            UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
            userRegisterRequest.setTelephone(loginParam.getTelephone());
            userRegisterRequest.setInviteCode(loginParam.getInviteCode());

            UserOperatorResponse response = userFacadeService.register(userRegisterRequest);
            if (response.getSuccess()) {
                userQueryResponse = userFacadeService.query(userQueryRequest);
                userInfo = userQueryResponse.getData();
                StpUtil.login(userInfo.getId(), new SaLoginModel().setIsLastingCookie(loginParam.getRememberMe())
                        .setTimeout(DEFAULT_LOGIN_SESSION_TIMEOUT));
                StpUtil.getSession().set(userInfo.getId().toString(), userInfo);
                LoginVO loginVO = new LoginVO(userInfo);
                return Result.success(loginVO);
            }

            return Result.error(response.getResponseCode(), response.getResponseMessage());
        } else {
            //登录
            StpUtil.login(userInfo.getId(), new SaLoginModel().setIsLastingCookie(loginParam.getRememberMe())
                    .setTimeout(DEFAULT_LOGIN_SESSION_TIMEOUT));
            StpUtil.getSession().set(userInfo.getId().toString(), userInfo);
            LoginVO loginVO = new LoginVO(userInfo);
            return Result.success(loginVO);
        }
    }

    @PostMapping("/logout")
    public Result<Boolean> logout() {
        StpUtil.logout();
        return Result.success(true);
    }

    @RequestMapping("test")
    public String test() {
        return "test";
    }

}
