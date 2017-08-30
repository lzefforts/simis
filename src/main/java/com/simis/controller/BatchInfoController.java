package com.simis.controller;

import com.simis.vo.ResultMsg;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * Created by 一拳超人
 */
@Controller
public class BatchInfoController {



    //批次设置收费标标准
    @ResponseBody
    @RequestMapping("/manage/batch")
    public ResultMsg setBatchPayAmt(String fatherNo, BigDecimal payAmt){
        ResultMsg msg = new ResultMsg();



        return msg;
    }
}
