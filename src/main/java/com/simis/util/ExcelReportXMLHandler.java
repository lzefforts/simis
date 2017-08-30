package com.simis.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.simis.exception.ApplicationException;
import com.simis.poi.vo.ExcelReportColumnModel;
import com.simis.poi.vo.ExcelReportModel;
import com.simis.poi.vo.ExcelReportStatisticsModel;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.StringUtils;

public class ExcelReportXMLHandler<T> {

    public static final String ROOT_PATH = "/report";
    public static final String REPORT_COLUMN = "report-header/column";

    @SuppressWarnings("unchecked")
    public List<ExcelReportModel> parse(File templateFile) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(templateFile);
        Element rootElement = (Element) document.selectSingleNode(ROOT_PATH);
        String fileName = rootElement.attributeValue("name");
        ExcelReportModel excelReport = new ExcelReportModel();
        excelReport.setReportName(fileName);
        List<Element> childrenEles = rootElement.selectNodes(REPORT_COLUMN);
        if (!childrenEles.isEmpty()) {
            parseExcelReportColumnTag(childrenEles, excelReport);
        }
        List<ExcelReportModel> reports = new ArrayList<ExcelReportModel>(1);
        reports.add(excelReport);
        return reports;
    }

    /**
     * 解析表头
     * 
     * @param childrenEles
     * @param excelReport
     */
    private void parseExcelReportColumnTag(List<Element> childrenEles, ExcelReportModel excelReport) {

        ExcelReportColumnModel column = null;
        List<ExcelReportColumnModel> columns = excelReport.getReportColumns();
        for (Element ele : childrenEles) {
            column = new ExcelReportColumnModel();
            String name = ele.attributeValue("name");
            String dataType = ele.attributeValue("datatype");
            String format = ele.attributeValue("format");
            String mapping = ele.attributeValue("mapping");
            String dictpath = ele.attributeValue("dictpath");
            String start = ele.attributeValue("start");
            String step = ele.attributeValue("step");
            start = (String) (start == null ? "0" : start);
            step = (String) (step == null ? "0" : step);

            parseExcelReportStatisticsTag(ele, column, excelReport);

            column.setName(name);
            column.setDataType(dataType);
            column.setFormat(format);
            column.setMapping(mapping);
            column.setDictpath(dictpath);
            column.setStart(start);
            column.setStep(step);
            columns.add(column);
        }
    }

    /**
     * 解析统计标签
     * 
     * @param ele
     *            xml元素
     * @param column
     *            ExcelReportColumnModel
     */
    private void parseExcelReportStatisticsTag(Element ele, ExcelReportColumnModel column, ExcelReportModel excelReport) {
        Element statisEle = ele.element("statistics");
        if (null != statisEle) {
            ExcelReportStatisticsModel statisticsModel = new ExcelReportStatisticsModel();
            column.setStatisticsModel(statisticsModel);
            column.setStatistics(true);
            excelReport.setStatistics(true);
            String title = statisEle.attributeValue("title");
            statisticsModel.setTitle(title);
            parseExcelReportFormulaTag(statisEle, statisticsModel);
        }

    }

    /**
     * 解析公式标签
     * 
     * @param statisEle
     *            xml元素
     * @param statisticsModel
     *            ExcelReportStatisticsModel
     */
    private void parseExcelReportFormulaTag(Element statisEle, ExcelReportStatisticsModel statisticsModel) {

        Element formulaEle = statisEle.element("formula");
        if (null == formulaEle) {
            throw new ApplicationException("模板未在<statistics>标签中配置<formula>");
        } else {
            ExcelReportStatisticsModel.ExcelReportFormulaModel formulaModel = null;
            formulaModel = statisticsModel.new ExcelReportFormulaModel();
            statisticsModel.setFormulaModel(formulaModel);
            String formula = formulaEle.attributeValue("value");
            if (StringUtils.isEmpty(formula)) {
                formula = formulaEle.getTextTrim();
            }
            formulaModel.setValue(formula);
        }

    }

}
