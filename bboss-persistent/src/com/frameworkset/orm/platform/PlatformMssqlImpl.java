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
 * MS SQL Platform implementation.
 *
 * @author <a href="mailto:mpoeschl@marmot.at">Martin Poeschl</a>
 * @version $Id: PlatformMssqlImpl.java,v 1.6 2004/02/22 06:27:19 jmcnally Exp $
 */
public class PlatformMssqlImpl extends PlatformDefaultImpl
{
    /**
     * Default constructor.
     */
    public PlatformMssqlImpl()
    {
        super();
        initialize();
    }

    /**
     * Initializes db specific domain mapping.
     */
    private void initialize()
    {
        setSchemaDomainMapping(new Domain(SchemaType.INTEGER, "INT"));
        setSchemaDomainMapping(new Domain(SchemaType.BOOLEANINT, "INT"));
        setSchemaDomainMapping(new Domain(SchemaType.DOUBLE, "FLOAT"));
        setSchemaDomainMapping(new Domain(SchemaType.LONGVARCHAR, "TEXT"));
        setSchemaDomainMapping(new Domain(SchemaType.DATE, "DATETIME"));
        setSchemaDomainMapping(new Domain(SchemaType.TIME, "DATETIME"));
        setSchemaDomainMapping(new Domain(SchemaType.TIMESTAMP, "DATETIME"));
        setSchemaDomainMapping(new Domain(SchemaType.BINARY, "BINARY(7132)"));
        setSchemaDomainMapping(new Domain(SchemaType.VARBINARY, "IMAGE"));
        setSchemaDomainMapping(new Domain(SchemaType.LONGVARBINARY, "IMAGE"));      
    }
    
    /**
     * @see Platform#getMaxColumnNameLength()
     */
    public int getMaxColumnNameLength()
    {
        return 30;
    }
    
    /**
     * @return Explicitly returns <code>NULL</code> if null values are
     * allowed (as recomended by Microsoft).
     * @see Platform#getNullString(boolean)
     */
    public String getNullString(boolean notNull) 
    {
        return (notNull ? "NOT NULL" : "NULL");
    }

}
