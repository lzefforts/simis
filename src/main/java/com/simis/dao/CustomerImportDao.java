package com.simis.dao;

import com.simis.base.BaseHibernateDao;
import com.simis.basedao.ExportDao;
import com.simis.model.CustomerImportModel;
import com.simis.model.CustomerModel;

/**
 * Created by 一拳超人 on 17/3/29.
 */
public interface CustomerImportDao extends BaseHibernateDao {


    /**
     * @Title: queryByCardNo
     * @Description: 根据卡号查询导入客户信息
     * @param cardNo
     * @return CustomerImportModel
     * @throws
     */
    CustomerImportModel queryByCardNo(String cardNo);

}
