package com.simis.dao.impl;

import com.simis.base.impl.BusiHibernateDaoImpl;
import com.simis.dao.DictionaryDao;
import com.simis.model.DictionaryModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 一拳超人
 */
@Repository("dictionaryDao")
public class DictionaryDaoImpl extends BusiHibernateDaoImpl implements DictionaryDao {

    @Override
    public DictionaryModel queryByKey(String key) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from DictionaryModel where key =:key and systemState=1");
        Map<String,Object> params = new HashMap<>();
        params.put("key",key);
        List<DictionaryModel> list = this.query(hql.toString(),params);
        if(list!=null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public void updateModel(DictionaryModel model) {
        StringBuilder hql = new StringBuilder("");
        hql.append("update DictionaryModel set value=:value where key=:key");
        Map<String,Object> conditions = new HashMap<>();
        conditions.put("value",model.getValue());
        conditions.put("key",model.getKey());
        this.update(hql.toString(),conditions);
    }
}
