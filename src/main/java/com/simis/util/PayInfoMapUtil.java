package com.simis.util;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 *
 * 用于支付的时候保存
 * Created by ArnoldLee on 17/5/31.
 */
public class PayInfoMapUtil {


    private static PayInfoMapUtil instance = null;

    private PayInfoMapUtil(){}

    private Map map;


    public static PayInfoMapUtil newInstance(){
        synchronized (PayInfoMapUtil.class){
            if(instance == null){
                //创建实例之前可能会有一些准备性的耗时工作
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                instance = new PayInfoMapUtil();
            }
            instance.map = Maps.newHashMap();
        }
        return instance;
    }

    public Map getMap() {
        return map;
    }
}
