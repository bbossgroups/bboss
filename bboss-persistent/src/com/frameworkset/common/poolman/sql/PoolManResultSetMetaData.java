package com.frameworkset.common.poolman.sql;

import com.frameworkset.common.poolman.handle.RowHandlerException;
import com.frameworkset.common.poolman.management.PoolManConfiguration;
import com.frameworkset.common.poolman.util.JDBCPool;
import com.frameworkset.orm.adapter.DB;
import org.frameworkset.util.ClassUtil;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/*
 *  An addition to the PoolMan Java Object Pooling and Caching Library
 *  Copyright (C) 1999-2001 The Code Studio
 *
 *  This file was contrbuted by and is
 *  Copyright (C) 2001 HotMagna, http://www.hotmagna.com
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  The full license is located at the root of this distribution
 *  in the LICENSE file.
 */

/** PoolManResultSetMetaData takes a copy of a java.sql.ResultSetMetaData
 * for later usage. Some databases use the underlying ResultSet for
 * ResultSetMetaData information, so closing the ResultSet makes the
 * ResultSetMetaData unavailable. Given a database-specific ResultSetMetaData
 * object, this class creates a copy of the values and makes them available
 * long after the ResultSet has been closed.
 *
 * This class is used by PoolManStatement and is stored in the cache.
 *
 */
public class PoolManResultSetMetaData implements java.sql.ResultSetMetaData, java.io.Serializable {
    private boolean columnLableUpperCase;
    private int _columnCount;
    private String[] _columnTypeName;
    private String[] _columnClassName;
//    private int[] _scale;
    private String[] _columnLabel;
    private String[] _columnLabel_upper;
//    private String[] _columnLabel_lower;

//    private boolean[] _autoIncrement;
    private int[] _columnDisplaySize;
    private String[] _catalogName;
    private String[] _columnName;
//    private boolean[] _writable;
//    private boolean[] _searchable;
    private int[] _columnType;
//    private boolean[] _currency;
    private String[] _tableName;
//    private int[] _nullable;
//    private boolean[] _signed;
//    private boolean[] _readOnly;
//    private boolean[] _definitelyWritable;
//    private int[] _precision;
//    private String[] _schemaName;
//    private boolean[] _caseSensitive;
    /**
     * 存储符合java规范java属性名称，转换规则为：aaa_bb_cc-->aaaBbCc
     */
    private String columnJavaName[];
    
    /**
     * 执行查询时，保存相同的字段出现在查询字段列表中的位置信息
     * 
     * 比如
     * Map<ColumnName,WrapInteger>
     */
    private Map samecols ;
    

    // used for storing error information from when getColumnClassName() fails
    private String _sqlReason;
    private String _sqlState;
    private int _sqlVendorCode;

    public static PoolManResultSetMetaData getCopy(JDBCPool pool, java.sql.ResultSetMetaData original) throws java.sql.SQLException {
        if (original instanceof PoolManResultSetMetaData)
            return (PoolManResultSetMetaData)original;
        else
            return new PoolManResultSetMetaData(pool,original);
    }
    
    public static PoolManResultSetMetaData getCopy(java.sql.ResultSetMetaData original) throws java.sql.SQLException {
        if (original instanceof PoolManResultSetMetaData)
            return (PoolManResultSetMetaData)original;
        else
            return new PoolManResultSetMetaData(null,original);
    }

    public boolean isColumnLableUpperCase() {
        return columnLableUpperCase;
    }

    public static class WrapInteger
    {
//        int i = 0;
        private String columnName;
        public String getColumnName(int index) {
            return (String)indexs.get(new Integer(index));
        }

        Map indexs = new HashMap();
        int count = 1;
        public WrapInteger(int count,int index,String columnName)
        {
            this.count = count;
            
            indexs.put(new Integer(index),columnName);
            this.columnName = columnName;
        }
       
        public void increament(int index)
        {
            count ++;
            indexs.put(new Integer(index),buildUUColname(columnName,index));;
        }
        
        public boolean containsamecol()
        {
            return count > 1;
        }
        
        public Map getIndexs()
        {
            return this.indexs;
        }
        
        public int getCount()
        {
            return count;
        }
        
        
        
    }
    public static final String col_uuid_split = "#$_";
    
