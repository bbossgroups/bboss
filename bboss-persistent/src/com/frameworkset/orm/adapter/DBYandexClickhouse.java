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

/*
 
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.orm.platform.PlatformMysqlImpl;
import com.frameworkset.util.SimpleStringUtil;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is used in order to connect to a MySQL database using the MM
 * drivers.  Simply comment the above and uncomment this code below and
 * fill in the appropriate values for DB_NAME, DB_HOST, DB_USER,
 * DB_PASS.
 *
 * <P><A HREF="http://www.worldserver.com/mm.mysql/">
 * http://www.worldserver.com/mm.mysql/</A>
 * <p>"jdbc:mysql://" + DB_HOST + "/" + DB_NAME + "?user=" +
 * DB_USER + "&password=" + DB_PASS;
 *
 * @author <a href="mailto:jon@clearink.com">Jon S. Stevens</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @version $Id: DBMM.java,v 1.13 2005/01/31 19:43:55 tfischer Exp $
 */
public class DBYandexClickhouse extends DBMM
{

//    /** A specialized date format for MySQL. */
//    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	 
    /**
     * Empty protected constructor.
     */
    protected DBYandexClickhouse()
    {
    	super();
    }
        
}
