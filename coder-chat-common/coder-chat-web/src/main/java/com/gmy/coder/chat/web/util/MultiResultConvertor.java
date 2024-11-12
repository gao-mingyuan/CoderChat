package com.gmy.coder.chat.web.util;


import com.gmy.coder.chat.base.response.PageResponse;
import com.gmy.coder.chat.web.vo.MultiResult;

import static com.gmy.coder.chat.base.response.ResponseCode.SUCCESS;

/**
 * @author gaomingyuan
 */
public class MultiResultConvertor {

    public static <T> MultiResult<T> convert(PageResponse<T> pageResponse) {
        MultiResult<T> multiResult = new MultiResult<T>(true, SUCCESS.name(), SUCCESS.name(), pageResponse.getDatas(), pageResponse.getTotal(), pageResponse.getCurrentPage(), pageResponse.getPageSize());
        return multiResult;
    }
}
