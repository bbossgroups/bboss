/*****************************************************************************
 *                                                                           *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     							 *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    * 
 *                                                                           *
 *  The Original Code is tag. The Initial Developer of the Original          *    
 *  Code is biaoping yin. Portions created by biaoping yin are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  biaoping.yin (yin-bp@163.com)                                            * 
 *                                                                           *
 *****************************************************************************/
package com.frameworkset.common.poolman.sql;

import java.io.Serializable;
import java.util.List;

/**
 * 执行批处理插入操作时，保存更新表名、主键信息语句和主键值对象
 * @author biaoping.yin
 * created on 2005-4-16
 * version 1.0 
 * java.lang.Comparable:便于程序对象自动排序
 */
public class UpdateSQL implements Serializable,java.lang.Comparable{
    private String updateSql;
    /**
     * 预编译执行的语句对应的参数表
     * datas<i,data>
     */
    private List datas = null;  
    private String tableName;
    private String dbName;
    
    public final static String TABLE_INFO_UPDATE = "update tableinfo set table_id_value=? where LOWER(table_name)=? and table_id_value <?"  ;
    
    public UpdateSQL(String dbName,String tableName,String updateSql,
    				 List datas)
    {
        this.updateSql = updateSql;
        this.datas = datas;
        this.tableName = tableName;
        this.dbName = dbName;
    }
    
    /**
     * @return Returns the updateSql.
     */
    public String getUpdateSql() {
        return updateSql;
    }
    /**
     * @param updateSql The updateSql to set.
     */
    public void setUpdateSql(String updateSql) {
        this.updateSql = updateSql;
    }
    
    /**
     * 判断两个sql语句是否相等
     */
    public boolean equals(Object updateStatement)
    {
        if(updateStatement == null)
            return false;
//        System.out.println("updateStatement.getClass():"+updateStatement.getClass());
//        UpdateSQL temp = (UpdateSQL)updateStatement;
//        if(temp.getTableName() == null || temp.getPrimaryKey() == null)
//            return false;
//        return this.tableName.equals(temp.getTableName()) && this.primaryKey.equals(temp.getPrimaryKey());
        return false;
            
    }
    /**
     * @return Returns the tableName.
     */
    public String getTableName() {
        return tableName;
    }
    /**
     * @param tableName The tableName to set.
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public List getDatas() {
		return datas;
	}
	
	
}
