package com.changgou.pay.service;

import java.util.Map;

/**
 * Created by fyf on 2019/9/5
 */
public interface WeixinPayService {
    /**
     * 创建二维码
     * @param out_trade_no 客户端自定义订单编号
     * @param total_fee 交易金额 单位 分
     * @return
     */
     Map createNative(String out_trade_no, String total_fee);

    /**
     * 查询支付状态
     * @param out_trade_no
     * @return
     */
     Map queryPayStatus(String out_trade_no);
}