    public static String buildUUColname(String name,int id)
    {
        return new StringBuilder(name).append(col_uuid_split ).append(id).toString();
    }
    private PoolManResultSetMetaData(JDBCPool pool,java.sql.ResultSetMetaData other) throws java.sql.SQLException {
//        resultSetMetaData = other;
        columnLableUpperCase = pool.getJDBCPoolMetadata().isColumnLableUpperCase();
        DB db = pool.getDbAdapter();
        _columnCount = other.getColumnCount();

        _columnTypeName = new String[_columnCount];
        _columnClassName = new String[_columnCount];
//        _scale = new int[_columnCount];
        _columnLabel = new String[_columnCount];
         _columnLabel_upper = new String[_columnCount];
//        _columnLabel_lower = new String[_columnCount];
//        _autoIncrement = new boolean[_columnCount];
        _columnDisplaySize = new int[_columnCount];
        _catalogName = new String[_columnCount];
        _columnName = new String[_columnCount];
//        _writable = new boolean[_columnCount];
//        _searchable = new boolean[_columnCount];
        _columnType = new int[_columnCount];
//        _currency = new boolean[_columnCount];
        _tableName = new String[_columnCount];
//        _nullable = new int[_columnCount];
//        _signed = new boolean[_columnCount];
//        _readOnly = new boolean[_columnCount];
//        _definitelyWritable = new boolean[_columnCount];
//        _precision = new int[_columnCount];
//        _schemaName = new String[_columnCount];
//        _caseSensitive = new boolean[_columnCount];
        samecols = new HashMap();
        
        if(PoolManConfiguration.isColumnNameMapping())
        {
        	columnJavaName = new String[_columnCount];
        }
        Map testM = new HashMap(); 
        
        String columnLabel = null;
        String columnName = null;
        for (int c = 0; c < _columnCount; c++) {
        	int rc = c + 1;
            _columnTypeName[c] = other.getColumnTypeName(rc);

            if (_columnClassName != null) {
                // this only works on JDBC compliant drivers
                try {
                    _columnClassName[c] = other.getColumnClassName(rc);
                } catch (java.sql.SQLException x) {
                    _columnClassName = null; // don't try again!
                    _sqlReason = x.getMessage();
                    _sqlState = x.getSQLState();
                    _sqlVendorCode = x.getErrorCode();
                } catch (Throwable e) {
                    _columnClassName = null; // don't try again!
                }

            }

//            _scale[c] = other.getScale(rc);
            columnName = other.getColumnName(rc);
            _columnName[c] = columnName;
            columnLabel = other.getColumnLabel(rc);
            if(columnLabel == null || columnLabel.equals("")) {
                columnLabel = columnName;
            }
            _columnLabel[c] = columnLabel;
            _columnLabel_upper[c] =  columnLabel.toUpperCase();
//            _columnLabel_lower[c] = columnLabel.toLowerCase();
           if(PoolManConfiguration.isColumnNameMapping())
           {
        	   this.columnJavaName[c] = ClassUtil.genJavaName(columnLabel);
           }
//            Integer idx = new Integer(c);
            String label = columnLableUpperCase?_columnLabel_upper[c]:columnLabel;
            WrapInteger wi = (WrapInteger)testM.get(label);
            if(wi == null)
            {
                wi = new WrapInteger(1,c,label);
                testM.put(label, wi);
            }
            else
            {
                wi.increament(c);
            }
//            _autoIncrement[c] = other.isAutoIncrement(rc);
            _columnDisplaySize[c] = other.getColumnDisplaySize(rc);

            try {
                _catalogName[c] = other.getCatalogName(rc);
            } catch (Exception e) {
            }
            if (_catalogName[c] == null)
                _catalogName[c] = "";


            
            if(db != null)
            {
//            	_writable[c] = db.isWritebable(other,rc);
//            	_searchable[c] = db.isSearchable(other,rc);
//            	 _signed[c] = db.isSigned(other,rc);
//            	 _definitelyWritable[c] = db.isDefinitelyWritable(other,rc);
            }
            else
            {
//            	try {
//					_writable[c] = other.isWritable(rc);
//				} catch (Exception e) {
//					
//				}
//            	try {
//            		 _searchable[c] = other.isSearchable(rc);
//				} catch (Exception e) {
//					
//				}
//            	
//            	try {
//            		 _signed[c] = other.isSigned(rc);
//				} catch (Exception e) {
//					
//				}
//            	try {
//            		_definitelyWritable[c] = other.isDefinitelyWritable(rc);
//				} catch (Exception e) {
//					
//				}
            	 
            	 
            }
           
            _columnType[c] = other.getColumnType(rc);
//            _currency[c] = other.isCurrency(rc);

            try {
                _tableName[c] = other.getTableName(rc);
            } catch (Exception e) {
            }
            if (_tableName[c] == null)
                _tableName[c] = "";

//            _nullable[c] = other.isNullable(rc);
//          
//            _readOnly[c] = other.isReadOnly(rc);
//           
//            try
//            {
//                _precision[c] = other.getPrecision(rc);
//            }
//            catch(Exception e)
//            {
//                
//            }

//            try {
//                _schemaName[c] = other.getSchemaName(rc);
//            } catch (Exception e) {
//            }
//            if (_schemaName[c] == null)
//                _schemaName[c] = "";
//
//            _caseSensitive[c] = false;//other.isCaseSensitive(rc);,fixed mysql 每次都会向mysql后台发送SHOW FULL COLUMNS FROM 指令
        }

        for (int c = 0; c < _columnCount; c++) {
//            Integer idx = new Integer(c);
            String name =  columnLableUpperCase?_columnLabel_upper[c]:_columnLabel[c];
            WrapInteger wi = (WrapInteger)testM.get(name);
            if(wi.containsamecol() && !samecols.containsKey(name))
            {
                samecols.put( name, wi);
            }
        }
        testM = null;
    }
    
