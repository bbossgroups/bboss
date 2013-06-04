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

import com.frameworkset.orm.adapter.DBFactory;
import com.frameworkset.orm.engine.model.Domain;
import com.frameworkset.orm.engine.model.SchemaType;

/**
 * Cloudscape Platform implementation.
 *
 * @author <a href="mailto:mpoeschl@marmot.at">Martin Poeschl</a>
 * @version $Id: PlatformCloudscapeImpl.java,v 1.5 2004/02/22 06:27:19 jmcnally Exp $
 */
public class PlatformCloudscapeImpl extends PlatformDefaultImpl
{
    /**
     * Default constructor.
     */
    public PlatformCloudscapeImpl()
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
        setSchemaDomainMapping(new Domain(SchemaType.BIGINT, "LONGINT"));
        setSchemaDomainMapping(new Domain(SchemaType.DOUBLE, "DOUBLE PRECISION"));
        setSchemaDomainMapping(new Domain(SchemaType.LONGVARCHAR, "LONG VARCHAR"));
        setSchemaDomainMapping(new Domain(SchemaType.BINARY, "LONG BINARY"));
        setSchemaDomainMapping(new Domain(SchemaType.VARBINARY, "LONG BINARY"));
        setSchemaDomainMapping(new Domain(SchemaType.LONGVARBINARY, "LONG VARBINARY"));
        setSchemaDomainMapping(new Domain(SchemaType.JAVA_OBJECT, "SERIALIZE"));    
    }

    /**
     * @see Platform#getAutoIncrement()
     */
    public String getAutoIncrement()
    {
        return "DEFAULT AUTOINCREMENT";
    }
    
    @Override
	public String getDBTYPE() {
		// TODO Auto-generated method stub
		return DBFactory.DBCloudscape;
	}
    
    
}
