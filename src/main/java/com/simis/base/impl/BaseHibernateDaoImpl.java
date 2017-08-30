package com.simis.base.impl;

import com.simis.base.BaseHibernateDao;
import com.simis.exception.ApplicationException;
import com.simis.util.HqlUtil;
import org.apache.poi.ss.formula.functions.T;
import org.hibernate.*;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * Created by 一拳超人.
 */
@Repository("baseHibernateDao")
public abstract class BaseHibernateDaoImpl implements BaseHibernateDao {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    public static final int DEFAULT_LOB_SIZE = 256;
    public static final int BATCH_COMMIT_COUNT = 200;
    @Autowired
    protected HibernateTemplate hibernateTemplate;
    @Autowired
    protected HibernateTemplate borrowHibernateTemplate;

    public BaseHibernateDaoImpl() {
    }

    public List getObjects(Class clazz) {
        return this.getHibernateTemplate().loadAll(clazz);
    }

    public Object getObject(Class clazz, Serializable id) {
        return this.getHibernateTemplate().get(clazz, id);
    }

    public void saveObject(Object o) {
        Assert.notNull(o, "保存对象为空!");
        this.getHibernateTemplate().save(o);
        this.getHibernateTemplate().flush();
    }

    public void updateObject(Object o) {
        Assert.notNull(o, "更新对象为空!");
        this.getHibernateTemplate().update(o);
        this.getHibernateTemplate().flush();
    }

    public void removeObject(Object o) {
        Assert.notNull(o, "删除对象为空!");
        this.getHibernateTemplate().delete(o);
        this.getHibernateTemplate().flush();
    }

    public void removeAll(Collection col) throws ApplicationException {
        try {
            this.getHibernateTemplate().deleteAll(col);
            this.getHibernateTemplate().flush();
        } catch (Exception var3) {
            this.log.error("context", var3);
            throw new ApplicationException("context", var3);
        }
    }

    public void removeObject(Class clazz, Serializable id) {
        this.getHibernateTemplate().delete(this.getObject(clazz, id));
        this.getHibernateTemplate().flush();
    }

    public void flush() {
        this.getHibernateTemplate().flush();
    }

    public void refresh(Object entity) throws ApplicationException {
        try {
            this.getHibernateTemplate().refresh(entity);
        } catch (Exception var3) {
            this.log.error("context", var3);
            throw new ApplicationException("context", var3);
        }
    }

    private void setQueryParameters(Query query, Map parameters) {
        if(parameters != null) {
            Iterator i = parameters.entrySet().iterator();

            while(i.hasNext()) {
                Map.Entry entry = (Map.Entry)i.next();
                String paramName = (String)entry.getKey();
                Object paramValue = entry.getValue();
                if(paramValue instanceof Collection) {
                    query.setParameterList(paramName, (Collection)paramValue);
                } else if(paramValue instanceof Object[]) {
                    query.setParameterList(paramName, (Object[])((Object[])paramValue));
                } else {
                    query.setParameter(paramName, paramValue);
                }
            }
        }

    }

    private void setQueryParameters(Query query, Object[] values) {
        if(values != null) {
            for(int i = 0; i < values.length; ++i) {
                query.setParameter(i, values[i]);
            }
        }

    }

    public List list(String hql) {
        return this.getHibernateTemplate().find(hql);
    }

