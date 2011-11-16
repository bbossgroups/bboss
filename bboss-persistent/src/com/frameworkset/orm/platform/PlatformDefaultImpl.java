/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.frameworkset.orm.platform;

/*
 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.sql.Types;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.frameworkset.orm.adapter.DBFactory;
import com.frameworkset.orm.engine.model.Domain;
import com.frameworkset.orm.engine.model.SchemaType;


/**
 * Default implementation for the Platform interface.
 *
 * @author <a href="mailto:mpoeschl@marmot.at">Martin Poeschl</a>
 * @version $Id: PlatformDefaultImpl.java,v 1.9 2004/02/22 06:27:19 jmcnally Exp $
 */
public class PlatformDefaultImpl implements Platform
{ 
    private Map schemaDomainMap;
    
    /**
     * Default constructor.
     */
    public PlatformDefaultImpl() 
    {
        initialize();
    }
    
    private void initialize()
    {
        schemaDomainMap = new Hashtable(30);
        Iterator iter = SchemaType.iterator();
        while (iter.hasNext()) 
        {
            SchemaType type = (SchemaType) iter.next();
            schemaDomainMap.put(type, new Domain(type));
        }
        schemaDomainMap.put(SchemaType.BOOLEANCHAR, 
                new Domain(SchemaType.BOOLEANCHAR, "CHAR"));
        schemaDomainMap.put(SchemaType.BOOLEANINT, 
                new Domain(SchemaType.BOOLEANINT, "INTEGER"));
    }

    protected void setSchemaDomainMapping(Domain domain) 
    {
    	
        schemaDomainMap.put(domain.getType(), domain);
    }
    
    /**
     * @see Platform#getMaxColumnNameLength()
     */
    public int getMaxColumnNameLength()
    {
        return 64;
    }

    /**
     * @see Platform#getNativeIdMethod()
     */
    public String getNativeIdMethod()
    {
        return Platform.IDENTITY;
    }

    /**
     * @see Platform#getDomainForJdbcType(SchemaType)
     */
    public Domain getDomainForSchemaType(SchemaType jdbcType) 
    {
        return (Domain) schemaDomainMap.get(jdbcType);
    }

    /**
     * @return Only produces a SQL fragment if null values are
     * disallowed.
     * @see Platform#getNullString(boolean)
     */
    public String getNullString(boolean notNull)
    {
        // TODO: Check whether this is true for all DBs.  Also verify
        // the old Sybase templates.
        return (notNull ? "NOT NULL" : "");
    }
    
