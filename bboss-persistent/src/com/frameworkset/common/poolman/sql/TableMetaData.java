package com.frameworkset.common.poolman.sql;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;



public class TableMetaData implements Comparable,Serializable{
	private String tableName;
	private String tableType;
	private String schema;
	private String remarks;
	private Set columns = new TreeSet();
	private Set primaryKeys = new TreeSet();
	private Set foreignKeys = new TreeSet();

	private Map columnsIdx = new HashMap();
	private Map primarysIdx  = new HashMap();;
	private Map foreignsIdx = new HashMap();


	public Set getColumns() {
		return columns;
	}
	public void addColumns(ColumnMetaData column) {
		this.columns.add(column);
		column.setTableMetaData(this);
		this.columnsIdx.put(column.getColumnName().toLowerCase(),column);
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableType() {
		return tableType;
	}
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
	public Set getPrimaryKeys() {
		return primaryKeys;
	}

	public void addPrimaryKey(ColumnMetaData column)
	{
		this.primaryKeys.add(column);
		column.setTableMetaData(this);
		
		this.primarysIdx.put(column.getColumnName().toLowerCase(),column);
	}

	public Set getForeignKeys() {
		return foreignKeys;
	}



	public void addForeignKey(ColumnMetaData foreignKey)
	{
		this.foreignKeys.add(foreignKey);
		foreignKey.setTableMetaData(this);
		this.foreignsIdx.put(foreignKey.getColumnName().toLowerCase(),foreignKey);
	}

	public ForeignKeyMetaData getForeignKeyMetaData(String columnName)
	{
		return (ForeignKeyMetaData)this.foreignsIdx.get(columnName.toLowerCase());
	}

	public PrimaryKeyMetaData getPrimaryKeyMetaData(String columnName)
	{
		return (PrimaryKeyMetaData )this.primarysIdx.get(columnName.toLowerCase());
	}
	public ColumnMetaData getColumnMetaData(String columnName) {

		return (ColumnMetaData)columnsIdx.get(columnName.toLowerCase());
	}

    public String toString()
    {
        StringBuffer ret = new StringBuffer();
        ret.append(this.tableName).append("(");
        Object[] objs = this.columns.toArray();
        boolean flag = false;
        ret.append("\r\n").append("		Columns:");
        for(int i = 0; i < objs.length; i ++)
        {
            if(flag)
                ret.append("\r\n")
                	.append(objs[i].toString());
            else
            {
                flag = true;
                ret.append("\r\n").append(objs[i].toString());
            }
        }
        ret.append("\r\n").append("		PrimaryKeys:");
        Object[] prks = this.primaryKeys.toArray();
        for(int i = 0; i < prks.length; i ++)
        {
            if(flag)
                ret.append("\r\n")
                	.append(prks[i].toString());
            else
            {
                flag = true;
                ret.append("\r\n").append(prks[i].toString());
            }
        }
        
        ret.append("\r\n").append("		ForeignKeys:");
        Object[] fks = this.foreignKeys.toArray();
        for(int i = 0; i < fks.length; i ++)
        {
            if(flag)
                ret.append("\r\n")
                	.append(fks[i].toString());
            else
            {
                flag = true;
                ret.append("\r\n").append(fks[i].toString());
            }
        }
        ret.append(")");
        return ret.toString();
    }
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	public int hashCode()
	{
		return this.tableName.toLowerCase().hashCode();
	}
	public boolean equals(Object obj)
	{
		if(obj instanceof TableMetaData)
		{
			TableMetaData temp = (TableMetaData)obj;
			if(temp != null)
				return temp.getTableName().equalsIgnoreCase(this.tableName);
			else
				return false;
		}
		else
			return false;
	}

	public int compareTo(Object obj) {
		if(obj instanceof TableMetaData)
		{
			TableMetaData temp = (TableMetaData)obj;
			if(temp != null)
			{
				int ret = this.tableName.toLowerCase().compareTo(temp.getTableName().toLowerCase());
				
				return ret;
			}
			else
			{
				return 0;
			}
		}
		else
			return 0;
	}



}
