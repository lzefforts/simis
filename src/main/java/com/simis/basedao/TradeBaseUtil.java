package com.simis.basedao;

import com.simis.exception.ApplicationException;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TradeBaseDao工具类 Created by 一拳超人 on 16/12/5.
 */
public class TradeBaseUtil {

    private TradeBaseUtil(){}

    private final static Logger logger = LoggerFactory.getLogger(TradeBaseUtil.class);

    /**
     * 提取beanClass中的属性
     *
     * @param beanClass
     * @return 对象集合
     */
    public static Object[] combinField(Class beanClass, Object[] fieldsObj) {
        if (fieldsObj != null && fieldsObj.length > 0) {
            return fieldsObj;
        }
        Field[] fields = handlerAllFields(beanClass);
        if(fields == null || fields.length == 0){
            logger.error(beanClass.getName()+"类属性为空!");
            throw new ApplicationException(beanClass.getName()+"类属性为空!");
        }
        Object[] fieldList = new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldList[i] = fields[i].getName();
        }
        return fieldList;
    }

    /**
     * 剔除serialVersionUID属性的干扰
     *
     * @param beanClass
     * @return 对象集合
     */
    public static Field[] handlerAllFields(Class beanClass){
        Field[] superFileds = handlerSuperFields(beanClass);
        Field[] nativeFileds = handlerFields(beanClass);
        Field[] fields = (Field[]) ArrayUtils.addAll(superFileds,nativeFileds);
        return fields;
    }

    /**
     * vo 取所有属性,model只取带注解的属性+baseModel的属性
     * @param beanClass
     * @return 对象集合
     */
    private static Field[] handlerFields(Class beanClass){
        //如果有MappedSuperclass注解的或者Entity注解的,认为是model,只取带column注解的字段
        if(beanClass.isAnnotationPresent(MappedSuperclass.class)
                || beanClass.isAnnotationPresent(Entity.class)){
            return getColumnFields(beanClass);
        }
        //如果是vo,则取所有属性字段,除了serialVersionUID
        Field[] fields = beanClass.getDeclaredFields();
        Field[] results = new Field[fields.length-1];
        int i = 0;
        for(Field field : fields){
            if(!"serialVersionUID".equals(field.getName())){
                results[i] = field;
                i++;
            }
        }
        return results;
    }

    /**
     * 获取含有column注解的属性
     * @param beanClass
     * @return 对象集合
     */
    private static Field[] getColumnFields(Class beanClass){
        Field[] fields = beanClass.getDeclaredFields();
        Map<String,Field> tempMap = new HashMap<>();
        for(Field field : fields){
            if(field.isAnnotationPresent(Column.class)){
                tempMap.put(field.getName(),field);
            }
        }
        Field[] results = new Field[tempMap.size()];
        int i = 0;
        for(Map.Entry<String, Field> sets : tempMap.entrySet()){
            results[i] = sets.getValue();
            i++;
        }
        return results;
    }

    /**
     * 父类的属性
     * @param beanClass
     * @return 对象集合
     */
    private static Field[] handlerSuperFields(Class beanClass){
        Class superClass = beanClass.getSuperclass();
        if(superClass.isAnnotationPresent(MappedSuperclass.class)){
            return handlerFields(superClass);
        }
        return null;
    }

    /**
     * 根据classz类型转换type
     * 
     * @param sql
     * @return String
     */
    public static String handlerSql(String sql) {
        if (StringUtils.isEmpty(sql)) {
            throw new ApplicationException("SQL为空!");
        }
        String newSql = sql.toLowerCase();
        String SELECT = "select";
        String FROM = "from";
        int begin = newSql.indexOf(SELECT);
        int end = newSql.indexOf(FROM);
        String result = newSql.substring(begin + SELECT.length(), end).replaceAll(" ", "");
        return result;
    }

    /**
     * 根据classz类型转换type
     * 
     * @param classz
     * @return Type
     */
    public static Type handlerType(Class classz) {
        if (classz.equals(BigDecimal.class)) {
            return StandardBasicTypes.BIG_DECIMAL;
        }
        if (classz.equals(String.class)) {
            return StandardBasicTypes.STRING;
        }
        if (classz.equals(Integer.class)) {
            return StandardBasicTypes.INTEGER;
        }
        if (classz.equals(Long.class)) {
            return StandardBasicTypes.LONG;
        }
        if (classz.equals(Date.class)) {
            return StandardBasicTypes.DATE;
        }
        return StandardBasicTypes.STRING;
    }

    /**
     * 查询结果类型ENUM
     */
    public enum QueryTypeEnum{
        VO,
        MODEL;
    }

    /**
     * 提取beanClass中的所有注解为column的属性(本类以及父类)
     *
     * @param beanClass
     * @return 对象集合
     */
    public static Object[] getAllColumnAnnoFileds(Class beanClass){
        Map<String,String> resultMap = new HashMap<>();
        //本类含有column注解的属性
        for(Field field : beanClass.getDeclaredFields()){
            if(field.isAnnotationPresent(Column.class)){
                resultMap.put(field.getName(),field.getName());
            }
        }
        //父类含有column注解的属性
        for(Field field : beanClass.getSuperclass().getDeclaredFields()){
            if(field.isAnnotationPresent(Column.class)){
                resultMap.put(field.getName(),field.getName());
            }
        }
        Object[] result = new Object[resultMap.size()];
        int i = 0;
        for(Map.Entry map : resultMap.entrySet()){
            result[i]=map.getValue();
            i++;
        }
        return result;
    }
}
