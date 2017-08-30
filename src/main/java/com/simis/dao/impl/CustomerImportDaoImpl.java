package com.simis.dao.impl;

import com.simis.base.impl.BaseHibernateDaoImpl;
import com.simis.basedao.vo.QueryCondition;
import com.simis.common.TradeExportExcelEnum;
import com.simis.dao.CustomerDao;
import com.simis.dao.CustomerImportDao;
import com.simis.model.CustomerImportModel;
import com.simis.model.CustomerModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 一拳超人 on 17/3/29.
 */
@Repository("customerImportDao")
public class CustomerImportDaoImpl extends BaseHibernateDaoImpl implements CustomerImportDao {

    @Override
    public CustomerImportModel queryByCardNo(String cardNo) {
        StringBuilder hql = new StringBuilder("from CustomerImportModel where userName =:userName and systemState=1");
        Map<String,Object> params = new HashMap<>();
        params.put("userName",cardNo);
        Object result =  this.queryUniquneObject(hql.toString(),params);
        if(result != null){
            return (CustomerImportModel)result;
        }
        return null;
    }
}
