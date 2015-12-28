package com.frameworkset.common.poolman.handle;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.frameworkset.util.ClassUtil;

import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.ResultMap;
import com.frameworkset.common.poolman.StatementInfo;
import com.frameworkset.common.poolman.sql.PoolManResultSetMetaData;
import com.frameworkset.common.poolman.util.SQLUtil;
import com.frameworkset.orm.engine.model.SchemaType;

/**
 * 
 * 
 * <p>Title: RowHandler.java</p>
 *
 * <p>Description: 行处理器</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * @Date Oct 22, 2008 9:35:14 PM
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class RowHandler<T> {
	/**
	 * 对已经处理好的行记录进行处理的逻辑
	 * @param rowValue 需要存放record记录中的列信息的对象，目前有两种主要的类型：普通java值对象，StringBuffer对象（用来拼接自定义的xml串）
	 * @param record 存放了当前记录的信息，包含查询列表中的所有列信息，例如：select id,name from test,那么Record中存放了列id，name的值
	 *               可以通过以下的方式获取：
	 *               record.getInt("id");//其中列名id的大小写不限制
	 *               record.getInt("name");//其中列名name的大小写不限制
	 */
	public abstract void handleRow(T rowValue,Record record) throws Exception;
	
//	public abstract T handleField_(Record record) throws Exception;
	
	protected PoolManResultSetMetaData meta;
        protected String dbname; 
        protected StatementInfo stmtInfo;
        public void init(StatementInfo stmtInfo,PoolManResultSetMetaData meta,String dbname)
        {
            this.meta = meta;
            this.dbname = dbname;
            this.stmtInfo = stmtInfo;
        }
        
        public void destroy()
        {
            this.meta = null;
            this.dbname = null;
        }
        
        public SchemaType getSchemaType(int clindex)
        {
           if(meta == null)
           {
               throw new RowHandlerException("源数据对象[meta]未初始化,无法进行行处理.");
           }
            try {
                int sqltype = meta.getColumnType(clindex);
                String typename = meta.getColumnTypeName(clindex);
                SchemaType schemaType = SQLUtil.getSchemaType(dbname, sqltype,typename); 
                return schemaType;
            } catch (Exception e) {
                
//                e.printStackTrace();
                throw new RowHandlerException(e);
            }
            
        }
        public SchemaType getSchemaType(String colName)
        {
           if(meta == null)
           {
               throw new RowHandlerException("源数据对象[meta]未初始化,无法进行行处理.");
           }
            try {
                int index = seekIndex(colName);
                int sqltype = meta.getColumnTypeByIndex(index );
                String typename = meta.getColumnTypeNameByIndex(index );
                SchemaType schemaType = SQLUtil.getSchemaType(dbname, sqltype,typename); 
                return schemaType;
            }
            catch(RowHandlerException e)
            {
                throw e;
            }
            catch (Exception e) {
                
//                e.printStackTrace();
                throw new RowHandlerException(e);
            }
            
        }
        private int seekIndex(String colName)
        {
//            String temp = colName.toUpperCase();
//            String[] columnLabel_uppers = meta.get_columnLabel_upper();
//            for(int i = 0; i < columnLabel_uppers.length ; i ++)
//            {
//                if(columnLabel_uppers[i].equals(temp))
//                    return i;
//            }
//            throw new RowHandlerException("查询结果中不存在列[" + colName + "].");
        	return meta.seekIndex(colName);
        }
        
        public   <V> V buildValueObject(ResultSet rs,
    			Class<V> valueObjectType) throws SQLException
    	{
    		
    		return ResultMap.buildValueObject(  rs,
    				  valueObjectType, 
    				  stmtInfo) ;
    	}
	

}
