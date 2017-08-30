package com.simis.poi;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by Soda on 2016/11/17.
 */
public interface ExcelReportService {

    /**
     * 根据SQL和字段模板XML生成Excel
     * 
     * @param sql
     * @param templateFile
     * @return
     */
    InputStream getExcelInputStream(String sql, Map<String, Object> params, File templateFile);

    /**
     * 根据已经查询好的数据(List<Map>)和模板XML生成Excel
     * 
     * @param mapList
     * @param templateFile
     * @return
     */
    InputStream getExcelInputStream(List<Map<String, Object>> mapList, File templateFile);
}
