package bboss.org.apache.velocity.test;

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

import junit.framework.Test;
import junit.framework.TestSuite;
import bboss.org.apache.velocity.runtime.RuntimeInstance;

import org.apache.commons.lang3.StringUtils;

/**
 * Test case for any miscellaneous stuff.  If it isn't big, and doesn't fit
 * anywhere else, it goes here
 *
 * @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 * @version $Id$
 */
public class MiscTestCase extends BaseTestCase
{
    public MiscTestCase (String name)
    {
        super(name);
    }

    public static Test suite ()
    {
        return new TestSuite(MiscTestCase.class);
    }

    public void testRuntimeInstanceProperties()
    {
        // check that runtime instance properties can be set and retrieved
        RuntimeInstance ri = new RuntimeInstance();
        ri.setProperty("baabaa.test","the answer");
        assertEquals("the answer",ri.getProperty("baabaa.test"));
    }

    public void testStringUtils()
    {
        /*
         *  some StringUtils tests
         */

        String arg = null;
        String res = StringUtils.trim(arg);
        assertNull(arg);

        arg = " test ";
        res = StringUtils.trim(arg);
        assertEquals("test",res);

        arg = "test";
        res = StringUtils.trim(arg);
        assertEquals("test",res);
    }

}
