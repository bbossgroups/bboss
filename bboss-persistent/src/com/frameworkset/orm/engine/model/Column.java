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
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;

import com.frameworkset.orm.engine.EngineException;
import com.frameworkset.orm.platform.Platform;
import com.frameworkset.orm.platform.PlatformDefaultImpl;

/**
 * A Class for holding data about a column used in an Application.
 *
 * @author <a href="mailto:leon@opticode.co.za">Leon Messerschmidt</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @author <a href="mailto:byron_foster@byron_foster@yahoo.com>Byron Foster</a>
 * @author <a href="mailto:mpoeschl@marmot.at>Martin Poeschl</a>
 * @version $Id: Column.java,v 1.28 2005/04/17 10:45:43 tfischer Exp $
 */
public class Column implements Serializable
{
    private static final SchemaType DEFAULT_TYPE = SchemaType.VARCHAR;
    /** Logging class from commons.logging */
    private static Logger log = Logger.getLogger(Column.class);
    private String name;
    private String description;
    private Domain domain = new Domain();
    private String javaName = null;
    private String javaNamingMethod;
    private boolean isNotNull = false;
    private String javaType;
    private Table parentTable;
    private int position;
    private boolean isPrimaryKey = false;
    private boolean isUnique = false;
    private boolean isAutoIncrement = false;
    private List referrers;
    // only one type is supported currently, which assumes the
    // column either contains the classnames or a key to
    // classnames specified in the schema.  Others may be
    // supported later.
    private String inheritanceType;
    private boolean isInheritance;
    private boolean isEnumeratedClasses;
    private List inheritanceList;
    private boolean needsTransactionInPostgres;

    /** generate is... setters for boolean columns if true */
    private boolean correctGetters = false;

    /** class name to do input validation on this column */
    private String inputValidator = null;

    /**
     * Creates a new instance with a <code>null</code> name.
     */
    public Column()
    {
        this(null);
    }

    /**
     * Creates a new column and set the name
     *
     * @param name column name
     */
    public Column(String name)
    {
        this.name = name;
    }

    /**
     * Return a comma delimited string listing the specified columns.
     *
     * @param columns Either a list of <code>Column</code> objects, or
     * a list of <code>String</code> objects with column names.
     */
    public static String makeList(List columns)
    {
        Object obj = columns.get(0);
        boolean isColumnList = (obj instanceof Column);
        if (isColumnList)
        {
            obj = ((Column) obj).getName();
        }
        StringBuffer buf = new StringBuffer((String) obj);
        for (int i = 1; i < columns.size(); i++)
        {
            obj = columns.get(i);
            if (isColumnList)
            {
                obj = ((Column) obj).getName();
            }
            buf.append(", ").append(obj);
        }
        return buf.toString();
    }

