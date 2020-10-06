package org.smart4j.chapter2.service;

import org.smart4j.chapter2.helper.DatabaseHelper;
import org.smart4j.chapter2.model.Customer;
import org.smart4j.chapter2.util.PropsUtil;

import java.util.List;
import java.util.Map;
import java.util.Properties;


//ort com.sun.media.jfxmedia.logging.Logger;

public class CustomerService {
    /**
     * 获取客户列表

     public List<Customer> getCustomerList() {
     //TODO
     return null;
     }





     */



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

    public Customer getCustomer(long id){
        return null;
    }

    /*public boolean createCustomer(Map<String,Object> fieldMap){
        return false;
    }

    public boolean updateCustomer(long id,Map<String,Object> fieldMap){
        return false;
    }


    */





    static {
        Properties conf= PropsUtil.loadProps("config.properties");
        DRIVER=conf.getProperty("jdbc.driver");
        URL=conf.getProperty("jdbc.url");
        USERNAME=conf.getProperty("jdbc.username");
        PASSWORD=conf.getProperty("jdbc.password");

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e){

            //LOGGER.error("can not load jdbc driver",e);
            //Logger.ERROR("can not load jdbc driver",e);
        }
    }

    /**
     * 获取客户列表
     * @return
     */
    public List<Customer> getCustomerList() {

        //Connection conn=DatabaseHelper.getConnection();
        //Connection conn=DatabaseHelper.getConnection();

        String sql = "SELECT * FROM customer";
        return DatabaseHelper.queryEntityList(Customer.class, sql);

    }

    /**
     *创建客户
     * @param fieldMap
     * @return
     */

    public  boolean createCustomer(Map<String,Object> fieldMap){
        return DatabaseHelper.insertEntity(Customer.class,fieldMap);
    }

    /**
     * 更新客户
     * @param id
     * @param fieldMap
     * @return
     */
    public boolean updateCustomer(long id,Map<String,Object>fieldMap){
        return DatabaseHelper.updateEntity(Customer.class,id,fieldMap);
    }

    /**
     * 删除客户
     * @param id
     * @return
     */

    public boolean deleteCustomer(long id){
        return DatabaseHelper.deleteEntity(Customer.class,id);
    }


}
