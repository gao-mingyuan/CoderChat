package com.gmy.coder.chat.gateway.auth;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.gmy.coder.chat.api.logic.constant.RoleEnum;
import com.gmy.coder.chat.api.logic.constant.UserStateEnum;
import com.gmy.coder.chat.api.logic.response.user.UserInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 自定义权限验证接口
 *
 * @author gaomingyuan
 */
@Component
public class StpInterfaceImpl implements StpInterface {
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        UserInfo userInfo = (UserInfo) StpUtil.getSessionByLoginId(loginId).get((String) loginId);

        //todo 根据角色给不同权限

        if (userInfo.getStatus().equals(UserStateEnum.NORMAL.getCode())) {
            return List.of(UserStateEnum.NORMAL.name());
        }

        if (userInfo.getStatus().equals(UserStateEnum.FROZEN.getCode())) {
            return List.of(UserStateEnum.FROZEN.name());
        }

        return null;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        UserInfo userInfo = (UserInfo) StpUtil.getSessionByLoginId(loginId).get((String) loginId);
        if (Objects.equals(userInfo.getUserRole(), RoleEnum.ADMIN.getCode())) {
            return List.of(RoleEnum.ADMIN.name());
        }
        return List.of(RoleEnum.CUSTOMER.name());
    }
}