    /**
     * Imports a column from an XML specification
     */
    public void loadFromXML(Attributes attrib)
    {
        String dom = attrib.getValue("domain");
        if (StringUtils.isNotEmpty(dom))
        {
            domain = new Domain(getTable().getDatabase().getDomain(dom));
        }
        else
        {
            domain = new Domain(getPlatform().getDomainForSchemaType(DEFAULT_TYPE));
            setType(attrib.getValue("type"));
        }
        //Name
        name = attrib.getValue("name");

        javaName = attrib.getValue("javaName");
        javaType = attrib.getValue("javaType");
        if (javaType != null && javaType.length() == 0)
        {
            javaType = null;
        }

        // retrieves the method for converting from specified name to
        // a java name.
        javaNamingMethod = attrib.getValue("javaNamingMethod");
        if (javaNamingMethod == null)
        {
            javaNamingMethod
                    = parentTable.getDatabase().getDefaultJavaNamingMethod();
        }

        //Primary Key
        String primaryKey = attrib.getValue("primaryKey");
        //Avoid NullPointerExceptions on string comparisons.
        isPrimaryKey = ("true".equals(primaryKey));

        // If this column is a primary key then it can't be null.
        if ("true".equals(primaryKey))
        {
            isNotNull = true;
        }

        // HELP: Should primary key, index, and/or idMethod="native"
        // affect isNotNull?  If not, please document why here.
        String notNull = attrib.getValue("required");
        isNotNull = (notNull != null && "true".equals(notNull));

        //AutoIncrement/Sequences
        String autoIncrement = attrib.getValue("autoIncrement");
        // autoincrement is false per default,
        // except if the column is a primary key
        // and the idMethod is native
        // and the platform's default id Method is identity
        // and autoIncrement is not excplicitly set to false
        isAutoIncrement = ("true".equals(autoIncrement)
                || (isPrimaryKey()
                    && IDMethod.NATIVE.equals(getTable().getIdMethod())
                    && Platform.IDENTITY.equals(
                            getPlatform().getNativeIdMethod())
                    && (!"false".equals(autoIncrement))));
        //Default column value.
        domain.replaceDefaultValue(attrib.getValue("default"));

        domain.replaceSize(attrib.getValue("size"));
        domain.replaceScale(attrib.getValue("scale"));

        inheritanceType = attrib.getValue("inheritance");
        isInheritance = (inheritanceType != null
                && !inheritanceType.equals("false"));

        this.inputValidator = attrib.getValue("inputValidator");
        description = attrib.getValue("description");
    }

    /**
     * Returns table.column
     */
    public String getFullyQualifiedName()
    {
        return (parentTable.getName() + '.' + name);
    }

    /**
     * Get the name of the column
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the name of the column
     */
    public void setName(String newName)
    {
        name = newName;
    }

    /**
     * Get the description for the Table
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Set the description for the Table
     *
     * @param newDescription description for the Table
     */
    public void setDescription(String newDescription)
    {
        description = newDescription;
    }

    /**
     * Get name to use in Java sources to build method names.
     *
     * @return the capitalised javaName
     */
    public String getJavaName()
    {
        if (javaName == null)
        {
            List inputs = new ArrayList(2);
            inputs.add(name);
            inputs.add(javaNamingMethod);
            try
            {
                javaName = NameFactory.generateName(NameFactory.JAVA_GENERATOR,
                                                    inputs);
            }
            catch (EngineException e)
            {
                log.error(e, e);
            }
        }
        return StringUtils.capitalize(javaName);
    }

    /**
     * Returns the name for the getter method to retrieve the value of this
     * column
     *
     * @return A getter method name for this column.
     * @since 3.2
     */
    public String getGetterName()
    {
        if (("boolean".equalsIgnoreCase(getJavaNative()) && isCorrectGetters()))
        {
            return "is" + StringUtils.capitalize(getJavaName());
        }
        else
        {
            return "get" + StringUtils.capitalize(getJavaName());
        }
    }

    /**
     * Returns the name for the setter method to set the value of this
     * column
     *
     * @return A setter method name for this column.
     * @since 3.2
     */
    public String getSetterName()
    {
        return "set" + StringUtils.capitalize(getJavaName());
    }

    /**
     * Get variable name to use in Java sources (= uncapitalised java name)
     */
    public String getUncapitalisedJavaName()
    {
        return StringUtils.uncapitalize(getJavaName());
    }

    /**
     * Set name to use in Java sources
     */
    public void setJavaName(String javaName)
    {
        this.javaName = javaName;
    }

    /**
     * Get type to use in Java sources
     */
    public String getJavaType()
    {
        return javaType;
    }

    /**
     * Get the location of this column within the table (one-based).
     * @return value of position.
     */
    public int getPosition()
    {
        return position;
    }

    /**
     * Get the location of this column within the table (one-based).
     * @param v  Value to assign to position.
     */
    public void setPosition(int  v)
    {
        this.position = v;
    }

    /**
     * Set the parent Table of the column
     */
    public void setTable(Table parent)
    {
        parentTable = parent;
    }

