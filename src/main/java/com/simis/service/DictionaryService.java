package com.simis.service;

import com.simis.model.DictionaryModel;

/**
 * 字典服务
 * Created by 一拳超人
 */
public interface DictionaryService {

    /**
     * @Title: queryByKey
     * @Description: 查询key
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
    void update(DictionaryModel model);
}
