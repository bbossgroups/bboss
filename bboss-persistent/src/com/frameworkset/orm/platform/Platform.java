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

import com.frameworkset.orm.engine.model.Domain;
import com.frameworkset.orm.engine.model.SchemaType;

/**
 * Interface for RDBMS platform specific behaviour.
 *
 * @author <a href="mailto:mpoeschl@marmot.at">Martin Poeschl</a>
 * @version $Id: Platform.java,v 1.8 2004/02/22 06:27:19 jmcnally Exp $
 */
public interface Platform
{
    /** constant for native id method */
    static final String IDENTITY = "identity";
    /** constant for native id method */
    static final String SEQUENCE = "sequence";
    
    /**
     * Returns the native IdMethod (sequence|identity)
     *
     * @return the native IdMethod
     */
    String getNativeIdMethod();

    /**
     * Returns the max column length supported by the db.
     *
     * @return the max column length
     */
    int getMaxColumnNameLength();

    /**
     * Returns the db specific domain for a jdbcType.
     *
     * @param jdbcType the jdbcType name
     * @return the db specific domain
     */
    Domain getDomainForSchemaType(SchemaType jdbcType);
    
    /**
     * Returns the db specific domain for a jdbcType.
     *
     * @param jdbcType the jdbcType name
     * @return the db specific domain
     */
    Domain getDomainForSchemaType(int jdbcType);
    
    /**
     * @return The RDBMS-specific SQL fragment for <code>NULL</code>
     * or <code>NOT NULL</code>.
     */
    String getNullString(boolean notNull);

    /**
     * @return The RDBMS-specific SQL fragment for autoincrement.
     */
    String getAutoIncrement();
    
    /**
     * Returns if the RDBMS-specific SQL type has a size attribute.
     * 
     * @param sqlType the SQL type
     * @return true if the type has a size attribute
     */
    boolean hasSize(String sqlType);
    
    /**
     * Returns if the RDBMS-specific SQL type has a size attribute.
     * 
     * @param sqlType the SQL type
     * @return true if the type has a size attribute
     */
    boolean hasSize(int sqlType);
    
    /**
     * Returns if the RDBMS-specific SQL type has a scale attribute.
     * 
     * @param sqlType the SQL type
     * @return true if the type has a scale attribute
     */
    boolean hasScale(String sqlType);
    
    /**
     * Returns if the RDBMS-specific SQL type has a scale attribute.
     * 
     * @param sqlType the SQL type
     * @return true if the type has a scale attribute
     */
    boolean hasScale(int sqlType); 
    
    String getDBTYPE();
    
    public SchemaType getSchemaTypeFromSqlType(int sqltype);
    
    
}
