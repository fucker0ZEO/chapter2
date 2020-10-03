package org.smart4j.chapter2test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.smart4j.chapter2test.model.Customer;
import org.smart4j.chapter2test.service.CustomerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CustomerService 单元测试
 */
public class CustomerServiceTest {
    private final CustomerService customerService;

    public CustomerServiceTest() {
        customerService=new CustomerService();
    }

    @Before
    public void init() {
        //初始化数据库
    }

    @Test
    public void getCustomerListTest() throws Exception{
        //List<Customer> customerList=customerService.getCustomerList();
        //Assert.assertArrayEquals(2,customerList.size());
        List<Customer> customerList=customerService.getCustomerList();
        Assert.assertEquals(2,customerList.size());
    }
    @Test
    public void getCustomerTest() throws Exception{
        long id=1;
        Customer customer=customerService.getCustomer(id);
        Assert.assertNotNull(customer);
    }
    @Test
    public void createCustomerTest() throws Exception{
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("name","customer100");
        fieldMap.put("contact","John");
        fieldMap.put("telephone","13512345678");
        boolean result=customerService.createCustomer(fieldMap);
        Assert.assertTrue(result);
    }
    @Test
    public void updateCustomerTest() throws Exception{
        long id=1;
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("contact","Eric");
        boolean result=customerService.updateCustomer(id,fieldMap);
        Assert.assertTrue(result);
    }

}

