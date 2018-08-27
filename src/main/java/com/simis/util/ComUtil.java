package com.simis.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 工具类
 * 
 * @author kaifeng1
 *
 */
public final class ComUtil {

    private static Logger logger = LoggerFactory.getLogger(ComUtil.class);
    private static ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
        // mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 判断一个对象是否为null,String的情形判断是否为空白字符
     * 
     * @param o
     * @return
     */
    public static boolean isBlank(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            return isBlank(o.toString());
        }
        return false;
    }

    /**
     * 将信息比如 在条件{0}和{1}的时候必填 格式化成具体信息
     * 
     * @param msg
     * @param params
     * @return
     */
    public static String formatMsg(String msg, Object... params) {
        String format = MessageFormat.format(msg, params);
        return format;
    }

    /**
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws NoSuchFieldException
     * 
     * @Title: mapConvert2Object 通过map key-value 给对象的属性赋值
     * @param @param map
     * @param @param cls
     * @param @param obj
     * @return void 返回类型
     * @throws
     */
    public static void mapConvert2Object(Map<String, Object> map, Object obj){
        if (map == null || obj == null) {
            return;
        }
        Set<String> fields = map.keySet();
        Iterator<String> it = fields.iterator();

        Class<?> cls = obj.getClass();
        nextLoop : while (it.hasNext()) {
            try {
                String fieldName = it.next();
                Field field = cls.getDeclaredField(fieldName);
                field.setAccessible(true);
                if(field.getType() == Date.class && !(map.get(fieldName) instanceof Date)){
                    String value = map.get(fieldName).toString();
                    Date newValue = null;
                  if(!DateTimeUtil.isValidDate(value)){
                        SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss 'GMT'Z",Locale.ENGLISH);
                        newValue= sf.parse(value);
                 }else{
                     newValue = DateTimeUtil.convertString2Date(value);
                 }
             
                    field.set(obj, newValue);
                }
                else if(field.getType() == BigDecimal.class && !(map.get(fieldName) instanceof BigDecimal)){
                    String value = map.get(fieldName).toString();
                    BigDecimal newValue = new BigDecimal(value);
                    field.set(obj, newValue);
                }
                else if(field.getType() == Long.class && !(map.get(fieldName) instanceof Long)){
                    String value = map.get(fieldName).toString();
                    Long newValue = Long.valueOf(value);
                    field.set(obj, newValue);
                }
                else{
                   field.set(obj, map.get(fieldName));
                }
                field.setAccessible(false);
            } catch (Exception e) {
                logger.error("执行出错", e);
                continue nextLoop;
            }
        }

    }

    /**
     * 将bean的信息转化为Map
     * 
     * @param obj
     * @param map
     */
    public static void convertBeanToMap(Object obj, Map<String, Object> map) {
        if (obj == null) {
            return;
        }
        Class<?> cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();

        for (Field f : fields) {
            try {
                String name = f.getName();
                f.setAccessible(true);
                map.put(name, f.get(obj));

            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.error("执行出错", e);
                continue;
            } finally {
                f.setAccessible(false);
            }
        }

    }

    /**
     * 判断两个字符串对象是否相等
     * 
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean isEqualString(String obj1, String obj2) {
        if (obj1 == null) {
            return false;
        }
        if (obj2 == null) {
            return false;
        }
        // 相等
        if (obj1.trim().equals(obj2.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 获取系统配置的bean
     * 
     * @param name
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        ApplicationContext ctx = SpringUtil.getApplicationContext();
        return (T) ctx.getBean(name);
    }

    /**
     * 
     * @Title: jsonStr2Model json字符串转换为对象
     * @param @param jsonStr
     * @param @param clazz
     * @param @return
     * @return T 返回类型
     * @throws
     */
    public static <T> T jsonStr2Model(String jsonStr, Class<T> clazz) {
        T model = null;
        try {
            model = mapper.readValue(jsonStr, clazz);
            return model;
        } catch (Exception e) {
            logger.error("执行出错", e);
        }
        return null;
    }


    /**
     * 判断list是否为空
     * 
     * @param list
     * @return 结果判断
     */
    public static boolean isEmpty(List<?> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }


    /**
     * 给指定条件添加SQL中的like操作符号
     * 
     * @param param
     * @return
     */
    public static String addSqlLikePara(String param) {
	return "%" + param + "%";
    }
    /**
     * 
	 * 格式化map输出字符串：<br>
	 * 例:map{111=aaaaa,222=bbbb}<br>
	 * 转换为字符串为：111:aaaaa,222:bbbb<br>
	 * @param map
	 * @return 
	 * String 返回类型
	 *
     */
    public static String formatMap2String(Map<String,Object> map){
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            if(stringBuilder.length() != 0){
                stringBuilder.append(",");
            }
            String key = it.next();
            Object obj = map.get(key);
            stringBuilder.append(key);
            stringBuilder.append(":");
            stringBuilder.append(String.valueOf(obj));
            
        }
        
        return stringBuilder.toString();
    }
    
    /**
     * 
     * 格式化map的value值输出字符串：<br>
     * 例:map{111=aaaaa,222=bbbb}<br>
     * 转换为字符串为：aaaaa<br>bbbb<br>
     * @param map
     * @return 
     * String 返回类型
     *
     */
    public static String formatMapValue2String(Map<String,Object> map){
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            Object obj = map.get(key);
            stringBuilder.append(String.valueOf(obj));
            stringBuilder.append("<br>");
        }
        
        return stringBuilder.toString();
    }
    

}
