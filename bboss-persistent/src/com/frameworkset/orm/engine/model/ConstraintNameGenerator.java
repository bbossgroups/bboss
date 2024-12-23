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

import com.frameworkset.orm.engine.EngineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A <code>NameGenerator</code> implementation for table-specific
 * constraints.  Conforms to the maximum column name length for the
 * type of database in use.
 *
 * @author <a href="mailto:dlr@finemaltcoding.com>Daniel Rall</a>
 * @version $Id: ConstraintNameGenerator.java,v 1.4 2004/02/22 06:27:19 jmcnally Exp $
 */
public class ConstraintNameGenerator implements NameGenerator
{
    /** Logging class from commons.logging */
    private static Logger log = LoggerFactory.getLogger(ConstraintNameGenerator.class);

    /**
     * First element of <code>inputs</code> should be of type {@link
     * com.frameworkset.orm.engine.model.Database}, second
     * should be a table name, third is the type identifier (spared if
     * trimming is necessary due to database type length constraints),
     * and the fourth is a <code>Integer</code> indicating the number
     * of this contraint.
     *
     * @see com.frameworkset.orm.engine.model.NameGenerator
     */
    public String generateName(List inputs)
        throws EngineException
    {
		StringBuilder name = new StringBuilder();
        Database db = (Database) inputs.get(0);
        name.append((String) inputs.get(1));
        String namePostfix = (String) inputs.get(2);
        String constraintNbr = inputs.get(3).toString();

        // Calculate maximum RDBMS-specific column character limit.
        int maxBodyLength = -1;
        try
        {
            int maxColumnNameLength = db.getPlatform().getMaxColumnNameLength();
            maxBodyLength = (maxColumnNameLength - namePostfix.length()
                    - constraintNbr.length() - 2);

            if (log.isDebugEnabled())
            {
                log.debug("maxColumnNameLength=" + maxColumnNameLength
                        + " maxBodyLength=" + maxBodyLength);
            }
        }
        catch (NumberFormatException maxLengthUnknown)
        {
        }

        // Do any necessary trimming.
        if (maxBodyLength != -1 && name.length() > maxBodyLength)
        {
            name.setLength(maxBodyLength);
        }

        name.append(STD_SEPARATOR_CHAR).append(namePostfix)
            .append(STD_SEPARATOR_CHAR).append(constraintNbr);

        return name.toString();
    }

	@Override
	public String generateName(List inputs, boolean IGNORE_FIRST_TOKEN)
			throws EngineException {
		StringBuilder name = new StringBuilder();
	        Database db = (Database) inputs.get(0);
	        name.append((String) inputs.get(1));
	        String namePostfix = (String) inputs.get(2);
	        String constraintNbr = inputs.get(3).toString();

	        // Calculate maximum RDBMS-specific column character limit.
	        int maxBodyLength = -1;
	        try
	        {
	            int maxColumnNameLength = db.getPlatform().getMaxColumnNameLength();
	            maxBodyLength = (maxColumnNameLength - namePostfix.length()
	                    - constraintNbr.length() - 2);

	            if (log.isDebugEnabled())
	            {
	                log.debug("maxColumnNameLength=" + maxColumnNameLength
	                        + " maxBodyLength=" + maxBodyLength);
	            }
	        }
	        catch (NumberFormatException maxLengthUnknown)
	        {
	        }

	        // Do any necessary trimming.
	        if (maxBodyLength != -1 && name.length() > maxBodyLength)
	        {
	            name.setLength(maxBodyLength);
	        }

	        name.append(STD_SEPARATOR_CHAR).append(namePostfix)
	            .append(STD_SEPARATOR_CHAR).append(constraintNbr);

	        return name.toString();
	}
}