    /**
     * Get the parent Table of the column
     */
    public Table getTable()
    {
        return parentTable;
    }

    /**
     * Returns the Name of the table the column is in
     */
    public String getTableName()
    {
        return parentTable.getName();
    }

    /**
     * A utility function to create a new column
     * from attrib and add it to this table.
     */
    public Inheritance addInheritance(Attributes attrib)
    {
        Inheritance inh = new Inheritance();
        inh.loadFromXML (attrib);
        addInheritance(inh);

        return inh;
    }

    /**
     * Adds a new inheritance definition to the inheritance list and set the
     * parent column of the inheritance to the current column
     */
    public void addInheritance(Inheritance inh)
    {
        inh.setColumn(this);
        if (inheritanceList == null)
        {
            inheritanceList = new ArrayList();
            isEnumeratedClasses = true;
        }
        inheritanceList.add(inh);
    }

    /**
     * Get the inheritance definitions.
     */
    public List getChildren()
    {
        return inheritanceList;
    }

    /**
     * Determine if this column is a normal property or specifies a
     * the classes that are represented in the table containing this column.
     */
    public boolean isInheritance()
    {
        return isInheritance;
    }

    /**
     * Determine if possible classes have been enumerated in the xml file.
     */
    public boolean isEnumeratedClasses()
    {
        return isEnumeratedClasses;
    }

    /**
     * Return the isNotNull property of the column
     */
    public boolean isNotNull()
    {
        return isNotNull;
    }

    /**
     * Set the isNotNull property of the column
     */
    public void setNotNull(boolean status)
    {
        isNotNull = status;
    }

    /**
     * Return NOT NULL String for this column
     *
     * @return "NOT NULL" if null values are not allowed or an empty String.
     */
    public String getNotNullString()
    {
        return getTable().getDatabase().getPlatform()
                .getNullString(this.isNotNull());
    }

    /**
     * Set if the column is a primary key or not
     */
    public void setPrimaryKey(boolean pk)
    {
        isPrimaryKey = pk;
    }

    /**
     * Return true if the column is a primary key
     */
    public boolean isPrimaryKey()
    {
        return isPrimaryKey;
    }

    /**
     * Set true if the column is UNIQUE
     */
    public void setUnique (boolean u)
    {
        isUnique = u;
    }

    /**
     * Get the UNIQUE property
     */
    public boolean isUnique()
    {
        return isUnique;
    }

    /**
     * Return true if the column requires a transaction in Postgres
     */
    public boolean requiresTransactionInPostgres()
    {
        return needsTransactionInPostgres;
    }

    /**
     * Utility method to determine if this column is a foreign key.
     */
    public boolean isForeignKey()
    {
        return (getForeignKey() != null);
    }

    /**
     * Determine if this column is a foreign key that refers to the
     * same table as another foreign key column in this table.
     */
    public boolean isMultipleFK()
    {
        ForeignKey fk = getForeignKey();
        if (fk != null)
        {
            Iterator fks = parentTable.getForeignKeys().iterator();
            while (fks.hasNext())
            {
                ForeignKey key = (ForeignKey) fks.next();
                if (key.getForeignTableName().equals(fk.getForeignTableName())
                        && !key.getLocalColumns().contains(this.name))
                {
                    return true;
                }
            }
        }

        // No multiple foreign keys.
        return false;
    }

    /**
     * get the foreign key object for this column
     * if it is a foreign key or part of a foreign key
     */
    public ForeignKey getForeignKey()
    {
        return parentTable.getForeignKey(this.name);
    }

    /**
     * Utility method to get the related table of this column if it is a foreign
     * key or part of a foreign key
     */
    public String getRelatedTableName()
    {
        ForeignKey fk = getForeignKey();
        return (fk == null ? null : fk.getForeignTableName());
    }

