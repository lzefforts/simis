package com.simis.common;

/**
 * 微信订单状态枚举
 * Created by ArnoldLee on 17/5/7.
 */
public enum WechatOrderStatusEnum {

    /**
     * 支付成功
     */
    SUCCESS("SUCCESS", "支付成功"),
    /**
     * 转入退款
     */
    REFUND("REFUND", "转入退款"),
    /**
     * 未支付
     */
    NOTPAY("NOTPAY", "未支付"),
    /**
     * 已关闭
     */
    CLOSED("CLOSED", "已关闭"),
    /**
     * 已撤销（刷卡支付）
     */
    REVOKED("REVOKED", "已撤销（刷卡支付）"),
    /**
     * 用户支付中
     */
    USERPAYING("USERPAYING", "用户支付中"),
    /**
     * 支付失败(其他原因，如银行返回失败)
     */
    PAYERROR("PAYERROR", "支付失败(其他原因，如银行返回失败)");

    private String code;

    private String description;

    private WechatOrderStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static WechatOrderStatusEnum createWechatOrderStatusEnum(String code) {
        WechatOrderStatusEnum result = null;
        WechatOrderStatusEnum[] values = WechatOrderStatusEnum.values();
        for (WechatOrderStatusEnum statusEnum : values) {
            if (statusEnum.getCode().equalsIgnoreCase(code)) {
                result = statusEnum;
                break;
            }
        }
        return result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isSameCode(String code) {
        return this.code.equals(code);
    }
}
