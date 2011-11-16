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

import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;


/**
 * A <code>NameGenerator</code> implementation for Java-esque names.
 *
 * @author <a href="mailto:dlr@finemaltcoding.com>Daniel Rall</a>
 * @author <a href="mailto:byron_foster@byron_foster@yahoo.com>Byron Foster</a>
 * @version $Id: JavaNameGenerator.java,v 1.4 2005/02/23 17:32:09 tfischer Exp $
 */
public class JavaNameGenerator implements NameGenerator
{
    /**
     * <code>inputs</code> should consist of two elements, the
     * original name of the database element and the method for
     * generating the name.  There are currently three methods:
     * <code>CONV_METHOD_NOCHANGE</code> - xml names are converted
     * directly to java names without modification.
     * <code>CONV_METHOD_UNDERSCORE</code> will capitalize the first
     * letter, remove underscores, and capitalize each letter before
     * an underscore.  All other letters are lowercased. "javaname"
     * works the same as the <code>CONV_METHOD_JAVANAME</code> method
     * but will not lowercase any characters.
     *
     * @param inputs list expected to contain two parameters, element
     * 0 contains name to convert, element 1 contains method for conversion.
     * @return The generated name.
     * @see com.frameworkset.orm.engine.model.NameGenerator
     */
    public String generateName(List inputs)
    {
        String schemaName = (String) inputs.get(0);
        String method = (String) inputs.get(1);
        String javaName = null;

        if (CONV_METHOD_UNDERSCORE.equals(method))
        {
            javaName = underscoreMethod(schemaName);
        }
        else if (CONV_METHOD_UNDERSCORE_OMIT_SCHEMA.equals(method))
        {
            javaName = underscoreOmitSchemaMethod(schemaName);
        }
        else if (CONV_METHOD_JAVANAME.equals(method))
        {
            javaName = javanameMethod(schemaName);
        }
        else if (CONV_METHOD_NOCHANGE.equals(method))
        {
            javaName = nochangeMethod(schemaName);
        }
        else
        {
            // if for some reason nothing is defined then we default
            // to the traditional method.
            javaName = underscoreMethod(schemaName);
        }

        return javaName;
    }

    /**
     * Converts a database schema name to java object name.  Removes
     * <code>STD_SEPARATOR_CHAR</code> and <code>SCHEMA_SEPARATOR_CHAR</code>,
     * capitilizes first letter of name and each letter after the 
     * <code>STD_SEPERATOR</code> and <code>SCHEMA_SEPARATOR_CHAR</code>,
     * converts the rest of the letters to lowercase.
     *
     * @param schemaName name to be converted.
     * @return converted name.
     * @see com.frameworkset.orm.engine.model.NameGenerator
     * @see #underscoreMethod(String)
     */
    protected String underscoreMethod(String schemaName)
    {
        StringBuffer name = new StringBuffer();
        
        // remove the STD_SEPARATOR_CHARs and capitalize 
        // the tokens
        StringTokenizer tok = new StringTokenizer
            (schemaName, String.valueOf(STD_SEPARATOR_CHAR));
        while (tok.hasMoreTokens())
        {
            String namePart = ((String) tok.nextElement()).toLowerCase();
            name.append(StringUtils.capitalize(namePart));
        }
        
        // remove the SCHEMA_SEPARATOR_CHARs and capitalize 
        // the tokens
        schemaName = name.toString();
        name = new StringBuffer();
        tok = new StringTokenizer
            (schemaName, String.valueOf(SCHEMA_SEPARATOR_CHAR));
        while (tok.hasMoreTokens())
        {
            String namePart = (String) tok.nextElement();
            name.append(StringUtils.capitalize(namePart));
        }
        return name.toString();
    }

    /**
     * Converts a database schema name to java object name. 
     * First, it removes all characters before the last occurence of 
     * .<code>SCHEMA_SEPARATOR_CHAR</code>. Then, in a second step, removes
     * <code>STD_SEPARATOR_CHAR</code>, capitilizes first letter of
     * name and each letter after the <code>STD_SEPERATOR</code>,
     * and converts the rest of the letters to lowercase.
     *
     * @param schemaName name to be converted.
     * @return converted name.
     * @see com.frameworkset.orm.engine.model.NameGenerator
     * @see #underscoreOmitSchemaMethod(String)
     */
    protected String underscoreOmitSchemaMethod(String schemaName)
    {
        // take only part after last dot
        int lastDotPos = schemaName.lastIndexOf(SCHEMA_SEPARATOR_CHAR);
        if (lastDotPos != -1) {
            schemaName = schemaName.substring(lastDotPos + 1);
        }
        StringBuffer name = new StringBuffer();
        StringTokenizer tok = new StringTokenizer
            (schemaName, String.valueOf(STD_SEPARATOR_CHAR));
        while (tok.hasMoreTokens())
        {
            String namePart = ((String) tok.nextElement()).toLowerCase();
            name.append(StringUtils.capitalize(namePart));
        }
        return name.toString();
    }

    /**
     * Converts a database schema name to java object name.  Operates
     * same as underscoreMethod but does not convert anything to
     * lowercase.
     *
     * @param schemaName name to be converted.
     * @return converted name.
     * @see com.frameworkset.orm.engine.model.NameGenerator
     * @see #underscoreMethod(String)
     */
    protected String javanameMethod(String schemaName)
    {
        StringBuffer name = new StringBuffer();
        StringTokenizer tok = new StringTokenizer
            (schemaName, String.valueOf(STD_SEPARATOR_CHAR));
        while (tok.hasMoreTokens())
        {
            String namePart = (String) tok.nextElement();
            name.append(StringUtils.capitalize(namePart));
        }

        // remove the SCHEMA_SEPARATOR_CHARs and capitalize 
        // the tokens
        schemaName = name.toString();
        name = new StringBuffer();
        
        tok = new StringTokenizer
            (schemaName, String.valueOf(SCHEMA_SEPARATOR_CHAR));
        while (tok.hasMoreTokens())
        {
            String namePart = (String) tok.nextElement();
            name.append(StringUtils.capitalize(namePart));
        }
        return name.toString();
    }

    /**
     * Converts a database schema name to java object name.  In this
     * case no conversion is made.
     *
     * @param name name to be converted.
     * @return The <code>name</code> parameter, unchanged.
     */
    protected final String nochangeMethod(String name)
    {
        return name;
    }
}
