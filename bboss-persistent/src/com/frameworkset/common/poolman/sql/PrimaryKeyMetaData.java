package com.frameworkset.common.poolman.sql;

import com.frameworkset.orm.adapter.DB;

public class PrimaryKeyMetaData extends ColumnMetaData {	
	
	public ColumnMetaData column;
	public PrimaryKeyMetaData(DB db)
	{
		super(db);
	} 
	/**
	 * </B> short => sequence number within primary key
	 */
    private int keySEQ;
    private String pkName;
	public int getKeySEQ() {
		return keySEQ;
	}
	public void setKeySEQ(int keySEQ) {
		this.keySEQ = keySEQ;
	}
	public String getPkName() {
		return pkName;
	}
	public void setPkName(String pkName) {
		this.pkName = pkName;
	}
	
	public String toString()
    {
        StringBuffer ret = new StringBuffer();
        ret.append("		columnName=").append(getColumnName()).append(",");
        ret.append("pkName=").append(pkName).append(",").append("keySEQ=").append(keySEQ + "");
        return ret.toString();
    }
	public ColumnMetaData getColumn() {
		return column;
	}
	public void setColumn(ColumnMetaData column) {
		this.column = column;
	}
	
	public int compareTo(Object obj) {
		
		if(obj instanceof PrimaryKeyMetaData)
		{
			PrimaryKeyMetaData temp = (PrimaryKeyMetaData)obj;
			if(temp != null)
			{
				if(getPkName() != null)
				{
					int ret = this.getPkName().toLowerCase().compareTo(temp.getPkName().toLowerCase());
					
					return ret;
				}
				else if(getColumnName() != null)
				{
					int ret = this.getColumnName().toLowerCase().compareTo(temp.getColumnName().toLowerCase());
					
					return ret;
				}
				else
					return 0;
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
