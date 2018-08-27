package com.simis.poi;


import com.simis.basedao.vo.QueryCondition;

import java.util.Map;

public interface TradeMatchExportService {

    QueryCondition getMatchSql(Map<String, Object> conditions, String sqlName,String exportType,String exportTime);

    Map<Object, String> getFile(String sqlName);
}
