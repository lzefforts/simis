package com.simis.util;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 一拳超人 on 17/3/29.
 */
public class HqlUtil {
    private static Logger log = Logger.getLogger(HqlUtil.class);

    private HqlUtil() {
    }

    public static Map<String, Object> createParamMap(String[] names, Object[] values) {
        Assert.notNull(names, "名称数组不能为空");
        Assert.notNull(values, "值数组不能为空");
        Assert.isTrue(names.length == values.length, "keys与values的数量不相等");
        HashMap map = new HashMap();

        for(int i = 0; i < names.length; ++i) {
            String key = names[i];
            Object value = values[i];
            map.put(key, value);
        }

        return map;
    }

    public static String putParamsEqual(Map<String, Object> paramMap, Object bean, String... propNames) throws ReflectiveOperationException {
        if(null != paramMap && null != bean && null != propNames) {
            String logic = "and";
            StringBuilder rtnSb = new StringBuilder();
            String[] arr$ = propNames;
            int len$ = propNames.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String propName = arr$[i$];
                String[] propNameArray = propName.split("\\.");
                String aliasName = "";
                if(propNameArray.length > 1) {
                    aliasName = propNameArray[0];
                    propName = propNameArray[1];
                }

                Object value = ModelUtil.getPropertyValue(bean, propName);
                boolean flag = false;
                if(null != value) {
                    if(value instanceof String) {
                        if(((String)value).trim().length() > 0) {
                            flag = true;
                        }
                    } else {
                        flag = true;
                    }

                    if(flag) {
                        rtnSb.append(" ").append(logic).append(" ").append(aliasName.length() > 0?aliasName + ".":"").append(propName).append("=:").append(propName);
                        paramMap.put(propName, value);
                    }
                }
            }

            return rtnSb.toString();
        } else {
            return "";
        }
    }

    public static String putParamsEqual4ManyObject(Map<String, Object> paramMap, Object bean, String... propNames) throws ReflectiveOperationException {
        if(paramMap != null && bean != null && propNames != null) {
            String logic = "and";
            StringBuilder rtnSb = new StringBuilder();
            String[] arr$ = propNames;
            int len$ = propNames.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String propName = arr$[i$];
                Integer pointIndex = Integer.valueOf(propName.lastIndexOf("."));
                if(pointIndex.intValue() > -1) {
                    String paramPropName = propName.replace(".", "");
                    String realPropName = propName.substring(pointIndex.intValue() + 1);
                    Object value = ModelUtil.getPropertyValue(bean, realPropName);
                    boolean flag = false;
                    if(null != value) {
                        if(value instanceof String) {
                            if(((String)value).trim().length() > 0) {
                                flag = true;
                            }
                        } else {
                            flag = true;
                        }

                        if(flag) {
                            rtnSb.append(" ").append(logic).append(" ").append(propName).append("=:").append(paramPropName);
                            paramMap.put(paramPropName, value);
                        }
                    }
                }
            }

            return rtnSb.toString();
        } else {
            return "";
        }
    }

    public static String putParamIn(Map<String, Object> paramMap, String propName, String values) {
        return putParamIn(paramMap, "", propName, values);
    }

    public static String putParamIn(Map<String, Object> paramMap, String aliasName, String propName, String values) {
        if(null != values && values.trim().length() != 0) {
            StringBuilder rtnSb = new StringBuilder();
            String[] vals = values.split(",");
            if(vals.length > 0) {
                rtnSb.append(" and ");
                if(aliasName != null && aliasName.length() > 0) {
                    rtnSb.append(aliasName).append(".");
                }

                rtnSb.append(propName).append(" in(");
            }

            for(int i = 0; i < vals.length; ++i) {
                String key = propName + i;
                String val = vals[i];
                rtnSb.append(":").append(key);
                if(i < vals.length - 1) {
                    rtnSb.append(",");
                }

                paramMap.put(key, val);
            }

            if(vals.length > 0) {
                rtnSb.append(") ");
            }

            return rtnSb.toString();
        } else {
            return "";
        }
    }

    public static String putParamIn4ManyObject(Map<String, Object> paramMap, String propName, String values) {
        return putParamIn(paramMap, "", propName, values);
    }

    public static String putParamIn4ManyObject(Map<String, Object> paramMap, String aliasName, String propName, String values) {
        if(null != values && values.trim().length() != 0) {
            StringBuilder rtnSb = new StringBuilder();
            String[] vals = values.split(",");
            if(vals.length > 0) {
                rtnSb.append(" and ");
                if(aliasName != null && aliasName.length() > 0) {
                    rtnSb.append(aliasName).append(".");
                }

                String i = propName.replace(".", "");
                rtnSb.append(i).append(" in(");
            }

            for(int var9 = 0; var9 < vals.length; ++var9) {
                String key = propName + var9;
                String val = vals[var9];
                rtnSb.append(":").append(key);
                if(var9 < vals.length - 1) {
                    rtnSb.append(",");
                }

                paramMap.put(key, val);
            }

            if(vals.length > 0) {
                rtnSb.append(") ");
            }

            return rtnSb.toString();
        } else {
            return "";
        }
    }

    public static String putParamIn(Map<String, Object> paramMap, String aliasName, String propName, Object[] values) {
        log.debug("value=" + values);
        if(null != values && values.length != 0) {
            StringBuilder rtnSb = new StringBuilder();
            if(values.length > 0) {
                rtnSb.append(" and ");
                if(aliasName != null && aliasName.length() > 0) {
                    rtnSb.append(aliasName).append(".");
                }

                rtnSb.append(propName).append(" in(");
            }

            for(int i = 0; i < values.length; ++i) {
                String key = propName + i;
                Object val = values[i];
                rtnSb.append(":").append(key);
                if(i < values.length - 1) {
                    rtnSb.append(",");
                }

                paramMap.put(key, val);
            }

            if(values.length > 0) {
                rtnSb.append(") ");
            }

            return rtnSb.toString();
        } else {
            return "";
        }
    }

    public static String putParamBetween(Map<String, Object> paramMap, String propName, Object amount1, Object amount2) throws Exception {
        if(null != amount1 && null != amount2) {
            if(amount1 instanceof String && ((String)amount1).trim().length() == 0) {
                return "";
            } else if(amount2 instanceof String && ((String)amount2).trim().length() == 0) {
                return "";
            } else {
                StringBuilder rtnSb = new StringBuilder();
                rtnSb.append(" and ( ").append(propName).append(" between :").append(propName).append("1").append(" and ").append(":").append(propName).append("2 )");
                paramMap.put(propName + "1", amount1);
                paramMap.put(propName + "2", amount2);
                return rtnSb.toString();
            }
        } else {
            return "";
        }
    }

    public static String putParamBetween4ManyObject(Map<String, Object> paramMap, String propName, Object amount1, Object amount2) throws Exception {
        String realName = "";
        if(null != amount1 && null != amount2) {
            Integer rtnSb = Integer.valueOf(propName.lastIndexOf("."));
            realName = propName.substring(rtnSb.intValue() + 1);
            if(amount1 instanceof String && ((String)amount1).trim().length() == 0) {
                return "";
            } else if(amount2 instanceof String && ((String)amount2).trim().length() == 0) {
                return "";
            } else {
                StringBuilder rtnSb1 = new StringBuilder();
                rtnSb1.append(" and ( ").append(propName).append(" between :").append(realName).append("1").append(" and ").append(":").append(realName).append("2 )");
                paramMap.put(realName + "1", amount1);
                paramMap.put(realName + "2", amount2);
                return rtnSb1.toString();
            }
        } else {
            return "";
        }
    }

    public static String putParamsLike(Map<String, Object> paramMap, Object bean, String... propNames) throws ReflectiveOperationException {
        StringBuilder rtnSb = new StringBuilder();
        String[] arr$ = propNames;
        int len$ = propNames.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String propName = arr$[i$];
            Object value = ModelUtil.getPropertyValue(bean, propName);
            if(null != value && value instanceof String && ((String)value).trim().length() > 0) {
                rtnSb.append(" and ").append(propName).append(" like :").append(propName);
                paramMap.put(propName, "%" + value + "%");
            }
        }

        return rtnSb.toString();
    }

    public static String putParamsLike4ManyObject(Map<String, Object> paramMap, Object bean, String... propNames) throws ReflectiveOperationException {
        StringBuilder rtnSb = new StringBuilder();
        String[] arr$ = propNames;
        int len$ = propNames.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String propName = arr$[i$];
            Integer pointIndex = Integer.valueOf(propName.lastIndexOf("."));
            if(pointIndex.intValue() > -1) {
                String paramPropName = propName.replace(".", "");
                String realPropName = propName.substring(pointIndex.intValue() + 1);
                Object value = ModelUtil.getPropertyValue(bean, realPropName);
                if(null != value && value instanceof String && ((String)value).trim().length() > 0) {
                    rtnSb.append(" and ").append(propName).append(" like :").append(paramPropName);
                    paramMap.put(paramPropName, value + "%");
                }
            }
        }

        return rtnSb.toString();
    }

    public static String putParamsInstr4ManyObject(Map<String, Object> paramMap, Object bean, String... propNames) throws ReflectiveOperationException {
        StringBuilder rtnSb = new StringBuilder();
        String[] arr$ = propNames;
        int len$ = propNames.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String propName = arr$[i$];
            Integer pointIndex = Integer.valueOf(propName.lastIndexOf("."));
            if(pointIndex.intValue() > -1) {
                String paramPropName = propName.replace(".", "");
                String realPropName = propName.substring(pointIndex.intValue() + 1);
                Object value = ModelUtil.getPropertyValue(bean, realPropName);
                if(null != value && value instanceof String && ((String)value).trim().length() > 0) {
                    rtnSb.append(" and instr(").append(propName).append(", :").append(paramPropName).append(") >0");
                    paramMap.put(paramPropName, value);
                }
            }
        }

        return rtnSb.toString();
    }

    public static String putParamsGt(Map<String, Object> paramMap, String name, Object value, boolean isEquals) {
        if(null == value) {
            return "";
        } else {
            StringBuilder rtnSb = new StringBuilder();
            String suffix = "_Gt";
            rtnSb.append(" and ").append(name).append(" >");
            if(isEquals) {
                rtnSb.append("=");
            }

            rtnSb.append(" :").append(name).append(suffix);
            paramMap.put(name + suffix, value);
            return rtnSb.toString();
        }
    }

    public static String putParamsGt4ManyObject(Map<String, Object> paramMap, String name, Object value, boolean isEquals) {
        if(null == value) {
            return "";
        } else {
            String paramPropName = name.replace(".", "");
            StringBuilder rtnSb = new StringBuilder();
            String suffix = "_Gt";
            rtnSb.append(" and ").append(name).append(" >");
            if(isEquals) {
                rtnSb.append("=");
            }

            rtnSb.append(" :").append(paramPropName).append(suffix);
            paramMap.put(paramPropName + suffix, value);
            return rtnSb.toString();
        }
    }

    public static String putParamsLt(Map<String, Object> paramMap, String name, Object value, boolean isEquals) {
        if(null == value) {
            return "";
        } else {
            StringBuilder rtnSb = new StringBuilder();
            String suffix = "_Lt";
            rtnSb.append(" and ").append(name).append(" <");
            if(isEquals) {
                rtnSb.append("=");
            }

            rtnSb.append(" :").append(name).append(suffix);
            paramMap.put(name + suffix, value);
            return rtnSb.toString();
        }
    }

    public static String putParamsLt4ManyObject(Map<String, Object> paramMap, String name, Object value, boolean isEquals) {
        if(null == value) {
            return "";
        } else {
            String paramPropName = name.replace(".", "");
            StringBuilder rtnSb = new StringBuilder();
            String suffix = "_Lt";
            rtnSb.append(" and ").append(name).append(" <");
            if(isEquals) {
                rtnSb.append("=");
            }

            rtnSb.append(" :").append(paramPropName).append(suffix);
            paramMap.put(paramPropName + suffix, value);
            return rtnSb.toString();
        }
    }

    public static String putParamUnequal(Map<String, Object> paramMap, String aliasName, String propName, Object[] values) {
        log.debug("value=" + values);
        if(null != values && values.length != 0) {
            StringBuilder rtnSb = new StringBuilder();
            if(values.length > 0) {
                rtnSb.append(" and ");
                if(aliasName != null && aliasName.length() > 0) {
                    rtnSb.append(aliasName).append(".");
                }

                rtnSb.append(propName).append("not in(");
            }

            for(int i = 0; i < values.length; ++i) {
                String key = propName + i;
                Object val = values[i];
                rtnSb.append(":").append(key);
                if(i < values.length - 1) {
                    rtnSb.append(",");
                }

                paramMap.put(key, val);
            }

            if(values.length > 0) {
                rtnSb.append(") ");
            }

            return rtnSb.toString();
        } else {
            return "";
        }
    }
}