    /**
     * Utility method to get the related column of this local column if this
     * column is a foreign key or part of a foreign key.
     */
    public String getRelatedColumnName()
    {
        ForeignKey fk = getForeignKey();
        if (fk == null)
        {
            return null;
        }
        else
        {
            return fk.getLocalForeignMapping().get(this.name).toString();
        }
    }

    /**
     * Adds the foreign key from another table that refers to this column.
     */
    public void addReferrer(ForeignKey fk)
    {
        if (referrers == null)
        {
            referrers = new ArrayList(5);
        }
        referrers.add(fk);
    }

    /**
     * Get list of references to this column.
     */
    public List getReferrers()
    {
        if (referrers == null)
        {
            referrers = new ArrayList(5);
        }
        return referrers;
    }

    /**
     * Sets the colunm type
     */
    public void setType(String torqueType)
    {
        SchemaType type = SchemaType.getEnum(torqueType);
        if (type == null)
        {
            log.warn("SchemaType " + torqueType + " does not exist");
            type = Column.DEFAULT_TYPE;
        }

        setType(type);
    }

    /**
     * Sets the colunm type
     */
    public void setType(SchemaType torqueType)
    {
        domain = new Domain(getPlatform().getDomainForSchemaType(torqueType));
        if (torqueType.equals(SchemaType.VARBINARY)
                || torqueType.equals(SchemaType.BLOB))
        {
            needsTransactionInPostgres = true;
        }
    }

    /**
     * Returns the column jdbc type as an object
     *
     * @deprecated the type conversion is handled by the platform package
     *             (since torque 3.2)
     */
    public Object getType()
    {
        return TypeMap.getJdbcType(domain.getType()).getName();
    }

    /**
     * Returns the column type as given in the schema as an object
     */
    public Object getTorqueType()
    {
        return domain.getType().getName();
    }

    /**
     * Utility method to see if the column is a string
     *
     * @deprecated will be removed after the 3.2 release
     */
    public boolean isString()
    {
        return (domain.getType().getName().indexOf ("CHAR") != -1);
    }

    /**
     * Utility method to return the value as an element to be usable
     * in an SQL insert statement. This is used from the SQL loader task
     */
    public boolean needEscapedValue()
    {
        String torqueType = domain.getType().getName();
        return (torqueType != null) && (torqueType.equals("VARCHAR")
                        || torqueType.equals("LONGVARCHAR")
                        || torqueType.equals("DATE")
                        || torqueType.equals("DATETIME")
                        || torqueType.equals("TIMESTAMP")
                        || torqueType.equals("TIME")
                        || torqueType.equals("CHAR")
                        || torqueType.equals("CLOB"));
    }

    /**
     * String representation of the column. This is an xml representation.
     *
     * @return string representation in xml
     */
    public String toString()
    {
        StringBuffer result = new StringBuffer();
        result.append("    <column name=\"").append(name).append('"');

        if (javaName != null)
        {
            result.append(" javaName=\"").append(javaName).append('"');
        }

        if (isPrimaryKey)
        {
            result.append(" primaryKey=\"").append(isPrimaryKey).append('"');
        }

        if (isNotNull)
        {
            result.append(" required=\"true\"");
        }
        else
        {
            result.append(" required=\"false\"");
        }

        result.append(" type=\"").append(domain.getType().getName()).append('"');

        if (domain.getSize() != null)
        {
            result.append(" size=\"").append(domain.getSize()).append('"');
        }

        if (domain.getScale() != null)
        {
            result.append(" scale=\"").append(domain.getScale()).append('"');
        }

        if (domain.getDefaultValue() != null)
        {
            result.append(" default=\"").append(domain.getDefaultValue()).append('"');
        }

        if (isInheritance())
        {
            result.append(" inheritance=\"").append(inheritanceType)
                .append('"');
        }

        // Close the column.
        result.append(" />\n");

        return result.toString();
    }

    /**
     * Returns the size of the column
     */
    public String getSize()
    {
        return domain.getSize();
    }

