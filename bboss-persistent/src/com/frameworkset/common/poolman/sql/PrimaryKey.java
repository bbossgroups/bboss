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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.NestedSQLException;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.util.JDBCPool;
import com.frameworkset.common.poolman.util.SQLManager;
import com.frameworkset.orm.adapter.DB;
import com.frameworkset.orm.transaction.JDBCTransaction;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.orm.transaction.TransactionManager;

/**
 * 封装表的主键信息 可能出现的问题： 1.如果通过其他的方式往数据库的相同表中插入数据，会造成主键冲突
 * 2.如果不同的应用同时往同一个数据库的相同表中插入数据，会造成主键冲突
 * 
 * @author biaoping.yin created on 2005-3-29 version 1.0
 */
public class PrimaryKey
{
	private static Logger log = Logger.getLogger(PrimaryKey.class);

	/** 多主键时主键及其当前值保存在本变量中 */
	private Map primaryKeys;

	/** 表的自增值 */
	private int increment = 1;

	/** 表的主键名称 */
	private String primaryKeyName;

	/** 表的名称 */
	private String tableName;

	/** 表的主键当前值 */
	private long curValue;

	/** 表的主键类型，缺省为int */
	private String type = "int";
	
	private String metaType = "int";

	/** 数据库连接池的名称 */
	private String dbname = null;

	/**
	 * 数据库表主键的前缀，该属性只有在主键类型为string时才起作用，例如： 设定表a的主键id的类型为string 则可指定主键为
	 * 
	 */
	private String prefix = "";

	private DB dbAdapter = null;

	private String maxSql = "";

	/**
	 * 生成数据库表主键模式 0:表示自动 1：表示复合
	 */
	private int keygenerator_mode = 0;
	
	private Object customKey = null;
	
	private boolean hasTableinfo = true;
	
	boolean synsequece = false;
	private String seqfunction;

	/**
	 * 主键的生成机制
	 */
	private String generator;
	
	private String select ;

