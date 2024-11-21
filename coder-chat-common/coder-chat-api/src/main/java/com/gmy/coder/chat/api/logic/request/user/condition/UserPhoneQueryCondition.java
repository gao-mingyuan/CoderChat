package com.gmy.coder.chat.api.logic.request.user.condition;

import lombok.*;

/**
 * @author gaomingyuan
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserPhoneQueryCondition implements UserQueryCondition {

    private static final long serialVersionUID = 1L;

    /**
     * 用户手机号
     */
    private String telephone;
}
