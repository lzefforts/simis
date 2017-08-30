import com.simis.common.WeChatPayConstants;
import com.simis.util.WXRequestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by ArnoldLee on 17/5/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-sims.xml"})
public class WXRequestUtilTest {

    protected static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WXRequestUtilTest.class);

    @Test
    public void testSendPayment(){
        String xml = combinPayPara();
        LOGGER.info("微信支付-请求的xml为:"+xml);
        String res = WXRequestUtil.httpsRequest(WeChatPayConstants.WECHAT_REQUEST_URL,"POST",xml);
        LOGGER.info("微信支付-返回的字符串为:"+res);
        Map<String,String> data = null;
        try {
            data = WXRequestUtil.doXMLParse(res);
        } catch (Exception e) {
            LOGGER.error("xml转换为map异常",e);
        }
        LOGGER.info("#############微信支付-请求结果为:#######begin########");
        for(String key : data.keySet()){
            LOGGER.info("key="+key+",value="+data.get(key));
        }
        LOGGER.info("#############微信支付-请求结果为:#######end########");
    }

    //组装支付请求参数
    private String combinPayPara(){
        int fee = (int)(0.01 * 100.00);
        SortedMap<String,String> param = new TreeMap<String,String>();
        param.put("appid", WeChatPayConstants.WECHAT_APP_ID);//公众账号ID
        param.put("body", "影视培训中心考试费用");//商品描述
        param.put("mch_id", WeChatPayConstants.WECHAT_MCH_ID);//微信支付分配的商户号
        param.put("nonce_str","MC4wMDIxODg2Mjc0NzcwMjA2NDM4Oj");//随机字符串
        param.put("notify_url", WeChatPayConstants.WECHAT_NOTIFY);//微信回调地址
        param.put("out_trade_no","SIMISwc14941496116957196649");//点单号
        param.put("product_id", "BJGBYSPXZX0001");
        param.put("spbill_create_ip", "127.0.0.1");
        param.put("total_fee", "1");//
        param.put("trade_type", "NATIVE");
        String sign = WXRequestUtil.GetSign(param);
        LOGGER.info("微信支付-sign为:"+sign);
        param.put("sign", sign);//签名	sign
        return WXRequestUtil.GetMapToXML(param);
    }


    @Test
    public void queryWechatOrderStatus(){
        String out_trade_no = "SIMISwc14996650232607315793";
        WXRequestUtil.queryPayOrder(out_trade_no);
    }

}
