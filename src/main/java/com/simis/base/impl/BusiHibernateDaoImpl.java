package com.simis.base.impl;

import com.simis.base.BusiHibernateDao;
import com.simis.exception.ApplicationException;
import com.simis.model.BaseModel;
import com.simis.util.CoreUtil;
import com.simis.util.PageTool;
import org.hibernate.*;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by 一拳超人 on 17/3/29.
 */
public class BusiHibernateDaoImpl<T extends BaseModel> extends BaseHibernateDaoImpl implements BusiHibernateDao<T> {
    private Class entityClass = null;

    public BusiHibernateDaoImpl() {
        Class c = this.getClass();
        Type t = c.getGenericSuperclass();
        if(t instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType)t).getActualTypeArguments();
            this.entityClass = (Class)p[0];
        }

    }

    public T save(T entity) throws ApplicationException {
        if(null == entity.getCreator()) {
            throw new ApplicationException("创建人不能为空 !");
        } else if(null == entity.getOperator()) {
            throw new ApplicationException("操作人不能为空 !");
        } else {
            Timestamp nowTime = CoreUtil.generateTimestamp();
            entity.setModifyTime(nowTime);
            entity.setCreateTime(nowTime);
            this.getHibernateTemplate().save(entity);
            if(this.log.isDebugEnabled()) {
                this.log.debug("entity " + entity + " will be saved");
            }

            return entity;
        }
    }

    public List<T> batchSave(List<T> entityList) throws ApplicationException {
        if(null != entityList && !entityList.isEmpty()) {
            Timestamp nowTime = CoreUtil.generateTimestamp();
            Iterator i$ = entityList.iterator();

            while(i$.hasNext()) {
                BaseModel t = (BaseModel)i$.next();
                t.setCreateTime(nowTime);
                t.setModifyTime(nowTime);
            }

            super.batchSaveVO(entityList);
        }

        if(this.log.isDebugEnabled()) {
            this.log.debug("entityList [" + entityList + "] be saved");
        }

        return entityList;
    }

    public T update(T entity) throws ApplicationException {
        if(null == entity.getCreator()) {
            throw new ApplicationException("创建人不能为空 !");
        } else if(null == entity.getOperator()) {
            throw new ApplicationException("操作人不能为空 !");
        } else {
            Timestamp nowTime = CoreUtil.generateTimestamp();
            entity.setModifyTime(nowTime);
            this.getHibernateTemplate().update(entity);
            if(this.log.isDebugEnabled()) {
                this.log.debug("entity " + entity + " will be updated");
            }

            return entity;
        }
    }

    public void delete(T entity) throws ApplicationException {
        this.getHibernateTemplate().delete(entity);
        this.getHibernateTemplate().flush();
    }

    public void deleteById(Serializable id) throws ApplicationException {
        Assert.notNull(id, "删除时发现id为空");
        this.delete(this.findById(id));
    }

    public void deleteAll(Collection<T> col) throws ApplicationException {
        this.getHibernateTemplate().deleteAll(col);
        this.getHibernateTemplate().flush();
    }

    public void deleteAllByIds(Collection<Serializable> col) throws ApplicationException {
        Assert.notNull(col, "删除时发现id的集合为空");
        Assert.notEmpty(col, "删除时发现id的集合中没有数据");
        HashSet objCol = new HashSet();
        Iterator i$ = col.iterator();

        while(i$.hasNext()) {
            Object id = i$.next();
            objCol.add(this.findById((Serializable)id));
        }

        this.deleteAll(objCol);
    }

    public void deleteOther(String entityName, Object entity) {
        this.getHibernateTemplate().delete(entityName, entity);
    }

    public void evict(T entity) {
        this.getHibernateTemplate().evict(entity);
    }

    public void evict(Collection<T> entitys) {
        Iterator i$ = entitys.iterator();

        while(i$.hasNext()) {
            BaseModel entity = (BaseModel)i$.next();
            this.getHibernateTemplate().evict(entity);
        }

    }

