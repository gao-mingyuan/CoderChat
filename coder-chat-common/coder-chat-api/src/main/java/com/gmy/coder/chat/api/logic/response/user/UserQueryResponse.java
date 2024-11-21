package com.gmy.coder.chat.api.logic.response.user;

import com.gmy.coder.chat.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author gaomingyuan
 */
@Setter
@Getter
@ToString
public class UserQueryResponse<T> extends BaseResponse {

    private static final long serialVersionUID = 1L;

    private T data;
}
