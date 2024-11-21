package com.gmy.coder.chat.api.logic.response.user;

import com.gmy.coder.chat.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户操作响应
 *
 * @author gaomingyuan
 */
@Getter
@Setter
public class UserOperatorResponse extends BaseResponse {

    private UserInfo user;
}
