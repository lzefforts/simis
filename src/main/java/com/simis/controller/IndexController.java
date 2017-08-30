package com.simis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 一拳超人
 */
@Controller
public class IndexController {


    @RequestMapping("/")
    public String mainPage(){
        return "register";
    }

    @RequestMapping("/simis/pay")
    public String payPage(){
        return "pay";
    }

    @RequestMapping("/simis/manage")
    public String managePage(){
        return "manage";
    }
}
