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

import java.io.Serializable;
import java.util.List;

import com.frameworkset.orm.engine.EngineException;

/**
 * The generic interface to a name generation algorithm.
 *
 * @author <a href="mailto:dlr@finemaltcoding.com>Daniel Rall</a>
 * @author <a href="mailto:byron_foster@byron_foster@yahoo.com>Byron Foster</a>
 * @version $Id: NameGenerator.java,v 1.3 2005/02/23 17:32:09 tfischer Exp $
 */
public interface NameGenerator extends Serializable
{
    /**
     * The character used by most implementations as the separator
     * between name elements.
     */
    char STD_SEPARATOR_CHAR = '_';

    /**
     * The character which separates the schema name from the table name
     */
    char SCHEMA_SEPARATOR_CHAR = '.';

    /**
     * Traditional method for converting schema table and column names
     * to java names.  The <code>CONV_METHOD_XXX</code> constants
     * define how names for columns and tables in the database schema
     * will be converted to java source names.
     *
     * @see JavaNameGenerator#underscoreMethod(String)
     */
    String CONV_METHOD_UNDERSCORE = "underscore";

    /**
     * Similar to {@link #CONV_METHOD_UNDERSCORE} except a possible
     * schema name (preceding a dot (.) )is omitted
     *
     * @see JavaNameGenerator#underscoreOmitSchemaMethod(String)
     */
    String CONV_METHOD_UNDERSCORE_OMIT_SCHEMA = "underscoreOmitSchema";

    /**
     * Similar to {@link #CONV_METHOD_UNDERSCORE} except nothing is
     * converted to lowercase.
     *
     * @see JavaNameGenerator#javanameMethod(String)
     */
    String CONV_METHOD_JAVANAME = "javaname";

    /**
     * Specifies no modification when converting from a schema column
     * or table name to a java name.
     */
    String CONV_METHOD_NOCHANGE = "nochange";

    /**
     * Given a list of <code>String</code> objects, implements an
     * algorithm which produces a name.
     *
     * @param inputs Inputs used to generate a name.
     * @return The generated name.
     * @throws EngineException if the name could not be generated
     */
    String generateName(List inputs) throws EngineException;
}
