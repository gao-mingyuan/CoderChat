package com.gmy.coder.chat.api.logic.request.user;

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
public class UserRegisterRequest extends BaseRequest {

    private String telephone;

    private String inviteCode;

    private String password;

}
