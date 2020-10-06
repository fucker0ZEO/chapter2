package org.smart4j.chapter2.helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter2.util.CollectionUtil;
import org.smart4j.chapter2.util.PropsUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 操作数据库助手类
 */
public final class DatabaseHelper {
    private static final ThreadLocal<Connection> CONNECTION_HOLDER;
    private static final QueryRunner QUERY_RUNNER;
    private static final BasicDataSource DATA_SOURCE;
    private static final Logger LOGGER= LoggerFactory.getLogger(DatabaseHelper.class);

    private static final String driver;
    private static final String url;
    private static final String username;
    private static final String password;
    //private static final QueryRunner QUERY_RUNNER=new QueryRunner();
    private static <T> String getTableName(Class<T> entityClass) {
        return entityClass.getSimpleName();
    }

    static {
        CONNECTION_HOLDER = new ThreadLocal<>();
        QUERY_RUNNER = new QueryRunner();

        Properties conf= PropsUtil.loadProps("config.properties");
        driver=conf.getProperty("jdbc.driver");
        url=conf.getProperty("jdbc.url");
        username=conf.getProperty("jdbc.username");
        password=conf.getProperty("jdbc.password");


        DATA_SOURCE=new BasicDataSource();
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(password);

    }

    /**
     * 获取数据库连接
     * @return
     */
    public static Connection getConnection(){
        //<1>
        Connection conn=CONNECTION_HOLDER.get();
        if (conn==null){
            try {
                conn= DATA_SOURCE.getConnection();
            }catch (SQLException e){
                LOGGER.error("get connection failure",e);
            }finally {
                //<2>
                CONNECTION_HOLDER.set(conn);
            }
        }

        return conn;
    }

    /**
     * 关闭数据库连接
     */
    public static void closeConnection(){
        //<1>
        Connection conn=CONNECTION_HOLDER.get();
        if(conn !=null){
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("close connection failure",e);
                throw new RuntimeException(e);
            }finally {
                //<3>
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 查询实体列表
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass,String sql,Object... params){
        List<T> entityList;
        try{
            Connection conn = getConnection();
            entityList=QUERY_RUNNER.query(conn,sql,new BeanListHandler<T>(entityClass),params);
        } catch (SQLException e){
            LOGGER.error("query entity list failure",e);
            throw new RuntimeException(e);
        }finally {
            //<4>
            closeConnection();
        }
        return entityList;
    }

    /**
     * 查询实体
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static<T> T queryEntity(Class<T> entityClass,String sql,Object...params) {
        T entity;

        try {
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("query entity failure", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return entity;

    }

    /**
     * 执行查询语句
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String, Object>> executeQuery(String sql,Object...params){
        List<Map<String, Object>> result;
        try {
            Connection conn=getConnection();
            result=QUERY_RUNNER.query(conn,sql,new MapListHandler(),params);
        }catch (SQLException e){
            LOGGER.error("execute query failure",e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 执行更新语句（包括update,insert,delete）
     * @param sql
     * @param params
     * @return
     */
    public static int executeUpdate(String sql,Object...params){
        int rows=0;
        try {
            Connection conn=getConnection();
            rows=QUERY_RUNNER.update(conn, sql, params);
        }catch (SQLException e){
            LOGGER.error("execute update failure",e);
            throw new RuntimeException(e);
        }finally {
            closeConnection();
        }
        return rows;
    }

    /**
     * 插入实体
     * @param entityClass
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static<T> boolean insertEntity(Class<T> entityClass,Map<String,Object> fieldMap){
        if (CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not insert entity: fieldMap is empty");
            return false;
        }
        String sql="INSERT INTO" + getTableName(entityClass);
        StringBuilder columns=new StringBuilder("(");
        StringBuilder values=new StringBuilder(")");
        for (String fieldName : fieldMap.keySet()){
            columns.append(fieldName).append(",");
            values.append("?,");
        }
        columns.replace(columns.lastIndexOf(","),columns.length(),")");
        values.append("?,");
        sql +=columns +" VALUES " + values;

        Object[] params=fieldMap.values().toArray();
        return executeUpdate(sql,params) ==1;
    }

    /**
     * 更新实体
     * @param entityClass
     * @param id
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static<T> boolean updateEntity(Class<T> entityClass,long id,Map<String,Object> fieldMap){
        if (CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not update entity:fieldMap is empty");
            return false;
        }
        String sql="UPDATE " + getTableName(entityClass) +" SET ";
        StringBuilder columns=new StringBuilder();
        for (String fieldName : fieldMap.keySet()){
            columns.append(fieldName).append("=?, ");
        }
        sql += columns.substring(0,columns.lastIndexOf(",")) + " WHERE id=?";

        List<Object> paramsList=new ArrayList<>();
        paramsList.add(fieldMap.values());
        paramsList.add(id);
        Object[] params=paramsList.toArray();

        return executeUpdate(sql,params) == 1;
    }

    /**
     * 删除实体
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public static <T> boolean deleteEntity(Class<T> entityClass,long id){
        String sql="DELETE FROM " + getTableName(entityClass) + "WHERE id=?";
        return executeUpdate(sql,id) == 1;
    }

    public static void executeSqlFile(String filePath){
        InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        //sourceAsStream(filePath);
        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
        try{
            String sql;
            while ((sql=reader.readLine()) !=null){
                executeUpdate(sql);
            }
        }catch (Exception e){
            LOGGER.error("execute sql file failure",e);
            throw new RuntimeException(e);
        }
    }


}
