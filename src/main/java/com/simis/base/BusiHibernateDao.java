package com.simis.base;

import com.simis.exception.ApplicationException;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by 一拳超人 on 17/3/29.
 */
public interface BusiHibernateDao<T> extends BaseHibernateDao {
    String QUERY_TYPE_EQUAL = "=";
    String QUERY_TYPE_LIKE = "like";
    String QUERY_TYPE_GT = ">";
    String QUERY_TYPE_GE = ">=";
    String QUERY_TYPE_LT = "<";
    String QUERY_TYPE_LE = "<=";

    T save(T var1) throws ApplicationException;

    T update(T var1) throws ApplicationException;

    void delete(T var1) throws ApplicationException;

    void deleteById(Serializable var1) throws ApplicationException;

    void deleteAll(Collection<T> var1) throws ApplicationException;

    void deleteAllByIds(Collection<Serializable> var1) throws ApplicationException;

    void deleteOther(String var1, Object var2);

    void evict(T var1);

    void evict(Collection<T> var1);

//    T merge(T var1) throws ApplicationException;

    void clear();

    List<T> findAll();

    T findById(Serializable var1);

    List<T> findByIds(String var1);

    List<T> findByField(String var1, Object var2);

    List<T> findByLikeField(String var1, Object var2);

    List<? extends T> findByExample(T var1);

    List query(String var1, Map<String, Object> var2);

    Integer execute(String var1, Map<String, Object> var2);

    Long getRowCount(String var1, Map<String, Object> var2);

    List<T> batchSave(List<T> var1) throws Exception;

    List sqlList(String var1, Object[] var2, Class var3, Object[] var4);
}
