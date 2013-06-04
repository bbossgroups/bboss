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
 * Postgresql Platform implementation.
 *
 * @author <a href="mailto:mpoeschl@marmot.at">Martin Poeschl</a>
 * @version $Id: PlatformPostgresqlImpl.java,v 1.6 2004/02/22 06:27:19 jmcnally Exp $
 */
public class PlatformPostgresqlImpl extends PlatformDefaultImpl
{
    /**
     * Default constructor.
     */
    public PlatformPostgresqlImpl()
    {
        super();
        initialize();
    }

    /**
     * Initializes db specific domain mapping.
     */
    private void initialize()
    {
        setSchemaDomainMapping(new Domain(SchemaType.BIT, "BOOLEAN"));
        setSchemaDomainMapping(new Domain(SchemaType.TINYINT, "INT2"));
        setSchemaDomainMapping(new Domain(SchemaType.SMALLINT, "INT2"));
        setSchemaDomainMapping(new Domain(SchemaType.BIGINT, "INT8"));
        setSchemaDomainMapping(new Domain(SchemaType.REAL, "FLOAT"));
        // TODO check this .. just copied from db.props
        setSchemaDomainMapping(new Domain(SchemaType.BOOLEANCHAR, "CHAR"));
        // TODO check this .. just copied from db.props
        setSchemaDomainMapping(new Domain(SchemaType.BOOLEANINT, "INT2"));
        setSchemaDomainMapping(new Domain(SchemaType.DOUBLE, "DOUBLE PRECISION"));
        setSchemaDomainMapping(new Domain(SchemaType.LONGVARCHAR, "TEXT"));
        setSchemaDomainMapping(new Domain(SchemaType.BINARY, "BYTEA"));
        setSchemaDomainMapping(new Domain(SchemaType.VARBINARY, "BYTEA"));
        setSchemaDomainMapping(new Domain(SchemaType.LONGVARBINARY, "BYTEA"));
        setSchemaDomainMapping(new Domain(SchemaType.CLOB, "TEXT"));
    }
    
    /**
     * @see Platform#getNativeIdMethod()
     */
    public String getNativeIdMethod()
    {
        return Platform.SEQUENCE;
    }

    /**
     * @see Platform#getAutoIncrement()
     */
    public String getAutoIncrement()
    {
        return "";
    }
    
}
