package com.simis.common;

/**
 * 微信支付常量
 * Created by 一拳超人 on 17/4/6.
 */
public class WeChatPayConstants {

    //微信支付-系统标示
    public static final String WECHAT_IDENTIFY = "wc";

    //微信支付-微信统一下单请求url
    public static final String WECHAT_REQUEST_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    //微信支付-分配的公众账号ID（企业号corpid即为此appId）
    public static final String WECHAT_APP_ID = "wx09182def8cf45005";//"wxd5609f7b5b4dd051";

    //微信支付-微信支付分配的商户号
    public static final String WECHAT_MCH_ID = "1434084702";//"1230989902";

    //微信支付-异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数
    public static final String WECHAT_NOTIFY = "http://114.255.149.98:9000/simis/onlinepay/weixinCallBack";

    //微信支付-密钥
    public static final String WECHAT_API_KEY = "CC6CF77158DC5DBCF141DEFD5E22B19C";//"kshdf554sdf4s5f2sf121sdfsd1f5sdf";

    //微信支付-订单查询-请求url
    public static final String WECHAT_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

    //请求微信支付,返回的code_url的key
    public final static String WECHAT_RESPONSE_CODE_URL_NAME = "code_url";

    //微信查询订单,返回的trade_state的key
    public final static String WECHAT_ORDER_QUERY_TRADE_STATUS = "trade_state";

    //微信查询订单,返回的total_fee的key
    public final static String WECHAT_ORDER_QUERY_TOTAL_FEE = "total_fee";
}
