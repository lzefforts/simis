package com.simis.basedao;

import java.util.List;
import java.util.Map;

/**
 * Created by 一拳超人 on 16/11/16.
 */
public interface TradeBaseDao {

    /**
     * 批量查询实体(基础版)
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
    List newSqlList(final String sql, final Class beanClass, final int firstResult, final int maxResults,
                    final Map<String, Object> params, final Object[] fields);

    /**
     * 批量查询实体(简洁版)
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
    List simpleList(final String sql, final Class beanClass, final int firstResult, final int maxResults);

    /**
     * 执行SQL查询
     *
     * @param sql
     * @return List<Map>
     */
    List<Map<String, Object>> querySqlForListMap(final String sql, final Map<String, Object> params);

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
    Object queryObjectSql(final String sql, final Map<String, Object> conditions, final Class beanClass);

    /**
     * 根据hql和输入参数查询
     * 
     * @param hql
     * @param params
     * @return
     */
    List queryListModel(String hql, Map<String, Object> params);

    /**
     * 根据SQL查询Object
     * 
     * @param sql
     *            sql语句
     * @param conditions
     *            sql语句中用到的参数
     * @param beanClass
     *            最终要返回的对象class (支持vo)
     * @return Object
     */
    Object queryUniqueResult(final String sql, final Map<String, Object> conditions, final Class beanClass, final TradeBaseUtil.QueryTypeEnum typeEnum);

    /**
     * 根据SQL查询count()
     *
     * @param sql
     *            sql语句
     * @param params
     *            sql语句中用到的参数
     * @return Long
     */
    Long getRowCountBySql(final String sql, final Map<String, Object> params);
}
