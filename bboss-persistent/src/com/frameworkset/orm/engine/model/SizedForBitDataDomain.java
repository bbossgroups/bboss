package com.frameworkset.orm.engine.model;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 * <p>
 * Supports the Derby / DB2 / SQL92 standard for defining Binary data fields 
 * with either CHAR(#) FOR BIT DATA or VARCHAR(#) FOR BIT DATA. This can be 
 * used in Platform implimentors initialize() methods, by using lines like:
 * </p>
 * <code>
 * setSchemaDomainMapping(new SizedForBitDataDomain(
 *                          SchemaType.BINARY, "CHAR", "1"));
 * setSchemaDomainMapping(new SizedForBitDataDomain(
 *                          SchemaType.VARBINARY, "VARCHAR"));
 * </code>
 * <p>
 * This will cause the Column.getSqlString() method to produce items similar to:
 * </p>
 * <code>
 * CHAR(#) FOR BIT DATA
 * VARCHAR(#)FOR BIT DATA
 * </code>
 * <p>
 * Where: # is the size= schema attribute or a default size specified in the
 * constructor.
 * </p>
 * <p>
 * Note that this is dependent on the platform implimentation correctly defining
 * BINARY and VARBINARY as having a size attribute in the "hasSize()" method.
 * </p>
 *

 * @author <a href="Monroe@DukeCE.com">Greg Monroe</a>
 */
public class SizedForBitDataDomain extends Domain
{

    /**
     * @see org.apache.torque.engine.database.model.Domain#Domain()
     */
    public SizedForBitDataDomain()
    {
        super();
    }

    /**
     * @see org.apache.torque.engine.database.model.Domain#Domain(String)
     */
    public SizedForBitDataDomain(String name)
    {
        super(name);
    }

    /**
     * @see org.apache.torque.engine.database.model.Domain#Domain(SchemaType)
     */
    public SizedForBitDataDomain(SchemaType type)
    {
        super(type);
    }

    /**
     * @see org.apache.torque.engine.database.model.Domain#
     *              Domain(SchemaType, String)
     */
    public SizedForBitDataDomain(SchemaType type, String sqlType)
    {
        super(type, sqlType);
    }

    /**
     * @see org.apache.torque.engine.database.model.Domain#
     *              Domain(SchemaType, String, String, String)
     */
    public SizedForBitDataDomain(SchemaType type, String sqlType, String size,
            String scale)
    {
        super(type, sqlType, size, scale);
    }

    /**
     * @see org.apache.torque.engine.database.model.Domain#
     *              Domain(SchemaType, String, String)
     */
    public SizedForBitDataDomain(SchemaType type, String sqlType, String size)
    {
        super(type, sqlType, size);
    }

    /**
     * @see org.apache.torque.engine.database.model.Domain#
     *              Domain(Domain)
     */
    public SizedForBitDataDomain(Domain domain)
    {
        super(domain);
    }

    /**
     * Returns the size postfix for the base SQL Column type.
     *
     * @return "(size) FOR BIT DATA" or just " FOR BIT DATA" if size
     * is null.
     * @see org.apache.torque.engine.database.model.Domain#getSize()
     */
    public String printSize()
    {
        String result = "";
        if ( getSize() != null )
        {
            result =  "(" + getSize() + ")";
        }
        result = result + " FOR BIT DATA";
        return result;
    }
}
