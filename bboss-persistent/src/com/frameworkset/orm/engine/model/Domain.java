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

import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;

import com.frameworkset.orm.platform.Platform;

/**
 * A Class for holding data about a column used in an Application.
 *
 * @author <a href="mailto:mpoeschl@marmot.at>Martin Poeschl</a>
 * @version $Id: Domain.java,v 1.7 2004/02/22 06:27:19 jmcnally Exp $
 */
public class Domain implements Serializable
{
    private String name;
    private String description;
    private String size;
    private String scale;
    /** type as defined in schema.xml */
    private SchemaType torqueType;
    private String sqlType;
    private String defaultValue;
    
    /**
     * Creates a new instance with a <code>null</code> name.
     */
    public Domain()
    {
        this.name = null;
    }

    /**
     * Creates a new Domain and set the name
     *
     * @param name column name
     */
    public Domain(String name)
    {
        this.name = name;
    }
    
    /**
     * Creates a new Domain and set the name
     */
    public Domain(SchemaType type)
    {
        this.name = null;
        this.torqueType = type;
        this.sqlType = type.getName();
    }

    /**
     * Creates a new Domain and set the name
     */
    public Domain(SchemaType type, String sqlType)
    {
        this.name = null;
        this.torqueType = type;
        this.sqlType = sqlType;
    }
    
    /**
     * Creates a new Domain and set the name
     */
    public Domain(SchemaType type, String sqlType, String size, String scale)
    {
        this.name = null;
        this.torqueType = type;
        this.sqlType = sqlType;
        this.size = size;
        this.scale = scale;
    }

    /**
     * Creates a new Domain and set the name
     */
    public Domain(SchemaType type, String sqlType, String size)
    {
        this.name = null;
        this.torqueType = type;
        this.sqlType = sqlType;
        this.size = size;
    }
    
    public Domain(Domain domain)
    {
        copy(domain);
    }
    
    public void copy(Domain domain)
    {
        this.defaultValue = domain.getDefaultValue();
        this.description = domain.getDescription();
        this.name = domain.getName();
        this.scale = domain.getScale();
        this.size = domain.getSize();
        this.sqlType = domain.getSqlType();
        this.torqueType = domain.getType();
    }
    
    /**
     * Imports a column from an XML specification
     */
    public void loadFromXML(Attributes attrib, Platform platform)
    {
        SchemaType schemaType = SchemaType.getEnum(attrib.getValue("type"));
        copy(platform.getDomainForSchemaType(schemaType));
        //Name
        name = attrib.getValue("name");
        //Default column value.
        defaultValue = attrib.getValue("default");
        size = attrib.getValue("size");
        scale = attrib.getValue("scale");

        description = attrib.getValue("description");
    }

    /**
     * @return Returns the description.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return Returns the scale.
     */
    public String getScale()
    {
        return scale;
    }

    /**
     * @param scale The scale to set.
     */
    public void setScale(String scale)
    {
        this.scale = scale;
    }
    
    /**
     * Replaces the size if the new value is not null.
     * 
     * @param value The size to set.
     */
    public void replaceScale(String value)
    {
        this.scale = StringUtils.defaultString(value, getScale());
    }

    /**
     * @return Returns the size.
     */
    public String getSize()
    {
        return size;
    }

    /**
     * @param size The size to set.
     */
    public void setSize(String size)
    {
        this.size = size;
    }
    
    /**
     * Replaces the size if the new value is not null.
     * 
     * @param value The size to set.
     */
    public void replaceSize(String value)
    {
        this.size = StringUtils.defaultString(value, getSize());
    }

    /**
     * @return Returns the torqueType.
     */
    public SchemaType getType()
    {
        return torqueType;
    }

    /**
     * @param torqueType The torqueType to set.
     */
    public void setType(SchemaType torqueType)
    {
        this.torqueType = torqueType;
    }

    /**
     * @param torqueType The torqueType to set.
     */
    public void setType(String torqueType)
    {
        this.torqueType = SchemaType.getEnum(torqueType);
    }
    
    /**
     * Replaces the default value if the new value is not null.
     * 
     * @param value The defaultValue to set.
     */
    public void replaceType(String value)
    {
        this.torqueType = SchemaType.getEnum(
                StringUtils.defaultString(value, getType().getName()));
    }
    
    /**
     * @return Returns the defaultValue.
     */
    public String getDefaultValue()
    {
        return defaultValue;
    }

    /**
     * Return a string that will give this column a default value.
     * @deprecated
     */
    public String getDefaultSetting()
    {
        StringBuffer dflt = new StringBuffer(0);
        if (getDefaultValue() != null)
        {
            dflt.append("default ");
            if (TypeMap.isTextType(getType()))
            {
                // TODO: Properly SQL-escape the text.
                dflt.append('\'').append(getDefaultValue()).append('\'');
            }
            else
            {
                dflt.append(getDefaultValue());
            }
        }
        return dflt.toString();
    } 
     
    /**
     * @param defaultValue The defaultValue to set.
     */
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }
    
    /**
     * Replaces the default value if the new value is not null.
     * 
     * @param value The defaultValue to set.
     */
    public void replaceDefaultValue(String value)
    {
        this.defaultValue = StringUtils.defaultString(value, getDefaultValue());
    }

    /**
     * @return Returns the sqlType.
     */
    public String getSqlType() 
    {
        return sqlType;
    }

    /**
     * @param sqlType The sqlType to set.
     */
    public void setSqlType(String sqlType) 
    {
        this.sqlType = sqlType;
    }

    /**
     * Return the size and scale in brackets for use in an sql schema.
     * 
     * @return size and scale or an empty String if there are no values 
     *         available.
     */
    public String printSize()
    {
        if (size != null && scale != null) 
        {
            return '(' + size + ',' + scale + ')';
        }
        else if (size != null) 
        {
            return '(' + size + ')';
        }
        else
        {
            return "";
        }
    }

}
