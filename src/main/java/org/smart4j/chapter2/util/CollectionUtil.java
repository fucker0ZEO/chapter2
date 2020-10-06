package org.smart4j.chapter2.util;

import org.apache.commons.collections4.MapUtils;

import java.util.Collection;
import java.util.Map;

public final class CollectionUtil {

    /**
     * 判断Collection 是否为空
     */
    public static boolean isEmpty(Collection<?> collection){
        return CollectionUtil.isEmpty(collection);
    }
    /**
     * 判断Collection 是否非空
     */
    public static boolean isNotEmpty(Collection<?> collection){
        return !isEmpty(collection);
    }
    /**
     * 判断Map是否为空
     */
    public static boolean isEmpty(Map<?,?> map){
        return MapUtils.isEmpty(map);
    }
    /**
     * 判断Map是否非空
     */
    public static boolean isNotEmpty(Map<?,?> map){
        return !isEmpty(map);
    }

}
