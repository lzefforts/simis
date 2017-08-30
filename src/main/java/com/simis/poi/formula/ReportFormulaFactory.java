package com.simis.poi.formula;

import com.simis.common.ExcelConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportFormulaFactory {

    private static ReportFormulaFactory instance = new ReportFormulaFactory();

    private static final Logger logger = LoggerFactory.getLogger(ReportFormulaFactory.class);

    private ReportFormulaFactory() {

    }

    public static ReportFormulaFactory getInstance() {
        return instance;
    }

    public IStatisticsFormula getCalcFormula(String formula) {

        IStatisticsFormula formulaImpl = null;
        if (ExcelConstants.SUM_FORMULA_OF_EXCEL.equals(formula)) {
            formulaImpl = new SumFormulaResolveImpl();
        }
        logger.info(" statistics formula [" + formulaImpl + "]");
        return formulaImpl;
    }

}
