package com.simis.util;

/**
 * Created by 一拳超人 on 17/4/5.
 */

import com.simis.common.WeChatPayConstants;
import com.simis.pay.wechat.common.MD5;
import com.simis.pay.wechat.common.Util;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.glassfish.jersey.internal.util.Base64;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.security.KeyStore;
import java.util.*;


/*
 * 微信支付-用户发起统一下单请求
 * 作者：董志平
 */
public class WXRequestUtil {
    protected static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WXRequestUtil.class);
    //test
    public static void main(String[] args) {
//        Map<String,String> sessionMap = Maps.newHashMap();
//        Map<String,String> resultMap = SendPayment("SYSTEM","影视培训中心考试费用",1,"BJGBYSPXZX0001",sessionMap);
//        LOGGER.info("#############微信支付-请求结果为:#######begin########");
//        for(String key : resultMap.keySet()){
//            LOGGER.info("key="+key+",value="+resultMap.get(key));
//        }
//        LOGGER.info("#############微信支付-请求结果为:#######end########");
        String orderNo = "SIMISwc14996650232607315793";
        queryPayOrder(orderNo);

    }

    //微信订单查询
    public static Map<String,String> queryPayOrder(String out_trade_no){
        LOGGER.info("微信订单查询,开始...");
        String url = WeChatPayConstants.WECHAT_QUERY_URL;
        LOGGER.info("微信订单查询-订单号为:"+out_trade_no);
        String xml = WXQueryParam(out_trade_no);
        LOGGER.info("微信订单查询-请求的xml为:"+xml);
        String res = httpsRequest(url,"POST",xml);
        LOGGER.info("微信订单查询-返回的字符串为:"+res);
        Map<String,String> data = null;
        try {
            data = doXMLParse(res);
        } catch (Exception e) {
            LOGGER.error("xml转换为map异常",e);
        }
        LOGGER.info("#############微信订单查询-请求结果为:#######begin########");
        if(data!=null){
            for(String key : data.keySet()){
                LOGGER.info("key="+key+",value="+data.get(key));
            }
        }
        LOGGER.info("#############微信订单查询-请求结果打印结束:#######end########");
        return data;
    }

    /*
     * 发起支付请求
     * body 商品描述
     * out_trade_no 订单号
     * total_fee    订单金额 单位:元
     * product_id   商品ID
     */
    public static Map<String,String> SendPayment(String out_trade_no,String userId,String body,double total_fee,String product_id,Map<String,String> sessionMap){
        LOGGER.info("微信支付-用户为{},描述为{},总费用为{},产品编码为{}",userId,body,total_fee,product_id);
        String url = WeChatPayConstants.WECHAT_REQUEST_URL;
        String xml = WXParamGenerate(body,out_trade_no,total_fee,product_id);
        LOGGER.info("微信支付-请求的xml为:"+xml);
        String res = httpsRequest(url,"POST",xml);
        LOGGER.info("微信支付-返回的字符串为:"+res);
        Map<String,String> data = null;
        try {
            data = doXMLParse(res);
        } catch (Exception e) {
            LOGGER.error("xml转换为map异常",e);
        }
        LOGGER.info("#############微信支付-请求结果为:#######begin########");
        for(String key : data.keySet()){
            LOGGER.info("key="+key+",value="+data.get(key));
        }
        LOGGER.info("#############微信支付-请求结果为:#######end########");
        return data;
    }

    public static String NonceStr(){
        String res = Base64.encodeAsString(Math.random()+"::"+new Date().toString()).substring(0, 30);
        LOGGER.info("微信支付-随机字符串为:"+res);
        return res;
    }

    public static String GetIp() {
        InetAddress ia=null;
        try {
            ia=InetAddress.getLocalHost();
            String localip=ia.getHostAddress();
            return localip;
        } catch (Exception e) {
            return null;
        }
    }

    public static String GetSign(Map<String,String> param){
        String StringA =  Util.formatUrlMap(param);
        LOGGER.info("微信支付-StringA为:"+StringA);
        LOGGER.info("微信支付-密钥为:"+WeChatPayConstants.WECHAT_API_KEY);
        String stringSignTemp = MD5.MD5Encode(StringA+"&key="+ WeChatPayConstants.WECHAT_API_KEY).toUpperCase();
        return stringSignTemp;
    }

    //Map转xml数据
    public static String GetMapToXML(Map<String,String> param){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (Map.Entry<String,String> entry : param.entrySet()) {
            sb.append("<"+ entry.getKey() +">");
            sb.append(entry.getValue());
            sb.append("</"+ entry.getKey() +">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    //微信订单查询参数设置
    public static String WXQueryParam(String out_trade_no){
        SortedMap<String,String> param = new TreeMap<String,String>();
        param.put("appid", WeChatPayConstants.WECHAT_APP_ID);//公众账号ID
        param.put("mch_id", WeChatPayConstants.WECHAT_MCH_ID);//微信支付分配的商户号
        param.put("nonce_str",WXRequestUtil.NonceStr());//随机字符串
        param.put("out_trade_no",out_trade_no);//商户订单号
        String sign = WXRequestUtil.GetSign(param);
        LOGGER.info("微信订单查询-sign为:"+sign);
        param.put("sign", sign);//签名	sign
        return WXRequestUtil.GetMapToXML(param);
    }


    //微信统一下单参数设置
    public static String WXParamGenerate(String description,String out_trade_no,double total_fee,String product_id){
        int fee = (int)(total_fee * 100.00);
        SortedMap<String,String> param = new TreeMap<String,String>();
        param.put("appid", WeChatPayConstants.WECHAT_APP_ID);//公众账号ID
        param.put("body", description);//商品描述
        param.put("mch_id", WeChatPayConstants.WECHAT_MCH_ID);//微信支付分配的商户号
        param.put("nonce_str",NonceStr());//随机字符串
        param.put("notify_url", WeChatPayConstants.WECHAT_NOTIFY);//微信回调地址
        param.put("out_trade_no",out_trade_no);//点单号
        param.put("product_id", product_id);
//        param.put("spbill_create_ip", GetIp());
        param.put("spbill_create_ip", "127.0.0.1");
        param.put("total_fee", fee+"");//
        param.put("trade_type", "NATIVE");
        String sign = GetSign(param);
        LOGGER.info("微信支付-sign为:"+sign);
        param.put("sign", sign);//签名	sign
        return GetMapToXML(param);
    }

    //发起微信支付请求
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            System.out.println("连接超时：{}"+ ce);
        } catch (Exception e) {
            System.out.println("https请求异常：{}"+ e);
        }
        return null;
    }

    //退款的请求方法
    public static String httpsRequest2(String requestUrl, String requestMethod, String outputStr) throws Exception {
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        StringBuilder res = new StringBuilder("");
        FileInputStream instream = new FileInputStream(new File("/home/apiclient_cert.p12"));
        try {
            keyStore.load(instream, "".toCharArray());
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, "1313329201".toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {

            HttpPost httpost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            StringEntity entity2 = new StringEntity(outputStr ,Consts.UTF_8);
            httpost.setEntity(entity2);
            System.out.println("executing request" + httpost.getRequestLine());

            CloseableHttpResponse response = httpclient.execute(httpost);

            try {
                HttpEntity entity = response.getEntity();

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    System.out.println("Response content length: " + entity.getContentLength());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                    String text = "";
                    res.append(text);
                    while ((text = bufferedReader.readLine()) != null) {
                        res.append(text);
                        System.out.println(text);
                    }

                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return  res.toString();

    }

    //xml解析
    public static Map<String, String> doXMLParse(String strxml) throws Exception {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        if(null == strxml || "".equals(strxml)) {
            return null;
        }

        Map<String,String> m = new HashMap<String,String>();
        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while(it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if(children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }

        //关闭流
        in.close();
        return m;
    }

    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if(!children.isEmpty()) {
            Iterator it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if(!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }


    public static boolean notifyResult(HttpServletRequest request,HttpServletResponse response){
        //读取参数
        LOGGER.error("解析微信支付回调参数,开始...");
        InputStream inputStream = null;
        StringBuffer sb = new StringBuffer();
        try {
            inputStream = request.getInputStream();
            LOGGER.error("获取inputStream,完成...");
        } catch (IOException e) {
            LOGGER.error("解析微信支付回调参数,异常结束...");
            LOGGER.error("获取inputStream,异常",e);
        }
        String s ;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            LOGGER.error("转换为BufferedReader,完成...");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("解析微信支付回调参数,异常结束...");
            LOGGER.error("转换为BufferedReader,异常",e);
        }
        try {
            while ((s = in.readLine()) != null){
                sb.append(s);
            }
            LOGGER.error("转换为String,完成...");
        } catch (IOException e) {
            LOGGER.error("解析微信支付回调参数,异常结束...");
            LOGGER.error("转换为String,异常",e);
        }
        try {
            in.close();
            LOGGER.error("关闭BufferedReader,完成...");
        } catch (IOException e) {
            LOGGER.error("解析微信支付回调参数,异常结束...");
            LOGGER.error("关闭BufferedReader,异常",e);
        }
        try {
            inputStream.close();
            LOGGER.error("关闭InputStream,完成...");
        } catch (IOException e) {
            LOGGER.error("解析微信支付回调参数,异常结束...");
            LOGGER.error("关闭InputStream,异常",e);
        }

        //解析xml成map
        Map<String, String> m = new HashMap<String, String>();
        try {
            m = doXMLParse(sb.toString());
            LOGGER.error("转换为Map,完成...");
        } catch (Exception e) {
            LOGGER.error("解析微信支付回调参数,异常结束...");
            LOGGER.error("转换为Map,异常",e);
        }

        //过滤空 设置 TreeMap
        SortedMap<String,String> packageParams = new TreeMap<String,String>();
        Iterator it = m.keySet().iterator();
        LOGGER.error("回调传入的参数为:");
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = m.get(parameter);
            String value = "";
            if(null != parameterValue) {
                value = parameterValue.trim();
            }
            packageParams.put(parameter, value);
            LOGGER.info("key="+parameter+",value="+value);
        }
        // 账号信息
        String key = WeChatPayConstants.WECHAT_API_KEY; // key
        //判断签名是否正确
        LOGGER.error("验证签名开始...");
        if(PayCommonUtil.isTenpaySign("UTF-8", packageParams)) {
            LOGGER.error("验证签名成功...");
            //------------------------------
            //处理业务开始
            //------------------------------
            String resXml = "";
            if("SUCCESS".equals((String)packageParams.get("result_code"))){
                LOGGER.error("支付成功处理...");
                // 这里是支付成功
                //////////执行自己的业务逻辑////////////////
                String mch_id = (String)packageParams.get("mch_id");
                String openid = (String)packageParams.get("openid");
                String is_subscribe = (String)packageParams.get("is_subscribe");
                String out_trade_no = (String)packageParams.get("out_trade_no");

                String total_fee = (String)packageParams.get("total_fee");

                LOGGER.info("mch_id:"+mch_id);
                LOGGER.info("openid:"+openid);
                LOGGER.info("is_subscribe:"+is_subscribe);
                LOGGER.info("out_trade_no:"+out_trade_no);
                LOGGER.info("total_fee:"+total_fee);
                //////////执行自己的业务逻辑////////////////

                LOGGER.info("支付成功");
                //通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                //------------------------------
                //处理业务完毕
                //------------------------------
                BufferedOutputStream out = null;
                try {
                    out = new BufferedOutputStream(
                            response.getOutputStream());
                    out.write(resXml.getBytes());
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    LOGGER.error("解析微信支付回调参数,异常结束...");
                    LOGGER.error("接受支付成功,返回给微信确认结果,异常",e);
                }
                return true;
            } else {
                LOGGER.error("支付成功失败处理...");
                LOGGER.info("支付失败,错误信息：" + packageParams.get("err_code"));
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                //------------------------------
                //处理业务完毕
                //------------------------------
                BufferedOutputStream out = null;
                try {
                    out = new BufferedOutputStream(
                            response.getOutputStream());
                    out.write(resXml.getBytes());
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    LOGGER.error("解析微信支付回调参数,异常结束...");
                    LOGGER.error("接受支付失败,返回给微信确认结果,异常",e);
                }
                return false;
            }

        } else{
            LOGGER.info("通知签名验证失败");
            return false;
        }
    }
}