package com.simis.controller;

import com.simis.common.Constants;
import com.simis.common.FeeEnum;
import com.simis.common.WeChatPayConstants;
import com.simis.common.WechatOrderStatusEnum;
import com.simis.model.CustomerModel;
import com.simis.model.DictionaryModel;
import com.simis.service.CustomerService;
import com.simis.service.DictionaryService;
import com.simis.service.ExamBatchService;
import com.simis.util.DateTimeUtil;
import com.simis.util.EmailUtil;
import com.simis.util.QRCodeUtil;
import com.simis.util.WXRequestUtil;
import com.simis.vo.EmailParaVo;
import com.simis.vo.FeeInfoVo;
import com.simis.vo.PayDetailVo;
import com.simis.vo.ResultMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by ArnoldLee on 17/4/8.
 */
//@Scope("prototype")
@Controller
@RequestMapping(value = "/onlinepay")
public class PayController {

    protected static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PayController.class);

    //存放支付的用户userId
//    private Map<String,String> sessionMap = Maps.newHashMap();

    //存放支付的用户userId
    private List<Map<String,String>> unPayTradeMapList = new ArrayList<>();

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private ExamBatchService examBatchService;

    @RequestMapping(value = "/customer/toregister")
    public String toRegister(){
        return "register";
    }

    @RequestMapping(value = "/topay")
    public String toPay(){
        return "/pay/pay";
    }

    @RequestMapping(value = "/manage/tomanage")
    public String toManage(){
        return "manage";
    }

    @RequestMapping(value = "/toPaySuccess")
    public String toPaySuccess(){
        return "/pay/paySuccess";
    }

    @RequestMapping(value = "/toPayFail")
    public String toPayFail(){
        return "/pay/payFail";
    }

    /**
     * @Title: pay
     * @Description: 计算用户交费,生成二维码,显示在页面端,让用户扫描微信支付
     * @return ResultMsg
     * @throws
     */
    @ResponseBody
    @RequestMapping(value = "/pay")
    public ResultMsg pay(PayDetailVo payDetailVo,HttpSession httpSession){
        ResultMsg resultMsg = new ResultMsg();
        if(httpSession.getAttribute("userId") == null){
            resultMsg.setSuccess(false);
            resultMsg.setMsg("请先登陆!");
            return resultMsg;
        }
        String userId = httpSession.getAttribute("userId").toString();
        LOGGER.info("#################登录的用户名为:{}",userId);
        CustomerModel customerModel = customerService.getCustomer(userId);
        if(Constants.YES.equals(customerModel.getIsAlreadyPaid())){
            resultMsg.setSuccess(false);
            resultMsg.setMsg("您已经交过费,无需再次交费!");
            return resultMsg;
        }
        boolean ifFull = examBatchService.getIsFull(customerModel.getExamTime());
        if(ifFull){
            resultMsg.setSuccess(false);
            resultMsg.setMsg("此考试时间交费人数已满!请联系管理员!");
            return resultMsg;
        }

        DictionaryModel examFeeDModel = dictionaryService.queryByKey(Constants.EXAM_FEE_KEY);
        payDetailVo.setExamFee(Double.valueOf(examFeeDModel.getValue()));

        if(!StringUtils.isEmpty(payDetailVo.getAddress())){
            customerModel.setExamFee(BigDecimal.valueOf(payDetailVo.getExamFee()));
            customerModel.setBookFee(BigDecimal.valueOf(payDetailVo.getBookFee()));
            customerModel.setVideoFee(BigDecimal.valueOf(payDetailVo.getVideoFee()));
            if(payDetailVo.getBookFee()>0){
                DictionaryModel mailFeeDModel = dictionaryService.queryByKey(Constants.MAIL_FEE_KEY);
                customerModel.setMailFee(BigDecimal.valueOf(Double.valueOf(mailFeeDModel.getValue())));
            }
            customerModel.setAddress(payDetailVo.getAddress());
            customerModel.setOperator(Constants.OPERATOR);
            customerModel.setModifyTime(new Date());
            customerService.update(customerModel);
        }

        FeeEnum feeEnum = getFeeEnum(payDetailVo);
        String fileName = "";
        String path = System.getProperty("evan.webapp");
        LOGGER.info("########项目绝对路径为:{}",path);
        Properties prop = System.getProperties();

        String os = prop.getProperty("os.name");
        if (os != null && (os.toLowerCase().indexOf("linux") > -1 || os.toLowerCase().indexOf("mac") > -1)) {
            path = path + "WEB-INF/resource/qcode/";
        } else {
            path = path + "WEB-INF\\resource\\qcode\\";
        }

        LOGGER.info("########qcode放置的绝对路径为:{}",path);
        double totalFee = payDetailVo.getExamFee()+payDetailVo.getBookFee()+payDetailVo.getVideoFee();
        if(payDetailVo.getBookFee()>0){
            DictionaryModel mailFeeDModel = dictionaryService.queryByKey(Constants.MAIL_FEE_KEY);
            totalFee += (mailFeeDModel==null?0:Double.valueOf(mailFeeDModel.getValue()).doubleValue());
        }

        Map<String,String> map = new HashMap<>();
        map.put("userId",userId);
        String out_trade_no = Constants.SYSTEM_IDENTIFY
                + WeChatPayConstants.WECHAT_IDENTIFY
                + userId.substring(userId.length()-4,userId.length())
                + System.currentTimeMillis()
                + (long) (Math.random() * 10000000L);//"simisTradeWe20170106113324",
        synchronized (map){
            map.put(userId,out_trade_no);
        }
        //订单编号,用于请求微信支付端
        customerModel.setOrderNo(out_trade_no);
        customerService.update(customerModel);


        Map<String,String> requestMap = WXRequestUtil.SendPayment(out_trade_no,userId,feeEnum.getDescription(),totalFee,feeEnum.getProductId(),map);
        synchronized (unPayTradeMapList){
            unPayTradeMapList.add(map);
        }

        if(requestMap.containsKey(WeChatPayConstants.WECHAT_RESPONSE_CODE_URL_NAME)){
            String codeUrl = requestMap.get(WeChatPayConstants.WECHAT_RESPONSE_CODE_URL_NAME);
            LOGGER.info("########生成微信支付二维码,开始#######");
            try {
                fileName = QRCodeUtil.encode(codeUrl, null,path, true);
                LOGGER.info("########qcode的名称为:{}",fileName);
            } catch (Exception e) {
                LOGGER.error("生成微信支付二维码异常",e);
            }
            LOGGER.info("########生成微信支付二维码,结束#######");
        }
        resultMsg.setSuccess(true);
        resultMsg.setMsg("resource/qcode/"+fileName);
        return resultMsg;
    }


    /**
     * @Title: weixinCallBack
     * @Description: 微信支付的回调
     * @return ResultMsg
     * @throws
     */
    @RequestMapping(value = "/weixinCallBack")
    public void weixinCallBack(HttpServletRequest request, HttpServletResponse response){
        LOGGER.info("########微信回调,开始#######");
//        boolean result = WXRequestUtil.notifyResult(request,response);
//        if(!result){
//            LOGGER.info("########微信回调,支付失败,结束#######");
//            return "/pay/payFail";
//        }
//        LOGGER.info("########微信回调,支付成功,更改用户支付状态以及发送邮件,开始#######");
//        String userId = sessionMap.get("customerId");
//        LOGGER.info("########客户id为"+userId+"#######");
//        CustomerModel customerModel = customerService.getCustomer(userId);
//        //更新支付状态为已缴费
//        customerModel.setIsAlreadyPaid(Constants.ALEADY_PAID);
//        customerModel.setPayDate(DateTimeUtil.getDate2String(null,new Date()));
//        customerModel.setModifyTime(new Date());
//        customerService.update(customerModel);
//        LOGGER.info("########微信回调,更改用户支付状态,正常结束#######");
//        String emailTemplateUrl = request.getSession().getServletContext()
//                .getRealPath(Constants.EMAIL_TEMPLATE_PATH);
//        //成功之后,发送交费成功邮件
//        LOGGER.info("########发送邮件,开始...#######");
//        EmailUtil.sendMail(customerModel.getEmail(),emailTemplateUrl,combinEmailParaVo(customerModel));
//        LOGGER.info("########发送邮件,正常结束#######");
        LOGGER.info("########微信回调,结束#######");
//        return "/pay/paySuccess";
    }


    private EmailParaVo combinEmailParaVo(CustomerModel customerModel){
        EmailParaVo paraVo = new EmailParaVo();
        paraVo.setCustomerName(customerModel.getName());
        paraVo.setExamTime(DateTimeUtil.converToChineseTimeStyle(customerModel.getExamTime()));
        paraVo.setSystemName(Constants.SYSTEM_NAME);
        paraVo.setExamName(Constants.EXAM_NAME);
        if (customerModel.getVideoFee()!=null && customerModel.getVideoFee().compareTo(BigDecimal.ZERO) > 0){
            DictionaryModel videoAdressDModel = dictionaryService.queryByKey(Constants.VIDEO_ADDRESS_KEY);
            DictionaryModel videoCodeDModel = dictionaryService.queryByKey(Constants.VIDEO_CODE_KEY);
            paraVo.setVideoAddress(videoAdressDModel.getValue());
            paraVo.setVideoCode(videoCodeDModel.getValue());
        }

        DictionaryModel mailUrlDModel = dictionaryService.queryByKey(Constants.EMAIL_ADDRESS_KEY);
        String sendEmailMother = mailUrlDModel.getValue()==null?Constants.SEND_EMAIL_MOTHER:mailUrlDModel.getValue();
        DictionaryModel mailCodeDModel = dictionaryService.queryByKey(Constants.EMAIL_CODE_KEY);
        String sendEmailMotherPassword = mailCodeDModel.getValue()==null?Constants.SEND_EMAIL_MOTHER_PASSWORD:mailCodeDModel.getValue();
        DictionaryModel mailHostDModel = dictionaryService.queryByKey(Constants.EMAIL_HOST_KEY);
        String sendEmailMotherHost = mailHostDModel.getValue()==null?Constants.SEND_EMAIL_SMTP_HOST:mailHostDModel.getValue();

        paraVo.setEmailMother(sendEmailMother);
        paraVo.setEmailMotherPassword(sendEmailMotherPassword);
        paraVo.setEmailMotherHost(sendEmailMotherHost);
        return paraVo;
    }

    private FeeEnum getFeeEnum(PayDetailVo payDetailVo){
        double examFee = payDetailVo.getExamFee();
        double bookFee = payDetailVo.getBookFee();
        double videoFee = payDetailVo.getVideoFee();
        FeeEnum feeEnum = null;
        //全部费用
        if(examFee > 0 && bookFee > 0 && videoFee > 0){
            feeEnum = FeeEnum.EXAM_FEE_AND_BOOK_FEE_AND_VIDEO_FEE;
        }
        //考试费+材料费
        if(examFee > 0 && bookFee > 0 && videoFee == 0){
            feeEnum = FeeEnum.EXAM_FEE_AND_BOOK_FEE;
        }
        //考试费+材料费
        if(examFee > 0 && bookFee == 0 && videoFee > 0){
            feeEnum = FeeEnum.EXAM_FEE_AND_VIDEO_FEE;
        }
        //考试费
        if(examFee > 0 && bookFee == 0 && videoFee == 0){
            feeEnum = FeeEnum.EXAM_FEE_ONLY;
        }
        LOGGER.info("#####费用类型为{}",feeEnum.getDescription());
        return feeEnum;
    }


    /**
     * @Title: queryOrderStatus
     * @Description: 查询订单支付状态
     * @return ResultMsg
     * @throws
     */
    @ResponseBody
    @RequestMapping(value = "/queryWechatOrderStatus")
    public ResultMsg queryWechatOrderStatus(HttpServletRequest request,HttpSession httpSession){
        ResultMsg resultMsg = new ResultMsg();
        LOGGER.info("####查询订单状态,开始....");
        //订单号
        String outTradeNo = null;
        String userId = null;
        synchronized (unPayTradeMapList){
            for(int i=0;i<unPayTradeMapList.size();i++){
                Map<String,String> map = unPayTradeMapList.get(i);
                for(String key : map.keySet()){
                    outTradeNo = map.get(key);
                    userId = key;
                    if(userId.equals(httpSession.getAttribute("userId").toString())){
                        resultMsg = doQueryTradeStatus(outTradeNo,userId,resultMsg,request);
                        break;
                    }
                }
            }
        }
        return resultMsg;
    }


    private ResultMsg doQueryTradeStatus(String outTradeNo,String userId,ResultMsg resultMsg,HttpServletRequest request){
        if(StringUtils.isEmpty(outTradeNo)){
            LOGGER.info("####订单号为空....");
            resultMsg.setSuccess(false);
            resultMsg.setMsg("订单号为空");
            resultMsg.setRemark("订单号为空");
            return resultMsg;
        }
        LOGGER.info("userId:"+userId);
        if(StringUtils.isEmpty(userId)){
            LOGGER.info("####没有用户登录....");
            resultMsg.setSuccess(false);
            resultMsg.setMsg("没有用户登录");
            resultMsg.setRemark("没有用户登录");
            return resultMsg;
        }
        CustomerModel customerModel = customerService.getCustomer(userId);
        if(StringUtils.isEmpty(customerModel)){
            LOGGER.info("####未查询到用户,cardNo为{}",userId);
            resultMsg.setSuccess(false);
            resultMsg.setMsg("未查询到用户,cardNo为"+userId);
            resultMsg.setRemark("未查询到用户,cardNo为"+userId);
            return resultMsg;
        }
        Map<String,String> requestMap = WXRequestUtil.queryPayOrder(outTradeNo);
        if(requestMap.containsKey(WeChatPayConstants.WECHAT_ORDER_QUERY_TRADE_STATUS)){
            String tradeStatus = requestMap.get(WeChatPayConstants.WECHAT_ORDER_QUERY_TRADE_STATUS);
            WechatOrderStatusEnum statusEnum = WechatOrderStatusEnum.createWechatOrderStatusEnum(tradeStatus);
            LOGGER.info("########订单支付结果为{}",statusEnum.getDescription());
            switch (statusEnum){
                case SUCCESS:{//支付成功
                    if(requestMap.containsKey(WeChatPayConstants.WECHAT_ORDER_QUERY_TOTAL_FEE)){
                        LOGGER.info("########更新支付成功的订单,对应的客户的总交费金额,开始...");
                        String totalFeeStr = requestMap.get(WeChatPayConstants.WECHAT_ORDER_QUERY_TOTAL_FEE);
                        Double totalFee = Double.valueOf(totalFeeStr);
                        double result = totalFee/100;
                        LOGGER.info("########总交费金额为{}",result);
                        customerModel.setIsAlreadyPaid(Constants.ALEADY_PAID);
                        customerModel.setPayDate(DateTimeUtil.getDate2String(null,new Date()));
                        customerModel.setOrderNo(outTradeNo);
                        customerModel.setTotalFee(BigDecimal.valueOf(result));
                        customerModel.setModifyTime(new Date());
                        customerModel.setOperator(Constants.OPERATOR);
                        customerService.update(customerModel);
                        LOGGER.info("########更新支付成功的订单,对应的客户的总交费金额,正常结束");
                        LOGGER.info("########更新考试批次内的总交费人数,开始...");
                        examBatchService.updatePaidPepNum(customerModel.getExamTime());
                        LOGGER.info("########更新考试批次内的总交费人数,结束");
                        if(Constants.NO.equals(customerModel.getIsAlreadyEmail())){
                            String emailTemplateUrl = request.getSession().getServletContext()
                                    .getRealPath(Constants.EMAIL_TEMPLATE_PATH);
                            //成功之后,发送交费成功邮件
                            LOGGER.info("########发送邮件,开始...#######");
                            EmailUtil.sendMail(customerModel.getEmail(),emailTemplateUrl,combinEmailParaVo(customerModel));
                            LOGGER.info("########发送邮件,正常结束#######");
                            customerModel.setIsAlreadyEmail(Constants.YES);
                            customerModel.setModifyTime(new Date());
                            customerModel.setOperator(Constants.OPERATOR);
                            customerService.update(customerModel);
                            LOGGER.info("########更新客户是否发送邮件标示,正常结束#######");
                        }
                    }
                    resultMsg.setSuccess(true);
                    resultMsg.setMsg("支付成功");
                    resultMsg.setRemark(Constants.YES);
                    synchronized (unPayTradeMapList){
                        for(int i=0;i<unPayTradeMapList.size();i++){
                            Map<String,String> map = unPayTradeMapList.get(i);
                            if(map.containsKey(userId)){
                                unPayTradeMapList.remove(map);
                            }
                        }
                    }
                    break;
                }
                case PAYERROR:{//支付失败
                    resultMsg.setSuccess(false);
                    resultMsg.setMsg("支付失败");
                    resultMsg.setRemark(Constants.NO);
                    break;
                }
                default:{}
            }
            LOGGER.info("########生成微信支付二维码,结束#######");
        }
        LOGGER.info("####查询订单状态,正常结束....");
        return resultMsg;
    }



    @ResponseBody
    @RequestMapping("/showFeeInfo")
    public FeeInfoVo showFeeInfo(){
        FeeInfoVo dictionaryVo = new FeeInfoVo();
        try {
            DictionaryModel examFeeDModel = dictionaryService.queryByKey(Constants.EXAM_FEE_KEY);
            DictionaryModel bookFeeDModel = dictionaryService.queryByKey(Constants.BOOK_FEE_KEY);
            DictionaryModel videoFeeDModel = dictionaryService.queryByKey(Constants.VIDEO_FEE_KEY);
            DictionaryModel mailFeeDModel = dictionaryService.queryByKey(Constants.MAIL_FEE_KEY);


            if(examFeeDModel!=null && !StringUtils.isEmpty(examFeeDModel.getValue())){
                dictionaryVo.setExamFee(examFeeDModel.getValue());
            }
            if(bookFeeDModel!=null && !StringUtils.isEmpty(bookFeeDModel.getValue())){
                dictionaryVo.setBookFee(bookFeeDModel.getValue());
            }
            if(videoFeeDModel!=null && !StringUtils.isEmpty(videoFeeDModel.getValue())){
                dictionaryVo.setVideoFee(videoFeeDModel.getValue());
            }
            if(mailFeeDModel!=null && !StringUtils.isEmpty(mailFeeDModel.getValue())){
                dictionaryVo.setMailFee(mailFeeDModel.getValue());
            }
        } catch (Exception e) {
            LOGGER.error("查询费用信息异常",e);
        }
        return dictionaryVo;
    }
}
