package com.simis.basedao.vo;

import java.util.Map;

public class QueryCondition {
    //条件语句
    private String whereSQL;
    
    //完整语句
    private String fullSQL;
    //条件取值
    private Map<String,Object> conditions;
    
    public Map<String, Object> getConditions() {
        return conditions;
    }
    public void setConditions(Map<String, Object> conditions) {
        this.conditions = conditions;
    }
    
    public String getWhereSQL() {
        return whereSQL;
    }
    public void setWhereSQL(String whereSQL) {
        this.whereSQL = whereSQL;
    }
    public String getFullSQL() {
        return fullSQL;
    }
    public void setFullSQL(String fullSQL) {
        this.fullSQL = fullSQL;
    }
    
    
  

}
