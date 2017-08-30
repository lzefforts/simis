package com.simis.poi.formula;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;

public abstract class BaseStatisticsFormula implements IStatisticsFormula {

    protected Format moneyFormat = new DecimalFormat("#,##0.00");

    @Override
    public abstract String calculate(List<Object> datas);

    protected String format(BigDecimal parameter) {
        return moneyFormat.format(parameter);
    }

}