//    public T merge(T entity) throws ApplicationException {
//        return (BaseModel)this.getHibernateTemplate().merge(entity);
//    }

    public void clear() {
        this.getHibernateTemplate().clear();
    }

    public List<T> findAll() {
        return this.getHibernateTemplate().loadAll(this.entityClass);
    }

    public T findById(Serializable id) {
        BaseModel entity = (BaseModel)this.getHibernateTemplate().get(this.entityClass, id);
        return null;//entity;
    }

    public List<T> findByIds(String idsStr) {
        if(null != idsStr && idsStr.trim().length() != 0) {
            ArrayList list = new ArrayList();
            if(!"".equals(idsStr.trim())) {
                String[] ids = idsStr.trim().split(",");
                String[] arr$ = ids;
                int len$ = ids.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    String id = arr$[i$];
                    BaseModel obj = this.findById(id.trim());
                    if(null != obj) {
                        list.add(obj);
                    }
                }
            }

            return list;
        } else {
            return new ArrayList();
        }
    }

    public List<T> findByField(String fieldName, Object value) {
        String hql = "from " + this.entityClass.getName() + " where " + fieldName + "=:value";
        List list = this.query(hql, this.createParamMap(new String[]{"value"}, new Object[]{value}));
        return list;
    }

    public List<T> findByLikeField(String fieldName, Object value) {
        String hql = "from " + this.entityClass.getName() + " where " + fieldName + " like :value";
        List list = this.query(hql, this.createParamMap(new String[]{"value"}, new Object[]{value}));
        return list;
    }

    public List<? extends T> findByExample(T example) {
        return this.getHibernateTemplate().findByExample(example);
    }

    public List query(final String hql, final Map<String, Object> params) {
        return this.getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(hql);
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
    }

    public Integer execute(final String hql, final Map<String, Object> params) {
        Assert.notNull(hql, "执行hql时，发现hql为空");
        Assert.hasLength(hql, "执行hql时，发现hql没有内容");
        return (Integer)this.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(hql);
                if(null != params) {
                    Iterator i$ = params.keySet().iterator();

                    while(i$.hasNext()) {
                        String key = (String)i$.next();
                        query.setParameter(key, params.get(key));
                    }
                }

                return Integer.valueOf(query.executeUpdate());
            }
        });
    }

    public List queryPart(final String hql, final Map<String, Object> params, final int firstResult, final int maxResults) {
        Assert.notNull(hql, "查询时发现hql为空");
        Assert.hasLength(hql, "查询时发现hql没有内容");
        return this.getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(hql);
                if(null != params) {
                    Iterator i$ = params.keySet().iterator();

                    while(i$.hasNext()) {
                        String key = (String)i$.next();
                        query.setParameter(key, params.get(key));
                    }
                }

                return query.setFirstResult(firstResult).setMaxResults(maxResults).list();
            }
        });
    }

    public Long getRowCount(String hql, Map<String, Object> params) {
        Assert.notNull(hql, "查询时发现hql为空");
        Assert.hasText("from", "查询语句不正确");
        int fromIdx = hql.indexOf("from");
        int orderIdx = hql.indexOf("order by");
        int groupIdx = hql.indexOf("group by");
        String countSql = "select count(*) ";
        if(orderIdx > 0) {
            countSql = countSql + hql.substring(fromIdx, orderIdx);
        } else {
            countSql = countSql + hql.substring(fromIdx);
        }

        List list = this.query(countSql, params);
        Long count = Long.valueOf(0L);
        if(groupIdx > 0) {
            count = Long.valueOf(Long.parseLong(String.valueOf(list.size())));
        } else {
            count = (Long)list.get(0);
        }

        return count != null?count:new Long(0L);
    }

    public PageTool queryForPage(String hql, Map<String, Object> params, PageTool page) {
        long rowCount = this.getRowCount(hql, params).longValue();
        page.setRowCount((int)rowCount);
        List records = this.queryPart(hql, params, page.getStart(), page.getPageSize());
        page.setRecords(records);
        return page;
    }

    public List sqlList(final String sql, final Object[] values, final Class beanClass, final Object[] fieldList) {
        List list = this.hibernateTemplate.executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery(sql);
                BusiHibernateDaoImpl.addSclar(sqlQuery, beanClass, fieldList);
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
                                                        } else if(field.getType() == Date.class) {
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
}
