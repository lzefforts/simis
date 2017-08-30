package com.simis.util;


import com.simis.poi.vo.ExcelReportColumnModel;
import com.simis.poi.vo.ExcelReportModel;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.List;

/**
 *
 * Created by ArnoldLee on 17/5/20.
 */
public class XmlUtil {

    /**
     * 控制台输出用的log
     */
    protected static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(XmlUtil.class);

    //测试xml地址
    private static String testEmailXml = "/Users/ArnoldLee/Downloads/intellj-workspace/simis/src/main/webapp/WEB-INF/email/email.xml";
    /**
     * 从读取XML文件
     *
     * @param fileName
     * @return Document对象
     */
    public static Document read(String fileName) {
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new File(fileName));
        } catch (DocumentException e) {
            LOGGER.error("read方法异常",e);
        }
        return document;
    }

    /**
     * 解析xml文件,返回一个字符串
     *  @param fileName
     *  @param rootPath
     *  @param childPath
     */
    public static String getXmlContent(String fileName,String rootPath,String childPath){
        Document document = read(fileName);
        String content = parse(document,rootPath,childPath);
        return content;
    }

    /**
     * 解析xml文件,返回一个字符串
     *  @param document
     *  @param rootPath
     *  @param childPath
     */
    private static String parse(Document document,String rootPath,String childPath){
        LOGGER.info("###解析xml文件开始...");
        Element rootElement = (Element) document.selectSingleNode(rootPath);
        List<Element> childrenEles = rootElement.selectNodes(childPath);
        StringBuilder content = new StringBuilder("");
        if (!childrenEles.isEmpty()) {
            parseExcelReportColumnTag(childrenEles,content);
        }
        LOGGER.info("###解析的xml文件为{}",content);
        LOGGER.info("###解析xml文件结束");
        return content.toString();
    }

    /**
     * 解析表头
     *
     * @param childrenEles
     */
    private static void parseExcelReportColumnTag(List<Element> childrenEles,StringBuilder content) {
        for (Element ele : childrenEles) {
            String text = ele.getText();
            content.append(text+"<br>");
        }
    }

    public static void main(String[] args) {
        Document document = read(testEmailXml);
        String rootPath = "email";
        String childPath = "content/videoLine";
        String content = parse(document,rootPath,childPath);
        System.out.println("########");
        System.out.println(content);

    }
}
