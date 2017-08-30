package com.simis.vo;

import java.io.Serializable;

/**
 * 邮件参数vo
 * Created by ArnoldLee on 17/5/20.
 */
public class EmailParaVo implements Serializable{

    private static final long serialVersionUID = 6291105215423814067L;
    //客户名称
    private String customerName;

    //考试时间
    private String examTime;

    //考试系统名称
    private String systemName;

    //考试名称
    private String examName;

    //视频地址
    private String videoAddress;

    //视频密码
    private String videoCode;

    //发送邮件的地址
    private String emailMother;

    //发送邮件的密码
    private String emailMotherPassword;

    //发送邮件的host
    private String emailMotherHost;


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getVideoAddress() {
        return videoAddress;
    }

    public void setVideoAddress(String videoAddress) {
        this.videoAddress = videoAddress;
    }

    public String getVideoCode() {
        return videoCode;
    }

    public void setVideoCode(String videoCode) {
        this.videoCode = videoCode;
    }

    public String getEmailMother() {
        return emailMother;
    }

    public void setEmailMother(String emailMother) {
        this.emailMother = emailMother;
    }

    public String getEmailMotherPassword() {
        return emailMotherPassword;
    }

    public void setEmailMotherPassword(String emailMotherPassword) {
        this.emailMotherPassword = emailMotherPassword;
    }

    public String getEmailMotherHost() {
        return emailMotherHost;
    }

    public void setEmailMotherHost(String emailMotherHost) {
        this.emailMotherHost = emailMotherHost;
    }
}
