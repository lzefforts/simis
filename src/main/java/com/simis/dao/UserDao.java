package com.simis.dao;

import com.simis.base.BaseHibernateDao;
import com.simis.basedao.ExportDao;
import com.simis.model.CustomerModel;
import com.simis.model.UserModel;

/**
 * Created by 一拳超人 on 17/3/29.
 */
public interface UserDao extends BaseHibernateDao {

    /**
     * @Title: queryByUserName
     * @Description: 查询用户
     * @param userName
     * @return void
     * @throws
     */
    UserModel queryByUserName(String userName);

    /**
     * @Title: validateUser
     * @Description: 验证登录
     * @param userName
     * @return boolean
     * @throws
     */
    boolean validateUser(String userName, String passWord);
}
