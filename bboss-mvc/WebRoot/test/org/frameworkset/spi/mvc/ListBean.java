package org.frameworkset.spi.mvc;

import com.frameworkset.orm.annotation.PrimaryKey;

/**
 * CREATE
    TABLE LISTBEAN
    (
        ID INTEGER NOT NULL,
        FIELDNAME VARCHAR(300),
        FIELDLABLE VARCHAR(300),
        FIELDTYPE VARCHAR(300),
        SORTORDER VARCHAR(300),
        ISPRIMARYKEY INTEGER,
        REQUIRED INTEGER,
        FIELDLENGTH INTEGER,
        ISVALIDATED INTEGER,
        CONSTRAINT LISTBEANKEY PRIMARY KEY (ID)
    )
    
    insert into TABLEINFO (TABLE_NAME, TABLE_ID_NAME, TABLE_ID_INCREMENT, TABLE_ID_VALUE, TABLE_ID_GENERATOR, TABLE_ID_TYPE, TABLE_ID_PREFIX) values ('LISTBEAN', 'id', 1, 0, null, 'int', null);
 * @author Administrator
 *
 */
public class ListBean {
	@PrimaryKey(pkname="ListBean",auto=true)
	private int id ;
	private String fieldName;
//	@Column(name="fileNamezzz",dataformat="格式转换" ,type="clob")
	private String fieldLable;
	private String fieldType;
	private String sortorder;
	private boolean isprimaryKey;
	private boolean required;
	private int fieldLength;
	private int isvalidated;
	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/**
	 * @return the fieldLable
	 */
	public String getFieldLable() {
		return fieldLable;
	}
	/**
	 * @param fieldLable the fieldLable to set
	 */
	public void setFieldLable(String fieldLable) {
		this.fieldLable = fieldLable;
	}
	/**
	 * @return the fieldType
	 */
	public String getFieldType() {
		return fieldType;
	}
	/**
	 * @param fieldType the fieldType to set
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	
	/**
	 * @return the isprimaryKey
	 */
	public boolean isIsprimaryKey() {
		return isprimaryKey;
	}
	/**
	 * @param isprimaryKey the isprimaryKey to set
	 */
	public void setIsprimaryKey(boolean isprimaryKey) {
		this.isprimaryKey = isprimaryKey;
	}
	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}
	/**
	 * @param required the required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
	/**
	 * @return the fieldLength
	 */
	public int getFieldLength() {
		return fieldLength;
	}
	/**
	 * @param fieldLength the fieldLength to set
	 */
	public void setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
	}
	/**
	 * @return the isvalidated
	 */
	public int getIsvalidated() {
		return isvalidated;
	}
	/**
	 * @param isvalidated the isvalidated to set
	 */
	public void setIsvalidated(int isvalidated) {
		this.isvalidated = isvalidated;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the sortorder
	 */
	public String getSortorder() {
		return sortorder;
	}
	/**
	 * @param sortorder the sortorder to set
	 */
	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}
	
	

}

