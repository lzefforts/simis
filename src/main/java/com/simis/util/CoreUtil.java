package com.simis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Created by 一拳超人 on 17/3/29.
 */
public class CoreUtil {
    private static Logger log = LoggerFactory.getLogger(CoreUtil.class);
    public static final int LINUX = 1;
    public static final int WINDOWS = 2;

    private CoreUtil() {
    }

    public static Long generateSortIdx() {
        return Long.valueOf(System.currentTimeMillis());
    }

    public static Timestamp generateTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String genUUIDString() {
        return UUID.randomUUID().toString();
    }

    public static int getServerOSType() {
        boolean resultCode = false;
        String osTag = System.getProperty("os.name");
        byte resultCode1;
        if(osTag.indexOf("Linux") == -1 && osTag.indexOf("Mac") == -1) {
            resultCode1 = 2;
        } else {
            resultCode1 = 1;
        }

        return resultCode1;
    }

    public static String getServerUser() {
        return System.getProperty("user.name");
    }

    public static String getLocalIP() {
        InetAddress addr = null;

        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException var4) {
            log.info("context", var4);
            return null;
        }

        byte[] ipAddr = addr.getAddress();
        String ipAddrStr = "";

        for(int i = 0; i < ipAddr.length; ++i) {
            if(i > 0) {
                ipAddrStr = ipAddrStr + ".";
            }

            ipAddrStr = ipAddrStr + (ipAddr[i] & 255);
        }

        return ipAddrStr;
    }
}
