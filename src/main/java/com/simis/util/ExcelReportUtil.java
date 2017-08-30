package com.simis.util;

import com.simis.poi.formula.IStatisticsFormula;
import com.simis.poi.formula.ReportFormulaFactory;
import com.simis.poi.vo.ExcelReportColumnModel;
import com.simis.poi.vo.ExcelReportModel;
import com.simis.poi.vo.ExcelReportStatisticsModel;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Description: Excel报表工具类
 * 
 * @author dekunzhao
 * @version 1.0
 * 
 *          <pre>
 * Modification History: 
 * Date         Author      Version     Description 
 * ------------------------------------------------------------------ 
 * 2013-12-02  zhaokevin       1.0        1.0 Version
 * </pre>
 */
@Component
public class ExcelReportUtil {

    private static String DICT_SEPARATOR = "/";
    public static final int HEADER_ROW_INDEX = 0;
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_MONEY_FORMAT = "#,##0.00";
    public static final int DEFAULT_LIST_SIZE = 128;
    private static final Logger logger = LoggerFactory.getLogger(ExcelReportUtil.class);

    private static Map<String, String> cacheMap = new HashMap<String, String>(DEFAULT_LIST_SIZE);

    public static final int EXECL_PER_SHEET_DATA_NUMS = 60000;

    private static ExcelReportUtil excelReportUtil;

    public static HSSFWorkbook genExcelReport(File templateFile, List<? extends Object> datas) throws Exception {
        HSSFWorkbook workbook = null;
        logger.info("generate excel report [" + templateFile.getName() + "] begin ...");
        try {
            String templateContent = FileUtils.readFileToString(templateFile, "UTF-8");
            if (logger.isDebugEnabled()) {
                logger.debug("解析模版－－－－－－－－－－start");
                logger.debug(templateContent);
                logger.debug("解析模版－－－－－－－－－－end");
            }

            ExcelReportXMLHandler<ExcelReportModel> xmlHandler = new ExcelReportXMLHandler();
            List<ExcelReportModel> reports = xmlHandler.parse(templateFile);

            ExcelReportModel report = reports.get(0);

            workbook = ExcelUtil.createWorkBook();
            // workbook = new HSSFWorkbook();
            String fileName = report.getReportName();

            int datasSize = datas.size();
            int pagesNum = datasSize / EXECL_PER_SHEET_DATA_NUMS;
            if (datasSize % EXECL_PER_SHEET_DATA_NUMS != 0) {
                pagesNum++;
            }
            if (datasSize == 0) {
                pagesNum++;
            }
            for (int i = 0; i < pagesNum; i++) {
                String currentSheetName = fileName;
                if (pagesNum != 1) {
                    currentSheetName += "-" + (i + 1);
                }
                HSSFSheet sheet = ExcelUtil.createSheet(workbook, currentSheetName);
                // HSSFSheet sheet = workbook.createSheet(currentSheetName);
                List<ExcelReportColumnModel> columns = report.getReportColumns();
                int columnSize = columns.size();

                Map<String, List<Object>> statisticsMap = new HashMap<String, List<Object>>(columnSize);
                HSSFCellStyle headerCellStyle = workbook.createCellStyle();
                headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                // 设置表头
                drawReportHeader(sheet, columns, headerCellStyle);
                // 构建表格内容

                int endIndex = EXECL_PER_SHEET_DATA_NUMS * (i + 1);
                if (endIndex >= datasSize) {
                    endIndex = datasSize;
                }
                drawReportContent(sheet, columns, datas.subList(EXECL_PER_SHEET_DATA_NUMS * i, endIndex), statisticsMap);

                boolean isStatistics = report.isStatistics();
                if (isStatistics) {
                    // 构建统计行
                    try {
                        drawReportStatisticsRow(sheet, columns, statisticsMap);
                    } catch (Exception e) {
                        logger.error("报表统计错误，模版名称=" + templateFile.getName(), e);
                    }
                }
                int colNums = columns.size();
                ExcelUtil.autoSizeColumn(sheet, 0, colNums);
            }
        } finally {
            ExcelReportUtil.cacheMap.clear();
        }

        logger.info("generate excel report [" + templateFile.getName() + "] end ...");
        return workbook;
    }

