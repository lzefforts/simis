package com.simis.util;

import com.simis.common.Constants;
import com.simis.model.DictionaryModel;
import com.simis.service.DictionaryService;
import com.simis.service.impl.DictionaryServiceImpl;
import com.simis.vo.EmailParaVo;
import org.springframework.util.StringUtils;

/**
 * 拼凑email内容
 * Created by ArnoldLee on 17/5/20.
 */
public class EmailUtil {
    /**
     * 控制台输出用的log
     */
    protected static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(XmlUtil.class);

    //测试xml地址
    private static String testEmailXml = "/Users/ArnoldLee/Downloads/intellj-workspace/simis/src/main/webapp/WEB-INF/email/email.xml";

    private static String DEFAULT_CODE = "UTF-8";

    //XML中代表客户姓名的标示符,用于替换成真正的值
    private static String CUSTOMER_NAME_TAG_XML = "{customerName}";

    //XML中代表考试时间的标示符,用于替换成真正的值
    private static String EXAM_TIME_TAG_XML = "{examTime}";

    //XML中代表系统名称的标示符,用于替换成真正的值
    private static String SYSTEM_NAME_TAG_XML = "{systemName}";

    //XML中代表考试名称的标示符,用于替换成真正的值
    private static String EXAM_NAME_TAG_XML = "{examName}";

    //XML中代表视频地址的标示符,用于替换成真正的值
    private static String VIDEO_URL_TAG_XML = "{videoUrl}";

    //XML中代表视频密码的标示符,用于替换成真正的值
    private static String VIDEO_CODE_TAG_XML = "{videoCode}";

    private static String getEmailContent(String emailFileUrl, EmailParaVo paraVo){
        StringBuilder content = new StringBuilder("");
        String templateContent = XmlUtil.getXmlContent(emailFileUrl,"email","content/line");
        content.append(templateContent);
        if(!StringUtils.isEmpty(paraVo.getVideoAddress()) && !StringUtils.isEmpty(paraVo.getVideoCode())){
            String videoContent = XmlUtil.getXmlContent(emailFileUrl,"email","content/videoLine");
            LOGGER.info("videoContent:{}",videoContent);
            content.append(videoContent);
        }
        return combinContent(content.toString(),paraVo);
    }

    private static String combinContent(String templateContent,EmailParaVo paraVo){
        StringBuilder content = new StringBuilder("");
        int customerNameIndex = templateContent.indexOf(CUSTOMER_NAME_TAG_XML);
        int examTimeIndex = templateContent.indexOf(EXAM_TIME_TAG_XML);
        int systemNameIndex = templateContent.indexOf(SYSTEM_NAME_TAG_XML);
        int examNameIndex = templateContent.indexOf(EXAM_NAME_TAG_XML);

        content.append(templateContent.substring(0,customerNameIndex)).append(paraVo.getCustomerName())
               .append(templateContent.substring(customerNameIndex+CUSTOMER_NAME_TAG_XML.length(),examTimeIndex)).append(paraVo.getExamTime())
                .append(templateContent.substring(examTimeIndex+EXAM_TIME_TAG_XML.length(),systemNameIndex)).append(paraVo.getSystemName())
                .append(templateContent.substring(systemNameIndex+SYSTEM_NAME_TAG_XML.length(),examNameIndex)).append(paraVo.getExamName())
                .append(templateContent.substring(examNameIndex+EXAM_NAME_TAG_XML.length()));

        if(!StringUtils.isEmpty(paraVo.getVideoAddress()) && !StringUtils.isEmpty(paraVo.getVideoCode())){
            int videoUrlIndex = content.indexOf(VIDEO_URL_TAG_XML);
            content.replace(videoUrlIndex,videoUrlIndex+VIDEO_URL_TAG_XML.length(),paraVo.getVideoAddress());
            int videoCodeIndex = content.indexOf(VIDEO_CODE_TAG_XML);
            content.replace(videoCodeIndex,videoCodeIndex+VIDEO_CODE_TAG_XML.length(),paraVo.getVideoCode());
        }

        LOGGER.info("邮件内容为:{}",content);
        return content.toString();
    }

    public static void main(String[] args) {
        EmailParaVo paraVo = new EmailParaVo();
        paraVo.setCustomerName("李祯");
        paraVo.setExamTime("2017年06月15日");
        paraVo.setSystemName(Constants.SYSTEM_NAME);
        paraVo.setExamName(Constants.EXAM_NAME);
        paraVo.setVideoAddress("百度云");
        paraVo.setVideoCode("1234");

        String content = getEmailContent(testEmailXml,paraVo);
        System.out.println(content);

        Mail4sinaUtil mail = new Mail4sinaUtil();
        mail.send("lzefforts@126.com","李祯", content,
                Constants.SEND_EMAIL_MOTHER,Constants.SEND_EMAIL_MOTHER_PASSWORD,Constants.SEND_EMAIL_SMTP_HOST);
    }

    public static void sendMail(String toEmail,String emailFileUrl,EmailParaVo paraVo){
        Mail4sinaUtil mail = new Mail4sinaUtil();
        String content = getEmailContent(emailFileUrl,paraVo);
        LOGGER.info("########发送邮件给"+toEmail+",开始#######");
        try {
            mail.send(toEmail,paraVo.getCustomerName(), content.toString(),
                    paraVo.getEmailMother(),paraVo.getEmailMotherPassword(),paraVo.getEmailMotherHost());
        } catch (Exception e) {
            LOGGER.info("########发送邮件给"+toEmail+",异常结束#######");
            LOGGER.error("发送邮件异常",e);
        }
        LOGGER.info("########发送邮件给"+toEmail+",正常结束#######");
    }

}
