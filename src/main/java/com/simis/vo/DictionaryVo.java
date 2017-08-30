package com.simis.vo;

import java.io.Serializable;

/**
 * Created by ArnoldLee on 17/5/21.
 */
public class DictionaryVo implements Serializable{

    private static final long serialVersionUID = 8867691125886243685L;
    //视频地址
    private String videoAddress;
    //视频密码
    private String videoCode;

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
}
