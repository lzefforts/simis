package com.simis.dao.impl;

import com.simis.base.impl.BaseHibernateDaoImpl;
import com.simis.dao.UserDao;
import com.simis.dao.WechatPayLogDao;
import com.simis.model.UserModel;
import com.simis.model.WechatPayLogModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 一拳超人
 */
@Repository("echatPayLogDao")
public class WechatPayLogDaoImpl extends BaseHibernateDaoImpl implements WechatPayLogDao {

    @Override
    public List<WechatPayLogModel> queryList() {
        StringBuilder hql = new StringBuilder("from WechatPayLogModel and systemState=1");
        return this.list(hql.toString());
    }
}