    public Map getSamecols() {
        return samecols;
    }
    public WrapInteger getSameColumns(int colIndex)
    {
        return  getSameColumns(this.getColumnLabelUpper(colIndex));
//        return  (WrapInteger)samecols.get(new Integer(colIndex));
    }
    public WrapInteger getSameColumnsByIndex(int colIndex)
    {
        return  getSameColumns(this.getColumnLabelUpperByIndex(colIndex));
//        return  (WrapInteger)samecols.get(new Integer(colIndex));
    }
    
    public WrapInteger getSameColumns(String colName)
    {
        return  (WrapInteger)samecols.get(colName);
//        return   getSameColumns((getColumnIndex(colName)));
    }
    


    public int getColumnCount() throws java.sql.SQLException {
        return _columnCount;
    }
    
    public int getColumnCounts()  {
        return _columnCount;
    }

    public java.lang.String getColumnTypeName(int column) throws java.sql.SQLException {
        return _columnTypeName[column - 1];
    }
    
    public java.lang.String getColumnTypeNameByIndex(int column) throws java.sql.SQLException {
        return _columnTypeName[column ];
    }

    public java.lang.String getColumnClassName(int column) throws java.sql.SQLException {
        if (_columnClassName == null) {
            // java.sql.ResultSetMetaData.getColumnClassName(int col) requires a JDBC 2 compliant database driver.
            throw new java.sql.SQLException(_sqlReason, _sqlState, _sqlVendorCode);
        }
        return _columnClassName[column - 1];
    }
    public java.lang.String getColumnClassNameByIndex(int column) throws java.sql.SQLException {
        if (_columnClassName == null) {
            // java.sql.ResultSetMetaData.getColumnClassName(int col) requires a JDBC 2 compliant database driver.
            throw new java.sql.SQLException(_sqlReason, _sqlState, _sqlVendorCode);
        }
        return _columnClassName[column];
    }

    public int getScale(int column) throws java.sql.SQLException {
//        return resultSetMetaData.getScale(column);
        return -1;
    }
    
    public int getScaleByIndex(int column) throws java.sql.SQLException {
//        return _scale[column ];
//        return resultSetMetaData.getScale(column+1);
        return -1;
    }

    public java.lang.String getColumnLabel(int column) throws java.sql.SQLException {
        return _columnLabel[column - 1];
    }
    
    public java.lang.String getColumnLabelByIndex(int column) throws java.sql.SQLException {
        return _columnLabel[column  ];
    }
    
    public java.lang.String getColumnLabelUpper(int column) {
//        if(columnLableUpperCase) {
            return _columnLabel_upper[column - 1];
//        }
//        else{
//            return _columnLabel[column - 1].toUpperCase();
//        }
    }
    
    public java.lang.String getColumnLabelUpperByIndex(int column) {
//        if(columnLableUpperCase) {
            return _columnLabel_upper[column ];
//        }
//        else{
//            return _columnLabel[column].toUpperCase();
//        }
    }

    public java.lang.String getColumnLabelLower(int column) {
        return _columnLabel[column - 1].toLowerCase();
    }

    public java.lang.String getColumnLabelLowerByIndex(int column) {
        return _columnLabel[column ].toLowerCase();
    }
    
    public java.lang.String getColumnJavaName(int column) {
        return this.columnJavaName[column - 1];
    }
    
    public java.lang.String getColumnJavaNameByIndex(int column) {
        return this.columnJavaName[column ];
    }

    public boolean isAutoIncrement(int column) throws java.sql.SQLException {
//        return _autoIncrement[column - 1];
        return false;
    }
    
