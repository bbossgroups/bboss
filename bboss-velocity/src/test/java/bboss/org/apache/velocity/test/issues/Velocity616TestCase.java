package bboss.org.apache.velocity.test.issues;

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

import bboss.org.apache.velocity.test.BaseTestCase;

/**
 * This class tests VELOCITY-616.
 */
public class Velocity616TestCase extends BaseTestCase
{
    public Velocity616TestCase(String name)
    {
       super(name);
    }

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        context.put("bar", "bar");
        context.put("foo", Boolean.FALSE);
    }

    public void testIfNoBrackets()
    {
        String template = "\\#if ($foo) \\$bar \\#end";
        String expected = "#if (false) $bar #end";
        assertEvalEquals(expected, template);
    }

    public void testForeachBrackets()
    {
        String template = "\\#{foreach}( $i in [1..3] )$i\\#{end}";
        String expected = "#{foreach}( $i in [1..3] )$i#{end}";
        assertEvalEquals(expected, template);
    }

    public void testIfBrackets()
    {
        String template = "\\#{if} ($foo) \\$bar \\#{end}";
        String expected = "#{if} (false) $bar #{end}";
        assertEvalEquals(expected, template);
    }

    public void testIfBracketsOnEndOnly()
    {
        String template = "\\#if( $foo ) \\$bar \\#{end}";
        String expected = "#if( false ) $bar #{end}";
        assertEvalEquals(expected, template);
    }

}
