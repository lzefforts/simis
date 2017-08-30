package com.simis.vo;

import java.io.Serializable;

/**
 * Created by ArnoldLee on 17/5/21.
 */
public class FeeInfoVo implements Serializable{

    private static final long serialVersionUID = 8867691125886243685L;
    //考试费用
    private String examFee;
    //材料费用
    private String bookFee;
    //视频费用
    private String videoFee;
    //邮寄费用
    private String mailFee;

    public String getExamFee() {
        return examFee;
    }

    public void setExamFee(String examFee) {
        this.examFee = examFee;
    }

    public String getBookFee() {
        return bookFee;
    }

    public void setBookFee(String bookFee) {
        this.bookFee = bookFee;
    }

    public String getVideoFee() {
        return videoFee;
    }

    public void setVideoFee(String videoFee) {
        this.videoFee = videoFee;
    }

    public String getMailFee() {
        return mailFee;
    }

    public void setMailFee(String mailFee) {
        this.mailFee = mailFee;
    }
}
