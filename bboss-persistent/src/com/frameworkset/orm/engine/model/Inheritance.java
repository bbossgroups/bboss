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

import org.xml.sax.Attributes;

/**
 * A Class for information regarding possible objects representing a table
 *
 * @author <a href="mailto:jmcnally@collab.net">John McNally</a>
 * @version $Id: Inheritance.java,v 1.2 2004/02/22 06:27:19 jmcnally Exp $
 */
public class Inheritance implements Serializable
{
    private String key;
    private String className;
    private String ancestor;
    private Column parent;

    /**
     * Imports foreign key from an XML specification
     *
     * @param attrib the xml attributes
     */
    public void loadFromXML (Attributes attrib)
    {
        setKey(attrib.getValue("key"));
        setClassName(attrib.getValue("class"));
        setAncestor(attrib.getValue("extends"));
    }

    /**
     * Get the value of key.
     * @return value of key.
     */
    public String getKey()
    {
        return key;
    }

    /**
     * Set the value of key.
     * @param v  Value to assign to key.
     */
    public void setKey(String  v)
    {
        this.key = v;
    }


    /**
     * Get the value of parent.
     * @return value of parent.
     */
    public Column getColumn()
    {
        return parent;
    }

    /**
     * Set the value of parent.
     * @param v  Value to assign to parent.
     */
    public void setColumn(Column  v)
    {
        this.parent = v;
    }

    /**
     * Get the value of className.
     * @return value of className.
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * Set the value of className.
     * @param v  Value to assign to className.
     */
    public void setClassName(String  v)
    {
        this.className = v;
    }

    /**
     * Get the value of ancestor.
     * @return value of ancestor.
     */
    public String getAncestor()
    {
        return ancestor;
    }

    /**
     * Set the value of ancestor.
     * @param v  Value to assign to ancestor.
     */
    public void setAncestor(String  v)
    {
        this.ancestor = v;
    }

    /**
     * String representation of the foreign key. This is an xml representation.
     *
     * @return string representation in xml
     */
    public String toString()
    {
        StringBuffer result = new StringBuffer();
        result.append(" <inheritance key=\"")
              .append(key)
              .append("\" class=\"")
              .append(className)
              .append('\"');


        if (ancestor != null)
        {
            result.append(" extends=\"")
                  .append(ancestor)
                  .append('\"');
        }

        result.append("/>");

        return result.toString();
    }
}
