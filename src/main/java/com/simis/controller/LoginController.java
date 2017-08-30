package com.simis.controller;

import com.google.common.collect.Maps;
import com.simis.model.CustomerModel;
import com.simis.model.UserModel;
import com.simis.service.CustomerService;
import com.simis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Created by 一拳超人
 */
@Controller
public class LoginController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String Login(String userName,String password){
        Map<String,String> sessionMap = Maps.newHashMap();
        CustomerModel customerModel = customerService.getCustomer(userName);
        if(customerModel!=null){
            boolean result = customerService.validateCustomer(userName, password);
            if(result){
                sessionMap.put("userId",userName);
                return "customer";
            }
            return "customerChangeInfo";
        }
        UserModel userModel = userService.findByUserName(userName);
        if(userModel != null){
            boolean result = userService.validateUser(userName, password);
            if(result){
                sessionMap.put("managerId",userName);
                return "manage";
            }
            return "user";
        }
        return null;
    }

}
