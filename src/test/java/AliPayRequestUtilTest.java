import com.simis.util.AliPayRequestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by ArnoldLee on 17/4/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-sims.xml"})
public class AliPayRequestUtilTest {

    @Autowired
    private AliPayRequestUtil aliPayRequestUtil;

    @Test
    public void testtradePay(){
        //订单标题
        String subject = "xxx品牌xxx门店当面付扫码消费";
        String totalAmount = "0.01";
        //订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买商品3件共20.00元";
        //商户门店编号(可选)
        String storeId = "test_store_id";
        aliPayRequestUtil.tradePay(subject,totalAmount,body,storeId);
    }

}
