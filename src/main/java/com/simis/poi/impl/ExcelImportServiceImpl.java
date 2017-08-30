package com.simis.poi.impl;

import com.simis.model.CustomerImportModel;
import com.simis.poi.ExcelImportService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 一拳超人
 */
@Service("excelImportService")
public class ExcelImportServiceImpl implements ExcelImportService {

    @Override
    public List<CustomerImportModel> getAllByExcel(String file) {
        return null;
    }
}
