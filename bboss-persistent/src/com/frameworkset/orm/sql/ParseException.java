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
package com.frameworkset.orm.sql;



/**
 * An Exception for parsing SQLToAppData.  This class
 * will probably get some extra features in future.
 *
 * @author <a href="mailto:leon@opticode.co.za">Leon Messerschmidt</a>
 * @version $Id: ParseException.java,v 1.3 2004/02/22 06:27:20 jmcnally Exp $
 */
public class ParseException extends Exception
{
    /**
     * constructor.
     *
     * @param err error message
     */
    public ParseException(String err)
    {
        super(err);
    }
}
