package com.simis.basedao.impl;

import com.simis.base.impl.BaseHibernateDaoImpl;
import com.simis.basedao.ColumnResolve;
import com.simis.basedao.TradeBaseDao;
import com.simis.basedao.TradeBaseUtil;
import org.hibernate.*;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 一拳超人 on 16/11/16.
 */
@Repository("tradeBaseDao")
public class TradeBaseDaoImpl extends BaseHibernateDaoImpl implements TradeBaseDao {
    @Autowired
    private ColumnResolve columnResolve;

    /**
     * 批量查询实体 Tips:1.支持select a.id,a.product_name .. from a 形式的sql 2.支持select *
     * from a 形式的sql(此种形式下,beanClass中属性必须有Column注解)
     *
     *
     * @param sql
     *            sql语句
     * @param beanClass
     *            将查询结果转换为<tt>T</tt>对象, beanClass中的字段要与sql中的查询结果字段名称一致, (if
     *            fields为空,那么要求beanClass中属性个数要与sql查询结果个数一致; if
     *            fields不为空,beanClass中属性只要包含fields属性即可)
     * @param firstResult
     *            从第几条记录开始(分页参数)
     * @param maxResults
     *            单页最大记录数(分页参数)
     * @param params
     *            </可以为空>,sql中用到的参数
     * @param fields
     *            </可为空>,为空时默认为beanClass的属性;
     *            不为空时,为sql查询结果对应的属性,此属性必须与sql中的查询结果字段名称一致, 但是可以少于beanClass中的属性;
     * @return List<beanClass>
     */
    @Override
    public List newSqlList(final String sql, final Class beanClass, final int firstResult, final int maxResults,
            final Map<String, Object> params, final Object[] fields) {
        final Object[] fieldList = TradeBaseUtil.combinField(beanClass, fields);
        final String newSql = columnResolve.resolve(sql, beanClass);
        List list = this.hibernateTemplate.executeFind(new HibernateCallback() {
            @SuppressWarnings("unchecked")
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery(newSql);
                // 添加要查询字段的标量
                addSclar(sqlQuery, beanClass, fieldList);
                Query query = sqlQuery;
                // 转换查询结果为T
                if (beanClass != null) {
                    query.setResultTransformer(Transformers.aliasToBean(beanClass));
                }
                setSQLQueryParameters(sqlQuery, params);
                if (firstResult > 0) {
                    query.setFirstResult(firstResult);
                }
                if (maxResults > 0) {
                    query.setMaxResults(maxResults);
                }
                return query.list();
            }
        });
        return list;
    }

    /**
     * 批量查询实体(简洁版) Tips:1.支持select a.id,a.product_name .. from a 形式的sql
     * 2.支持select * from a 形式的sql(此种形式下,beanClass中属性必须有Column注解)
     *
     * @param sql
     *            sql语句
     * @param beanClass
     *            将查询结果转换为<tt>T</tt>对象, beanClass中的字段要与sql中的查询结果字段名称一致, (if
     *            fields为空,那么要求beanClass中属性个数要与sql查询结果个数一致; if
     *            fields不为空,beanClass中属性只要包含fields属性即可)
     * @param firstResult
     *            从第几条记录开始(分页参数)
     * @param maxResults
     *            单页最大记录数(分页参数)
     * @return List<beanClass>
     */
    @Override
    public List simpleList(final String sql, final Class beanClass, final int firstResult, final int maxResults) {
        return this.newSqlList(sql, beanClass, firstResult, maxResults, null, null);
    }

    @Override
    public List<Map<String, Object>> querySqlForListMap(final String sql, final Map<String, Object> params) {
        List mapList = hibernateTemplate.executeFind(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery(sql);
                sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                setSQLQueryParameters(sqlQuery, params);
                return sqlQuery.list();
            }
        });
        return mapList;
    }

    /**
     * 根据SQL查询Object
     * 
     * @param sql
     *            sql语句
     * @param conditions
     *            sql语句中用到的参数
     * @param beanClass
     *            最终要返回的对象class
     *            (不支持model,暂时只支持BigDecimal/String/Integer/Long/Date)
     * @return Object
     */
    @Override
    public Object queryObjectSql(final String sql, final Map<String, Object> conditions, final Class beanClass) {
        Object result = hibernateTemplate.execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery(sql);
                setSQLQueryParameters(sqlQuery, conditions);
                sqlQuery.addScalar(TradeBaseUtil.handlerSql(sql), TradeBaseUtil.handlerType(beanClass));
                return sqlQuery.uniqueResult();
            }
        });
        return result;
    }

    @Override
    public List queryListModel(String hql, Map<String, Object> params) {
        return this.list(hql, params);
    }

    /**
     * 根据SQL查询Object
     * 
     * @param sql
     *            sql语句
     * @param conditions
     *            sql语句中用到的参数
     * @param beanClass
     *            最终要返回的对象class (支持vo\model)
     * @param typeEnum
     *            最终要返回的对象class的类型
     * @return Object
     */
    @Override
    public Object queryUniqueResult(final String sql, final Map<String, Object> conditions, final Class beanClass,final TradeBaseUtil.QueryTypeEnum typeEnum) {
        Object[] fields = null;
        switch (typeEnum){
            case VO:{
                break;
            }
            case MODEL:{
                fields = TradeBaseUtil.getAllColumnAnnoFileds(beanClass);
                break;
            }
            default:{}
        }

        List list = this.newSqlList(sql, beanClass, 0, 0, conditions, fields);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public Long getRowCountBySql(final String sql, final Map<String, Object> params) {
        Assert.notNull(sql, "查询时发现sql为空");
        Assert.hasText("rowCount", "查询语句不正确");
        Object result = hibernateTemplate.execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery(sql);
                setSQLQueryParameters(sqlQuery, params);
                sqlQuery.addScalar("rowCount", TradeBaseUtil.handlerType(Long.class));
                return sqlQuery.uniqueResult();
            }
        });
        return result == null ? new Long(0L) : (Long) result;
    }

    protected static <T> void addSclar(SQLQuery sqlQuery, Class<T> clazz, Object[] felds) {
        if (clazz == null) {
            throw new NullPointerException("[clazz] could not be null!");
        } else {
            List fieldList = fields(felds);
            if (!fieldList.isEmpty()) {
                Field[] fields = TradeBaseUtil.handlerAllFields(clazz);

                Iterator i$ = fieldList.iterator();

                while (i$.hasNext()) {
                    String fieldName = (String) i$.next();
                    Field[] arr$ = fields;
                    int len$ = fields.length;

                    for (int i$1 = 0; i$1 < len$; ++i$1) {
                        Field field = arr$[i$1];
                        if (fieldName.equals(field.getName())) {
                            if (field.getType() != Long.TYPE && field.getType() != Long.class) {
                                if (field.getType() != Integer.TYPE && field.getType() != Integer.class) {
                                    if (field.getType() != Character.TYPE && field.getType() != Character.class) {
                                        if (field.getType() != Short.TYPE && field.getType() != Short.class) {
                                            if (field.getType() != Double.TYPE && field.getType() != Double.class) {
                                                if (field.getType() != Float.TYPE && field.getType() != Float.class) {
                                                    if (field.getType() != Boolean.TYPE
                                                            && field.getType() != Boolean.class) {
                                                        if (field.getType() == String.class) {
                                                            sqlQuery.addScalar(field.getName(), Hibernate.STRING);
                                                        } else if (field.getType() == Date.class) {
                                                            sqlQuery.addScalar(field.getName(), Hibernate.TIMESTAMP);
                                                        } else if (field.getType() == BigDecimal.class) {
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

    /**
     * 设置占位符的查询条件
     * @param sqlQuery
     * @param params
     */
    private static void setSQLQueryParameters(SQLQuery sqlQuery, final Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                if (params.get(key).getClass().isArray()) {
                    sqlQuery.setParameterList(key, (Object[]) params.get(key));
                } else if (params.get(key) instanceof List) {
                    sqlQuery.setParameterList(key, (List) params.get(key));
                } else {
                    sqlQuery.setParameter(key, params.get(key));
                }
            }
        }
    };
}