    public boolean isAutoIncrementByIndex(int column) throws java.sql.SQLException {
//        return _autoIncrement[column ];
        return false;
    }

    public int getColumnDisplaySize(int column) throws java.sql.SQLException {
        return _columnDisplaySize[column - 1];
    }
    public int getColumnDisplaySizeByIndex(int column) throws java.sql.SQLException {
        return _columnDisplaySize[column];
    }

    public java.lang.String getCatalogName(int column) throws java.sql.SQLException {
        return _catalogName[column - 1];
    }
    
    public java.lang.String getCatalogNameByIndex(int column) throws java.sql.SQLException {
        return _catalogName[column];
    }

    public java.lang.String getColumnName(int column) throws java.sql.SQLException {
        return _columnName[column - 1];
    }
    
    public java.lang.String getColumnNameByIndex(int column) throws java.sql.SQLException {
        return _columnName[column ];
    }

    public boolean isWritable(int column) throws java.sql.SQLException {
//        return _writable[column - 1];
        return false;
    }
    
    public boolean isWritableByIndex(int column) throws java.sql.SQLException {
//        return _writable[column ];
        return false;
    }

    public boolean isSearchable(int column) throws java.sql.SQLException {
//        return _searchable[column - 1];
        return false;
    }
    
    public boolean isSearchableByIndex(int column) throws java.sql.SQLException {
//        return _searchable[column];
        return false;
    }

    public int getColumnType(int column) throws java.sql.SQLException {
        return _columnType[column - 1];
    }
    
    public int getColumnTypeByIndex(int column) throws java.sql.SQLException {
        return _columnType[column ];
    }

    public boolean isCurrency(int column) throws java.sql.SQLException {
//        return _currency[column - 1];
        return false;
    }
    
    public boolean isCurrencyByIndex(int column) throws java.sql.SQLException {
//        return _currency[column ];
        return false;
    }

    public java.lang.String getTableName(int column) throws java.sql.SQLException {
        return _tableName[column - 1];
    }
    public java.lang.String getTableNameByIndex(int column) throws java.sql.SQLException {
        return _tableName[column ];
    }

    public int isNullable(int column) throws java.sql.SQLException {
//        return _nullable[column - 1];
        return -1;
    }
    
    public int isNullableByIndex(int column) throws java.sql.SQLException {
//        return _nullable[column];
        return -1;
    }

    public boolean isSigned(int column) throws java.sql.SQLException {
//        return _signed[column - 1];
        return false;
    }
    public boolean isSignedByIndex(int column) throws java.sql.SQLException {
//        return _signed[column ];
        return false
                ;
    }

    public boolean isReadOnly(int column) throws java.sql.SQLException {
//        return _readOnly[column - 1];
        return false;
    }
    public boolean isReadOnlyByIndex(int column) throws java.sql.SQLException {
//        return _readOnly[column ];
        return false;
    }

    public boolean isDefinitelyWritable(int column) throws java.sql.SQLException {
//        return _definitelyWritable[column - 1];
        return false;
    }
    
    public boolean isDefinitelyWritableByIndex(int column) throws java.sql.SQLException {
//        return _definitelyWritable[column ];
        return false;
    }

    public int getPrecision(int column) throws java.sql.SQLException {
//        return _precision[column - 1];
        return -1;
    }
    
    public int getPrecisionByIndex(int column) throws java.sql.SQLException {
//        return _precision[column ];
        return -1;
    }

    public java.lang.String getSchemaName(int column) throws java.sql.SQLException {
//        return _schemaName[column - 1];
        return null;
    }
    
    public java.lang.String getSchemaNameByIndex(int column) throws java.sql.SQLException {
//        return _schemaName[column ];
        return null;
    }

    public boolean isCaseSensitive(int column) throws java.sql.SQLException {
//        return _caseSensitive[column - 1];
        return false;
    }
    public boolean isCaseSensitiveByIndex(int column) throws java.sql.SQLException {
//        return _caseSensitive[column ];
        return false;
    }

	public String[] get_columnLabel_upper() {
		return _columnLabel_upper;
	}
	
	public String toString()
	{
	    
	    if(this._columnLabel == null || this._columnLabel.length == 0)
	        return "";
	    StringBuilder ret = new StringBuilder();
	    boolean flag = false;
	    for(String name :this._columnLabel)
	    {
	        if(!flag)
	        {
	            ret.append(name);
	            flag = true;
	        }
	        else
	        {
	            ret.append(",").append(name);
	        }
	    }
	    return ret.toString();
	    
	}
	
