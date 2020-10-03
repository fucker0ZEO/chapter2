package org.smart4j.chapter2test.util;

import org.slf4j.LoggerFactory;

import javax.imageio.IIOException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public final class PropsUtil {
    /**
     * 这里和原书略有不同，做了一个类型转换 (Logger)
     */
    private static final Logger LOGGER= (Logger) LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 加载属性文件
     */
    public static Properties loadProps(String fileName){
        Properties props=null;
        InputStream is=null;
        try {
            is=Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if(is == null){
                try {
                    throw new FileNotFoundException(fileName+"file is not found");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            props=new Properties();
            try {
                props.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IIOException e){
            LOGGER.error("load properties file failure",e);
            
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
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
        return getString(props, key);
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

}
