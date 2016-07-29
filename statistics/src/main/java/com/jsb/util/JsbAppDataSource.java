package com.jsb.util;


import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.logging.jdbc.ConnectionLogger;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class JsbAppDataSource {

       // private static Logger log = Logger.getLogger(JlAppDataSource.class);

       
        public static Connection getConnection() throws SQLException {
            return  getConnection(getJLPIDataSource());
        }
 


        public static Connection getConnection(DataSource ds) throws SQLException {
            if("true".equalsIgnoreCase(JsbAppResources.getProperty("JDBC_LOGGER"))){
                return  ConnectionLogger.newInstance(ds.getConnection(),LogFactory.getLog(JsbAppDataSource.class.getName()),0);
            }else{ 
                return ds.getConnection();
           }
        }

        public static DataSource getJLPIDataSource() throws SQLException{
            PooledDataSource ds = null;
            /*if(DATA_SOURCES.containsKey(Globals.JLPIDS)){
                ds = (PooledDataSource)DATA_SOURCES.get(Globals.JLPIDS);
            }else{*/
                ds = new PooledDataSource(JsbAppResources.getProperty("JLPI_DRIVER"),JsbAppResources.getProperty("JLPI_DBURL"),JsbAppResources.getProperty("JLPI_DBUSERNAME"),JsbAppResources.getProperty("JLPI_DBPASSWD"));
                ds.setDefaultAutoCommit(true);
               /* DATA_SOURCES.put(Globals.JLPIDS,ds);
            }*/
            return ds;
        }
 

}