    /**
     * Description:构建统计行
     * 
     * @author zhaokevin
     * @param sheet
     * @param columns
     */
    private static void drawReportStatisticsRow(HSSFSheet sheet, List<ExcelReportColumnModel> columns,
            Map<String, List<Object>> statisticsMap) {
        logger.info("draw excel report statistics column begin ...");
        int lastRowNum = sheet.getLastRowNum();
        HSSFRow row = ExcelUtil.row(sheet, lastRowNum + 1);
        // HSSFRow row = sheet.createRow(lastRowNum + 1);
        for (ExcelReportColumnModel column : columns) {
            boolean isStatistics = column.isStatistics();
            if (!isStatistics) {
                continue;
            }
            ExcelReportStatisticsModel statisticsModel = null;
            statisticsModel = column.getStatisticsModel();
            String title = statisticsModel.getTitle();
            ExcelReportStatisticsModel.ExcelReportFormulaModel formulaModel = null;
            formulaModel = statisticsModel.getFormulaModel();
            String formula = formulaModel.getValue();
            int columnIndex = column.getColumnIndex();
            String columnId = column.getColumnId();
            List<Object> calcDataList = statisticsMap.get(columnId);
            String statisticsResult = computeByFormula(formula, calcDataList);
            HSSFCell cell = row.createCell(columnIndex);
            cell.setCellValue(title + "：" + statisticsResult);
        }
        logger.info("draw excel report statistics column end ...");
    }

    private static String computeByFormula(String formula, List<Object> calcDataList) {
        IStatisticsFormula formulaImpl = ReportFormulaFactory.getInstance().getCalcFormula(formula);
        return formulaImpl.calculate(calcDataList);
    }

    /*
     * public static HSSFWorkbook genExcelReport(String templateFilePath, List<?
     * extends Object> datas) throws Exception { File templateFile =
     * ResourceUtils.getFile("classpath:" + PhenixConst.REPORT_CONFIG_DICT +
     * templateFilePath); return genExcelReport(templateFile, datas); }
     */

    /**
     * 构建报表内容
     * 
     * @author zhaokevin
     * @param sheet
     *            当前数据所在Sheet
     * @param columns
     *            报表表头实体
     * @param datas
     *            数据
     * @throws Exception
     */
    private static void drawReportContent(HSSFSheet sheet, List<ExcelReportColumnModel> columns,
            List<? extends Object> datas, Map<String, List<Object>> statisticsMap) throws Exception {
        logger.info("draw excel report content begin ...");
        int length = datas.size();
        for (int index = 1; index <= length; index++) {
            HSSFRow row = ExcelUtil.row(sheet, index);
            // HSSFRow row = sheet.createRow(index);
            Object data = datas.get(index - 1);
            drawRowData(row, columns, data, statisticsMap);
        }
        logger.info("draw excel report content end ...");
    }

    /**
     * 构建表头
     * 
     * @author zhaokevin
     * @param sheet
     *            当前数据所在Sheet
     * @param columns
     *            报表表头实体
     * @param headerCellStyle
     *            表头样式
     */
    private static void drawReportHeader(HSSFSheet sheet, List<ExcelReportColumnModel> columns,
            CellStyle headerCellStyle) {
        logger.info("draw excel report header begin ...");
        HSSFRow headerRow = ExcelUtil.row(sheet, HEADER_ROW_INDEX);
        // HSSFRow headerRow = sheet.createRow(HEADER_ROW_INDEX);
        for (int index = 0; index < columns.size(); index++) {
            ExcelReportColumnModel column = columns.get(index);
            String name = column.getName();
            /*
             * HSSFCell cell = headerRow.createCell(index);
             * cell.setCellValue(name); cell.setCellStyle(headerCellStyle);
             */
            HSSFCell cell = ExcelUtil.cell(headerRow, index, name, headerCellStyle);
            if (column.isStatistics()) {
                int rowIndex = cell.getRowIndex();
                int columnIndex = cell.getColumnIndex();
                column.setRowIndex(rowIndex);
                column.setColumnIndex(columnIndex);
            }
        }
        logger.info("draw excel report header end ...");
    }

    /**
     * 构建行数据
     * 
     * @author zhaokevin
     * @param row
     *            Excel行对象
     * @param columns
     *            定义列模型
     * @param data
     *            行数据
     * @throws Exception
     */
    private static void drawRowData(HSSFRow row, List<ExcelReportColumnModel> columns, Object data,
            Map<String, List<Object>> statisticsMap) throws Exception {

        ExcelReportColumnModel reportColumnModel = null;
        int size = columns.size();
        for (int index = 0; index < size; index++) {
            reportColumnModel = columns.get(index);
            String dataType = reportColumnModel.getDataType();
            String format = reportColumnModel.getFormat();
            String mapping = reportColumnModel.getMapping();
            String dictpath = reportColumnModel.getDictpath();
            String start = reportColumnModel.getStart();
            Object value = null;
            Object formatValue = null;
            if ((!StringUtils.isEmpty(dataType)) && dataType.equalsIgnoreCase("sequence")) {
                if (StringUtils.isEmpty(start)) {
                    int rowNum = row.getRowNum();
                    reportColumnModel.setStart(String.valueOf(rowNum));
                }
                formatValue = reportColumnModel.getNextSequenceValue();
            } else {
                value = genValueFromData(data, mapping, index);
                if (value == null) {
                    continue;
                }
                if (!StringUtils.isEmpty(dictpath)) {
                    Object dictValue = null;
                    dictValue = attributeHandler(dictpath, value);
                    if (null == dictValue) {
                        logger.info("dictpath=[" + dictpath + "] not exists !!!value=" + value);
                    } else {
                        value = dictValue;
                    }
                }
                // if (value == null) {
                // throw new RuntimeException("dictpath=[" + dictpath
                // + "] not exists !!!");
                // }
                // formatValue = dealValue(dataType, format, value);
                formatValue = formatValuedealValue(dataType, format, value);
            }
            ExcelUtil.cell(row, index, formatValue);
            // HSSFCell cell = row.createCell(index);
            // cell.setCellValue(formatValue + "");
            // cell.setcellty
            // cellValue(row, index, formatValue);
            boolean iStatistics = reportColumnModel.isStatistics();
            if (iStatistics) {
                String columnId = reportColumnModel.getColumnId();
                List<Object> calcDataList = statisticsMap.get(columnId);
                if (null == calcDataList) {
                    calcDataList = new ArrayList<Object>(DEFAULT_LIST_SIZE);
                    statisticsMap.put(columnId, calcDataList);
                }
                calcDataList.add(value);
            }
        }
    }

