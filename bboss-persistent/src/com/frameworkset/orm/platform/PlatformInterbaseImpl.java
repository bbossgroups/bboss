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
 * Interbase Platform implementation.
 *
 * @author <a href="mailto:mpoeschl@marmot.at">Martin Poeschl</a>
 * @version $Id: PlatformInterbaseImpl.java,v 1.5 2004/02/22 06:27:19 jmcnally Exp $
 */
public class PlatformInterbaseImpl extends PlatformDefaultImpl
{
    /**
     * Default constructor.
     */
    public PlatformInterbaseImpl()
    {
        super();
        initialize();
    }

    /**
     * Initializes db specific domain mapping.
     */
    private void initialize()
    {
        setSchemaDomainMapping(new Domain(SchemaType.CHAR, "VARCHAR"));
        setSchemaDomainMapping(new Domain(SchemaType.BOOLEANCHAR, "VARCHAR"));
        setSchemaDomainMapping(new Domain(SchemaType.LONGVARCHAR, "VARCHAR"));
        setSchemaDomainMapping(new Domain(SchemaType.VARBINARY, "BLOB"));
    }

}
