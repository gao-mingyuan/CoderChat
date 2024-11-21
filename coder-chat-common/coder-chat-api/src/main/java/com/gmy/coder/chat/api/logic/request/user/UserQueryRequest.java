package com.gmy.coder.chat.api.logic.request.user;

import com.gmy.coder.chat.api.logic.request.user.condition.UserIdQueryCondition;
import com.gmy.coder.chat.api.logic.request.user.condition.UserPhoneAndPasswordQueryCondition;
import com.gmy.coder.chat.api.logic.request.user.condition.UserPhoneQueryCondition;
import com.gmy.coder.chat.api.logic.request.user.condition.UserQueryCondition;
import com.gmy.coder.chat.base.request.BaseRequest;
import lombok.*;

/**
 * @author gaomingyuan
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryRequest extends BaseRequest {

    private UserQueryCondition userQueryCondition;

    public UserQueryRequest(Long userId) {
        UserIdQueryCondition userIdQueryCondition = new UserIdQueryCondition();
        userIdQueryCondition.setUserId(userId);
        this.userQueryCondition = userIdQueryCondition;
    }

    public UserQueryRequest(String telephone) {
        UserPhoneQueryCondition userPhoneQueryCondition = new UserPhoneQueryCondition();
        userPhoneQueryCondition.setTelephone(telephone);
        this.userQueryCondition = userPhoneQueryCondition;
    }

    public UserQueryRequest(String telephone, String password) {
        UserPhoneAndPasswordQueryCondition userPhoneAndPasswordQueryCondition = new UserPhoneAndPasswordQueryCondition();
        userPhoneAndPasswordQueryCondition.setTelephone(telephone);
        userPhoneAndPasswordQueryCondition.setPassword(password);
        this.userQueryCondition = userPhoneAndPasswordQueryCondition;
    }

}
