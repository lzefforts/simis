package com.simis.service.impl;

import com.simis.dao.DictionaryDao;
import com.simis.model.DictionaryModel;
import com.simis.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ArnoldLee on 17/5/20.
 */
@Service("dictionaryService")
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private DictionaryDao dictionaryDao;

    @Override
    public DictionaryModel queryByKey(String key) {
        return dictionaryDao.queryByKey(key);
    }

    @Override
    public void update(DictionaryModel model) {
        dictionaryDao.update(model);
    }
}
