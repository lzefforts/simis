package com.simis.service.impl;

import com.simis.common.Constants;
import com.simis.dao.CustomerImportDao;
import com.simis.model.CustomerImportModel;
import com.simis.service.CustomerImportService;
import com.simis.vo.CustomerImportVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 一拳超人 on 17/3/29.
 */
@Service("customerImportService")
public class CustomerImportServiceImpl implements CustomerImportService {

    @Autowired
    private CustomerImportDao customerImportDao;

    @Override
    public void saveImportData(List<CustomerImportVo> list) {
        List<CustomerImportModel> saveResults = new ArrayList<>();
        List<CustomerImportModel> changeLists = new ArrayList<>();
        for(CustomerImportVo vo : list){
            CustomerImportModel importModel = customerImportDao.queryByCardNo(vo.getCardNo());
            if(importModel != null){
                importModel.setExamTime(vo.getExamTime());
                importModel.setName(vo.getName());
                importModel.setSex(vo.getSex());
                importModel.setPhone(vo.getPhone());
                importModel.setModifyTime(new Date());
                importModel.setOperator(Constants.OPERATOR);
                changeLists.add(importModel);
            }
            else{
                CustomerImportModel model = combinModel(vo);
                saveResults.add(model);
            }
        }
        if(changeLists.size() > 0){
            customerImportDao.batchUpdateVO(changeLists);
        }
        if(saveResults.size() > 0) {
            customerImportDao.batchSaveVO(saveResults);
        }
    }

    public CustomerImportModel combinModel(CustomerImportVo vo){
        CustomerImportModel model = new CustomerImportModel();
        model.setName(vo.getName());
        model.setSex(vo.getSex());
        model.setPhone(vo.getPhone());
        model.setEmail(vo.getEmail());
        model.setCardNo(vo.getCardNo());
        model.setUserName(vo.getCardNo());
        model.setPayDate(vo.getPayDate());
        model.setIsAlreadyPaid(vo.getIsAlreadyPaid());
        model.setClGovRegisterDate(vo.getClGovRegisterDate());
        model.setExamTime(vo.getExamTime());
        model.setCardType("0");
        model.setCreateTime(new Date());
        model.setModifyTime(new Date());
        model.setCreator(Constants.OPERATOR);
        model.setOperator(Constants.OPERATOR);
        model.setState("0");

        return model;
    }
}
