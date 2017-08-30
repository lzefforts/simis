package com.simis.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 交费vo
 * Created by ArnoldLee on 17/4/30.
 */
public class PayDetailVo implements Serializable {
    private static final long serialVersionUID = -8887328339680537584L;

    //考试费用
    private double examFee;
    //材料费用
    private double bookFee;
    //视频费用
    private double videoFee;
    //收件地址
    private String address;
    //邮寄费用
    private double mailFee;


    public double getExamFee() {
        return examFee;
    }

    public void setExamFee(double examFee) {
        this.examFee = examFee;
    }

    public double getBookFee() {
        return bookFee;
    }

    public void setBookFee(double bookFee) {
        this.bookFee = bookFee;
    }

    public double getVideoFee() {
        return videoFee;
    }

    public void setVideoFee(double videoFee) {
        this.videoFee = videoFee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getMailFee() {
        return mailFee;
    }

    public void setMailFee(double mailFee) {
        this.mailFee = mailFee;
    }
}
