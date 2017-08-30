package com.simis.basedao;


import com.simis.basedao.vo.QueryCondition;
import com.simis.common.ExportTypeEnum;
import com.simis.common.TradeExportExcelEnum;

import java.util.Map;

/**
 * 导出专用方法
 * Created by 一拳超人 on 17/3/14.
 */
public interface ExportDao{

    /**
     * 根据条件导出流水
     * @param conditions
     * @param exportEnum
     * @return
     */
    QueryCondition exportExcel(Map<String, Object> conditions, TradeExportExcelEnum exportEnum, ExportTypeEnum exportTypeEnum,String exportTime);

}