    public SchemaType getSchemaTypeFromSqlType(int sqltype)
    {
    	
    	switch(sqltype)
    	{
    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>BIT</code>.
    	 */
    		case Types.BIT:
    			return SchemaType.BIT;

    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>TINYINT</code>.
    	 */
    		case Types.TINYINT:
    			return SchemaType.TINYINT;

    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>SMALLINT</code>.
    	 */
    		case Types.SMALLINT:
    			return SchemaType.SMALLINT;

    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>INTEGER</code>.
    	 */
    		case Types.INTEGER:
    			return SchemaType.INTEGER;

    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>BIGINT</code>.
    	 */
    		case Types.BIGINT:
    			return SchemaType.BIGINT;

    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>FLOAT</code>.
    	 */
    		case Types.FLOAT:
    			return SchemaType.FLOAT;

    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>REAL</code>.
    	 */
    		case Types.REAL:
    			return SchemaType.REAL;

    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>DOUBLE</code>.
    	 */
    		case Types.DOUBLE:
    			return SchemaType.DOUBLE;

    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>NUMERIC</code>.
    	 */
    		case Types.NUMERIC:
    			return SchemaType.NUMERIC;

    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>DECIMAL</code>.
    	 */
    		case Types.DECIMAL:
    			return SchemaType.DECIMAL;

    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>CHAR</code>.
    	 */
    		case Types.CHAR:
    			return SchemaType.CHAR;

    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>VARCHAR</code>.
    	 */
    		case Types.VARCHAR:
    			return SchemaType.VARCHAR;


    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>LONGVARCHAR</code>.
    	 */
    		case Types.LONGVARCHAR:
    			return SchemaType.LONGVARCHAR;


    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>DATE</code>.
    	 */
    		case Types.DATE:
    			return SchemaType.DATE;

    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>TIME</code>.
    	 */
    		case Types.TIME:
    			return SchemaType.TIME;

    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>TIMESTAMP</code>.
    	 */
    		case Types.TIMESTAMP:
    			return SchemaType.TIMESTAMP;


    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>BINARY</code>.
    	 */
    		case Types.BINARY:
    			return SchemaType.BINARY;

    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>VARBINARY</code>.
    	 */
    		case Types.VARBINARY:
    			return SchemaType.VARBINARY;

    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>LONGVARBINARY</code>.
    	 */
    		case Types.LONGVARBINARY:
    			return SchemaType.LONGVARBINARY;

    	/**
    	 * <P>The constant in the Java programming language, sometimes referred
    	 * to as a type code, that identifies the generic SQL type 
    	 * <code>NULL</code>.
    	 */
    		case Types.NULL:
    			return SchemaType.NULL;

    	    /**
    	     * The constant in the Java programming language that indicates
    	     * that the SQL type is database-specific and
    	     * gets mapped to a Java object that can be accessed via
    	     * the methods <code>getObject</code> and <code>setObject</code>.
    	     */
    			
    		case Types.OTHER:
    			return SchemaType.OTHER;	
    		

    	        

    	    /**
    	     * The constant in the Java programming language, sometimes referred to
    	     * as a type code, that identifies the generic SQL type
    	     * <code>JAVA_OBJECT</code>.
    	     * @since 1.2
    	     */
    		case Types.JAVA_OBJECT:
    			return SchemaType.JAVA_OBJECT;	

    	    /**
    	     * The constant in the Java programming language, sometimes referred to
    	     * as a type code, that identifies the generic SQL type
    	     * <code>DISTINCT</code>.
    	     * @since 1.2
    	     */
    		case Types.DISTINCT:
    			return SchemaType.DISTINCT;
    		
    	    /**
    	     * The constant in the Java programming language, sometimes referred to
    	     * as a type code, that identifies the generic SQL type
    	     * <code>STRUCT</code>.
    	     * @since 1.2
    	     */
    		case Types.STRUCT:
    			return SchemaType.STRUCT;

    	    /**
    	     * The constant in the Java programming language, sometimes referred to
    	     * as a type code, that identifies the generic SQL type
    	     * <code>ARRAY</code>.
    	     * @since 1.2
    	     */
    		case Types.ARRAY:
    			return SchemaType.ARRAY;

    	    /**
    	     * The constant in the Java programming language, sometimes referred to
    	     * as a type code, that identifies the generic SQL type
    	     * <code>BLOB</code>.
    	     * @since 1.2
    	     */
    		case Types.BLOB:
    			return SchemaType.BLOB;

    	    /**
    	     * The constant in the Java programming language, sometimes referred to
    	     * as a type code, that identifies the generic SQL type
    	     * <code>CLOB</code>.
    	     * @since 1.2
    	     */
    		case Types.CLOB:
    			return SchemaType.CLOB;

    	    /**
    	     * The constant in the Java programming language, sometimes referred to
    	     * as a type code, that identifies the generic SQL type
    	     * <code>REF</code>.
    	     * @since 1.2
    	     */
    		case Types.REF:
    			return SchemaType.REF;
    	        
    	    /**
    	     * The constant in the Java programming language, somtimes referred to
    	     * as a type code, that identifies the generic SQL type <code>DATALINK</code>.
    	     *
    	     * @since 1.4
    	     */
    		case Types.DATALINK:
    			return SchemaType.DATALINK;

    	    /**
    	     * The constant in the Java programming language, somtimes referred to
    	     * as a type code, that identifies the generic SQL type <code>BOOLEAN</code>.
    	     *
    	     * @since 1.4
    	     */
    		case Types.BOOLEAN:
    			return SchemaType.BOOLEANCHAR;
    		default:
    			return SchemaType.DEFAULT;
    		

    	}
    }

    /**
     * @see Platform#getAutoIncrement()
     */
    public String getAutoIncrement()
    {
        return "IDENTITY";
    }

    /**
     * @see Platform#hasScale(String)
     * TODO collect info for all platforms
     */
    public boolean hasScale(String sqlType)
    {
        return true;
    }

    /**
     * @see Platform#hasSize(String)
     * TODO collect info for all platforms
     */
    public boolean hasSize(String sqlType)
    {
        return true;
    }

	public Domain getDomainForSchemaType(int jdbcType)
	{
		// TODO Auto-generated method stub
		return this.getDomainForSchemaType(this.getSchemaTypeFromSqlType(jdbcType));
	}

	public boolean hasSize(int sqlType)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean hasScale(int sqlType)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public String getDBTYPE()
	{
		
		return DBFactory.DBNone;
	}

}
