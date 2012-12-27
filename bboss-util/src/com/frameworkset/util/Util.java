/*****************************************************************************
 *                                                                           *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     							 *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    *
 *                                                                           *
 *  The Original Code is tag. The Initial Developer of the Original          *
 *  Code is biaoping yin. Portions created by biaoping yin are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  biaoping.yin (yin-bp@163.com)                                            *
 *                                                                           *
 *****************************************************************************/

package com.frameworkset.util;

/**
 * @author biaoping.yin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;
public class Util implements Serializable{
	public Util()
		{
		}

		public static String getLastToken(String str, String tokenSeparator)
		{
			return str.substring(str.lastIndexOf(tokenSeparator) + 1, str.length());
		}

		public static boolean isNull(String s)
		{
			return s == null || s.length() < 1;
		}

		public static boolean isNull(String s, String val)
		{
			return isNull(s) || s.compareTo(val) == 0;
		}

		public static String stackTrace(Throwable t)
		{
			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			String s = sw.toString();
			try
			{
				sw.close();
			}
			catch(IOException e)
			{
				//cat.error("::stackTrace - cannot close the StringWriter object", e);
			}
			return s;
		}

		public static String dbString(String v)
		{
			StringBuffer sb = new StringBuffer();
			return isNull(v) ? "" : sb.append("'").append(v).append("'").toString();
		}

		public static String dumpHashTable(Hashtable table, boolean html)
		{
			Enumeration keys = table.keys();
			Enumeration values = table.elements();
			StringBuffer sb = new StringBuffer();
			String eof = "\n";
			if(html)
				eof = "<br>\n";
			for(; keys.hasMoreElements(); sb.append("  key [").append(keys.nextElement().toString()).append("] = [").append(values.nextElement().toString()).append("]").append(eof));
			return sb.toString();
		}

		public static String addURLParameter(String URL, String paramName, String paramValue)
		{
			String param = paramName + "=" + paramValue;
			return addURLParameter(URL, param);
		}

		public static String addURLParameter(String URL, String parameter)
		{
			StringBuffer sb = new StringBuffer(URL);
			if(URL.lastIndexOf('?') == -1)
				sb.append("?");
			else
				sb.append("&");
			sb.append(parameter);
			return sb.toString();
		}

		public static String remove(String str, String until)
		{
			String val = null;
			int indx = str.indexOf(until);
			if(indx != -1)
				val = str.substring(indx + until.length(), str.length());
			return val;
		}

		static Class _mthclass$(String x0)
		{
			try
			{
				return Class.forName(x0);
			}
			catch(ClassNotFoundException x1)
			{
				throw new NoClassDefFoundError(x1.getMessage());
			}
		}
		
		/**
		 * determine the OS name
		 * 
		 * @return The name of the OS
		 */
		public static final String getOS() {
			return System.getProperty("os.name");
		}

		/**
		 * @return True if the OS is a Windows derivate.
		 */
		public static final boolean isWindows() {
			return getOS().contains("Windows");
		}

		/**
		 * @return True if the OS is a Linux derivate.
		 */
		public static final boolean isLinux() {
			return getOS().contains("Linux");
		}
		
		/**
		 * @return True if the OS is a Linux derivate.
		 */
		public static final boolean isUnix() {
			return getOS().contains("Unix");
		}
		
		/**
		 * @return True if the OS is a Windows derivate.
		 */
		public static final boolean isWindows(String osname) {
			return osname.contains("Windows");
		}

		/**
		 * @return True if the OS is a Linux derivate.
		 */
		public static final boolean isLinux(String osname) {
			return osname.contains("Linux");
		}
		
		/**
		 * @return True if the OS is a Linux derivate.
		 */
		public static final boolean isUnix(String osname) {
			return osname.contains("Unix");
		}
		
		
		

}