	/**
	 * 构造函数，构建表的基本信息
	 * 
	 * @param Connection con 由biaoping.yin添加
	 * @param dbname
	 *            表所属的数据库链接池
	 * @param tableName
	 *            表的名称
	 * @param primaryKeyName
	 *            表的主键名称
	 * @param increment
	 *            表主键自增值
	 * @param curValue
	 *            表主键当前值
	 */
	public PrimaryKey(String dbname, String tableName, String primaryKeyName,
			Object customKey,Connection con)
	{
		this.dbname = dbname;
		dbAdapter = SQLManager.getInstance().getDBAdapter(dbname);
		this.tableName = tableName;
		this.primaryKeyName = primaryKeyName;
	
		
		JDBCPool pool = (JDBCPool) (SQLManager.getInstance().getPool(dbname));
		synsequece = pool.getJDBCPoolMetadata().synsequence();
		this.seqfunction = pool.getJDBCPoolMetadata().getSeqfunction();
		try{
			TableMetaData table = pool.getTableMetaData(con,tableName);
			if(table != null)
			{
				if(primaryKeyName != null)
				{
					ColumnMetaData cd = pool.getColumnMetaData(con,tableName,primaryKeyName);
					
					int type_ = cd.getDataType();
					this.setType_(type_);
				}
				else
				{
					Set keys = table.getPrimaryKeys();
					if(keys != null)
					{
						Iterator keyitrs = keys.iterator();
						if(keyitrs.hasNext())
						{
							PrimaryKeyMetaData key = (PrimaryKeyMetaData)keyitrs.next();
							this.primaryKeyName = key.getColumnName().toLowerCase();
							this.setType_(key.getColumn().getDataType());
						}
					}
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		String mode = pool.getKeygenerate();
		if (mode.trim().equalsIgnoreCase("auto"))
			keygenerator_mode = 0;
		else if (mode.trim().equalsIgnoreCase("composite"))
			keygenerator_mode = 1;
		this.customKey = customKey;

	}
	
	private void setType_(int type_)
	{
		this.type = getJavaType( type_);
		this.metaType = type;
	}
	
	public static String getJavaType(int sqltype_)
	{
		String javatype = "int";
		switch(sqltype_)
		{
			case Types.INTEGER:
				javatype = "int";break;
			case Types.NUMERIC:
				javatype = "long";break;
			case Types.SMALLINT:
				javatype = "int";break;
			case Types.DECIMAL:
				javatype = "int";break;
			case Types.DOUBLE:
				javatype = "long";break;
			case Types.FLOAT:
				javatype = "long";break;
				
			case Types.VARCHAR:
				javatype = "string";
				
			
		}
		return javatype;
	}
	
	

	public PrimaryKey(String dbname, String tableName, String primaryKeyName,
			int increment, long curValue, String type, String prefix,
			String maxSql,Connection con)
	{
		this.dbname = dbname;
		dbAdapter = SQLManager.getInstance().getDBAdapter(dbname);
		this.tableName = tableName;
		this.primaryKeyName = primaryKeyName;
		this.increment = increment;
		this.curValue = curValue;
		JDBCPool pool = (JDBCPool) (SQLManager.getInstance().getPool(dbname));
		synsequece = pool.getJDBCPoolMetadata().synsequence();
		this.seqfunction = pool.getJDBCPoolMetadata().getSeqfunction();
		String mode = pool.getKeygenerate();
		if (mode.trim().equalsIgnoreCase("auto"))
			keygenerator_mode = 0;
		else if (mode.trim().equalsIgnoreCase("composite"))
			keygenerator_mode = 1;
		if (type != null && !type.trim().equals(""))
			this.type = type;
		if (prefix != null && !prefix.trim().equals(""))
			this.prefix = prefix;
		this.maxSql = maxSql;
		if(type.equals("sequence"))
		{
			ColumnMetaData cd = pool.getColumnMetaData(con,tableName,primaryKeyName);
			if(cd != null)
			{
				int type_ = cd.getDataType();
				this.metaType = PrimaryKey.getJavaType(type_);
			}
			else
			{
				metaType = "int";
			}
		}
		else if(type.equals("uuid"))
		{
			ColumnMetaData cd = pool.getColumnMetaData(con,tableName,primaryKeyName);
			if(cd != null)
			{
				int type_ = cd.getDataType();
				this.metaType = PrimaryKey.getJavaType(type_);
			}
			else
			{
				metaType = "string";
			}
		}
		else 
		{
			this.metaType = type;
		}
		

	}
	
	public PrimaryKey(String dbname, String tableName, String primaryKeyName,
			int increment, long curValue, String type, String prefix,
			String maxSql,String generator,Connection con)
	{
		this.dbname = dbname;
		this.generator = generator;
		dbAdapter = SQLManager.getInstance().getDBAdapter(dbname);
		this.tableName = tableName;
		this.primaryKeyName = primaryKeyName;
		this.increment = increment;
		this.curValue = curValue;
		JDBCPool pool = (JDBCPool) (SQLManager.getInstance().getPool(dbname));
		synsequece = pool.getJDBCPoolMetadata().synsequence();
		this.seqfunction = pool.getJDBCPoolMetadata().getSeqfunction();
		String mode = pool.getKeygenerate();
		if (mode.trim().equalsIgnoreCase("auto"))
			keygenerator_mode = 0;
		else if (mode.trim().equalsIgnoreCase("composite"))
			keygenerator_mode = 1;
		if (type != null && !type.trim().equals(""))
			this.type = type;
		if (prefix != null && !prefix.trim().equals(""))
			this.prefix = prefix;
		this.maxSql = maxSql;
		
		if(type.equals("sequence"))
		{
			ColumnMetaData cd = pool.getColumnMetaData(con,tableName,primaryKeyName);
			if(cd != null)
			{
				int type_ = cd.getDataType();
				this.metaType = PrimaryKey.getJavaType(type_);
			}
			else
			{
				metaType = "int";
			}
		}
		else if(type.equals("uuid"))
		{
			ColumnMetaData cd = pool.getColumnMetaData(con,tableName,primaryKeyName);
			if(cd != null)
			{
				int type_ = cd.getDataType();
				this.metaType = PrimaryKey.getJavaType(type_);
			}
			else
			{
				metaType = "string";
			}
		}
		else 
		{
			this.metaType = type;
		}

	}

	// public static long parserSequence(String sequence,String prefix,String
	// type,String table_name)
	// {
	// long new_table_id_value = 0;
	// if(type == null || type.trim().equals("") ||
	// type.trim().equalsIgnoreCase("int")
	// || type.trim().equalsIgnoreCase("integer")
	// || type.trim().equalsIgnoreCase("java.lang.Integer")
	// || type.trim().equalsIgnoreCase("long")
	// || type.trim().equalsIgnoreCase("java.lang.long")
	// || type.trim().equalsIgnoreCase("short"))
	// new_table_id_value = sequence == null ||
	// sequence.equals("")?0L:Long.parseLong(sequence);
	// else
	// {
	// String temp_id = sequence;
	//        	
	// if(prefix == null || prefix.trim().equals("") )
	// {
	// log.debug("tableinfo中没有指定[" + table_name + "]的主键前缀'table_id_prefix'字段");
	// }
	// else
	// {
	// if(temp_id != null && temp_id.length() > prefix.length())
	// temp_id = temp_id.substring(prefix.trim().length());
	//        		
	//        		
	// }
	// try
	// {
	// if(temp_id != null)
	// new_table_id_value = Integer.parseInt(temp_id);
	// }
	// catch(Exception e)
	// {
	// log.error("tableinfo中没有指定[" + table_name +
	// "]的主键前缀'table_id_prefix'字段,主键值为["+ temp_id + "]不是合法的数字。");
	// //e.printStackTrace();
	// new_table_id_value = 0;
	// }
	// }
	// return new_table_id_value;
	//    
	// }
	 
	/**
	 * added by biaoping.yin on 2008.05.29
	 */
	public PrimaryKey() {
		// TODO Auto-generated constructor stub
	}

	private boolean exist(Connection con,long curValue) throws SQLException
	{
		String select = "select count(1) from " + this.tableName  + " where " + this.primaryKeyName + "=?" ;
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
//			String update = "update tableinfo set table_id_value=" + this.curValue +" where table_name='"+ tableName.toLowerCase() + "' and table_id_value <" + this.curValue  ;
			dbUtil.preparedSelect(this.dbname,select);
			if (this.metaType.equals("int") || this.metaType.equals("java.lang.Integer")
					|| this.metaType.equals("java.lang.integer")
					|| this.metaType.equalsIgnoreCase("integer"))
			{
				dbUtil.setInt(1,(int)curValue);
			}
			else if (this.metaType.equals("java.lang.Long") || this.metaType.equals("java.lang.long")
					|| this.metaType.equalsIgnoreCase("long"))
			{
				dbUtil.setLong(1,curValue);
			}
			
			

			else if (this.metaType.equals("java.lang.String")
					|| this.metaType.equalsIgnoreCase("string"))
			{
				
				dbUtil.setString(1,this.prefix + curValue + "");				
			}

			else
			{
				dbUtil.setString(1,this.prefix + curValue + "");
			}
			
			
			dbUtil.executePrepared(con);
			if(dbUtil.getInt(0,0) > 0)
				return true;
		} catch (SQLException e1) {
			
			e1.printStackTrace();
			throw e1;
		
		} catch (Exception e1) {
			
			e1.printStackTrace();
			throw new SQLException(e1.getMessage());
		}
		finally
		{
			dbUtil.resetPrepare();
		}
		return false;
		
 	}

	/**
	 * 生成表的主键值
	 * 
	 * @return Sequence
	 * @throws SQLException 
	 */
	public Sequence generateObjectKey() throws SQLException
	{
		return generateObjectKey(null);
	}
	/**
	 * 生成表的主键值
	 * 
	 * @return Sequence
	 * @throws SQLException 
	 */
	public Sequence generateObjectKey(Connection con) throws SQLException 
	{
		
			Sequence sequence = new Sequence();
			if (type.equals("sequence"))
			{
				long curValue = this.curValue;
//				String sql = "select " + this.generator + ".nextval from dual";
				do
				{
//					PreparedDBUtil dbutil = new PreparedDBUtil();
					try {
//						if()
//						dbutil.preparedSelect(this.dbname, sql);
//						dbutil.executePrepared(con);
//						if(dbutil.size() <= 0)
//						{
////							System.out.println("select " + this.generator + ".nextval from dual");
//							throw new SQLException("[select " + this.generator + ".nextval from dual] from [" + dbname + "] failed:retrun records is 0.");
//						}
//						curValue = dbutil.getInt(0,0);
						
						curValue = this.dbAdapter.getNextValue(this.seqfunction,generator, con, this.dbname);
						
						if(this.synsequece && this.exist(con,curValue) )
							continue;
							
					} catch (SQLException e) {
						throw e;
					}
//					sequence.setPrimaryKey(new Long(curValue));
//					sequence.setSequence(curValue);
					break;
				}
				while(true);
				this.curValue = curValue;
				if (this.metaType.equals("int") || this.metaType.equals("java.lang.Integer")
						|| this.metaType.equals("java.lang.integer")
						|| this.metaType.equalsIgnoreCase("integer"))
				{
					sequence.setPrimaryKey(new Long(curValue));
					sequence.setSequence(curValue);
					return sequence;
				}
				if (this.metaType.equals("java.lang.Long") || this.metaType.equals("java.lang.long")
						|| this.metaType.equalsIgnoreCase("long"))
				{
					sequence.setPrimaryKey(new Long(curValue));
					sequence.setSequence(curValue);
					return sequence;
				}
				
				

				if (this.metaType.equals("java.lang.String")
						|| this.metaType.equalsIgnoreCase("string"))
				{
					sequence.setPrimaryKey(this.prefix + curValue + "");
					sequence.setSequence(curValue);
					return sequence;
				}

				else
				{
					sequence.setPrimaryKey(this.prefix + curValue + "");
					sequence.setSequence(curValue);
					return sequence;
				}
			}
			else if(type.equals("uuid"))
			{
//			    UUID.randomUUID().toString();
			    sequence.setPrimaryKey(UUID.randomUUID().toString());
			    sequence.setSequence(this.curValue);
			    return sequence;
			}
			else
			{
				synchronized (this)
				{
					switch (keygenerator_mode)
					{
						case 0:// 自动模式
			
							curValue += increment;
			
							break;
						case 1:
							
							curValue += increment;
							synchroDB(con);
							break;
					}
					if (this.metaType.equals("int") || this.metaType.equals("java.lang.Integer")
							|| this.metaType.equals("java.lang.integer")
							|| this.metaType.equalsIgnoreCase("integer"))
					{
						sequence.setPrimaryKey(new Long(curValue));
						sequence.setSequence(curValue);
						return sequence;
					}
					if (this.metaType.equals("java.lang.Long") || this.metaType.equals("java.lang.long")
							|| this.metaType.equalsIgnoreCase("long"))
					{
						sequence.setPrimaryKey(new Long(curValue));
						sequence.setSequence(curValue);
						return sequence;
					}
					
					

					if (this.metaType.equals("java.lang.String")
							|| this.metaType.equalsIgnoreCase("string"))
					{
						sequence.setPrimaryKey(this.prefix + curValue + "");
						sequence.setSequence(curValue);
						return sequence;
					}

					else
					{
						sequence.setPrimaryKey(this.prefix + curValue + "");
						sequence.setSequence(curValue);
						return sequence;
					}
				}
			}

		// return curValue;
	}

	public Sequence generateObjectKey(String type, String prefix) throws SQLException
	{
		 return  generateObjectKey(type, prefix,null);
	}
	/**
	 * 根据主键类型和主键的前缀生成表的主键
	 * 
	 * @param type
	 *            表的主键类型
	 * @param prefix
	 *            表的主键前缀
	 * @return
	 * @throws SQLException 
	 */

	public Sequence generateObjectKey(String type, String prefix,Connection con) throws SQLException
	{
		
			Sequence sequence = new Sequence();

			if (type.equals("sequence")) //不需要锁
			{
				long curValue = this.curValue;
//				String sql = "select " + this.generator + ".nextval from dual";
				do
				{
//					PreparedDBUtil dbutil = new PreparedDBUtil();
					try {
//						dbutil.preparedSelect(this.dbname,sql);
//						dbutil.executePrepared(con);
//						curValue = dbutil.getLong(0,0);
//						if(this.synsequece && this.exist(con,curValue))
//							continue;
						curValue = this.dbAdapter.getNextValue(this.seqfunction,generator, con, this.dbname);
						if(this.synsequece && this.exist(con,curValue))
							continue;
							
					} catch (SQLException e) {
						
						e.printStackTrace();
						throw e;
					}
//					sequence.setPrimaryKey(new Long(curValue));
//					sequence.setSequence(curValue);
//					return sequence;
					break;
				}
				while(true);
				this.curValue = curValue;
				if (this.metaType.equals("int") || this.metaType.equals("java.lang.Integer")
						|| this.metaType.equals("java.lang.integer")
						|| this.metaType.equalsIgnoreCase("integer"))
				{
					sequence.setPrimaryKey(new Long(curValue));
					sequence.setSequence(curValue);
					return sequence;
				}
				if (this.metaType.equals("java.lang.Long") || this.metaType.equals("java.lang.long")
						|| this.metaType.equalsIgnoreCase("long"))
				{
					sequence.setPrimaryKey(new Long(curValue));
					sequence.setSequence(curValue);
					return sequence;
				}
				

				if (this.metaType.equals("java.lang.String")
						|| this.metaType.equalsIgnoreCase("string"))
				{
					sequence.setPrimaryKey(this.prefix + curValue + "");
					sequence.setSequence(curValue);
					return sequence;
				}

				else
				{
					sequence.setPrimaryKey(this.prefix + curValue + "");
					sequence.setSequence(curValue);
					return sequence;
				}
			}
			else if(type.equals("uuid"))
            {
//              UUID.randomUUID().toString();
                sequence.setPrimaryKey(UUID.randomUUID().toString());
                sequence.setSequence(this.curValue);
                return sequence;
            }
			else //需要锁
			{
				synchronized (this)
				{
					switch (keygenerator_mode)
					{
						case 0:// 自动模式
							curValue += increment;
							break;
						case 1:
							curValue += increment;
							synchroDB(con);
							break;
					}
					if (this.metaType.equals("int") || this.metaType.equals("java.lang.Integer")
							|| this.metaType.equals("java.lang.integer")
							|| this.metaType.equalsIgnoreCase("integer"))
					{
						sequence.setPrimaryKey(new Long(curValue));
						sequence.setSequence(curValue);
						return sequence;
					}
					if (this.metaType.equals("java.lang.Long") || this.metaType.equals("java.lang.long")
							|| this.metaType.equalsIgnoreCase("long"))
					{
						sequence.setPrimaryKey(new Long(curValue));
						sequence.setSequence(curValue);
						return sequence;
					}
					

					if (this.metaType.equals("java.lang.String")
							|| this.metaType.equalsIgnoreCase("string"))
					{
						sequence.setPrimaryKey(this.prefix + curValue + "");
						sequence.setSequence(curValue);
						return sequence;
					}
					else
					{
						sequence.setPrimaryKey(this.prefix + curValue + "");
						sequence.setSequence(curValue);
						return sequence;
					}
				}
			}

//			Sequence sequence = new Sequence();		
		

		// return curValue;
	}

	// /**
	// * 生成表的主键值
	// * @return long
	// */
	// public long generateKey()
	// {
	// synchronized(this)
	// {
	//
	// switch(keygenerator_mode)
	// {
	// case 0://自动模式
	// curValue += increment;
	// break;
	// case 1:
	// curValue += increment;
	// synchroDB();
	// break;
	// }
	//
	// return curValue;
	// }
	//
	// //return curValue;
	// }

	/**
	 * 同步当前缓冲中存放的对应表的最大值与数据库表中最大值
	 * @throws SQLException 
	 */
	protected void synchroDB(Connection con) throws SQLException
	{
//		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		JDBCTransaction tx = null;
		boolean outcon = true;
		
		try
		{
			if(con == null)
			{
				tx = TransactionManager.getTransaction();
				if(tx == null)
				{
					con = SQLManager.getInstance().requestConnection(dbname);
				}
				else
				{
					con = tx.getConnection(dbname);
				}
				outcon = false;
			}
			stmt = con.createStatement();

			rs = stmt.executeQuery(maxSql);
			long temp = 0;
			if (rs.next())
			{
				temp = rs.getLong(1);
			}
			if (temp >= this.curValue)
			{
				curValue = temp + 1;
			}
			
//			else if(temp < this.curValue )
//			{
//				
//				curValue = temp + 1;
//				
//				System.out.println("curValue=========:" + curValue);
//			}
		}
		catch (SQLException e)
		{
//			log.error("同步当前缓冲中表[" + tableName + "]的主键[" + primaryKeyName
//					+ "]最大值与数据库该表主键最大值失败，系统采用自动产生的主键：" + e.getMessage());
			throw new NestedSQLException("同步当前缓冲中表[" + tableName + "]的主键[" + primaryKeyName
					+ "]最大值与数据库该表主键最大值失败，系统采用自动产生的主键：", e);
		} catch (TransactionException e) {
			
//			e.printStackTrace();
//			log.error("同步当前缓冲中表[" + tableName + "]的主键[" + primaryKeyName
//					+ "]最大值与数据库该表主键最大值失败，系统采用自动产生的主键：" + e.getMessage());
			throw new NestedSQLException("同步当前缓冲中表[" + tableName + "]的主键[" + primaryKeyName
					+ "]最大值与数据库该表主键最大值失败，系统采用自动产生的主键：" , e);
//			throw new SQLException(e.getMessage());
		}
		finally
		{
			if(con != null)
			{
				JDBCPool.closeResources(stmt, rs);
				if(!outcon)
				{
					
					if(tx == null)
					{
						JDBCPool.closeConnection(con);
					}
					
				}
			}
			con = null;
		}
	}

	public static String changeID(Serializable id, String dbName, String type)
	{

		if (type.equals("int") || type.equals("java.lang.Integer")
				|| type.equals("java.lang.integer")
				|| type.equalsIgnoreCase("integer"))
		{

			return id.toString()  ;
		}
		if (type.equals("java.lang.Long") || type.equals("java.lang.long")
				|| type.equalsIgnoreCase("long"))
		{

			return id.toString();
		}

		if (type.equals("java.lang.String") || type.equalsIgnoreCase("string"))
		{
			char stringDelimiter = SQLManager.getInstance()
					.getDBAdapter(dbName).getStringDelimiter();

			return "" + stringDelimiter + id + stringDelimiter;
		}

		else
		{
			char stringDelimiter = SQLManager.getInstance()
					.getDBAdapter(dbName).getStringDelimiter();

			return "" + stringDelimiter + id + stringDelimiter;
		}

	}

	/**
	 * 恢复表的主键值，当异常发生或插入不成功时执行该方法
	 * 
	 * @return long
	 */
	public long restoreKey(Object oldValue)
	{
		synchronized (this)
		{
			long temp = getKeyID(oldValue);
			if (curValue == temp)
				curValue -= increment;
		}

		return curValue;

	}

	public long getKeyID(Object key)
	{
		long temp = 0;
		if (key instanceof Long)
			temp = ((Long) key).longValue();
		else
		{
			String t_ = key.toString();
			t_ = t_.substring(this.prefix.length());
			try
			{
				temp = Long.parseLong(t_);
			}
			catch (Exception e)
			{
				temp = 0;
			}
		}
		return temp;
	}

	/**
	 * 当数据插入操作成功的时候设置当前的主键值到缓冲和tableinfo表中
	 * 
	 * @param newValue
	 * @param stmt
	 * @param updateSql
	 * @throws SQLException
	 */
	public synchronized void setCurValue(long newValue, Statement stmt,
			String updateSql) throws SQLException
	{
		{
			// 当新值比原来的值要小时才执行更新操作，否则不执行
			if (curValue < newValue)
			{
				//stmt.executeUpdate(updateSql);
				this.curValue = newValue;
			}
		}
	}
	
//	private final static String update = "update tableinfo set table_id_value=? where upper(table_name)=? and table_id_value <?"  ;
	/**
	 * 更新表的主键，当单独获取数据库的主键时，需要在生成主键后调用本方法
	 * 以便同步tableinfo表中的信息
	 * @throws SQLException 
	 *
	 */
	public void updateTableinfo(Connection con) throws SQLException
	{
//		PreparedDBUtil dbUtil = new PreparedDBUtil();
//		try {
////			String update = "update tableinfo set table_id_value=" + this.curValue +" where table_name='"+ tableName.toLowerCase() + "' and table_id_value <" + this.curValue  ;
//			dbUtil.preparedUpdate(this.dbname,update);
//			dbUtil.setInt(1,(int)this.curValue);
//			dbUtil.setString(2,this.tableName.toUpperCase());
//			dbUtil.setInt(3,(int)this.curValue);
//			dbUtil.executePrepared(con);
//		} catch (SQLException e1) {
//			throw e1;
////			e1.printStackTrace();
//		
//		} catch (Exception e1) {
//			throw new SQLException (e1.getMessage());
//	//		e1.printStackTrace();
//		}
//		finally
//		{
//			dbUtil.resetPrepare();
//		}		
		
	}

	/**
	 * @return Returns the curValue.
	 */
	public long getCurValue()
	{
		return curValue;
	}

	/**
	 * @return Returns the increment.
	 */
	public int getIncrement()
	{
		return increment;
	}

	/**
	 * @return Returns the primaryKeyName.
	 */
	public String getPrimaryKeyName()
	{
		return primaryKeyName;
	}

	/**
	 * @return Returns the tableName.
	 */
	public String getTableName()
	{
		return tableName;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getDbname()
	{
		return dbname;
	}

	public void setHasTableinfo(boolean hasTableinfo) {
		this.hasTableinfo = hasTableinfo;
	}
	
	public boolean hasTableinfo() {
		return this.hasTableinfo;
	}
	
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("table=").append(this.tableName).append(",primaryKey=").append(this.primaryKeyName)
			.append(",type=").append(this.type);
		return buffer.toString();
//		return 
	}

	public String getMetaType() {
		return metaType;
	}

	public void setMetaType(String metaType) {
		this.metaType = metaType;
	}
	
	public static void main(String[] args)
	{
	    for(int i = 0;  i < 10; i ++)
	    System.out.println(UUID.randomUUID().toString().length());
	}

}