    /**
     * Set the size of the column
     */
    public void setSize(String newSize)
    {
        domain.setSize(newSize);
    }

    /**
     * Returns the scale of the column
     */
    public String getScale()
    {
        return domain.getScale();
    }

    /**
     * Set the scale of the column
     */
    public void setScale(String newScale)
    {
        domain.setScale(newScale);
    }

    /**
     * Return the size and scale in brackets for use in an sql schema.
     *
     * @return size and scale or an empty String if there are no values
     *         available.
     */
    public String printSize()
    {
        return domain.printSize();
    }

    /**
     * Return a string that will give this column a default value.
     * @deprecated
     */
     public String getDefaultSetting()
     {
         return domain.getDefaultSetting();
     }

    /**
     * Set a string that will give this column a default value.
     */
    public void setDefaultValue(String def)
    {
        domain.setDefaultValue(def);
    }

    /**
     * Get a string that will give this column a default value.
     */
    public String getDefaultValue()
    {
        return domain.getDefaultValue();
    }

    /**
     * Returns the class name to do input validation
     */
    public String getInputValidator()
    {
       return this.inputValidator;
    }

    /**
     * Return auto increment/sequence string for the target database. We need to
     * pass in the props for the target database!
     */
    public boolean isAutoIncrement()
    {
        return isAutoIncrement;
    }

    /**
     * Set the auto increment value.
     * Use isAutoIncrement() to find out if it is set or not.
     */
    public void setAutoIncrement(boolean value)
    {
        isAutoIncrement = value;
    }

    public String getAutoIncrementString()
    {
        if (isAutoIncrement()
                && IDMethod.NATIVE.equals(getTable().getIdMethod()))
        {
            return getPlatform().getAutoIncrement();
        }
        return "";
    }

    /**
     * Set the column type from a string property
     * (normally a string from an sql input file)
     */
    public void setTypeFromString(String typeName, String size)
    {
        String tn = typeName.toUpperCase();
        setType(tn);

        if (size != null)
        {
            domain.setSize(size);
        }

        if (tn.indexOf("CHAR") != -1)
        {
            domain.setType(SchemaType.VARCHAR);
        }
        else if (tn.indexOf("INT") != -1)
        {
            domain.setType(SchemaType.INTEGER);
        }
        else if (tn.indexOf("FLOAT") != -1)
        {
            domain.setType(SchemaType.FLOAT);
        }
        else if (tn.indexOf("DATE") != -1)
        {
            domain.setType(SchemaType.DATE);
        }
        else if (tn.indexOf("TIME") != -1)
        {
            domain.setType(SchemaType.TIMESTAMP);
        }
        else if (tn.indexOf("BINARY") != -1)
        {
            domain.setType(SchemaType.LONGVARBINARY);
        }
        else
        {
            domain.setType(SchemaType.VARCHAR);
        }
    }

    /**
     * Return a string representation of the
     * Java object which corresponds to the JDBC
     * type of this column. Use in the generation
     * of MapBuilders.
     */
    public String getJavaObject()
    {
        return TypeMap.getJavaObject(domain.getType());
    }

    /**
     * Return a string representation of the primitive java type which
     * corresponds to the JDBC type of this column.
     *
     * @return string representation of the primitive java type
     */
    public String getJavaPrimitive()
    {
        return TypeMap.getJavaNative(domain.getType());
    }

    /**
     * Return a string representation of the native java type which corresponds
     * to the JDBC type of this column. Use in the generation of Base objects.
     * This method is used by torque, so it returns Key types for primaryKey and
     * foreignKey columns
     *
     * @return java datatype used by torque
     */
    public String getJavaNative()
    {
        String jtype = TypeMap.getJavaNativeObject(domain.getType());
        if (isUsePrimitive())
        {
            jtype = TypeMap.getJavaNative(domain.getType());
        }

        return jtype;
    }

