package com.simis.poi;

import com.simis.model.CustomerImportModel;

import java.util.List;

/**
 * Created by 一拳超人
 */
public interface ExcelImportService {


    List<CustomerImportModel> getAllByExcel(String file);
}
