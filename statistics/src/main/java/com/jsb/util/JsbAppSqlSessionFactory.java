package com.jsb.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;



public class JsbAppSqlSessionFactory {

    //private static Logger log = Logger.getLogger(JlAppSqlSessionFactory.class);

  


    

 

    private static SqlSessionFactory  getSqlSessionFactory(DataSource dataSource,String configfile,String dsdesc){
        Configuration configuration = null;
        XMLConfigBuilder xmlConfigBuilder = null;
        try {
            InputStream inputStream =  Resources.getResourceAsStream(configfile) ;
            xmlConfigBuilder = new XMLConfigBuilder(inputStream,"development");
            inputStream.close();
            xmlConfigBuilder.parse();
           // log.info("Parsed configuration file: '"+configfile+"' ,dataSource : '"+dsdesc+"' success ");
        } catch (Exception ex) {
        	ex.printStackTrace();
           // log.error("Failed to parse config resource: '"+configfile+"'", ex);
        } finally {
            ErrorContext.instance().reset();
        }
        configuration = xmlConfigBuilder.getConfiguration();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        configuration.setEnvironment(environment);
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        return builder.build(configuration);
    }

    public static SqlSessionFactory getJlPISqlSessionFactory() throws SQLException {
        SqlSessionFactory sqlfactory = null;
          
             sqlfactory = getSqlSessionFactory(JsbAppDataSource.getJLPIDataSource(),"com/jsb/util/mybatis-config.xml","JSB");
             
         return sqlfactory;
    }
 

    //@SuppressWarnings("unchecked")
    public static <T> T getMapper(Class<T> type,SqlSessionFactory sqlSessionFactory){
        SqlSession session = sqlSessionFactory.openSession(true);
        //T result =  session.getMapper(type);
        //session.close();
        //SQLSESSION.put(sqlSessionId,session);
        return session.getMapper(type);
    }

    /*public static void closeSqlSession(String sqlSessionId){
        //log.info("sqlSessionId"+SQLSESSION);
        if(SQLSESSION.containsKey(sqlSessionId)){
            SqlSession session = (SqlSession) SQLSESSION.remove(sqlSessionId);;
            session.close();

            log.info("sqlSessionId"+SQLSESSION);
        }
    }*/
    
    
    
    public static void main(String args[]) throws Exception{
    	SqlSessionFactory sql =	getJlPISqlSessionFactory();
    	SitcMapper mapper =  	getMapper(SitcMapper.class,sql);
    	String excel = "2000" ,tableName = "t_sitc_13";
    	List<SitcModel> models=	mapper.getListByModel(tableName);
    	
    	StringBuffer sb = new StringBuffer();
    	/*Map<Integer,Integer> map = new HashMap<Integer,Integer>();
    	for(SitcModel st : models){
    		map.put(st.getCountryid(), st.getNrcaq());
    	}*/
    	//System.out.println(map);
    	
    	Map<String,Double> map = new HashMap<String,Double>();
    	sb.append(" ");
    	for(SitcModel xx : models){
    		sb.append(",").append(xx.getCountryid());
    	}sb.append("\n");
    	Integer tmp = 0;
    	String key = "",key1 = null;
    	for(SitcModel yyy : models){
    		sb.append(yyy.getCountryid());
    		
    		for(SitcModel xx : models){
                tmp = xx.getNrcaq();
                key = yyy.getCountryid()+"-"+xx.getCountryid();
                key1 = xx.getCountryid() +"-"+yyy.getCountryid();
                
                if(yyy.getCountryid().equals(xx.getCountryid())){
                	sb.append(",").append(1);	
                }else{
                	if(map.containsKey(key)){
                    	sb.append(",").append(map.get(key));	
                    }else if(map.containsKey(key1)){
                    	sb.append(",").append(map.get(key1));	                	
                    }else{
                    	Integer gtsl = mapper.getCount(xx.getCountryid(), yyy.getCountryid(),tableName);
                    	Double gt = Math.min(BigDecimalUtil.div(gtsl,tmp), BigDecimalUtil.div(gtsl,yyy.getNrcaq()));
                    	map.put(key1, gt);
                    	map.put(key, gt);
            			sb.append(",").append(gt);	
                    }
                }
                
                
                //System.out.println(yyy.getNrcaq()+""+tmp+"=" +(yyy.getNrcaq() + tmp)/tmp );
                
    		}sb.append("\n");
    	}
    	File target = new File("d:/xxx/");
    	target.mkdirs();
    	//target.getParent().mkdirs();
    	Writer osw = new OutputStreamWriter(new FileOutputStream(target.getPath()+"/"+excel+".csv"),"UTF-8");
    	osw.write(sb.toString());
    	osw.close();
    }
}
