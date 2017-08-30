package com.simis.dao;

import com.simis.base.BusiHibernateDao;
import com.simis.model.DictionaryModel;

/**
 * 字典操作dao
 * Created by 一拳超人 on 17/3/29.
 */
public interface DictionaryDao extends BusiHibernateDao {

    /**
     * @Title: queryByUserName
     * @Description: 查询用户
     * @param key
     * @return DictionaryModel
     * @throws
     */
    DictionaryModel queryByKey(String key);

    /**
     * @Title: update
     * @Description: 更新model
     * @param model
     * @return DictionaryModel
     * @throws
     */
    void updateModel(DictionaryModel model);

}
