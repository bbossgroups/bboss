/*
 *  PoolMan Java Object Pooling and Caching Library
 *  Copyright (C) 1999-2001 The Code Studio
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  The full license is located at the root of this distribution
 *  in the LICENSE file.
 */
package com.frameworkset.common.poolman.sql;

// Codestudio PoolMan Library

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.frameworkset.common.poolman.util.JDBCPool;
import com.frameworkset.common.poolman.util.SQLManager;

/**
 * The PoolMan class is the core Driver implementation.
 * It can be loaded via the DriverManager, and is also accessed
 * through the PoolManDataSource.
 */
public class PoolMan implements Driver {


    public static String PROTOCOL = "jdbc:poolman:";

    public static final String XML_CONFIG_FILE = "poolman.xml";
    public static final String PROPS_CONFIG_FILE = "poolman.props";

    public static final int VERSIONID = 2;
    public static final int RELEASEID = 0;

    public static boolean STARTED = false;


    public static void main(String args[]) {
        if ((null == args) || (args.length < 1)) {
            System.out.println("\nUSAGE: java com.frameworkset.common.poolman.sql.PoolMan \"[db_name]\"\n" + "Where the [db_name] parameter corresponds to a dbname " + "specified in your poolman.xml file.\n");
            System.exit(1);
        }

        System.out.print("Attempting to connect to " + args[0] + "... ");
        Connection con = null;
        try {
            Class.forName("com.frameworkset.common.poolman.sql.PoolMan").newInstance();
            con = DriverManager.getConnection("jdbc:poolman://" + args[0]);
            System.out.print("CONNECTED.\nTest passed, exiting.\n\n");
        } catch (Exception e) {
            System.out.print("FAILED.\nTest Failed with the following error:\n");
            e.printStackTrace();
            System.out.print("\n");
        }
        finally {
            try {
                con.close();
            } catch (SQLException se) {
            }
        }
        System.exit(1);
    }

    public PoolMan() {
        registerDriver();
    }

    /** Registers the Driver with the DriverManager. */
    private void registerDriver() {
        boolean registered = false;
        try {
            for (Enumeration enum1 = DriverManager.getDrivers(); enum1.hasMoreElements();) {
                Driver d = (Driver) enum1.nextElement();
                if (d instanceof com.frameworkset.common.poolman.sql.PoolMan)
                    registered = true;
            }
            if (!registered)
                DriverManager.registerDriver(this);
        } catch (SQLException se) {
            throw new RuntimeException("PoolMan Driver Failed to Load and Register with DriverManager");
        }
    }

    /** Convenience method, not necessary if lazy loading is preferred. */
    public static void start()
            throws Exception {
        if (!STARTED)
            new com.frameworkset.common.poolman.management.PoolManBootstrap().start();
    }

    /** Convenience method, returns the default DataSource. */
    public static DataSource getDataSource()
            throws SQLException {
        return findDataSource(null);
    }

    /** Convenience method, merely calls findDataSource(dbname) */
    public static DataSource getDataSource(String dbname)
            throws SQLException {

        return findDataSource(dbname);
    }

    public static DataSource findDataSource()
            throws SQLException {
        return findDataSource(null);
    }

    /** Convenience method to return a named DataSource */
    public static DataSource findDataSource(String dbname)
            throws SQLException {

        SQLManager manager = SQLManager.getInstance();

        JDBCPool jpool = null;

        // see if we're looking for the unnamed default DataSource
        if (dbname == null) {
            jpool = (JDBCPool) manager.getPool(null);
        }

        else {

            // try the JNDI Name first
            try {
                jpool = (JDBCPool) manager.getPoolByJNDIName(dbname,true);
            } catch (NullPointerException npe1) {
            }

            // next try the dbname
            if (jpool == null) {
                try {
                    jpool = (JDBCPool) manager.getPool(dbname);
                } catch (NullPointerException npe2) {
                }
            }
        }

        // if it didn't exist under either name, throw a SQLException
        if (jpool == null) {
            throw new SQLException("No such datasource: " + dbname + ". Check your poolman.xml config, and be sure you " + "are using a valid dbname parameter (use dbname, not jndiName)");
        }

        // if it did exist, return its DataSource
        return jpool.getDataSource();
    }

    /** Determine whether the dbname is valid for this PoolMan instance. */
    public static boolean nameIsValid(String dbname) {
        SQLManager manager = SQLManager.getInstance();
        for (Enumeration enum1 = manager.getAllPoolnames(); enum1.hasMoreElements();) {
            String name = enum1.nextElement().toString();
            if (dbname.equals(name))
                return true;
        }
        return false;
    }

    /*
      DRIVER METHODS
    */

    public boolean acceptsURL(String url)
            throws SQLException {
        if (url.startsWith("jdbc:poolman"))
            return true;
        return false;
    }

    public static boolean acceptsURLString(String url) {
        if (url.startsWith("jdbc:poolman"))
            return true;
        return false;
    }


    public Connection connect(String url, Properties info)
            throws SQLException {

        if (!acceptsURL(url))
            return null;

        try {

            SQLManager manager = SQLManager.getInstance();

            String dbname = null;

            if (url.indexOf("//") != -1) {
                dbname = url.substring((url.lastIndexOf("/") + 1), url.length());
            }

            // Properties override the URL. Don't know if this is wise or not.
            if ((null != info) && (info.containsKey("dbname")))
                dbname = info.getProperty("dbname");

            if ((dbname == null) || (dbname.equals(""))) {
                // return a PooledConnection to the default pool
                return manager.requestConnection();
            }
            else {
                // return a PooledConnection to pool named in poolman.xml that matches the driver URL
                try {
                    return manager.requestConnection(dbname);
                } catch (Exception e) {
                    throw new SQLException(e.getMessage());
                }
            }
        } catch (Exception pe) {
            throw new SQLException(pe.getMessage());
        }

    }

    public static Connection connect(String url)
            throws SQLException {

        if (!acceptsURLString(url))
            return null;

        try {

            SQLManager manager = SQLManager.getInstance();
            String dbname = null;

            if (url.indexOf("//") != -1) {
                dbname = url.substring((url.lastIndexOf("/") + 1), url.length());
            }

            if ((dbname == null) || (dbname.equals(""))) {
                return manager.requestConnection();
            }
            else {
                try {
                    return manager.requestConnection(dbname);
                } catch (Exception e) {
                    throw new SQLException(e.getMessage());
                }

            }

        } catch (Exception pe) {
            throw new SQLException(pe.getMessage());
        }

    }

    public int getMajorVersion() {
        return VERSIONID;
    }

    public int getMinorVersion() {
        return RELEASEID;
    }

    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return null;
    }

    public boolean jdbcCompliant() {
        // it's impossible to be compliant when we cannot guarantee the
        // behavior of underlying drivers
        return false;
    }

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

}
