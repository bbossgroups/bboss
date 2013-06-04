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

import junit.framework.TestCase;

import com.frameworkset.orm.engine.transform.XmlToAppData;

/**
 * Tests for domain handling (for MySql).
 *
 * @version $Id: DomainTest.java,v 1.11 2004/03/12 12:25:32 seade Exp $
 */
public class DomainTest extends TestCase
{
    private XmlToAppData xmlToAppData = null;
    private Database db = null;

    public DomainTest(String name)
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        xmlToAppData = new XmlToAppData("mysql", "defaultpackage");
        db = xmlToAppData.parseFile(
            "src/com/frameworkset/orm/engine/model/domaintest-schema.xml");
    }

    protected void tearDown() throws Exception
    {
        xmlToAppData = null;
        super.tearDown();
    }

    /**
     * test if the tables get the package name from the properties file
     */
    public void testAllAttributes() throws Exception
    {
        Domain amount = db.getDomain("amount");
        System.out.println("amount:"+amount);
        assertEquals(SchemaType.NUMERIC, amount.getType());
        assertEquals("DECIMAL", amount.getSqlType());
        assertEquals("10", amount.getSize());
        assertEquals("2", amount.getScale());
        assertEquals("0", amount.getDefaultValue());
        assertEquals("amount domain", amount.getDescription());
    }

    /**
     * test if the tables get the package name from the properties file
     */
    public void testDomainColumn() throws Exception
    {
        Table table = db.getTable("product");
        Column name = table.getColumn("name");
        System.out.println("name:"+name);
        assertEquals("VARCHAR", name.getType());
        assertEquals("VARCHAR", name.getDomain().getSqlType());
        assertEquals("40", name.getSize());
        assertEquals("name VARCHAR(40)  ", name.getSqlString());
        Column price = table.getColumn("price");
        assertEquals("NUMERIC", price.getTorqueType());
        assertEquals("NUMERIC", price.getType());
        assertEquals("DECIMAL", price.getDomain().getSqlType());
        assertEquals("10", price.getSize());
        assertEquals("2", price.getScale());
        assertEquals("0", price.getDefaultValue());
        assertEquals("(10,2)", price.printSize());
        assertEquals("price DECIMAL(10,2) default 0  ", price.getSqlString());
    }

    /**
     * test if the tables get the package name from the properties file
     */
    public void testExtendedDomainColumn() throws Exception
    {
        Table table = db.getTable("article");
        Column price = table.getColumn("price");
        assertEquals("NUMERIC", price.getTorqueType());
        assertEquals("NUMERIC", price.getType());
        assertEquals("DECIMAL", price.getDomain().getSqlType());
        assertEquals("12", price.getSize());
        assertEquals("2", price.getScale());
        assertEquals("1000", price.getDefaultValue());
        assertEquals("(12,2)", price.printSize());
        assertEquals("price DECIMAL(12,2) default 1000  ", price.getSqlString());
    }

    public void testDecimalColumn() throws Exception
    {
        Table table = db.getTable("article");
        Column col = table.getColumn("decimal_col");
        assertEquals("DECIMAL", col.getTorqueType());
        assertEquals("DECIMAL", col.getType());
        assertEquals("DECIMAL", col.getDomain().getSqlType());
        assertEquals("10", col.getSize());
        assertEquals("3", col.getScale());
        assertEquals("(10,3)", col.printSize());
        assertEquals("decimal_col DECIMAL(10,3)  ", col.getSqlString());
    }

    public void testDateColumn() throws Exception
    {
        Table table = db.getTable("article");
        Column col = table.getColumn("date_col");
        assertEquals("DATE", col.getTorqueType());
        assertEquals("DATE", col.getType());
        assertEquals("DATETIME", col.getDomain().getSqlType());
        assertEquals("", col.printSize());
        assertEquals("date_col DATETIME  ", col.getSqlString());
    }

    public void testNativeAutoincrement() throws Exception
    {
        Table table = db.getTable("native");
        Column col = table.getColumn("native_id");
        assertEquals("AUTO_INCREMENT", col.getAutoIncrementString());
        assertEquals("native_id MEDIUMINT NOT NULL AUTO_INCREMENT", col.getSqlString());
        col = table.getColumn("name");
        assertEquals("", col.getAutoIncrementString());
    }

    public void testIdBrokerAutoincrement() throws Exception
    {
        Table table = db.getTable("article");
        Column col = table.getColumn("article_id");
        assertEquals("", col.getAutoIncrementString());
        assertEquals("article_id MEDIUMINT NOT NULL ", col.getSqlString());
        col = table.getColumn("name");
        assertEquals("", col.getAutoIncrementString());
    }

    public void testBooleanint() throws Exception
    {
        Table table = db.getTable("types");
        Column col = table.getColumn("cbooleanint");
        assertEquals("", col.getAutoIncrementString());
        assertEquals("BOOLEANINT", col.getTorqueType());
        assertEquals("INTEGER", col.getType());
        assertEquals("INTEGER", col.getDomain().getSqlType());
        assertEquals("cbooleanint INTEGER  ", col.getSqlString());
    }

    public void testBlob() throws Exception
    {
        Table table = db.getTable("types");
        Column col = table.getColumn("cblob");
        assertEquals("", col.getAutoIncrementString());
        assertEquals("BLOB", col.getTorqueType());
        assertEquals("LONGBLOB", col.getDomain().getSqlType());
        assertEquals("cblob LONGBLOB ", col.getSqlString());
    }

}
