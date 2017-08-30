package com.simis.poi.formula;


import com.simis.exception.ApplicationException;
import com.simis.util.Arith;

import java.math.BigDecimal;
import java.util.List;

/**
 * 求和
 * 
 * @author zhaokevin
 *
 */
public class SumFormulaResolveImpl extends BaseStatisticsFormula {

    @Override
    public String calculate(List<Object> datas) {

        BigDecimal resultValue = BigDecimal.ZERO;
        for (Object obj : datas) {
            BigDecimal tmpValue = BigDecimal.ZERO;
            if (obj.getClass().equals(BigDecimal.class)) {
                tmpValue = (BigDecimal) obj;
            } else if (obj.getClass().equals(Double.class) || obj.getClass().equals(Integer.class)) {
                tmpValue = new BigDecimal(obj.toString());
            } else {
                throw new ApplicationException("求和数据不合法，数据类型为" + obj.getClass().getName());
            }
            resultValue = Arith.add(resultValue, tmpValue);
        }
        return format(resultValue);
    }

}
