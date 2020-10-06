package org.smart4j.chapter2.util;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**import java.util.logging.Logger;
 * 这里存在着自动导包不注意时的错
 * 本来应该 import org.slf4j.Logger;
 * 结果却导了 java.util.logging.Logger
 * 导致后面做了一个类型转换，甚至是无法在其他类中调用LOGGER
 * 更无法输出error级别的日志，之前了解过JDK支持的输入日志和log4j是不同的，
 * 其中的差异之一就是error日志，那个时候就怀疑调用的是JDK自带的日志，而不是slf4j
 */

public final class PropsUtil {
    /**
     * 这里和原书略有不同，做了一个类型转换 (Logger)--这是导包出错导致的
     */
    private static final Logger LOGGER=  LoggerFactory.getLogger(PropsUtil.class);


    /**
     * 加载属性文件
     */
    public static Properties loadProps(String fileName){
        Properties props=null;
        InputStream is=null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                throw new FileNotFoundException(fileName + "file is not found");
            }
            props = new Properties();
            props.load(is);
        }catch (IOException e){
            LOGGER.error("load properties file failure",e);
        } finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                    LOGGER.error("close input stream failure",e);
                }
            }
        }
        return props;
    }
    /**
     * 获取字符型属性
     * 原书这里是：
     * return getString(props, key,"");
     *
     * 而实际实现时根据IDE提示删除了冗余参数来调用
     */
    public static String getString(Properties props,String key){
        return getString(props, key,"");
    }
    /**
     * 获取字符串属性（可指定默认值）
     */
    public static String getString(Properties props,String key,String defaultValue){
        String value=defaultValue;
        if(props.containsKey(key)){
            value=props.getProperty(key);
        }
        return value;
    }
    /**
     * 获取数值型属性（默认值为0）
     */
    public static int getInt(Properties props,String key){
        return getInt(props, key,0);
    }
    /**
     * 获取数值型属性（可指定默认值）
     */
    public static int getInt(Properties props,String  key,int defaultValue){
        int value=defaultValue;
        if(props.containsKey(key)){
            value=CastUtil.castInt(props.getProperty(key));
        }
        return value;
    }
    /**
     * 获取boolean型属性（默认值为false）
     */
    public static boolean getBoolean(Properties props,String key){
        return getBoolean(props, key,false);
    }
    /**
     * 获取boolean型属性（可指定默认值）
     */
    public static boolean getBoolean(Properties pros,String key,Boolean defaultValue){
        boolean value=defaultValue;
        if(pros.containsKey(key)){
            value=CastUtil.castBoolean(pros.getProperty(key));
        }
        return value;
    }

}
