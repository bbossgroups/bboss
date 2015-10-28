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
package com.frameworkset.orm.adapter;

import com.frameworkset.orm.platform.PlatformDb2400Impl;



/**
 * Torque Database Adapter for DB2/400 on the IBM AS400 platform.
 *
 * @author <a href="mailto:sweaver@rippe.com">Scott Weaver</a>
 * @author <a href="mailto:vido@ldh.org">Augustin Vidovic</a>
 * @version $Id: DBDB2400.java,v 1.7 2005/01/31 19:43:55 tfischer Exp $
 */
public class DBDB2400 extends DBDB2App
{
   /**
    * UpperCase/IgnoreCase sql function in DB2/400
    */
   public static final String UCASE = "UCASE";


    /**
     * DBDB2400 constructor.
     */
    protected DBDB2400()
    {
        super();
        this.platform = new PlatformDb2400Impl();
    }

    /**
     * This method is used to ignore case.
     *
     * @param in The string whose case to ignore.
     * @return The string in a case that can be ignored.
     */
    public String ignoreCase(String in)
    {
        String s = formatCase(in);
        return s;
    }

    /**
     * This method is used to ignore case.
     *
     * @param in The string to transform to upper case.
     * @return The upper case string.
     */
    public String toUpperCase(String in)
    {
        String s = formatCase(in);
        return s;
    }

    /**
     * Convenience method for String-formatting
     * upper/ignore case statements.
     *
     * @param in The string to transform to upper case.
     * @return The upper case string.
     */
    private String formatCase(String in)
    {
        return new StringBuffer(UCASE + "(").append(in).append(")").toString();
    }

    /**
     * This method is used to check whether the database supports
     * limiting the size of the resultset.
     *
     * @return LIMIT_STYLE_DB2.
     */
    public int getLimitStyle()
    {
        return DB.LIMIT_STYLE_DB2;
    }
}
