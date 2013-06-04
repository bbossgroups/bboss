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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.xml.sax.Attributes;

/**
 * A class for information about foreign keys of a table.
 *
 * @author <a href="mailto:fedor.karpelevitch@home.com">Fedor</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @version $Id: ForeignKey.java,v 1.4 2004/02/22 06:27:19 jmcnally Exp $
 */
public class ForeignKey implements Serializable
{
    private String foreignTableName;
    private String name;
    private String onUpdate;
    private String onDelete;
    private Table parentTable;
    private List localColumns = new ArrayList(3);
    private List foreignColumns = new ArrayList(3);

    // the uppercase equivalent of the onDelete/onUpdate values in the dtd
    private static final String NONE    = "NONE";
    private static final String SETNULL = "SETNULL";

    /**
     * Imports foreign key from an XML specification
     *
     * @param attrib the xml attributes
     */
    public void loadFromXML(Attributes attrib)
    {
        foreignTableName = attrib.getValue("foreignTable");
        name = attrib.getValue("name");
        onUpdate = attrib.getValue("onUpdate");
        onDelete = attrib.getValue("onDelete");
        onUpdate = normalizeFKey(onUpdate);
        onDelete = normalizeFKey(onDelete);
    }

    /**
     * Normalizes the input of onDelete, onUpdate attributes
     *
     * @param attrib the attribute to normalize
     * @return nomalized form
     */
    private String normalizeFKey(String attrib)
    {
        if (attrib == null)
        {
            attrib = NONE;
        }

        attrib = attrib.toUpperCase();
        if (attrib.equals(SETNULL))
        {
            attrib = "SET NULL";
        }
        return attrib;
    }

    /**
     * Returns whether or not the onUpdate attribute is set
     *
     * @return true if the onUpdate attribute is set
     */
    public boolean hasOnUpdate()
    {
       return !onUpdate.equals(NONE);
    }

    /**
     * Returns whether or not the onDelete attribute is set
     *
     * @return true if the onDelete attribute is set
     */
    public boolean hasOnDelete()
    {
       return !onDelete.equals(NONE);
    }

    /**
     * Returns the onUpdate attribute
     *
     * @return the onUpdate attribute
     */
    public String getOnUpdate()
    {
       return onUpdate;
    }

    /**
     * Returns the onDelete attribute
     *
     * @return the onDelete attribute
     */
    public String getOnDelete()
    {
       return onDelete;
    }

    /**
     * Sets the onDelete attribute
     *
     * @param value the onDelete attribute
     */
    public void setOnDelete(String value)
    {
       onDelete = normalizeFKey(value);
    }

    /**
     * Sets the onUpdate attribute
     *
     * @param value the onUpdate attribute
     */
    public void setOnUpdate(String value)
    {
       onUpdate = normalizeFKey(value);
    }

    /**
     * Returns the name attribute.
     *
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name attribute.
     *
     * @param name the name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get the foreignTableName of the FK
     *
     * @return the name of the foreign table
     */
    public String getForeignTableName()
    {
        return foreignTableName;
    }

    /**
     * Set the foreignTableName of the FK
     *
     * @param tableName the name of the foreign table
     */
    public void setForeignTableName(String tableName)
    {
        foreignTableName = tableName;
    }

    /**
     * Set the parent Table of the foreign key
     *
     * @param parent the table
     */
    public void setTable(Table parent)
    {
        parentTable = parent;
    }

    /**
     * Get the parent Table of the foreign key
     *
     * @return the parent table
     */
    public Table getTable()
    {
        return parentTable;
    }

    /**
     * Returns the name of the table the foreign key is in
     *
     * @return the name of the table
     */
    public String getTableName()
    {
        return parentTable.getName();
    }

    /**
     * Adds a new reference entry to the foreign key
     *
     * @param attrib the xml attributes
     */
    public void addReference(Attributes attrib)
    {
        addReference(attrib.getValue("local"), attrib.getValue("foreign"));
    }

    /**
     * Adds a new reference entry to the foreign key
     *
     * @param local name of the local column
     * @param foreign name of the foreign column
     */
    public void addReference(String local, String foreign)
    {
        localColumns.add(local);
        foreignColumns.add(foreign);
    }

    /**
     * Returns a comma delimited string of local column names
     *
     * @return the local column names
     */
    public String getLocalColumnNames()
    {
        return Column.makeList(getLocalColumns());
    }

    /**
     * Returns a comma delimited string of foreign column names
     *
     * @return the foreign column names
     */
    public String getForeignColumnNames()
    {
        return Column.makeList(getForeignColumns());
    }

    /**
     * Returns the list of local column names. You should not edit this List.
     *
     * @return the local columns
     */
    public List getLocalColumns()
    {
        return localColumns;
    }

    /**
     * Utility method to get local column names to foreign column names
     * mapping for this foreign key.
     *
     * @return table mapping foreign names to local names
     */
    public Hashtable getLocalForeignMapping()
    {
        Hashtable h = new Hashtable();

        for (int i = 0; i < localColumns.size(); i++)
        {
            h.put(localColumns.get(i), foreignColumns.get(i));
        }

        return h;
    }

    /**
     * Returns the list of foreign column names. You should not edit this List.
     *
     * @return the foreign columns
     */
    public List getForeignColumns()
    {
        return foreignColumns;
    }

    /**
     * Utility method to get foreign column names to local column names
     * mapping for this foreign key.
     *
     * @return table mapping local names to foreign names
     */
    public Hashtable getForeignLocalMapping()
    {
        Hashtable h = new Hashtable();

        for (int i = 0; i < localColumns.size(); i++)
        {
            h.put(foreignColumns.get(i), localColumns.get(i));
        }

        return h;
    }

    /**
     * String representation of the foreign key. This is an xml representation.
     *
     * @return string representation in xml
     */
    public String toString()
    {
        StringBuffer result = new StringBuffer();
        result.append("    <foreign-key foreignTable=\"")
            .append(getForeignTableName())
            .append("\" name=\"")
            .append(getName())
            .append("\">\n");

        for (int i = 0; i < localColumns.size(); i++)
        {
            result.append("        <reference local=\"")
                .append(localColumns.get(i))
                .append("\" foreign=\"")
                .append(foreignColumns.get(i))
                .append("\"/>\n");
        }
        result.append("    </foreign-key>\n");
        return result.toString();
    }
}
