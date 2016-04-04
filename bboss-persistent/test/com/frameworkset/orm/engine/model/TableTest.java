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

import junit.framework.TestCase;

import com.frameworkset.orm.engine.transform.XmlToAppData;

/**
 * Tests for package handling.
 *
 * @author <a href="mailto:mpoeschl@marmot.at>Martin Poeschl</a>
 * @version $Id: TableTest.java,v 1.9 2004/02/22 06:29:38 jmcnally Exp $
 */
public class TableTest extends TestCase implements Serializable
{
    private XmlToAppData xmlToAppData = null;
    private Database db = null;

    public TableTest(String name)
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        xmlToAppData = new XmlToAppData("mysql", "defaultpackage");
        db = xmlToAppData.parseFile(
            "src/test/org/apache/torque/engine/database/model/tabletest-schema.xml");
    }

    protected void tearDown() throws Exception
    {
        xmlToAppData = null;
        super.tearDown();
    }

    /**
     * test if the tables get the package name from the properties file
     */
    public void testIdMethodHandling() throws Exception
    {
        assertEquals(IDMethod.ID_BROKER, db.getDefaultIdMethod());
        Table table = db.getTable("table_idbroker");
        assertEquals(IDMethod.ID_BROKER, table.getIdMethod());
		Table table2 = db.getTable("table_native");
		assertEquals(IDMethod.NATIVE, table2.getIdMethod());
    }

    public void testNoPk() throws Exception
    {
        Table table = db.getTable("nopk");
        assertFalse(table.hasPrimaryKey());
        List pks = table.getPrimaryKey();
        assertTrue(pks.size() == 0);
    }

    public void testSinglePk() throws Exception
    {
        Table table = db.getTable("singlepk");
        assertTrue(table.hasPrimaryKey());
        List pks = table.getPrimaryKey();
        assertTrue(pks.size() == 1);
        Column col = (Column) pks.get(0);
        assertEquals(col.getName(), "singlepk_id");
    }

    public void testMultiPk() throws Exception
    {
        Table table = db.getTable("multipk");
        assertTrue(table.hasPrimaryKey());
        List pks = table.getPrimaryKey();
        assertTrue(pks.size() == 2);
        Column cola = (Column) pks.get(0);
        assertEquals(cola.getName(), "multipk_a");
        Column colb = (Column) pks.get(1);
        assertEquals(colb.getName(), "multipk_b");
        assertEquals(table.printPrimaryKey(), "multipk_a,multipk_b");
    }

    public void testSingleFk() throws Exception
    {
        Table table = db.getTable("singlefk");
        List fks = table.getForeignKeys();
        assertTrue(fks.size() == 1);
        ForeignKey fk = (ForeignKey) fks.get(0);
        assertEquals(fk.getForeignTableName(), "singlepk");
        assertTrue(fk.getForeignColumns().size() == 1);
        assertFalse(fk.hasOnDelete());
        assertFalse(fk.hasOnUpdate());
    }

    public void testOnUpdateOnDelete() throws Exception
    {
        Table table = db.getTable("singlefk1");
        List fks = table.getForeignKeys();
        assertTrue(fks.size() == 1);
        ForeignKey fk = (ForeignKey) fks.get(0);
        assertTrue(fk.hasOnUpdate());
        assertEquals("CASCADE", fk.getOnUpdate());
        assertTrue(fk.hasOnDelete());
        assertEquals("SET NULL", fk.getOnDelete());
    }

    public void testMultiFk() throws Exception
    {
        Table table = db.getTable("multifk");
        List fks = table.getForeignKeys();
        assertTrue(fks.size() == 1);
        ForeignKey fk = (ForeignKey) fks.get(0);
        assertEquals(fk.getForeignTableName(), "multipk");
        assertTrue(fk.getForeignColumns().size() == 2);
    }

    public void testReferrers() throws Exception
    {
        Table table = db.getTable("singlepk");
        List refs = table.getReferrers();
        assertTrue(refs.size() == 1);
        ForeignKey fk = (ForeignKey) refs.get(0);
        assertEquals(fk.getTableName(), "singlefk");
    }

    public void testUnique() throws Exception
    {
        Table table = db.getTable("unique_test");
        List unices = table.getUnices();
        assertTrue(unices.size() == 1);
        Unique unique = (Unique) unices.get(0);
        assertEquals(unique.getName(), "unique_name");
        assertTrue(unique.getColumns().size() == 2);
    }

}
