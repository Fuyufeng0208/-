package com.changgou.pay.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.pay.service.WeixinPayService;
import com.github.wxpay.sdk.WXPayUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fyf on 2019/9/5
 */
@RestController
@RequestMapping("/weixin/pay")
public class WeixinPayController {

    @Value("${mq.pay.exchange.order}")
    private String exchange;
    @Value("${mq.pay.queue.order}")
    private String queue;
    @Value("${mq.pay.routing.key}")
    private String routing;

    @Autowired
    private WeixinPayService weixinPayService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired(required = false)
    private Environment env;

    /**
     * 创建二维码连接地址返回给前端，生成二维码
     * @param out_trade_no
     * @param total_fee
     * @return
     */
    @RequestMapping(value = "/create/native")
    public Result createNative(String out_trade_no, String total_fee){
        Map map = weixinPayService.createNative(out_trade_no, total_fee);
        return new Result(true, StatusCode.OK,"二维码生成成功",map);
    }

    /**
     * 查询支付状态
     * @param out_trade_no
     * @return
     */
    @GetMapping(value = "/status/query")
    public Result queryStatus(String out_trade_no){
        Map map = weixinPayService.queryPayStatus(out_trade_no);
        return new Result(true,StatusCode.OK,"订单状态查询成功",map);
    }

    /**
     * 接受微信支付结果的通知
     * @param
     * @return
     */
    @RequestMapping("/notify/url")
    public Map<String, String> jieshouResult(HttpServletRequest request){
        /*try {

            //1.获取流信息
            ServletInputStream ins = request.getInputStream();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();


            //todo
            byte[] buffer = new byte[1024];
            int len;
            while ((len = ins.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }

            bos.close();
            ins.close();

            //2.转换成XML字符串
            byte[] bytes = bos.toByteArray();

            //微信支付系统传递过来的XML的字符串
            String resultStrXML = new String(bytes, "utf-8");
            //3.转成MAP
            Map<String, String> map = WXPayUtil.xmlToMap(resultStrXML);

            System.out.println(resultStrXML);

            //4.发送消息给Rabbitmq  .........
            String data = JSON.toJSONString(map);
            rabbitTemplate.convertAndSend(env.getProperty("mq.pay.exchange.order"),env.getProperty("mq.pay.routing.key"),data);

            //5.返回微信的接收请况(XML的字符串)

            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("return_code", "SUCCESS");
            resultMap.put("return_msg", "OK");
            return WXPayUtil.mapToXml(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/
        try {
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("return_code", "SUCCESS");
            resultMap.put("return_msg", "OK");
            resultMap.put("out_trade_no", "1169445387992432640");
            resultMap.put("transaction_id", "1217752501201407033233368018");
            //附加参数,创建二维码时传递过去，微信原样返回
            //Map<String,String> attach = new HashMap<>();
            resultMap.put("username","xiaoxiong");
            resultMap.put("queue","mq.queue.seckillorder");//队列名称
            resultMap.put("routingkey","mq.queue.seckillorder");//路由key
            resultMap.put("exchange","mq.exchange.seckillorder");

            String data = JSON.toJSONString(resultMap);
            //rabbitTemplate.convertAndSend(env.getProperty("mq.pay.exchange.order"),env.getProperty("mq.pay.routing.key"),data);
            //rabbitTemplate.convertAndSend(env.getProperty("mq.pay.exchange.order"),env.getProperty("mq.pay.routing.key"),data);
            //动态的从attach参数中获取数据
            rabbitTemplate.convertAndSend(resultMap.get("exchange"),resultMap.get("routingkey"),data);

            return resultMap;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