	public String toDetailString()
	{
	    if(this._columnLabel == null || this._columnLabel.length == 0)
            return "";
        StringBuilder ret = new StringBuilder();
//        boolean flag = false;
        int i = 0;
        for(String name :this._columnLabel)
        {
            
            if(i == 0)
            {
                ret.append("columnname=").append(name).append("||").append("sqltype=").append(this._columnTypeName[i]).append("||javatype=").append(this._columnClassName[i]);
                
            }
            else
            {
                ret.append("\r\ncolumnname=").append(name).append("||").append("sqltype=").append(this._columnTypeName[i]).append("||javatype=").append(this._columnClassName[i]);
            }
            i ++;
        }
        return ret.toString();
	}
	public int seekIndex(String colName)
    {
//        if(columnLableUpperCase) {
            String temp = colName.toUpperCase();
            for (int i = 0; i < _columnLabel_upper.length; i++) {
                if (_columnLabel_upper[i].equals(temp))
                    return i;
            }
//        }
//        else{
//            for (int i = 0; i < _columnLabel.length; i++) {
//                if (_columnLabel[i].equals(colName))
//                    return i;
//            }
//        }
        throw new RowHandlerException("查询结果中不存在列[" + colName + "].");
    }
	
	/**列名获取列的信息开始
	 * @throws SQLException */
	public String getCatalogName(String colName) throws SQLException
	{
		return this.getCatalogNameByIndex(this.seekIndex(colName));
	}
	public String getColumnClassName(String colName) throws SQLException
	{
		return this.getColumnClassNameByIndex(this.seekIndex(colName));
	}
	public int getColumnDisplaySize(String colName) throws SQLException{
		return this.getColumnDisplaySizeByIndex(this.seekIndex(colName) );
	}
	public String getColumnLabel(String colName) throws SQLException{
		return this.getColumnLabelByIndex(this.seekIndex(colName) );
	}
	public String getColumnLabelUpper(String colName) throws SQLException{
		return this.getColumnLabelUpperByIndex(this.seekIndex(colName) );
	}
	public String getColumnName(String colName) throws SQLException{
		return this.getColumnNameByIndex(this.seekIndex(colName) );
	}
	public int getColumnType(String colName) throws SQLException{
		return this.getColumnTypeByIndex(this.seekIndex(colName) );
	}
	public String getColumnTypeName(String colName) throws SQLException{
		return this.getColumnTypeNameByIndex(this.seekIndex(colName) );
	}
	
	public int getPrecision(String colName) throws SQLException{
		return this.getPrecisionByIndex(this.seekIndex(colName) );
	}
	public int getScale(String colName) throws SQLException{
		return this.getScaleByIndex(this.seekIndex(colName) );
	}
	public String getSchemaName(String colName) throws SQLException{
		return this.getSchemaNameByIndex(this.seekIndex(colName) );
	}
	public String getTableName(String colName) throws SQLException{
		return this.getTableNameByIndex(this.seekIndex(colName) );
	}
	public boolean isAutoIncrement(String colName) throws SQLException{
		return this.isAutoIncrementByIndex(this.seekIndex(colName) );
	}
	public boolean isCaseSensitive(String colName) throws SQLException{
		return this.isCaseSensitiveByIndex(this.seekIndex(colName) );
	}
	public boolean isCurrency(String colName) throws SQLException{
		return this.isCurrencyByIndex(this.seekIndex(colName) );
	}
	public boolean isDefinitelyWritable(String colName) throws SQLException{
		return this.isDefinitelyWritableByIndex(this.seekIndex(colName) );
	}
	public int isNullable(String colName) throws SQLException{
		return this.isNullableByIndex(this.seekIndex(colName) );
	}
	public boolean isReadOnly(String colName) throws SQLException{
		return this.isReadOnlyByIndex(this.seekIndex(colName) );
	}
	public boolean isSearchable(String colName) throws SQLException{
		return this.isSearchableByIndex(this.seekIndex(colName) );
	}
	public boolean isSigned(String colName) throws SQLException{
		return this.isSignedByIndex(this.seekIndex(colName) );
	}
	public boolean isWritable(String colName) throws SQLException{
		return this.isWritableByIndex(this.seekIndex(colName) );
	}
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	public String[] getColumnJavaName() {
		return columnJavaName;
	}
	public String[] get_columnLabel() {
		return _columnLabel;
	}
	public int[] get_columnType() {
		return _columnType;
	}
//    public String[] getColumnLabelLower(){
//        return _columnLabel_lower;
//    }
}

