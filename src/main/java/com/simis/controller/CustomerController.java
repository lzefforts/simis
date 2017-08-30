package com.simis.controller;

import com.google.common.collect.Maps;
import com.simis.common.Constants;
import com.simis.model.CustomerModel;
import com.simis.model.ExamBatchModel;
import com.simis.service.CustomerService;
import com.simis.service.DictionaryService;
import com.simis.service.ExamBatchService;
import com.simis.util.DateTimeUtil;
import com.simis.vo.ResultMsg;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by 一拳超人 on 17/3/29.
 */
@Controller
@RequestMapping(value = "/customer")
public class CustomerController {

    /**
     * 控制台输出用的log
     */
    protected static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ExamBatchService examBatchService;

    @Autowired
    private DictionaryService dictionaryService;

    @RequestMapping(value = "/toregister")
    public String toRegister(){
        return "register";
    }

    @RequestMapping(value = "/topay")
    public String toPay(){
        return "/pay/pay";
    }

    @RequestMapping(value = "/tomanage")
    public String toManage(){
        return "manage";
    }

    /**
     * @Title: login
     * @Description: 用户登陆
     * @param userName
     * @param passWord
     * @return ResultMsg
     * @throws
     */
    @ResponseBody
    @RequestMapping(value = "/login")
    public ResultMsg login(String userName,String passWord,HttpSession httpSession){
        ResultMsg resultMsg = new ResultMsg();
        LOGGER.info("登陆的用户名为{},密码为{}",userName,passWord);
        CustomerModel customerModel = customerService.getCustomer(userName);
        if(!DateTimeUtil.validateIsEnd(resultMsg,dictionaryService.queryByKey(Constants.END_DAYS_SYMBOL).getValue())){
            return resultMsg;
        }
        if(customerModel!=null){
            boolean result = customerService.validateCustomer(userName, passWord);
            if(result){
                httpSession.setAttribute("userId",userName);
                resultMsg.setSuccess(true);
                resultMsg.setMsg("登陆成功");
                resultMsg.setRemark(customerModel.getIsAlreadyPaid());
                return resultMsg;
            }
        }
        resultMsg.setSuccess(false);
        resultMsg.setMsg("用户名或者密码不正确");
        return resultMsg;
    }

    /**
     * @Title: regiter
     * @Description: 用户注册
     * @param customerModel
     * @return ResultMsg
     * @throws
     */
    @ResponseBody
    @RequestMapping(value = "/register")
    public ResultMsg regiter(CustomerModel customerModel,HttpSession httpSession){
        ResultMsg msg = new ResultMsg();
        try {
//            if(!DateTimeUtil.validateIsEnd(msg)){
//                return msg;
//            }
            CustomerModel model = customerService.getCustomer(customerModel.getCardNo());
            if(model!=null){
                LOGGER.info("身份证号为{}的已存在注册信息,不需再注册!",customerModel.getCardNo());
                msg.setMsg("此身份证号已注册过,无需再次注册!");
                msg.setSuccess(false);
                return msg;
            }
            customerService.executeRegister(customerModel);
            httpSession.setAttribute("userId",customerModel.getUserName());
            msg.setSuccess(true);
            msg.setMsg("注册成功");
            msg.setRemark("0");
        } catch (Exception e) {
            LOGGER.error("注册异常",e);
            msg.setSuccess(false);
            msg.setMsg("注册失败");
        }
        return msg;
    }


    /**
     * @Title: regiter
     * @Description: 用户注册
     * @return ResultMsg
     * @throws
     */
    @ResponseBody
    @RequestMapping(value = "/getExamTime")
    public List<ExamBatchModel> getExamTime(){
        List<ExamBatchModel> results = examBatchService.getNowTimeList();
        return results;
    }

}
