package com.gmy.coder.chat.api.logic.response.user;

import com.gmy.coder.chat.api.logic.constant.UserStateEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

/**
 * @author gaomingyuan
 */
@Getter
@Setter
@NoArgsConstructor
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    private Long id;

    /**
     * 昵称
     */
    private String name;

    /**
     * 状态
     *
     * @see UserStateEnum
     */
    private Integer status;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 性别 1为男性，2为女性
     */
    private Integer sex;

    /**
     * 用户角色
     */
    private Integer userRole;

}
