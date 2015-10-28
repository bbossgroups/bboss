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

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.frameworkset.common.poolman.util.SQLManager;

/**
 *
 * <p>Title: DBAdapter</p>
 *
 * <p>Description: 消除数据之间的差异性</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class DBAdapter implements Serializable{
    /*********************************************
     *  Method following are inheritate from     *
     *  com.frameworkset.orm.adapter.DB          *
     *********************************************/

    public static final String toUpperCase(String in) {
        return SQLManager.getInstance().getDBAdapter().toUpperCase(in);
    }

    public static final String getIDMethodType(String dbName) {
        return SQLManager.getInstance().getDBAdapter(dbName).getIDMethodType();
    }

    public static final String getIDMethodSQL(Object obj,String dbName) {
        return SQLManager.getInstance().getDBAdapter(dbName).getIDMethodSQL(obj);
    }

    public static final void lockTable(Connection con, String table,String dbName) throws SQLException {
        SQLManager.getInstance().getDBAdapter(dbName).lockTable(con,table);
    }

    public static final void unlockTable(Connection con, String table,String dbName) throws SQLException {
        SQLManager.getInstance().getDBAdapter(dbName).unlockTable(con,table);
    }

    public static final String ignoreCase(String in,String dbName) {
        return SQLManager.getInstance().getDBAdapter(dbName).ignoreCase(in);
    }

    /**
     * This method is used to ignore case in an ORDER BY clause.
     * Usually it is the same as ignoreCase, but some databases
     * (Interbase for example) does not use the same SQL in ORDER BY
     * and other clauses.
     *
     * @param in The string whose case to ignore.
     * @return The string in a case that can be ignored.
     */
    public static final String ignoreCaseInOrderBy(String in,String dbName)
    {
        return SQLManager.getInstance().getDBAdapter(dbName).ignoreCaseInOrderBy(in);
    }

    /**
     * This method is used to check whether the database natively
     * supports limiting the size of the resultset.
     *
     * @return True if the database natively supports limiting the
     * size of the resultset.
     */
    public static final boolean supportsNativeLimit(String dbName)
    {
        return SQLManager.getInstance().getDBAdapter(dbName).supportsNativeLimit();
    }

    /**
     * This method is used to check whether the database natively
     * supports returning results starting at an offset position other
     * than 0.
     *
     * @return True if the database natively supports returning
     * results starting at an offset position other than 0.
     */
    public static final boolean supportsNativeOffset(String dbName)
    {
        return SQLManager.getInstance().getDBAdapter(dbName).supportsNativeOffset();
    }

   /**
    * This method is for the SqlExpression.quoteAndEscape rules.  The rule is,
    * any string in a SqlExpression with a BACKSLASH will either be changed to
    * "\\" or left as "\".  SapDB does not need the escape character.
    *
    * @return true if the database needs to escape text in SqlExpressions.
    */

    public static final boolean escapeText(String dbName)
    {
        return SQLManager.getInstance().getDBAdapter(dbName).escapeText();
    }

    /**
     * This method is used to check whether the database supports
     * limiting the size of the resultset.
     *
     * @return The limit style for the database.
     */
    public static final int getLimitStyle(String dbName)
    {
        return SQLManager.getInstance().getDBAdapter(dbName).getLimitStyle();
    }

    /**
     * This method is used to format any date string.
     * Database can use different default date formats.
     *
     * @param date the Date to format
     * @return The proper date formatted String.
     */
    public static final String getDateString(Date date,String dbName)
    {
        return SQLManager.getInstance().getDBAdapter(dbName).getDateString(date);
    }

    /**
     * This method is used to format a boolean string.
     *
     * @param b the Boolean to format
     * @return The proper date formatted String.
     */
    public static final String getBooleanString(Boolean b,String dbName)
    {
        return SQLManager.getInstance().getDBAdapter(dbName).getBooleanString(b);
    }

    /**
    * Returns the character used to indicate the beginning and end of
    * a piece of text used in a SQL statement (generally a single
    * quote).
    *
    * @return The text delimeter.
    */
   public static final char getStringDelimiter(String dbName)
   {
       return SQLManager.getInstance().getDBAdapter(dbName).getStringDelimiter();
   }

   public static final String toUpperCase(String in,String dbName) {
		return SQLManager.getInstance().getDBAdapter(dbName).toUpperCase(in);
	}

	public static final String getIDMethodType() {
		return SQLManager.getInstance().getDBAdapter().getIDMethodType();
	}

	public static final String getIDMethodSQL(Object obj) {
		return SQLManager.getInstance().getDBAdapter().getIDMethodSQL(obj);
	}

	public static final void lockTable(Connection con, String table) throws SQLException {
		SQLManager.getInstance().getDBAdapter().lockTable(con,table);
	}

	public static final void unlockTable(Connection con, String table) throws SQLException {
		SQLManager.getInstance().getDBAdapter().unlockTable(con,table);
	}

	public static final String ignoreCase(String in) {
		return SQLManager.getInstance().getDBAdapter().ignoreCase(in);
	}

	/**
	 * This method is used to ignore case in an ORDER BY clause.
	 * Usually it is the same as ignoreCase, but some databases
	 * (Interbase for example) does not use the same SQL in ORDER BY
	 * and other clauses.
	 *
	 * @param in The string whose case to ignore.
	 * @return The string in a case that can be ignored.
	 */
	public static final String ignoreCaseInOrderBy(String in)
	{
		return SQLManager.getInstance().getDBAdapter().ignoreCaseInOrderBy(in);
	}

	/**
	 * This method is used to check whether the database natively
	 * supports limiting the size of the resultset.
	 *
	 * @return True if the database natively supports limiting the
	 * size of the resultset.
	 */
	public static final boolean supportsNativeLimit()
	{
		return SQLManager.getInstance().getDBAdapter().supportsNativeLimit();
	}

	/**
	 * This method is used to check whether the database natively
	 * supports returning results starting at an offset position other
	 * than 0.
	 *
	 * @return True if the database natively supports returning
	 * results starting at an offset position other than 0.
	 */
	public static final boolean supportsNativeOffset()
	{
		return SQLManager.getInstance().getDBAdapter().supportsNativeOffset();
	}

   /**
	* This method is for the SqlExpression.quoteAndEscape rules.  The rule is,
	* any string in a SqlExpression with a BACKSLASH will either be changed to
	* "\\" or left as "\".  SapDB does not need the escape character.
	*
	* @return true if the database needs to escape text in SqlExpressions.
	*/

	public static final boolean escapeText()
	{
		return SQLManager.getInstance().getDBAdapter().escapeText();
	}

	/**
	 * This method is used to check whether the database supports
	 * limiting the size of the resultset.
	 *
	 * @return The limit style for the database.
	 */
	public static final int getLimitStyle()
	{
		return SQLManager.getInstance().getDBAdapter().getLimitStyle();
	}

	/**
	 * This method is used to format any date string.
	 * Database can use different default date formats.
	 *
	 * @param date the Date to format
	 * @return The proper date formatted String.
	 */
	public static final String getDateString(Date date)
	{
		return SQLManager.getInstance().getDBAdapter().getDateString(date);
	}

	 public static final String getDateString(String date)
	 {
		 return SQLManager.getInstance().getDBAdapter().getDateString(date);
	 }

	/**
	 * This method is used to format a boolean string.
	 *
	 * @param b the Boolean to format
	 * @return The proper date formatted String.
	 */
	public static final String getBooleanString(Boolean b)
	{
		return SQLManager.getInstance().getDBAdapter().getBooleanString(b);
	}

	/**
	* Returns the character used to indicate the beginning and end of
	* a piece of text used in a SQL statement (generally a single
	* quote).
	*
	* @return The text delimeter.
	*/
   public static final char getStringDelimiter()
   {
	   return SQLManager.getInstance().getDBAdapter().getStringDelimiter();
   }

   public static final Object getCharValue(ResultSet res, int i, String value)
   {
       return SQLManager.getInstance().getDBAdapter().getCharValue(res, i, value);
   }

//   public static final Object getCharValue(byte[] bytes,String dbName)
//   {
//       //System.out.println(dbName + ":" + SQLManager.getInstance().getDBAdapter(dbName));
//       return SQLManager.getInstance().getDBAdapter(dbName).getCharValue(bytes);
//   }



}
