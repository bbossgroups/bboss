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

import junit.framework.TestCase;

/**
 * @author martin
 */
public class ColumnTest extends TestCase {

    public void testRequiresTransactionInPostgres() {
        Column col = new Column();
        col.setType("VARBINARY");
        assertTrue(col.requiresTransactionInPostgres());
        col = new Column();
        col.setType("INTEGER");
        assertFalse(col.requiresTransactionInPostgres());
        col = new Column();
        col.setType("BLOB");
        assertTrue(col.requiresTransactionInPostgres());
    }

}
