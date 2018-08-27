package com.simis.controller;

import com.simis.basedao.vo.QueryCondition;
import com.simis.common.Constants;
import com.simis.common.ExportTypeEnum;
import com.simis.poi.ExcelReportService;
import com.simis.poi.TradeMatchExportService;
import com.simis.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ExportExcelController {
    /**
     * 控制台输出用的log
     */
    protected static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ExportExcelController.class);


    @Autowired
    private ExcelReportService excelReportService;

    @Autowired
    private TradeMatchExportService tradeMatchExportService;

    /**
     * @Title: exportMatchExcel
     * @Description: 根据条件导出excel
     * @param @return
     * @param @throws Exception
     * @throws
     */
    // @ResponseBody
    @RequestMapping(value = "manage/export", method = RequestMethod.POST)
    public void exportMatchExcel(HttpServletRequest request, HttpServletResponse response, String sqlName,String exportType,String exportTime)
            throws Exception {
        Map<String, Object> conditions = new HashMap<String, Object>();
        OutputStream ous = null;
        ByteArrayInputStream fileInputStream = null;
        Enumeration parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement().toString();
            Object value = request.getParameter(key);
            if(key.equalsIgnoreCase("sqlName")){
                  continue;
            }
            if (value != null && !value.equals("")) {
                conditions.put(key, value);
            }
        }
        Map<Object, String> map = tradeMatchExportService.getFile(sqlName);
        String fileName = map.get("fileName") == null ? "" : map.get("fileName");

        String file = map.get("xmlName") == null ? "" : map.get("xmlName");

        QueryCondition qc= tradeMatchExportService.getMatchSql(conditions, sqlName,exportType,exportTime);

        String templetDire = request.getSession().getServletContext()
                .getRealPath(Constants.EXPORT_TEMPLATE_PATH + file);
        File tempFile = new File(templetDire);

        try {
            if (!qc.getFullSQL().isEmpty()) {
                fileInputStream = (ByteArrayInputStream) excelReportService.getExcelInputStream(qc.getFullSQL(),qc.getConditions(), tempFile);
                byte[] bytes = new byte[fileInputStream.available()];
                fileInputStream.read(bytes);

                ous = new BufferedOutputStream(response.getOutputStream());
                response.reset();

                ExportTypeEnum exportTypeEnum = ExportTypeEnum.createExportTypeEnum(exportType);
                String typeNameStr = exportTypeEnum.getDescription();
                String dateStr = DateTimeUtil.getDate2String(DateTimeUtil.YYYYMMDDHHMMSS,new Date());
                fileName = typeNameStr+"_"+dateStr;
//                fileName = new String(fileName.getBytes("utf-8"),"utf-8");
//                response.setCharacterEncoding("utf-8");
                String agent = request.getHeader("USER-AGENT").toLowerCase();
                if (agent.contains("firefox")) {
                    response.setCharacterEncoding("utf-8");
                    response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1") + ".xls");
                } else {
                    fileName= URLEncoder.encode(fileName,"utf-8");
                    response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xls");
                }
//                response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
                response.setHeader("Content-Type", "application/vnd.ms-excel");
                ous.write(bytes);
                ous.flush();
            }

        } catch (Exception e) {
            LOGGER.error("导出抛出异常", e);
        } finally {
            try {
                if (ous != null) {
                    ous.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }

            } catch (IOException e) {
                LOGGER.error("导出异常", e);
            }
        }

    }

}
