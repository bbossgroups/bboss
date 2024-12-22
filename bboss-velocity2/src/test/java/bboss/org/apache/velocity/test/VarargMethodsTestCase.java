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

import bboss.org.apache.velocity.VelocityContext;

/**
 * Used to check that vararg method calls on references work properly
 */
public class VarargMethodsTestCase extends BaseTestCase
{
    public VarargMethodsTestCase(final String name)
    {
        super(name);
    }

    @Override
    protected void setUpContext(VelocityContext context)
    {
        context.put("nice", new NiceTool());
        context.put("nasty", new NastyTool());
        context.put("objects", new Object[] { this, VelocityContext.class });
        context.put("strings", new String[] { "one", "two" });
        context.put("doubles", new double[] { 1.5, 2.5 });
        context.put("float", 1f);
        context.put("ints", new int[] { 1, 2 });
    }

    public void testStrings()
    {
        assertEvalEquals("onetwo", "$nice.var($strings)");
        assertEvalEquals("onetwo", "$nice.var('one','two')");
        assertEvalEquals("one", "$nice.var('one')");
        assertEvalEquals("", "$nice.var()");
    }

    public void testDoubles()
    {
        assertEvalEquals("4.0", "$nice.add($doubles)");
        assertEvalEquals("3.0", "$nice.add(1,2)");
        assertEvalEquals("1.0", "$nice.add(1)");
        assertEvalEquals("0.0", "$nice.add()");
    }

    public void testFloatToDoubleVarArg()
    {
        assertEvalEquals("1.0", "$nice.add($float)");
    }

    public void testStringVsStrings()
    {
        assertEvalEquals("onlyone", "$nasty.var('one')");
        assertEvalEquals("onlynull", "$nasty.var($null)");
        assertEvalEquals("", "$nasty.var()");
    }

    public void testIntVsDoubles()
    {
        assertEvalEquals("1", "$nasty.add(1)");
        assertEvalEquals("1.0", "$nasty.add(1.0)");
        assertEvalEquals("3.0", "$nasty.add(1.0,2)");
    }

    public void testInts()
    {
        assertEvalEquals("3", "$nasty.add($ints)");
        assertEvalEquals("3", "$nasty.add(1,2)");
        assertEvalEquals("1", "$nasty.add(1)");
        // add(int[]) wins because it is "more specific"
        assertEvalEquals("0", "$nasty.add()");
    }

    public void testStringsVsObjectsAKASubclassVararg()
    {
        assertEvalEquals("objects", "$nice.test($objects)");
        assertEvalEquals("objects", "$nice.test($nice,$nasty,$ints)");
        assertEvalEquals("strings", "$nice.test('foo')");
    }

    public void testObjectVarArgVsObjectEtc()
    {
        assertEvalEquals("object,string", "$nasty.test($nice,'foo')");
    }

    public void testObjectVarArgVsObjectVelocity605()
    {
        assertEvalEquals("string", "$nasty.test('joe')");
        assertEvalEquals("object", "$nasty.test($nice)");
    }

    public void testNoArgs()
    {
        assertEvalEquals("noargs", "$nasty.test()");
    }

    public void testPassingArrayToVarArgVelocity642()
    {
        assertEvalEquals("[one, two]", "$nasty.test642($strings)");
        assertEvalEquals("[1, 2]", "#set( $list = [1..2] )$nasty.test642($list.toArray())");
    }

    public void testNullToPrimitiveVarArg()
    {
        assertEvalEquals("int[]", "$nasty.test649($null)");
    }

    public void testArgsBeforeVarargWithNoArgs()
    {
        assertEvalEquals("String,String,Object[]", "$nasty.test651('a','b')");
    }

    public void testVelocity651()
    {
        assertEvalEquals("String,List", "$nasty.test651('test',['TEST'])");
    }

    public void testMax()
    {
        assertEvalEquals("4", "$nasty.max(4, 3.5)");
    }

    public static class NiceTool
    {
        public String var(String[] ss)
        {
            StringBuilder out = new StringBuilder();
            for (String s : ss)
            {
                out.append(s);
            }
            return out.toString();
        }

        public double add(double[] dd)
        {
            double total = 0;
            for (double aDd : dd)
            {
                total += aDd;
            }
            return total;
        }

        public String test(Object[] oo)
        {
            return "objects";
        }

        public String test(String[] oo)
        {
            return "strings";
        }

    }

    public static class NastyTool extends NiceTool
    {
        public String var(String s)
        {
            return "only"+s;
        }

        public int add(int[] ii)
        {
            int total = 0;
            for (int anIi : ii)
            {
                total += anIi;
            }
            return total;
        }

        public int add(int i)
        {
            return i;
        }

        public String test()
        {
            return "noargs";
        }

        public Object test(Object arg)
        {
            return "object";
        }

        public Object test(String arg)
        {
            return "string";
        }

        @Override
        public String test(Object[] array)
        {
            return "object[]";
        }

        public String test(Object object, String property)
        {
            return "object,string";
        }

        public String test642(Object[] array)
        {
            //JDK5: return Arrays.deepToString(array);
            if (array == null)
            {
                return null;
            }
            StringBuilder o = new StringBuilder("[");
            for (int i=0; i < array.length; i++)
            {
                if (i > 0)
                {
                    o.append(", ");
                }
                o.append((array[i]));
            }
            o.append("]");
            return o.toString();
        }

        public String test649(int[] array)
        {
            return "int[]";
        }

        public String test651(String s, String s2, Object[] args)
        {
            return "String,String,Object[]";
        }

        public String test651(String s, java.util.List l)
        {
            return "String,List";
        }

        public Number max(Number n1, Number n2) { return max(new Number[] { n1, n2 }); }

        public Number max(Number[] numbers)
        {
            if (numbers.length == 0) return null;
            int minindex = -1, i = 0;
            double val = Double.MIN_VALUE;
            for (Number n : numbers)
            {
                if (n.floatValue() > val)
                {
                    minindex = i;
                    val = n.floatValue();
                }
                ++i;
            }
            if (minindex < 0) minindex = 0;
            return numbers[minindex];
        }

    }

}