    /**
     * Description:执行标签属性处理
     * 
     * @author zhaokevin
     * @param dictpath
     *            字典key
     * @param value
     *            数据操作值
     * @return String
     */
    private static String attributeHandler(String dictpath, Object value) {
        String tmpDictPath = dictpath + DICT_SEPARATOR + String.valueOf(value);
        String result = mappingDictValue(tmpDictPath);
        return result;
    }

    /**
     * Description:字典值转换
     * 
     * @author zhaokevin
     * @param dictpath
     *            字典key
     * @return String
     */
    private static String mappingDictValue(String dictpath) {

        String result = getCacheValue(dictpath);
        if (StringUtils.isEmpty(result)) {
            //result = getDictValue(dictpath);
        }
        return result;
    }

    /**
     * Description:从缓存中取值
     * 
     * @author zhaokevin
     * @param dictpath
     *            字典key
     * @return String
     */
    private static String getCacheValue(String dictpath) {
        String result = ExcelReportUtil.cacheMap.get(dictpath);
        return result;
    }

    /**
     * 根据mapping映射属性从data实体中获取value
     * 
     * @author zhaokevin
     * @param data
     *            数据实体，可以为Map，也可为Bean
     * @param mapping
     *            映射字段值
     * @return 数据实体对应mapping字段的值
     * @throws Exception
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object genValueFromData(Object data, String mapping, int index) throws Exception {
        Object value = null;
        Class clazz = data.getClass();
        try {
            if (clazz.isAssignableFrom(Map.class) || clazz.isAssignableFrom(HashMap.class)) {
                value = ((Map) data).get(mapping);
            } else {
                // value = PropertyUtils.getProperty(data, mapping);
                value = ((Object[]) data)[index];
            }
        } catch (Exception e) {
            logger.error("执行出错", e);
        }
        return value;
    }

    /**
     * 数据转换，当指定dataType与format后，将原始值转换成format之后得结果
     * 
     * @author zhaokevin
     * @param dataType
     *            数据类型，默认为string,可设置成date，bigdecimal
     * @param format
     *            当dataType指定成date｜bigdecimal时，需要提供数据格式
     * @param value
     *            数据原始值
     * @return 转换后得数据
     */
    private static Object formatValuedealValue(String dataType, String format, Object value) {
        if (logger.isDebugEnabled()) {
            logger.info("转换数据值=" + value);
        }
        Object result = value;
        Format d = null;
        if (dataType != null) {
            if (dataType.toLowerCase().equals("date")) {
                if (StringUtils.isEmpty(format)) {
                    format = DEFAULT_DATE_FORMAT;
                }
                d = new SimpleDateFormat(format);
            } else if (dataType.toLowerCase().equals("bigdecimal")) {
                if (StringUtils.isEmpty(format)) {
                    format = DEFAULT_MONEY_FORMAT;
                }
                d = new DecimalFormat(format);
            }
            if (d != null) {
                result = d.format(value);
            }
        }
        if (result instanceof BigDecimal) {
            String str = result.toString();
            result = new Double(str);
        }
        if (result instanceof Character) {
            result = result.toString();
        }
        if (logger.isDebugEnabled()) {
            logger.info("转换数据结果值=" + result);
        }
        return result;
    }

    private static void cellValue(HSSFRow row, int index, Object cellValue) {
        HSSFCell cell = row.createCell(index);
        if (cellValue instanceof BigDecimal) {
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(((BigDecimal) cellValue).intValue());
        } else if (cellValue instanceof Float) {
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(((Float) cellValue).intValue());
        } else {
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(cellValue.toString());
        }
    }

}
