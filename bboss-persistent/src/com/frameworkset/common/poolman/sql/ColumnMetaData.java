package com.frameworkset.common.poolman.sql;

import java.io.Serializable;

import com.frameworkset.orm.adapter.DB;
import com.frameworkset.orm.engine.model.Domain;
import com.frameworkset.orm.engine.model.SchemaType;



/*
<LI><B>TABLE_CAT</B> String => table catalog (may be <code>null</code>)
*	<LI><B>TABLE_SCHEM</B> String => table schema (may be <code>null</code>)
*	<LI><B>TABLE_NAME</B> String => table name
*	<LI><B>COLUMN_NAME</B> String => column name
*	<LI><B>DATA_TYPE</B> int => SQL type from java.sql.Types
*	<LI><B>TYPE_NAME</B> String => Data source dependent type name,
*  for a UDT the type name is fully qualified
*	<LI><B>COLUMN_SIZE</B> int => column size.  For char or date
*	    types this is the maximum number of characters, for numeric or
*	    decimal types this is precision.
*	<LI><B>BUFFER_LENGTH</B> is not used.
*	<LI><B>DECIMAL_DIGITS</B> int => the number of fractional digits
*	<LI><B>NUM_PREC_RADIX</B> int => Radix (typically either 10 or 2)
*	<LI><B>NULLABLE</B> int => is NULL allowed.
*      <UL>
*      <LI> columnNoNulls - might not allow <code>NULL</code> values
*      <LI> columnNullable - definitely allows <code>NULL</code> values
*      <LI> columnNullableUnknown - nullability unknown
*      </UL>
*	<LI><B>REMARKS</B> String => comment describing column (may be <code>null</code>)
* 	<LI><B>COLUMN_DEF</B> String => default value (may be <code>null</code>)
*	<LI><B>SQL_DATA_TYPE</B> int => unused
*	<LI><B>SQL_DATETIME_SUB</B> int => unused
*	<LI><B>CHAR_OCTET_LENGTH</B> int => for char types the
*       maximum number of bytes in the column
*	<LI><B>ORDINAL_POSITION</B> int	=> index of column in table
*      (starting at 1)
*	<LI><B>IS_NULLABLE</B> String => "NO" means column definitely
*      does not allow NULL values; "YES" means the column might
*      allow NULL values.  An empty string means nobody knows.
*  <LI><B>SCOPE_CATLOG</B> String => catalog of table that is the scope
*      of a reference attribute (<code>null</code> if DATA_TYPE isn't REF)
*  <LI><B>SCOPE_SCHEMA</B> String => schema of table that is the scope
*      of a reference attribute (<code>null</code> if the DATA_TYPE isn't REF)
*  <LI><B>SCOPE_TABLE</B> String => table name that this the scope
*      of a reference attribure (<code>null</code> if the DATA_TYPE isn't REF)
*  <LI><B>SOURCE_DATA_TYPE</B> short => source type of a distinct type or user-generated
*      Ref type, SQL type from java.sql.Types (<code>null</code> if DATA_TYPE
*      isn't DISTINCT or user-generated REF)
*  </OL>*/
public class ColumnMetaData implements Comparable,Serializable{

	private TableMetaData tableMetaData;
	private String columnName;
	private int dataType;
	private String typeName;
	private int colunmSize;
	private int numPrecRadix;
	private String columnDefaultValue;
	private String remarks;
	private DB db;
	private Domain domain;
	private SchemaType schemaType;
	private int DECIMAL_DIGITS;
	/**
	 * yes,no, unknown
	 */
	private String nullable;
	private int CHAR_OCTET_LENGTH;
	public String getColumnDefaultValue() {
		return columnDefaultValue;
	}
	public void setColumnDefaultValue(String columnDefaultValue) {
		this.columnDefaultValue = columnDefaultValue;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public int getColunmSize() {
		return colunmSize;
	}
	public void setColunmSize(int colunmSize) {
		this.colunmSize = colunmSize;
	}
	public int getDataType() {
		return dataType;
	}
	public void setDataType(int dataType) {
		this.domain = db.getDomainForSchemaType(dataType);
		this.schemaType = domain.getType();
		this.dataType = dataType;
	} 
	
	public String getNullable() {
		return nullable;
	}
	public void setIsNullable(String isNullable) {
		this.nullable = isNullable;
	}
	public int getNumPrecRadix() {
		return numPrecRadix;
	}
	public void setNumPrecRadix(int numPrecRadix) {
		this.numPrecRadix = numPrecRadix;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public TableMetaData getTableMetaData() {
		return tableMetaData;
	}
	public void setTableMetaData(TableMetaData tableMetaData) {
		this.tableMetaData = tableMetaData;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

    public String toString()
     {
         StringBuffer ret = new StringBuffer();
         ret.append("		columnName=")
            .append(columnName)
            .append(",")
            .append("dataType=")
            .append(dataType)
            .append(",")
            .append("typeName=")
            .append(typeName)
            .append(",")
            .append("colunmSize=")
            .append(colunmSize)
            .append(",")
            .append("numPrecRadix=")
            .append(numPrecRadix)
            .append(",")
            .append("DECIMAL_DIGITS=")
            .append(this.getDECIMAL_DIGITS())
            .append(",")
            .append("columnDefaultValue=")
            .append(columnDefaultValue)
            .append(",")
            .append("remarks=")
            .append(remarks)
            .append(",")
            .append("nullable=")
            .append(nullable);
         return ret.toString();
     }
    
    public ColumnMetaData(DB db)
    {
    	this.db = db;
    }
	public SchemaType getSchemaType() {
		return schemaType;
	}
	public int compareTo(Object obj) {
		
		if(obj instanceof ColumnMetaData)
		{
			ColumnMetaData temp = (ColumnMetaData)obj;
			if(temp != null)
			{
				int ret = this.getColumnName().toLowerCase().compareTo(temp.getColumnName().toLowerCase());
				
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
	public int getDECIMAL_DIGITS() {
		return DECIMAL_DIGITS;
	}
	
	public void setDECIMAL_DIGITS(int decimal_digits) {
		DECIMAL_DIGITS = decimal_digits;
	}
	
	public void setCHAR_OCTET_LENGTH(int CHAR_OCTET_LENGTH) {
		this.CHAR_OCTET_LENGTH = CHAR_OCTET_LENGTH;
		
	}
	public int getCHAR_OCTET_LENGTH() {
		return CHAR_OCTET_LENGTH;
	}
}
