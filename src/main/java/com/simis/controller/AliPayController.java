package com.simis.controller;

import com.simis.vo.ResultMsg;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 支付宝付款controller
 * Created by 一拳超人 on 17/3/29.
 */
@Controller
@RequestMapping(value = "/onlinepay")
public class AliPayController {


    @RequestMapping("/alipay")
    public ResultMsg alipay(){
        ResultMsg msg = new ResultMsg();



        return msg;
    }


    @RequestMapping("/wechatpay")
    public ResultMsg wechatpay(){
        ResultMsg msg = new ResultMsg();



        return msg;
    }
}
