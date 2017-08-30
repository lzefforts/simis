package com.simis.dao;

import com.simis.base.BaseHibernateDao;
import com.simis.basedao.ExportDao;
import com.simis.model.CustomerModel;

/**
 * Created by 一拳超人 on 17/3/29.
 */
public interface CustomerDao extends BaseHibernateDao,ExportDao {

    /**
     * @Title: queryByUserName
     * @Description: 查询用户
     * @param userName
     * @return void
     * @throws
     */
    CustomerModel queryByUserName(String userName);


    /**
     * @Title: validateCustomer
     * @Description: 验证登录
     * @param userName
     * @return boolean
     * @throws
     */
    boolean validateCustomer(String userName,String passWord);

    /**
     * @Title: update
     * @Description: 删除examTime的数据
     * @param examTime
     * @return void
     * @throws
     */
    void deleteByExamTime(String examTime);
}
