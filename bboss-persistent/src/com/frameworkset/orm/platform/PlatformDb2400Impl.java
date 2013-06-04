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
 * DB2-AS400 Platform implementation.
 *
 * @author <a href="mailto:mpoeschl@marmot.at">Martin Poeschl</a>
 * @version $Id: PlatformDb2400Impl.java,v 1.4 2004/02/22 06:27:19 jmcnally Exp $
 */
public class PlatformDb2400Impl extends PlatformDefaultImpl
{
    /**
     * Default constructor.
     */
    public PlatformDb2400Impl()
    {
        super();
        initialize();
    }

    /**
     * Initializes db specific domain mapping.
     */
    private void initialize()
    {
        setSchemaDomainMapping(new Domain(SchemaType.TINYINT, "SMALLINT"));
        setSchemaDomainMapping(new Domain(SchemaType.INTEGER, "INT"));
        setSchemaDomainMapping(new Domain(SchemaType.BOOLEANINT, "INT"));
        setSchemaDomainMapping(new Domain(SchemaType.DOUBLE, "DOUBLE PRECISION"));
        setSchemaDomainMapping(new Domain(SchemaType.DECIMAL, "DEC"));
        setSchemaDomainMapping(new Domain(SchemaType.CHAR, "VARCHAR"));
        setSchemaDomainMapping(new Domain(SchemaType.BOOLEANCHAR, "VARCHAR"));
        setSchemaDomainMapping(new Domain(SchemaType.LONGVARCHAR, "LONG VARCHAR"));
        setSchemaDomainMapping(new Domain(SchemaType.VARBINARY, "VARCHAR (32000) FOR BIT DATA"));
        setSchemaDomainMapping(new Domain(SchemaType.LONGVARBINARY, "LONG VARCHAR FOR BIT DATA"));
    }
    
    /**
     * @see Platform#getMaxColumnNameLength()
     */
    public int getMaxColumnNameLength()
    {
        return 30;
    }

}
