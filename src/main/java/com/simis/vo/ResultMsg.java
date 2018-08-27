package com.simis.vo;

import java.io.Serializable;

/**
 * @author 一拳超人
 * @Description: 返回信息bean
 * @date 2016年9月19日 下午5:21:18
 */
public class ResultMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;

    private String msg;

    private boolean flag;

    private String remark;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
