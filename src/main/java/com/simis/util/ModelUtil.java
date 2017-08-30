package com.simis.util;

import com.simis.exception.ApplicationException;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by 一拳超人 on 17/3/29.
 */
public class ModelUtil {
    private static final Logger log = LoggerFactory.getLogger(ModelUtil.class);

    private ModelUtil() {
    }

    public static Object getPropertyValue(Object obj, String propName) throws ReflectiveOperationException {
        return PropertyUtils.getProperty(obj, propName);
    }

    public static Map<String, Object> convBeanToMap(Object bean, String... propNames) throws ApplicationException {
        HashMap rtn = new HashMap();
        String[] arr$ = propNames;
        int len$ = propNames.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String propName = arr$[i$];
            Object propValue = null;

            try {
                if(PropertyUtils.isReadable(bean, propName)) {
                    propValue = PropertyUtils.getProperty(bean, propName);
                }
            } catch (Exception var9) {
                log.info("获取其它属性时出错：", var9);
                throw new ApplicationException("获取其它属性时出错：", var9);
            }

            rtn.put(propName, propValue);
        }

        return rtn;
    }

    public static void copyProps(Object source, Object target, String... ignorProps) {
        BeanUtils.copyProperties(source, target, ignorProps);
    }

    public static void populateProps(Object desc, Map<String, Object> source) throws ReflectiveOperationException {
        Set entrySet = source.entrySet();
        Iterator it = entrySet.iterator();

        while(true) {
            Map.Entry entry;
            Object value;
            do {
                if(!it.hasNext()) {
                    return;
                }

                entry = (Map.Entry)it.next();
                value = entry.getValue();
            } while(value == null);

            Class clazz = entry.getValue().getClass();
            String name = (String)entry.getKey();
            if(clazz.isAssignableFrom(Date.class) || clazz.isAssignableFrom(Time.class) || clazz.isAssignableFrom(Timestamp.class)) {
                Long times = (Long) MethodUtils.invokeMethod(value, "getTime", (Object[])null);
                value = new java.util.Date(times.longValue());
            }

            org.apache.commons.beanutils.BeanUtils.setProperty(desc, name, value);
        }
    }
}
