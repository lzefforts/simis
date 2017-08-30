package com.simis.basedao;

import com.simis.exception.ApplicationException;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Version;
import java.lang.reflect.Field;

/**
 * 解析column,拼接sql 把select * from a转换成 select a.id id,a.product_name productName
 * ... from a 通过解析classz中的属性完成,要求属性中添加column注解(javax.persistence.Column) Created
 * by 一拳超人 on 16/12/1.
 */
@Component
public class ColumnResolve {

    /**
     * 返回解析后的完整sql
     * 
     * @param classz
     * @return String
     */
    public String resolve(String sql, Class classz) {
        if (!checkStar(sql)) {
            return sql;
        }
        return buildSql(sql, classz);
    }

    /**
     * 组装完整sql
     * 
     * @param sql
     * @param classz
     * @return String
     */
    public String buildSql(String sql, Class classz) {
        String middleStr = resolveSql(classz);
        int beginAt = sql.toLowerCase().indexOf("from");
        String endStr = sql.substring(beginAt);
        StringBuilder newSql = new StringBuilder("");
        newSql.append("select ");
        newSql.append(middleStr + " ");
        newSql.append(endStr);
        return newSql.toString();
    }

    /**
     * 解析column注解,拼接中间sql
     * 
     * @param classz
     * @return String
     */
    public String resolveSql(Class classz) {
        StringBuilder sql = new StringBuilder();
        Field[] fields = TradeBaseUtil.handlerAllFields(classz);
        boolean isHaveColumn = false;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isAnnotationPresent(Column.class)) {
                Column column = fields[i].getAnnotation(Column.class);
                sql.append(column.name());
                sql.append(" as ");
                sql.append(fields[i].getName());
                sql.append(",");
                isHaveColumn = true;
            }
            if (fields[i].isAnnotationPresent(Version.class)) {
                sql.append(fields[i].getName());
                sql.append(" as ");
                sql.append(fields[i].getName());
                sql.append(",");
                isHaveColumn = true;
            }
        }
        if (!isHaveColumn) {
            throw new ApplicationException(classz.getName() + "类中的属性都不含有column注解!如需继续使用select,需要在属性中添加column注解!");
        }
        return sql.substring(0, sql.length() - 1);
    }

    /**
     * 检查sql中是否有*号
     * 
     * @param sql
     * @return boolean
     */
    public boolean checkStar(String sql) {
        if (sql.contains("*")) {
            return true;
        }
        return false;
    }
}