    /**
     * Return Village asX() method which corresponds to the JDBC type
     * which represents this column.
     */
    public String getVillageMethod()
    {
        String vmethod = TypeMap.getVillageObjectMethod(domain.getType());
        if (isUsePrimitive())
        {
            vmethod = TypeMap.getVillageMethod(domain.getType());
        }

        return vmethod;
    }

    /**
     * Return ParameterParser getX() method which
     * corresponds to the JDBC type which represents this column.
     */
    public String getParameterParserMethod()
    {
        return TypeMap.getPPMethod(domain.getType());
    }

    /**
     * Returns true if the column type is boolean in the
     * java object and a numeric (1 or 0) in the db.
     */
    public boolean isBooleanInt()
    {
        return TypeMap.isBooleanInt(domain.getType());
    }

    /**
     * Returns true if the column type is boolean in the
     * java object and a String ("Y" or "N") in the db.
     */
    public boolean isBooleanChar()
    {
        return TypeMap.isBooleanChar(domain.getType());
    }

    /**
     * Returns true if the column type is boolean in the
     * java object and a Bit ("1" or "0") in the db.
     */
    public boolean isBit()
    {
        return TypeMap.isBit(domain.getType());
    }

    /**
     * returns true, if the columns java native type is an
     * boolean, byte, short, int, long, float, double, char
     */
    public boolean isPrimitive()
    {
        String t = getJavaNative();
        return "boolean".equals(t)
            || "byte".equals(t)
            || "short".equals(t)
            || "int".equals(t)
            || "long".equals(t)
            || "float".equals(t)
            || "double".equals(t)
            || "char".equals(t);
    }

    public boolean isUsePrimitive()
    {
        String s = getJavaType();
        return (s != null && s.equals("primitive"))
            || (s == null && !"object".equals(
               getTable().getDatabase().getDefaultJavaType()));
    }

    /**
     * @return Returns the domain.
     */
    public Domain getDomain()
    {
        return domain;
    }

    /**
     * @param domain The domain to set.
     */
    public void setDomain(Domain domain)
    {
        this.domain = domain;
    }

    private Platform getPlatform()
    {
        try
        {
            Table table = getTable();
            Database dataBase = table.getDatabase();
            Platform platform = dataBase.getPlatform();
            return platform;
        }
        catch (Exception ex)
        {
            log.warn("could not load platform implementation");
        }
        return new PlatformDefaultImpl();
    }

    public String getSqlString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(getName());
        sb.append(' ');
        sb.append(getDomain().getSqlType());
        if (getPlatform().hasSize(getDomain().getSqlType()))
        {
            sb.append(getDomain().printSize());
            sb.append(' ');
        }
        if (getDomain().getDefaultValue() != null)
        {
            sb.append("default ");
            if (TypeMap.isTextType(getDomain().getType()))
            {
                // TODO: Properly SQL-escape the text.
                sb.append('\'').append(getDefaultValue()).append('\'');
            }
            else
            {
                sb.append(getDefaultValue());
            }
            sb.append(' ');
        }
        
        sb.append(getNotNullString());
        sb.append(' ');
        sb.append(getAutoIncrementString());
        return sb.toString();
    }

    /**
     * Return the correctGetters property of the column
     *
     * @return The currentValue of the correctGetters property.
     * @since 3.2
     */
    public boolean isCorrectGetters()
    {
        return correctGetters;
    }

    /**
     * Set the correctGetters property of the column. If set to true, the
     * column returns is&lt;xxx&gt; as the getter name which is correct for the
     * Bean Specs but incompatible to pre-3.2 releases.
     *
     * @param correctGetters The new value of the correctGetters property.
     * @since 3.2
     */
    public void setCorrectGetters(boolean correctGetters)
    {
        this.correctGetters = correctGetters;
    }

    /**
     * add by biaoping.yin on 2005.9.8
     * @param javaType String
     */
    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }
}
