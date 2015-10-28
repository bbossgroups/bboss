package com.frameworkset.common.poolman.sql;

import java.io.Serializable;

import com.frameworkset.orm.adapter.DB;

/**
 * 
 * 
 * <p>Title: ForeignKeyMetaData.java</p>
 *
 * <p>Description: </p>
 *
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2009-6-1 下午08:58:51
 * @author biaoping.yin
 * @version 1.0
 */
public class ForeignKeyMetaData extends ColumnMetaData implements Serializable {
	private String PKTABLE_NAME;
	private String PKCOLUMN_NAME;
	private String FKTABLE_NAME;
	private String FKCOLUMN_NAME;
	private String KEY_SEQ;
	private int UPDATE_RULE;
	private int DELETE_RULE;
	private String FK_NAME;
	private String PK_NAME;
	private String DEFERRABILITY;
	public ForeignKeyMetaData(DB db)
	{
		super(db);
	}
	public String getDEFERRABILITY() {
		return DEFERRABILITY;
	}
	public void setDEFERRABILITY(String deferrability) {
		DEFERRABILITY = deferrability;
	}
	public int getDELETE_RULE() {
		return DELETE_RULE;
	}
	public void setDELETE_RULE(int delete_rule) {
		DELETE_RULE = delete_rule;
	}
	public String getFK_NAME() {
		return FK_NAME;
	}
	public void setFK_NAME(String fk_name) {
		FK_NAME = fk_name;
	}
	public String getFKCOLUMN_NAME() {
		return FKCOLUMN_NAME;
	}
	public void setFKCOLUMN_NAME(String fkcolumn_name) {
		FKCOLUMN_NAME = fkcolumn_name;
	}
	public String getFKTABLE_NAME() {
		return FKTABLE_NAME;
	}
	public void setFKTABLE_NAME(String fktable_name) {
		FKTABLE_NAME = fktable_name;
	}
	public String getKEY_SEQ() {
		return KEY_SEQ;
	}
	public void setKEY_SEQ(String key_seq) {
		KEY_SEQ = key_seq;
	}
	public String getPK_NAME() {
		return PK_NAME;
	}
	public void setPK_NAME(String pk_name) {
		PK_NAME = pk_name;
	}
	public String getPKCOLUMN_NAME() {
		return PKCOLUMN_NAME;
	}
	public void setPKCOLUMN_NAME(String pkcolumn_name) {
		PKCOLUMN_NAME = pkcolumn_name;
	}
	public String getPKTABLE_NAME() {
		return PKTABLE_NAME;
	}
	public void setPKTABLE_NAME(String pktable_name) {
		PKTABLE_NAME = pktable_name;
	}
	public int getUPDATE_RULE() {
		return UPDATE_RULE;
	}
	public void setUPDATE_RULE(int update_rule) {
		UPDATE_RULE = update_rule;
	}
	
	public String toString()
    {	
        StringBuffer ret = new StringBuffer();
        ret.append("		PKTABLE_NAME=")
           .append(PKTABLE_NAME)
           .append(",")
           .append("PKCOLUMN_NAME=")
           .append(PKCOLUMN_NAME)
           .append(",")
           .append("FKTABLE_NAME=")
           .append(FKTABLE_NAME)
           .append(",")
           .append("FKCOLUMN_NAME=")
           .append(FKCOLUMN_NAME)
           .append(",")
           .append("KEY_SEQ=")
           .append(KEY_SEQ)
           .append(",")
           .append("UPDATE_RULE=")
           .append(UPDATE_RULE)
           .append(",")
           .append("DELETE_RULE=")
           .append(DELETE_RULE)
           .append(",")
           .append("PK_NAME=")
           .append(PK_NAME)
           
           .append(",")
           .append("DEFERRABILITY=")
           .append(DEFERRABILITY)
           .append(",")
           .append("FK_NAME=")
           .append(FK_NAME);
        return ret.toString();
    }
	/**
	* PKTABLE_NAME</B> String => primary key table name
    *      being imported
    *	<LI><B>PKCOLUMN_NAME</B> String => primary key column name
    *      being imported
    *	<LI><B>FKTABLE_CAT</B> String => foreign key table catalog (may be <code>null</code>)
    *	<LI><B>FKTABLE_SCHEM</B> String => foreign key table schema (may be <code>null</code>)
    *	<LI><B>FKTABLE_NAME</B> String => foreign key table name
    *	<LI><B>FKCOLUMN_NAME</B> String => foreign key column name
    *	<LI><B>KEY_SEQ</B> short => sequence number within a foreign key
    *	<LI><B>UPDATE_RULE</B> short => What happens to a
    *       foreign key when the primary key is updated:
    *      <UL>
    *      <LI> importedNoAction - do not allow update of primary 
    *               key if it has been imported
    *      <LI> importedKeyCascade - change imported key to agree 
    *               with primary key update
    *      <LI> importedKeySetNull - change imported key to <code>NULL</code>
    *               if its primary key has been updated
    *      <LI> importedKeySetDefault - change imported key to default values 
    *               if its primary key has been updated
    *      <LI> importedKeyRestrict - same as importedKeyNoAction 
    *                                 (for ODBC 2.x compatibility)
    *      </UL>
    *	<LI><B>DELETE_RULE</B> short => What happens to 
    *      the foreign key when primary is deleted.
    *      <UL>
    *      <LI> importedKeyNoAction - do not allow delete of primary 
    *               key if it has been imported
    *      <LI> importedKeyCascade - delete rows that import a deleted key
    *      <LI> importedKeySetNull - change imported key to NULL if 
    *               its primary key has been deleted
    *      <LI> importedKeyRestrict - same as importedKeyNoAction 
    *                                 (for ODBC 2.x compatibility)
    *      <LI> importedKeySetDefault - change imported key to default if 
    *               its primary key has been deleted
    *      </UL>
    *	<LI><B>FK_NAME</B> String => foreign key name (may be <code>null</code>)
    *	<LI><B>PK_NAME</B> String => primary key name (may be <code>null</code>)
    *	<LI><B>DEFERRABILITY</B> short => can the evaluation of foreign key 
    *      constraints be deferred until commit
    *      <UL>
    *      <LI> importedKeyInitiallyDeferred - see SQL92 for definition
    *      <LI> importedKeyInitiallyImmediate - see SQL92 for definition 
    *      <LI> importedKeyNotDeferrable - see SQL92 for definition 
    *      </UL>
    *  </OL>
    */
	
	ColumnMetaData column;
	public void setColumn(ColumnMetaData columnMetaData) {
		// TODO Auto-generated method stub
		this.column = columnMetaData;
	}
	public ColumnMetaData getColumn() {
		return column;
	}
	
	public int compareTo(Object obj) {
		
		if(obj instanceof ForeignKeyMetaData)
		{
			ForeignKeyMetaData temp = (ForeignKeyMetaData)obj;
			if(temp != null)
			{
				int ret = this.getFK_NAME().toLowerCase().compareTo(temp.getFK_NAME().toLowerCase());
				
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
