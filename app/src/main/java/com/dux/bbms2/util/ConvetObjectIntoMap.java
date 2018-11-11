package com.dux.bbms2.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ConvetObjectIntoMap {
    public static Map<String, Object> Object(Object obj) throws
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {
        Class<?> pomclass = obj.getClass();
        pomclass = obj.getClass();
        Method[] methods = obj.getClass().getMethods();


        Map<String, Object> map = new HashMap<String, Object>();
        for (Method m : methods) {
            if (m.getName().startsWith("get") && !m.getName().startsWith("getClass")) {
                Object value = (Object) m.invoke(obj);
                map.put(m.getName().substring(3), (Object) value);
            }
        }
        return map;
    }
}
