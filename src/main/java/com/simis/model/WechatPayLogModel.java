package com.simis.model;

import javax.persistence.*;

/**
 * 微信支付日志记录
 * Created by ArnoldLee on 17/5/7.
 */
@Entity
@Table(name = "sims_wechat_pay_log")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class WechatPayLogModel extends BaseModel{


    private static final long serialVersionUID = 609009363858733974L;
    /**
     * pkid 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="pkid")
    private Long pkid;

    /**
     * 发送的信息（生成二维码）
     */
    @Column(name="send_message")
    private String sendMessage;

    /**
     * 微信返回的信息（生成二维码）
     */
    @Column(name="receive_message")
    private String receive_message;

    /**
     * 支付后微信的回调信息
     */
    @Column(name="callback_message")
    private String callbackMessage;

    /**
     * 身份证号
     */
    @Column(name="card_no")
    private String cardNo;

    public Long getPkid() {
        return pkid;
    }

    public void setPkid(Long pkid) {
        this.pkid = pkid;
    }

    public String getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(String sendMessage) {
        this.sendMessage = sendMessage;
    }

    public String getReceive_message() {
        return receive_message;
    }

    public void setReceive_message(String receive_message) {
        this.receive_message = receive_message;
    }

    public String getCallbackMessage() {
        return callbackMessage;
    }

    public void setCallbackMessage(String callbackMessage) {
        this.callbackMessage = callbackMessage;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
}
