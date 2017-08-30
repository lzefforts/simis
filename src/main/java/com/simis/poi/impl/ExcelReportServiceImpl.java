package com.simis.poi.impl;

import com.simis.basedao.TradeBaseDao;
import com.simis.poi.ExcelReportService;
import com.simis.util.ExcelReportUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by Soda on 2016/11/17.
 */
@Service("excelReportService")
public class ExcelReportServiceImpl implements ExcelReportService {

    /**
     * 控制台输出用的log
     */
    protected static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ExcelReportServiceImpl.class);

    @Autowired
    private TradeBaseDao tradeBaseDao;

    @Override
    public InputStream getExcelInputStream(String sql, Map<String, Object> params, File templateFile) {
        List<Map<String, Object>> mapList = tradeBaseDao.querySqlForListMap(sql, params);
        return getExcelInputStream(mapList, templateFile);
    }

    @Override
    public InputStream getExcelInputStream(List<Map<String, Object>> mapList, File templateFile) {
        try {
            HSSFWorkbook workbook = ExcelReportUtil.genExcelReport(templateFile, mapList);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.flush();
            workbook.write(baos);
            byte[] ba = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(ba);
            return bais;
        } catch (Exception e) {
            LOGGER.error("执行出错", e);
        }
        return null;
    }
}
