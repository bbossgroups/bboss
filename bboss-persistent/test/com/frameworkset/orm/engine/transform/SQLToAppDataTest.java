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
package com.frameworkset.orm.engine.transform;

/*
 * Copyright 2003,2004 The Apache Software Foundation.
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

import java.io.IOException;

import junit.framework.TestCase;

import com.frameworkset.orm.engine.model.Database;
import com.frameworkset.orm.engine.model.Table;
import com.frameworkset.orm.sql.ParseException;



/**
 * @author <a href="mailto:mpoeschl@marmot.at">Martin Poeschl</a>
 * @version $Id: SQLToAppDataTest.java,v 1.2 2004/02/22 06:29:38 jmcnally Exp $
 */
public class SQLToAppDataTest extends TestCase
{

    /**
     * Class to test for void SQLToAppData(String, String)
     */
    public void testMySql() throws Exception
    {
//        SQLToAppData sqlToAppData = new SQLToAppData(
//                "src/test/org/apache/torque/engine/database/transform/mysql.sql",
//                "mysql");
        SQLToAppData sqlToAppData = new SQLToAppData(
              "src/com/frameworkset/orm/engine/transform/mysql.sql");

        Database db = sqlToAppData.execute();
        assertTrue(db.getTables().size() > 0);
        Table course = db.getTable("course");
        System.out.println(course);
        assertTrue(course != null);
    }

    public static void main(String[] args) throws IOException, ParseException
    {
        SQLToAppData sqlToAppData = new SQLToAppData(
        "D:\\project\\torque\\src\\generator\\src\\test\\org\\apache\\torque\\engine\\database\\transform\\mysql.sql");

	  Database db = sqlToAppData.execute();
	  //assertTrue(db.getTables().size() > 0);
	  Table course = db.getTable("ab");
	  System.out.println(course);
	  //assertTrue(course != null);
    }

}
