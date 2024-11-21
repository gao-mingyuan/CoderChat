package com.gmy.coder.chat.auth.param;

import lombok.Getter;
import lombok.Setter;

/**
 * @author gaomingyuan
 */
@Setter
@Getter
public class LoginParam extends RegisterParam {

    /**
     * 记住我
     */
    private Boolean rememberMe;
}
