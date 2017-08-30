package com.simis.poi.vo;

/**
 * Description:报表统计模型
 * 
 * @author zhaokevin
 * 
 */
public class ExcelReportStatisticsModel {

    /**
     * 统计列标题
     */
    private String title;
    /**
     * 报表公式
     */
    private ExcelReportFormulaModel formulaModel;

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the formulaModel
     */
    public ExcelReportFormulaModel getFormulaModel() {
        return formulaModel;
    }

    /**
     * @param formulaModel
     *            the formulaModel to set
     */
    public void setFormulaModel(ExcelReportFormulaModel formulaModel) {
        this.formulaModel = formulaModel;
    }

    /**
     * Description:报表统计模型
     * 
     */
    public class ExcelReportFormulaModel {
        private String value;

        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value
         *            the value to set
         */
        public void setValue(String value) {
            this.value = value;
        }

    }

}
