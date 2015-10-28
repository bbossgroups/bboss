package com.frameworkset.sqlexecutor;


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
public class ListBean extends ParentListBean{
	
	

}

