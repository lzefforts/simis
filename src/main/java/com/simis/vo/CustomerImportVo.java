package com.simis.vo;

import java.io.Serializable;

/**
 * Created by 一拳超人
 */
public class CustomerImportVo implements Serializable{

    private static final long serialVersionUID = 3208010369551778136L;

    /**
     * 客户姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     *手机号
     */
    private String phone;

    /**
     * 邮件
     */
    private String email;

    /**
     * 证件号码
     */
    private String cardNo;

    /**
     * 语言委员会注册时间
     */
    private String clGovRegisterDate;

    /**
     * 是否已经交费，0：否，1：是
     */
    private String isAlreadyPaid;

    /**
     * 交费日期
     */
    private String payDate;



    private String examTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getClGovRegisterDate() {
        return clGovRegisterDate;
    }

    public void setClGovRegisterDate(String clGovRegisterDate) {
        this.clGovRegisterDate = clGovRegisterDate;
    }

    public String getIsAlreadyPaid() {
        return isAlreadyPaid;
    }

    public void setIsAlreadyPaid(String isAlreadyPaid) {
        this.isAlreadyPaid = isAlreadyPaid;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }
}
