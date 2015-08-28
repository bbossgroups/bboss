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



import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * This class creates different {@link com.frameworkset.orm.adapter.DB}
 * objects based on specified JDBC driver name.
 *
 * @author <a href="mailto:frank.kim@clearink.com">Frank Y. Kim</a>
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @author <a href="mailto:ralf@reswi.ruhr.de">Ralf Stranzenbach</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @author <a href="mailto:hps@intermeta.de">Henning P. Schmiedehausen</a>
 * @version $Id: DBFactory.java,v 1.37 2005/01/31 19:43:55 tfischer Exp $
 */
public class DBFactory 
{
	private static Logger log = Logger.getLogger(DBFactory.class);
    /**
     * JDBC driver to Torque Adapter map.
     */

    private static Map<String,Class> adapters = java.util.Collections.synchronizedMap(new HashMap<String,Class>(40));

    public static final String  DBDB2400 = "as400";
    public static final String  DBDB2App = "db2app";
    public static final String  DBDB2Net = "db2net";
    public static final String  DBCloudscape = "cloudscape";
    public static final String  DBHypersonicSQL = "hypersonic";
    public static final String  DBInterbase = "interbase";
    public static final String  DBInstantDB =  "instantdb";
    public static final String  DBMSSQL = "mssql";
    public static final String  DBMMysql = "mysql";
    public static final String  DBMariaDB = "mariadb";
    public static final String  DBOracle = "oracle";
    public static final String  DBPostgres = "postgresql";
    public static final String  DBSapDB = "sapdb";
    public static final String  DBSybase = "sybase";
    public static final String  DBWeblogic = "weblogic";
    public static final String  DBAxion = "axion";
    public static final String  DBInformix = "informix";
    public static final String  DBOdbc = "odbc";
    public static final String  DBAccess = "msaccess";

    public static final String  DBDerby =  "derby";

    public static final String  DBNone = "";
    public static final String  SQLITEX = "sqlitex";

    /**
     * Initialize the JDBC driver to Torque Adapter map.
     * 
     */
    static
    {
        adapters.put("com.ibm.as400.access.AS400JDBCDriver", DBDB2400.class);
        adapters.put("COM.ibm.db2.jdbc.app.DB2Driver", DBDB2App.class);
        adapters.put("COM.ibm.db2.jdbc.net.DB2Driver", DBDB2Net.class);
        adapters.put("COM.cloudscape.core.JDBCDriver", DBCloudscape.class);
        adapters.put("org.hsql.jdbcDriver", DBHypersonicSQL.class);
        adapters.put("org.hsqldb.jdbcDriver", DBHypersonicSQL.class);
        adapters.put("interbase.interclient.Driver", DBInterbase.class);
        adapters.put("org.enhydra.instantdb.jdbc.idbDriver", DBInstantDB.class);
        adapters.put("com.microsoft.jdbc.sqlserver.SQLServerDriver",
            DBMSSQL.class);
        adapters.put("com.microsoft.sqlserver.jdbc.SQLServerDriver",
                     DBMSSQL.class);
        adapters.put("net.sourceforge.jtds.jdbc.Driver",
                DBMSSQL.class);
        
        
        adapters.put("com.jnetdirect.jsql.JSQLDriver", DBMSSQL.class);
        adapters.put("org.gjt.mm.mysql.Driver", DBMM.class);
        adapters.put("com.mysql.jdbc.Driver", DBMM.class);
        adapters.put("org.mariadb.jdbc.Driver", DBMariaDB.class);
        adapters.put("oracle.jdbc.driver.OracleDriver", DBOracle.class);
        adapters.put("oracle.jdbc.OracleDriver", DBOracle.class);
        adapters.put("org.postgresql.Driver", DBPostgres.class);
        adapters.put("com.sap.dbtech.jdbc.DriverSapDB", DBSapDB.class);
        adapters.put("com.sybase.jdbc.SybDriver", DBSybase.class);
        adapters.put("com.sybase.jdbc2.jdbc.SybDriver", DBSybase.class);
        adapters.put("weblogic.jdbc.pool.Driver", DBWeblogic.class);
        adapters.put("org.axiondb.jdbc.AxionDriver", DBAxion.class);
        adapters.put("com.informix.jdbc.IfxDriver", DBInformix.class);
        adapters.put("sun.jdbc.odbc.JdbcOdbcDriver", DBOdbc.class);

        adapters.put("com.ibm.db2.jcc.DB2Driver", DBDerby.class);
        adapters.put("org.apache.derby.jdbc.EmbeddedDriver", DBDerby.class);
        adapters.put("org.apache.derby.jdbc.ClientDriver", DBDerby.class);
        


        // add some short names to be used when drivers are not used
        adapters.put("as400", DBDB2400.class);        
        adapters.put("db2app", DBDB2App.class);
        adapters.put("db2net", DBDB2Net.class);
        adapters.put("cloudscape", DBCloudscape.class);
        adapters.put("hypersonic", DBHypersonicSQL.class);
        adapters.put("interbase", DBInterbase.class);
        adapters.put("instantdb", DBInstantDB.class);
        adapters.put("mssql", DBMSSQL.class);
        adapters.put("mysql", DBMM.class);
        adapters.put("mariadb", DBMariaDB.class);
        adapters.put("oracle", DBOracle.class);
        adapters.put("postgresql", DBPostgres.class);
        adapters.put("sapdb", DBSapDB.class);
        adapters.put("sybase", DBSybase.class);
        adapters.put("weblogic", DBWeblogic.class);
        adapters.put("axion", DBAxion.class);
        adapters.put("informix", DBInformix.class);
        adapters.put("odbc", DBOdbc.class);
        adapters.put("msaccess", DBOdbc.class);

        adapters.put("derby", DBDerby.class);
        adapters.put(SQLITEX, DBSQLiteXerial.class);
        adapters.put("org.sqlite.JDBC", DBSQLiteXerial.class);
        adapters.put("org.h2.Driver", DBH2.class);
        adapters.put("h2", DBH2.class);
        adapters.put("", DBNone.class);
    }

