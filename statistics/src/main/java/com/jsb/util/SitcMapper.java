package com.jsb.util;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * TSitcDAO接口
 *
 * @author admin
 */
public interface SitcMapper{

	//------------------请在此添加自定义方法（开始）------------------
	//------------------请在此添加自定义方法（结束）------------------
	
	public List<SitcModel> getListByModel(@Param("tableName")String tableName);
	
	
	public int getCount(@Param("ctry1") Integer ctry1,@Param("ctry2") Integer ctry2,@Param("tableName")String tableName);
    
}
