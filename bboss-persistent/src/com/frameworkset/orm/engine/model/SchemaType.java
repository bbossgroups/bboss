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
package com.frameworkset.orm.engine.model;

/*
 
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Serializable;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

/**
 * Enum for types used in Torque schema.xml files.
 *
 * @author <a href="mailto:mpoeschl@marmot.at>Martin Poeschl</a>
 * @version $Id: SchemaType.java 239626 2005-08-24 12:19:51Z henning $
 */
public class SchemaType extends Enum implements Serializable
{
    /**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>BIT</code>.
	 */
    public static final SchemaType BIT = new SchemaType("BIT");
    
    /**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>TINYINT</code>.
	 */
    public static final SchemaType TINYINT = new SchemaType("TINYINT");
    
    /**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>SMALLINT</code>.
	 */
		
    public static final SchemaType SMALLINT = new SchemaType("SMALLINT");
    

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>INTEGER</code>.
	 */
	
    public static final SchemaType INTEGER = new SchemaType("INTEGER");
    /**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>BIGINT</code>.
	 */
	
    public static final SchemaType BIGINT = new SchemaType("BIGINT");
	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>FLOAT</code>.
	 */
	
    public static final SchemaType FLOAT = new SchemaType("FLOAT");
    /**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>REAL</code>.
	 */

    public static final SchemaType REAL = new SchemaType("REAL");
    
    /**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>NUMERIC</code>.
	 */


    public static final SchemaType NUMERIC = new SchemaType("NUMERIC");

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>DECIMAL</code>.
	 */
		
    public static final SchemaType DECIMAL = new SchemaType("DECIMAL");
    
    /**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>CHAR</code>.
	 */
		
    public static final SchemaType CHAR = new SchemaType("CHAR");
    /**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>VARCHAR</code>.
	 */
		
    public static final SchemaType VARCHAR = new SchemaType("VARCHAR");
 
    /**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>LONGVARCHAR</code>.
	 */
			
    public static final SchemaType LONGVARCHAR = new SchemaType("LONGVARCHAR");
    /**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>DATE</code>.
	 */
		
    public static final SchemaType DATE = new SchemaType("DATE");
    /**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>TIME</code>.
	 */
		
    public static final SchemaType TIME = new SchemaType("TIME");
    /**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>TIMESTAMP</code>.
	 */

    public static final SchemaType TIMESTAMP = new SchemaType("TIMESTAMP");
  
	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>BINARY</code>.
	 */
		
    public static final SchemaType BINARY = new SchemaType("BINARY");
    /**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>VARBINARY</code>.
	 */
		

    public static final SchemaType VARBINARY = new SchemaType("VARBINARY");
    /**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>LONGVARBINARY</code>.
	 */
		
    public static final SchemaType LONGVARBINARY = new SchemaType("LONGVARBINARY");
    /**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>NULL</code>.
	 */
		
    public static final SchemaType NULL = new SchemaType("NULL");

    /**
     * The constant in the Java programming language that indicates
     * that the SQL type is database-specific and
     * gets mapped to a Java object that can be accessed via
     * the methods <code>getObject</code> and <code>setObject</code>.
     */
	
    public static final SchemaType OTHER = new SchemaType("OTHER");
    
    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * <code>JAVA_OBJECT</code>.
     * @since 1.2
     */
        
    public static final SchemaType JAVA_OBJECT = new SchemaType("JAVA_OBJECT");
    
    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * <code>DISTINCT</code>.
     * @since 1.2
     */
        
    public static final SchemaType DISTINCT = new SchemaType("DISTINCT");
    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * <code>STRUCT</code>.
     * @since 1.2
     */

    public static final SchemaType STRUCT = new SchemaType("STRUCT");
    
    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * <code>ARRAY</code>.
     * @since 1.2
     */
        
    public static final SchemaType ARRAY = new SchemaType("ARRAY");
    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * <code>BLOB</code>.
     * @since 1.2
     */

    public static final SchemaType BLOB = new SchemaType("BLOB");
    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * <code>CLOB</code>.
     * @since 1.2
     */
        
    public static final SchemaType CLOB = new SchemaType("CLOB");
    /**
     * The constant in the Java programming language, sometimes referred to
     * as a type code, that identifies the generic SQL type
     * <code>REF</code>.
     * @since 1.2
     */
        
    public static final SchemaType REF = new SchemaType("REF");
    /**
     * The constant in the Java programming language, somtimes referred to
     * as a type code, that identifies the generic SQL type <code>BOOLEAN</code>.
     * public final static int BOOLEAN = 16;
     * @since 1.4
     */
    
    public static final SchemaType BOOLEANINT = new SchemaType("BOOLEANINT");
    public static final SchemaType BOOLEANCHAR = new SchemaType("BOOLEANCHAR");

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>DOUBLE</code>.
	 */
    public static final SchemaType DOUBLE = new SchemaType("DOUBLE");
    
    /**
     * The constant in the Java programming language, somtimes referred to
     * as a type code, that identifies the generic SQL type <code>DATALINK</code>.
     *
     * @since 1.4
     */
    public final static SchemaType DATALINK = new SchemaType("DATALINK");
    
    public final static SchemaType DEFAULT = SchemaType.TINYINT;

    private SchemaType(String type)
    {
        super(type);
    }

    public static SchemaType getEnum(String type)
    {
    	Types s;
        return (SchemaType) getEnum(SchemaType.class, type);
    }

    public static Map getEnumMap()
    {
        return getEnumMap(SchemaType.class);
    }

    public static List getEnumList()
    {
        return getEnumList(SchemaType.class);
    }

    public static Iterator iterator()
    {
        return iterator(SchemaType.class);
    }
    
    public String getJavaType()
    {
    	return TypeMap.getJavaNative(this);
    }
    
   

}
