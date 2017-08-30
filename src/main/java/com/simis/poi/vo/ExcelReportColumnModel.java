package com.simis.poi.vo;


import com.simis.util.CoreUtil;

public class ExcelReportColumnModel {

    /**
     * 控制台输出用的log
     */
    protected static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ExcelReportColumnModel.class);

    public static final String COLUMN_SEPARATOR = "_";

    public static final String COLUMN_PREFIX = "col" + COLUMN_SEPARATOR;

    private String name;
    private String dataType;
    private String format;
    private String mapping;
    private String dictpath;
    private String start;
    private String step;
    private ExcelReportStatisticsModel statisticsModel;

    private boolean isStatistics = false;
    private int rowIndex = 0;
    private int columnIndex = 0;
    private String columnId;
    private int stepValue;
    private int currentValue;
    private boolean iSeqInit = false;

    public ExcelReportColumnModel() {
        this.genColumnId();
    }

    /**
     * @return the mapping
     */
    public String getMapping() {
        return mapping;
    }

    /**
     * @param mapping
     *            the mapping to set
     */
    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * @param dataType
     *            the dataType to set
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format
     *            the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @return the dictpath
     */
    public String getDictpath() {
        return dictpath;
    }

    /**
     * @return the isStatistics
     */
    public boolean isStatistics() {
        return isStatistics;
    }

    /**
     * @param isStatistics
     *            the isStatistics to set
     */
    public void setStatistics(boolean isStatistics) {
        this.isStatistics = isStatistics;
    }

    /**
     * @param dictpath
     *            the dictpath to set
     */
    public void setDictpath(String dictpath) {
        this.dictpath = dictpath;
    }

    /**
     * @return the rowIndex
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /**
     * @param rowIndex
     *            the rowIndex to set
     */
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    /**
     * @return the columnIndex
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /**
     * @param columnIndex
     *            the columnIndex to set
     */
    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public void setStart(String start) {
        this.start = start;
        try {
            this.currentValue = Integer.parseInt(start);
        } catch (Exception e) {
            LOGGER.error("执行出错", e);
        }
    }

    public String getStart() {
        return this.start;
    }

    public void setStep(String step) {
        this.step = step;
        try {
            this.stepValue = Integer.parseInt(step);
        } catch (Exception e) {
            LOGGER.error("执行出错", e);
            stepValue = 1;
        }
    }

    public String getStep() {
        return this.step;
    }

    public int getCurrentValue() {
        return this.currentValue;
    }

    /**
     * @return the statisticsModel
     */
    public ExcelReportStatisticsModel getStatisticsModel() {
        return statisticsModel;
    }

    /**
     * @param statisticsModel
     *            the statisticsModel to set
     */
    public void setStatisticsModel(ExcelReportStatisticsModel statisticsModel) {
        this.statisticsModel = statisticsModel;
    }

    public String getColumnId() {
        return columnId;
    }

    public int getNextSequenceValue() {
        int nextValue = 0;

        if (!"sequence".equals(this.getDataType())) {
            throw new RuntimeException("invoke error,datatype is not sequence!");
        }
        if (!iSeqInit) {
            this.currentValue = this.currentValue - this.stepValue;
            iSeqInit = true;
        }
        nextValue = this.currentValue + this.stepValue;
        this.currentValue = nextValue;
        return nextValue;
    }

    private void genColumnId() {
        String randomStr = CoreUtil.genUUIDString().replaceAll("-", "");
        this.columnId = randomStr;
    }

}