    public List list(final String hql, final Map parameters) {
        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(hql);
                BaseHibernateDaoImpl.this.setQueryParameters(query, parameters);
                return query.list();
            }
        };
        return (List)this.getHibernateTemplate().execute(callback);
    }

    public Object queryUniquneObject(final String hql, final Map parameters) {
        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(hql);
                BaseHibernateDaoImpl.this.setQueryParameters(query, parameters);
                Object result = null;
                result = query.uniqueResult();
                return result;
            }
        };
        return this.getHibernateTemplate().execute(callback);
    }

    public List list(String hql, Object[] values) {
        return this.getHibernateTemplate().find(hql, values);
    }

    public List list(String[] hqls) {
        List result = null;
        if(hqls.length > 0) {
            result = this.list(hqls[0]);
        }

        for(int i = 1; i < hqls.length; ++i) {
            result.addAll(this.list(hqls[i]));
        }

        return result;
    }

    public int update(final String hql, final Map parameters) {
        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(hql);
                BaseHibernateDaoImpl.this.setQueryParameters(query, parameters);
                return new Integer(query.executeUpdate());
            }
        };
        return ((Integer)this.getHibernateTemplate().execute(callback)).intValue();
    }

    public int update(final String hql, final Object[] values) {
        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(hql);
                BaseHibernateDaoImpl.this.setQueryParameters(query, values);
                return new Integer(query.executeUpdate());
            }
        };
        return ((Integer)this.getHibernateTemplate().execute(callback)).intValue();
    }

    public int execute(final String statement, final Object[] values, final int queryType) {
        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Object query = null;
                if(queryType == 1) {
                    query = session.createSQLQuery(statement);
                } else if(queryType == 2) {
                    query = session.createQuery(statement);
                }

                BaseHibernateDaoImpl.this.setQueryParameters((Query)query, (Object[])values);
                return new Integer(((Query)query).executeUpdate());
            }
        };
        return ((Integer)this.getHibernateTemplate().execute(callback)).intValue();
    }

    public void batchSaveVO(final List objectList) {
        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                int i = 0;

                for(Iterator it = objectList.iterator(); it.hasNext(); ++i) {
                    session.save(it.next());
                    if(i % 200 == 0) {
                        session.flush();
                        session.clear();
                    }
                }

                session.flush();
                session.clear();
                return null;
            }
        };
        this.getHibernateTemplate().execute(callback);
    }

    public void batchUpdateVO(final List objectList) {
        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                int i = 0;

                for(Iterator it = objectList.iterator(); it.hasNext(); ++i) {
                    session.update(it.next());
                    if(i % 200 == 0) {
                        session.flush();
                        session.clear();
                    }
                }

                session.flush();
                session.clear();
                return null;
            }
        };
        this.getHibernateTemplate().execute(callback);
    }

    public void batchSaveOrUpdateVO(final List objectList) {
        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                int i = 0;

                for(Iterator it = objectList.iterator(); it.hasNext(); ++i) {
                    session.saveOrUpdate(it.next());
                    if(i % 200 == 0) {
                        session.flush();
                        session.clear();
                    }
                }

                session.flush();
                session.clear();
                return null;
            }
        };
        this.getHibernateTemplate().execute(callback);
    }

    public List querySql(final String sql, final Map<String, Object> params) {
        this.disableCache();
        List rtnList = this.getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery(sql);
                if(null != params) {
                    Iterator i$ = params.keySet().iterator();

                    while(i$.hasNext()) {
                        String key = (String)i$.next();
                        query.setParameter(key, params.get(key));
                    }
                }

                return query.list();
            }
        });
        this.enableCache();
        return rtnList;
    }

    public List list(String sql, Object[][] entities, Object[][] scalaries) {
        return this.list(sql, entities, scalaries, (Object[])null);
    }

    public List list(String sql, Object[][] entities, Object[][] scalaries, int maxResults) {
        return this.list(sql, entities, scalaries, (Object[])null, maxResults);
    }

    public List list(String sql, Object[][] entities, Object[][] scalaries, Object[] values) {
        return this.list(sql, entities, scalaries, values, 0);
    }

    public List list(String sql, Object[][] entities, Object[][] scalaries, Object[] values, int maxResults) {
        return this.list(sql, entities, scalaries, values, 0, maxResults);
    }

    public List list(final String sql, final Object[][] entities, final Object[][] scalaries, final Object[] values, final int firstResult, final int maxResults) {
        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery(sql);
                int i;
                if(entities != null) {
                    for(i = 0; i < entities.length; ++i) {
                        query.addEntity(entities[i][0].toString(), (Class)entities[i][1]);
                    }
                }

                if(scalaries != null) {
                    for(i = 0; i < scalaries.length; ++i) {
                        query.addScalar(scalaries[i][0].toString(), (Type)scalaries[i][1]);
                    }
                }

                BaseHibernateDaoImpl.this.setQueryParameters(query, (Object[])values);
                if(firstResult > 0) {
                    query.setFirstResult(firstResult);
                }

                if(maxResults > 0) {
                    query.setMaxResults(maxResults);
                }

                return query.list();
            }
        };
        return (List)this.getHibernateTemplate().execute(callback);
    }

    public Object jdbcCall(String sql, Object[] inValues, int outType, int[] inIndexes, int outIndex) {
        Assert.notNull(sql, "sql语句为空!");
        return this.jdbcCall(sql, inValues, outType, inIndexes, outIndex, 256);
    }

    public Object jdbcCall(String sql, Object[] inValues, int outType, int[] inIndexes, int outIndex, int lobSize) {
        Assert.notNull(sql, "sql语句为空!");
        Object[] outValues = this.jdbcCall(sql, inValues, new int[]{outType}, inIndexes, new int[]{outIndex}, lobSize);
        return outValues != null && outValues.length > 0?outValues[0]:null;
    }

    public Object[] jdbcCall(String sql, Object[] inValues, int[] outTypes, int[] inIndexes, int[] outIndexes) {
        return this.jdbcCall(sql, inValues, outTypes, inIndexes, outIndexes, 256);
    }

    public Object[] jdbcCall(final String sql, final Object[] inValues, final int[] outTypes, final int[] inIndexes, final int[] outIndexes, final int lobSize) {
        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                CallableStatement cs = session.connection().prepareCall(sql);
                BaseHibernateDaoImpl.this.setCallableStatementInParameters(cs, inValues, inIndexes);
                BaseHibernateDaoImpl.this.registerCallableStatementOutParameter(cs, outTypes, outIndexes);
                cs.execute();
                Object[] result = BaseHibernateDaoImpl.this.getCallableStatementOutParameter(cs, outTypes, outIndexes, lobSize);
                cs.close();
                return result;
            }
        };
        return (Object[])((Object[])this.getHibernateTemplate().execute(callback));
    }

    private void setCallableStatementInParameters(CallableStatement cs, Object[] values, int[] indexes) throws SQLException {
        if(values != null) {
            int i;
            if(indexes != null && indexes.length == values.length) {
                for(i = 0; i < values.length; ++i) {
                    cs.setObject(indexes[i], values[i]);
                }
            } else {
                for(i = 0; i < values.length; ++i) {
                    cs.setObject(i + 1, values[i]);
                }
            }
        }

    }

    private void registerCallableStatementOutParameter(CallableStatement cs, int[] types, int[] indexes) throws SQLException {
        if(types != null) {
            int i;
            if(indexes != null && indexes.length == types.length) {
                for(i = 0; i < types.length; ++i) {
                    cs.registerOutParameter(indexes[i], types[i]);
                }
            } else {
                for(i = 0; i < types.length; ++i) {
                    cs.registerOutParameter(i + 1, types[i]);
                }
            }
        }

    }

    private Object[] getCallableStatementOutParameter(CallableStatement cs, int[] types, int[] indexs, int lobSize) throws SQLException {
        Object[] result = null;
        if(types != null) {
            result = new Object[types.length];
            int i;
            if(indexs != null && indexs.length == types.length) {
                for(i = 0; i < types.length; ++i) {
                    result[i] = this.getCallableStatementOutParameter(cs, types[i], indexs[i], lobSize);
                }
            } else {
                for(i = 0; i < types.length; ++i) {
                    result[i] = this.getCallableStatementOutParameter(cs, types[i], i + 1, lobSize);
                }
            }
        }

        return result;
    }

    private Object getCallableStatementOutParameter(CallableStatement cs, int type, int index, int lobSize) throws SQLException {
        Object result = null;
        switch(type) {
            case 2004:
                result = this.getBlobOutParameter(cs, index, lobSize);
                break;
            case 2005:
                result = this.getClobOutParameter(cs, index, lobSize);
                break;
            default:
                result = cs.getObject(index);
        }

        return result;
    }

    private Object getBlobOutParameter(CallableStatement cs, int index, int lobSize) {
        byte[] result = null;

        try {
            Blob e = cs.getBlob(index);
            if(e != null) {
                if(lobSize <= 0) {
                    result = e.getBytes(1L, (int)e.length());
                } else {
                    result = e.getBytes(1L, lobSize);
                }
            }
        } catch (SQLException var6) {
            this.log.error("context", var6);
        }

        return result;
    }

    private Object getClobOutParameter(CallableStatement cs, int index, int lobSize) {
        String result = null;

        try {
            Clob e = cs.getClob(index);
            if(e != null) {
                if(lobSize <= 0) {
                    result = e.getSubString(1L, (int)e.length());
                } else {
                    result = e.getSubString(1L, lobSize);
                }
            }
        } catch (SQLException var6) {
            this.log.error("context", var6);
        }

        return result;
    }

    public void batchJdbcUpdate(final String[] sqls) {
        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Statement ps = session.connection().createStatement();

                for(int i = 0; i < sqls.length; ++i) {
                    ps.addBatch(sqls[i]);
                }

                ps.executeBatch();
                session.flush();
                session.clear();
                return null;
            }
        };
        this.getHibernateTemplate().execute(callback);
    }

    public int jdbcUpdate(final String sql, final Object[] values) {
        HibernateCallback callback = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery(sql);
                BaseHibernateDaoImpl.this.setQueryParameters(query, (Object[])values);
                return Integer.valueOf(query.executeUpdate());
            }
        };
        return ((Integer)this.getHibernateTemplate().execute(callback)).intValue();
    }

    protected void setPreparedStatementParameters(PreparedStatement ps, Object[] values) throws SQLException {
        if(values != null) {
            for(int i = 0; i < values.length; ++i) {
                ps.setObject(i + 1, values[i]);
            }
        }

    }

    public Long getRowCountBySql(String sql, List<Object> paramList) {
        Assert.notNull(sql, "查询时发现sql为空");
        Assert.hasText("rowCount", "查询语句不正确");
        Object[][] scalaries = new Object[][]{{"rowCount", Hibernate.LONG}};
        new ArrayList();
        List result;
        if(paramList != null) {
            result = this.list(sql, (Object[][])null, scalaries, paramList.toArray());
        } else {
            result = this.list(sql, (Object[][])null, scalaries, (Object[])null);
        }

        return result.isEmpty()?new Long(0L):(Long)result.get(0);
    }

    public List sqlList(final String sql, final Object[] values, final Class beanClass, final Object[] fieldList) {
        List list = this.hibernateTemplate.executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery(sql);
                BaseHibernateDaoImpl.addSclar(sqlQuery, beanClass, fieldList);
                SQLQuery query = sqlQuery;
                if(beanClass != null) {
                    sqlQuery.setResultTransformer(Transformers.aliasToBean(beanClass));
                }

                if(values != null && values.length > 0) {
                    int i = 0;
                    Object[] arr$ = values;
                    int len$ = arr$.length;

                    for(int i$ = 0; i$ < len$; ++i$) {
                        Object obj = arr$[i$];
                        query.setParameter(i++, obj);
                    }
                }

                return query.list();
            }
        });
        return list;
    }

    protected static <T> void addSclar(SQLQuery sqlQuery, Class<T> clazz, Object[] felds) {
        if(clazz == null) {
            throw new NullPointerException("[clazz] could not be null!");
        } else {
            List fieldList = fields(felds);
            if(!fieldList.isEmpty()) {
                Field[] fields = clazz.getDeclaredFields();
                Iterator i$ = fieldList.iterator();

                while(i$.hasNext()) {
                    String fieldName = (String)i$.next();
                    Field[] arr$ = fields;
                    int len$ = fields.length;

                    for(int i$1 = 0; i$1 < len$; ++i$1) {
                        Field field = arr$[i$1];
                        if(fieldName.equals(field.getName())) {
                            if(field.getType() != Long.TYPE && field.getType() != Long.class) {
                                if(field.getType() != Integer.TYPE && field.getType() != Integer.class) {
                                    if(field.getType() != Character.TYPE && field.getType() != Character.class) {
                                        if(field.getType() != Short.TYPE && field.getType() != Short.class) {
                                            if(field.getType() != Double.TYPE && field.getType() != Double.class) {
                                                if(field.getType() != Float.TYPE && field.getType() != Float.class) {
                                                    if(field.getType() != Boolean.TYPE && field.getType() != Boolean.class) {
                                                        if(field.getType() == String.class) {
                                                            sqlQuery.addScalar(field.getName(), Hibernate.STRING);
                                                        } else if(field.getType() == java.util.Date.class) {
                                                            sqlQuery.addScalar(field.getName(), Hibernate.TIMESTAMP);
                                                        } else if(field.getType() == BigDecimal.class) {
                                                            sqlQuery.addScalar(field.getName(), Hibernate.BIG_DECIMAL);
                                                        }
                                                    } else {
                                                        sqlQuery.addScalar(field.getName(), Hibernate.BOOLEAN);
                                                    }
                                                } else {
                                                    sqlQuery.addScalar(field.getName(), Hibernate.FLOAT);
                                                }
                                            } else {
                                                sqlQuery.addScalar(field.getName(), Hibernate.DOUBLE);
                                            }
                                        } else {
                                            sqlQuery.addScalar(field.getName(), Hibernate.SHORT);
                                        }
                                    } else {
                                        sqlQuery.addScalar(field.getName(), Hibernate.CHARACTER);
                                    }
                                } else {
                                    sqlQuery.addScalar(field.getName(), Hibernate.INTEGER);
                                }
                            } else {
                                sqlQuery.addScalar(field.getName(), Hibernate.LONG);
                            }
                        }
                    }
                }
            }

        }
    }

    public static List<String> fields(Object[] object) {
        ArrayList fields = new ArrayList();

        for(int i = 0; i < object.length; ++i) {
            fields.add((String)object[i]);
        }

        return fields;
    }

    protected Map<String, Object> createParamMap(String[] names, Object[] values) {
        return HqlUtil.createParamMap(names, values);
    }

    public void enableCache() {
        if(!this.getHibernateTemplate().isCacheQueries()) {
            this.getHibernateTemplate().setCacheQueries(true);
        }

    }

    public void disableCache() {
        if(this.getHibernateTemplate().isCacheQueries()) {
            this.getHibernateTemplate().setCacheQueries(false);
        }

    }

    protected HibernateTemplate getHibernateTemplate() {
        return this.hibernateTemplate;
    }

    @Autowired
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
        this.disableCache();
    }

    public HibernateTemplate getBorrowHibernateTemplate() {
        return this.borrowHibernateTemplate;
    }

    public void setBorrowHibernateTemplate(HibernateTemplate borrowHibernateTemplate) {
        this.borrowHibernateTemplate = borrowHibernateTemplate;
    }
}
