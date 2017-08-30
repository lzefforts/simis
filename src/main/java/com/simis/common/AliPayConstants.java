package com.simis.common;

/**
 * 支付宝支付常量
 * Created by ArnoldLee on 17/4/7.
 */
public class AliPayConstants {

    //支付宝支付-系统标示
    public static final String IDENTIFY = "ali";

    //支付宝支付-#此为沙箱环境的网关
    public static final String OPEN_API_DOMAIN = "https://openapi.alipay.com/gateway.do";

    public static final String MCLOUD_API_DOMAIN = "http://mcloudmonitor.com/gateway.do";
    //#此为沙箱环境的商户UID
    public static final String PID = "2088102172329883";

    public static final String APPID = "2016082000300485";//2017040706578278

    //# RSA私钥、公钥和支付宝公钥
    //#此处请填写你的商户私钥且转PKCS8格式
    public static final String PRIVATE_KEY = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAMKXZrFR+rnvYgBs9qz2cE1mCSIBReaqan+5Pf5+02Hyj4HzcNTTWqHFm91IH3wYPyhpM7XlbgJ5yWJtgC4g1lz75r8a+UCyuxP8by1LV/44Gi/TIfLSgATfQ73OcM9imXocRdYz2ZCwqi1gV+b3UDoy/Da5w07gRWizFzS6Vq1rAgMBAAECgYEAqHHc4GRBsRCKeinYtK1Vhqcj0Yg11Lvy85z3si0fNY26dvs8R5gFydzC/Mx5f8rNPUUYUHQn+4CqOR3D/c291X1iToV2NEVLHeJrOUDknP4oQriqt2w9pZ8rzwZp2jcWvRVUF4zTpEiMppmORP6spRfX6DLZg29SFI6GZWu6TkCQQDp3mim1BhuS3YONEZgqC69zn0/DGOFkeIx0S18qAu1X4I1FEjVTkY4HPdwihpgYajm0UFg1lk8mTiunHpZRCnAkEA1QF6U1AKjM6zsVdEnRXEDTCC75uVJGSYFJWHHx9Pjyd9vX8nSZV0Z0U4V0ZG0n0yvHj5LRO6U5FCqFRw1WixnQJBALmCKz8SvF/H9N6LiwmSPY6w5q82kNRlRc7wSceNspQT0wqL5+SACG98M0xXY5j1HmiOlHxgCTvyriXOwObivQcCQQCTNaNB4uZ3q/86R/KukbVd3DIRwLfRYAhO6Yxp8Oy+Je/bv/359+Vr3cXzYyldHZOr9/tVsPWr/Y9Q4JLemq1tAkEAlBU7+4EdzFap7e/FMgyKD5DmL8H2iAEuMRRCPL84GhFfK/7PSQ/40NgKxpTgY44NlElHXcRPw5CZu6gqdiNJOA==";

    //#此处请填写你的商户公钥
    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIbDQEBAQUAA4GNADCBiQKBgQDCl2axUfq572IAbPas9nBNZgkiAUXmqmp/uT3+ftNh8o+B83DU01qhxZvdSB98GD8oaTO15W4CeclibYAuINZc++a/GvlAsrsT/G8tS1f+OBov0yHy0oAE30O9znDPYpl6HEXWM9mQsKotYFfm91A6Mvw2ucNO4EVosxc0ulatawIDAQAB";

    //#此为沙箱环境的公钥
    public static final String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIgHnOn7LLILlKETd6BFRJ0GqgS2Y3mn1wMQmyh9zEyWlz5p1zrahRahbXAfCfSqshSNfqOmAQzSHRVjCqjsAw1jyqrXaPdKBmr90DIpIxmIyKXv4GGAkPyJ/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB";

    //# 当面付最大查询次数和查询间隔（毫秒）
    public static final String MAX_QUERY_RETRY = "5";
    public static final String QUERY_DURATION = "5000";

    //# 当面付最大撤销次数和撤销间隔（毫秒）
    public static final String MAX_CANCEL_RETRY = "3";
    public static final String CANCEL_DURATION = "2000";

    //# 交易保障线程第一次调度延迟和调度间隔（秒）
    public static final String HEARTBEAT_DELAY = "5";
    public static final String HEARTBEAT_DURATION = "900";
}
