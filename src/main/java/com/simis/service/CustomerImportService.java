package com.simis.service;

import com.simis.model.CustomerModel;
import com.simis.vo.CustomerImportVo;

import java.util.List;

/**
 * Created by 一拳超人 on 17/3/29.
 */
public interface CustomerImportService {


    /**
     * @Title: saveImportData
     * @Description: 保存导入数据
     * @param list
     * @return void
     * @throws
     */
    void saveImportData(List<CustomerImportVo> list);
}
