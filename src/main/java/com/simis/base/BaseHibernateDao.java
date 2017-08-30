package com.simis.base;

import com.simis.exception.ApplicationException;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by 一拳超人 on 17/3/29.
 */
public interface BaseHibernateDao {
    List getObjects(Class var1);

    Object getObject(Class var1, Serializable var2);

    void saveObject(Object var1);

    void updateObject(Object var1);

    void removeObject(Object var1);

    void removeAll(Collection var1) throws ApplicationException;

    void removeObject(Class var1, Serializable var2);

    void flush();

    void refresh(Object var1) throws ApplicationException;

    void batchSaveVO(List var1);

    void batchUpdateVO(List var1);

    void batchSaveOrUpdateVO(List var1);

    List sqlList(String var1, Object[] var2, Class var3, Object[] var4);

    List list(String hql);
}