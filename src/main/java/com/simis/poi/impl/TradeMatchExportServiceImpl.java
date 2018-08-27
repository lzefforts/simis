package com.simis.poi.impl;

import com.simis.basedao.ExportDao;
import com.simis.basedao.vo.QueryCondition;
import com.simis.common.ExportTypeEnum;
import com.simis.common.TradeExportExcelEnum;
import com.simis.poi.TradeMatchExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("tradeMatchExportService")
public class TradeMatchExportServiceImpl implements TradeMatchExportService {
    /**
     * 获取上下文
     */
    @Autowired
    private ApplicationContext ctx;


    @Override
    public QueryCondition getMatchSql(Map<String, Object> conditions, String sqlName,String exportType,String exportTime) {
        TradeExportExcelEnum excelEnum = TradeExportExcelEnum.createTradeExportExcelEnum(sqlName);
        ExportTypeEnum exportTypeEnum = ExportTypeEnum.createExportTypeEnum(exportType);
        String beanName = excelEnum.getBeanName();
        //导出的dao的接口需要继承ExportDao,dao的实现类实现exportExcel方法
        ExportDao exportDao = (ExportDao)ctx.getBean(beanName);
        QueryCondition sql = exportDao.exportExcel(conditions,excelEnum,exportTypeEnum,exportTime);
        return sql;
    }

    @Override
    public Map<Object, String> getFile(String sqlName) {
        Map<Object, String> map = new HashMap<Object, String>();
        TradeExportExcelEnum excelEnum = TradeExportExcelEnum.createTradeExportExcelEnum(sqlName);
        String name = excelEnum.getName();
        String xmlName = excelEnum.getXmlName();
        map.put("fileName", name);
        map.put("xmlName", xmlName);
        return map;
    }

}
