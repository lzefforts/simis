package com.simis.dao.impl;

import com.simis.base.impl.BaseHibernateDaoImpl;
import com.simis.dao.UserDao;
import com.simis.model.CustomerModel;
import com.simis.model.UserModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 一拳超人
 */
@Repository("userDao")
public class UserDaoImpl extends BaseHibernateDaoImpl implements UserDao {

    @Override
    public UserModel queryByUserName(String userName) {
        StringBuilder hql = new StringBuilder("from UserModel where userName =:userName and systemState=1");
        Map<String,Object> params = new HashMap<>();
        params.put("userName",userName);
        return (UserModel)this.queryUniquneObject(hql.toString(),params);
    }

    @Override
    public boolean validateUser(String userName, String passWord) {
        StringBuilder hql = new StringBuilder("from UserModel where userName =:userName and passWord =:passWord and systemState=1");
        Map<String,Object> params = new HashMap<>();
        params.put("userName",userName);
        params.put("passWord",passWord);
        if(this.queryUniquneObject(hql.toString(),params) == null){
            return false;
        }
        return true;
    }
}
