package com.gmy.coder.chat.api.websocket.request;

import lombok.Data;

/**
 * websocket前端请求体
 *
 * @author gaomingyuan
 */
@Data
public class WSBaseReq {
    /**
     * 请求类型 0.心跳检测,1.新消息
     *
     * @see com.gmy.coder.chat.api.websocket.constant.WSReqTypeEnum
     */
    private Integer type;

    /**
     * 每个请求包具体的数据，类型不同结果不同
     */
    private String data;
}
