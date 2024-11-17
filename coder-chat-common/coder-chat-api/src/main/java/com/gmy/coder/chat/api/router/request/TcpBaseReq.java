package com.gmy.coder.chat.api.router.request;

import lombok.Data;

/**
 * 路由服务tcp请求体
 *
 * @author gaomingyuan
 */
@Data
public class TcpBaseReq {
    /**
     * 请求类型 0.心跳检测,1.服务注册
     *
     * @see com.gmy.coder.chat.api.router.constant.RouterReqTypeEnum
     */
    private Integer type;

    /**
     * 每个请求包具体的数据，类型不同结果不同
     */
    private String data;
}
