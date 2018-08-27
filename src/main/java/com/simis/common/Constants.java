package com.simis.common;

import java.math.BigDecimal;

/**
 * Created by 一拳超人
 */
public class Constants {

    //系统标示
    public static final String SYSTEM_IDENTIFY = "SIMIS";

    //操作者
    public final static String OPERATOR = "system";

    //未交费
    public final static String NOT_PAID = "0";

    //已交费
    public final static String ALEADY_PAID = "1";

    //导出excel模板所在位置
    public final static String EXPORT_TEMPLATE_PATH = "/WEB-INF/template/";

    //邮件模板所在位置
    public final static String EMAIL_TEMPLATE_PATH = "/WEB-INF/email/email.xml";

    //性别-男
    public final static String SEX_MAN = "0";

    //性别-女
    public final static String SEX_WOMAN = "1";

    //人数限制为260人
    public final static int PEOPLE_LIMIT=260;

    //否
    public final static String NO = "0";

    //是
    public final static String YES = "1";

    //定义考试费用为50
    public final static double examFee = 50;

    //系统标示
    public static final String END_DAYS = "2017-06-21";

    //系统标示
    public static final String END_DAYS_SYMBOL = "END_DAY";

    //发送邮件的母邮件
    public static final String SEND_EMAIL_MOTHER = "diantaipeixun@vip.sina.com";//"simiscn@sina.com";

    //发送邮件的母邮件,密码
    public static final String SEND_EMAIL_MOTHER_PASSWORD = "rbc@pxzx65674445";//"!QAZ2wsx";

    //发送邮件的HOST
    public static final String SEND_EMAIL_SMTP_HOST = "smtp.vip.sina.com";//"smtp.sina.com";

    //系统名称
    public static final String SYSTEM_NAME = "北京人民广播电台语言文字测试中心";

    //系统名称
    public static final String EXAM_NAME = "普通话测试";

    //邮寄费用的字典key
    public static final String MAIL_FEE_KEY = "MAIL_FEE";

    //考试费用的字典key
    public static final String EXAM_FEE_KEY = "EXAM_FEE";

    //材料费用的字典key
    public static final String BOOK_FEE_KEY = "BOOK_FEE";

    //视频费用的字典key
    public static final String VIDEO_FEE_KEY = "VIDEO_FEE";

    //视频地址的字典key
    public static final String VIDEO_ADDRESS_KEY = "VIDEO_URL";

    //视频密码的字典key
    public static final String VIDEO_CODE_KEY = "VIDEO_CODE";

    //第一次考试时间
    public static final String FIRST_EXAM_TYPE = "0";

    //第二次考试时间
    public static final String SECOND_EXAM_TYPE = "1";

    //数据状态,有效,为0
    public final static String DATA_INVALID_STATUS = "0";

    //数据状态,有效,为1
    public final static String DATA_VALID_STATUS= "1";

    //发送邮件地址的字典key
    public static final String EMAIL_ADDRESS_KEY = "EMAIL_URL";

    //发送邮件密码的字典key
    public static final String EMAIL_CODE_KEY = "EMAIL_CODE";

    //发送邮件密码的字典key
    public static final String EMAIL_HOST_KEY = "EMAIL_HOST";

    //test
    public static final String TEST = "test";
}
