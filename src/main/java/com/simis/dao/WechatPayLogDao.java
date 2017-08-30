package com.simis.dao;

import com.simis.base.BaseHibernateDao;
import com.simis.model.WechatPayLogModel;

import java.util.List;

/**
 * Created by 一拳超人 on 17/3/29.
 */
public interface WechatPayLogDao extends BaseHibernateDao {

   List<WechatPayLogModel> queryList();

}
