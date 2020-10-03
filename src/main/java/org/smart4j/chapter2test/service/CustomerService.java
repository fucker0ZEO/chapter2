package org.smart4j.chapter2test.service;

import org.smart4j.chapter2test.model.Customer;
import org.smart4j.chapter2test.util.PropsUtil;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CustomerService {
    /**
     * 获取客户列表
     */
    public List<Customer> getCustomerList() {
        //TODO

        return null;
    }
    /**
     * 获取客户
     */
    public Customer getCustomer(long id){
        return null;
    }
    /**
     * 创建客户
     */
    public boolean createCustomer(Map<String,Object> fieldMap){
        return false;
    }
    /**
     * 更新客户
     */
    public boolean updateCustomer(long id,Map<String,Object> fieldMap){
        return false;
    }
    /**
     * 删除客户
     */
    public boolean deleteCustomer(long id){
        return false;
    }



    /**public List<Customer> getCustomerList() {
        List<Customer> customerList = null;
        return customerList;
    }**
     public List<Customer> getCustomerList() {
     }
     */
    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        Properties conf= PropsUtil.loadProps("config.properties");
        DRIVER=conf.getProperty("jdbc.driver");
        URL=conf.getProperty("jdbc.url");
        USERNAME=conf.getProperty("jdbc.username");
        PASSWORD=conf.getProperty("jdbc.password");

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e){
           // LOGGER.error("can not load jdbc driver",e);
            //Logger.ERROR("can not load jdbc driver",e);
        }
    }

}