    /**
     * Creates a new instance of the Torque database adapter associated
     * with the specified JDBC driver or adapter key.
     *
     * @param driver The fully-qualified name of the JDBC driver to
     * create a new adapter instance for or a shorter form adapter key.
     * @return An instance of a Torque database adapter.
     * @throws InstantiationException throws if the JDBC driver could not be
     *      instantiated
     */
    public static DB create(String driver)
        throws InstantiationException
    {
        if(driver == null || driver.trim().equals(""))
            driver = "";

        Class adapterClass =  adapters.get(driver);

        if (adapterClass != null)
        {
            try
            {
                DB adapter = (DB) adapterClass.newInstance();
                if(driver.indexOf(".") < 0)
                	adapter.setDbtype(driver);
                else
                {
                	adapter.setDbtype(lookupdbtype(adapterClass,driver));
                }
                // adapter.setJDBCDriver(driver);
                return adapter;
            }
            catch (IllegalAccessException e)
            {
                throw new InstantiationException(
                    "Could not instantiate adapter for JDBC driver: "
                    + driver
                    + ": Assure that adapter bytecodes are in your classpath");
            }
        }
        else
        {
            throw new InstantiationException(
                "Unknown JDBC driver: "
                + driver
                + ": Check your configuration file");
        }
    }
    private static String lookupdbtype(Class dbadaptor,String dbtype)
    {
    	Iterator<Map.Entry<String, Class>> it = adapters.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<String, Class> entry = it.next();
			String key = entry.getKey();
			Class value = entry.getValue();
			
			if(dbadaptor == value)
			{
				
				if(key.indexOf(".") < 0)
					return key;
				
					
				
			}
			
		}
		return dbtype;
    }
    private static void replaceOthers(Class newclz,String newkey,Class defaultclazz)
    {
    	Iterator<Map.Entry<String, Class>> it = adapters.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<String, Class> entry = it.next();
			String key = entry.getKey();
			Class value = entry.getValue();
			
			if(defaultclazz == value)
			{
				
				
				if(!newkey.equals(key))
				{
					log.debug("Use custom adapter["+newclz+"] to replace default adapter["+defaultclazz + "] for db[" + key+"]" );
					adapters.put(key, newclz);
				}
					
				
			}
			
		}
    }
    public static void addDBAdaptors(Map<String,String> adaptors)
    {
    	if(adaptors == null || adaptors.size() == 0)
    	{
    		return ;
    	}
    	else
    	{
    		Iterator<Map.Entry<String, String>> it = adaptors.entrySet().iterator();
    		while(it.hasNext())
    		{
    			Map.Entry<String, String> entry = it.next();
    			String key = entry.getKey();
    			String value = entry.getValue();
    			Class defaultclazz = adapters.get(key);
    			if(defaultclazz != null)
    			{
    				log.debug("Use custom adapter["+value+"] to replace default adapter["+defaultclazz + "] for db[" + key+"]" );
    				try {
						Class newclz = Class.forName(value);
						replaceOthers(newclz,key,defaultclazz);
						adapters.put(key, newclz);
						
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    			else
    			{
    				log.debug("Add custom adapter["+value+"] with db[" + key+"]" );
    				try {
						Class newclz = Class.forName(value);
						
						adapters.put(key, newclz);
						
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    			
    		}
    	}
    	
    }
}
