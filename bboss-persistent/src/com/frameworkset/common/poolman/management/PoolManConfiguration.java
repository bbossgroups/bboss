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
 *  distributed under the License is distributed on an "AS IS" bboss persistent,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.frameworkset.common.poolman.management;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.PoolManConstants;
import com.frameworkset.util.ValueObjectUtil;
import com.frameworkset.velocity.BBossVelocityUtil;

/**
 * This service configures ObjectPools by first looking for 'poolman.xml' in the
 * CLASSPATH. If it finds it, it parses the database entries and generic Class
 * entries and creates MBeans and pools for them, adding each one to an
 * appropriate PoolManager and the MBeanServer. If it cannot find this xml file,
 * it looks for 'poolman.props' and loads those properties in order to create
 * the pools (with no MBeans). If it cannot find poolman.props, it looks for the
 * deprecated pool.props file and treats it as if it were named 'poolman.props'.
 * If it cannot find any of these files, it throws an Exception.
 */
public class PoolManConfiguration implements Serializable {
	private static Logger log = Logger.getLogger(PoolManConfiguration.class);

	private String configFile;
	private ConfigParser handler;

	private ArrayList datasources;
	private ArrayList genericObjects;

	private boolean useJMX = PoolManConstants.DEFAULT_USE_JMX;
	public static String jndi_principal = null;
	public static String jndi_credentials = null;
	private String[] filterdbname = null;
	private String dbnamespace;
	public static String pooltemplates;

	/**
	 * 用户自定义的适配器
	 */
	public Map<String, String> adaptors = new HashMap<String, String>();

	public PoolManConfiguration(String configFile, String dbname) {

		// this.configFile = configFile;
		// this.datasources = new ArrayList();
		// this.genericObjects = new ArrayList();
		// this.filterdbname = dbname;
		this(configFile, null, dbname == null ? null : new String[] { dbname });

	}

	public PoolManConfiguration(String configFile, String dbnamespace,
			String[] dbnames) {

		this.configFile = configFile;
		this.datasources = new ArrayList();
		this.genericObjects = new ArrayList();
		this.filterdbname = dbnames;
		this.dbnamespace = dbnamespace;

	}

	public PoolManConfiguration(String configFile) {

		this(configFile, null);
	}

	public boolean isUsingJMX() {
		return handler.isManagementJMX();
	}

	/** Load DataSource info from XML and create a Service for each entry set. */
	public void loadConfiguration(Map context) throws Exception {

		// first try XML
		try {
			parseXML(context);
		} catch (NullPointerException ne) {
			ne.printStackTrace();
			// then try deprecated properties
			System.out.println("\n** ERROR: Unable to find XML file "
					+ configFile + ": " + ne);
			// don't try the props files anymore, it's been over a year
			// System.out.println("** WARNING: Attempting to use deprecated properties files\n");
			// this.datasources =
			// parseProperties(PoolManConstants.PROPS_CONFIG_FILE);

		} catch (Exception e) {
			System.out.println("\n** ERROR: Unable to parse XML file "
					+ configFile + ": " + e);
		}

	}

	public ArrayList getDataSources() {
		return this.datasources;
	}

	public ArrayList getGenericPools() {
		return this.genericObjects;
	}

	public Properties getAdminProperties() {
		return handler.getAdminProps();
	}

	private static Object lock = new Object();

	private static void initpooltemplates() {
		if (pooltemplates == null) {
			synchronized (lock) {
				if (pooltemplates == null) {
					InputStream reader = null;
					ByteArrayOutputStream swriter = null;
					OutputStream temp = null;
					try {
						reader = ValueObjectUtil
								.getInputStreamFromFile(PoolManConstants.XML_CONFIG_FILE_TEMPLATE);

						swriter = new ByteArrayOutputStream();
						temp = new BufferedOutputStream(swriter);

						int len = 0;
						byte[] buffer = new byte[1024];
						while ((len = reader.read(buffer)) > 0) {
							temp.write(buffer, 0, len);
						}
						temp.flush();
						pooltemplates = swriter.toString("UTF-8");

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						if (reader != null)
							try {
								reader.close();
							} catch (IOException e) {
							}
						if (swriter != null)
							try {
								swriter.close();
							} catch (IOException e) {
							}
						if (temp != null)
							try {
								temp.close();
							} catch (IOException e) {
							}
					}
					// pooltemplates =
					// ValueObjectUtil.getFileContent(PoolManConstants.XML_CONFIG_FILE_TEMPLATE,"UTF-8");
				}
			}

		}
	}

	private void parseXML(Map context) throws Exception {

		/* CHANGED TO USE JAXP */

		if (!this.configFile.equals(PoolManConstants.XML_CONFIG_FILE_TEMPLATE)) {
			URL confURL = PoolManConfiguration.class.getClassLoader()
					.getResource(configFile);
			if (confURL == null)
				PoolManConfiguration.class.getClassLoader().getResource(
						"/" + configFile);

			if (confURL == null)
				getTCL().getResource(configFile);
			if (confURL == null)
				getTCL().getResource("/" + configFile);
			if (confURL == null)
				confURL = ClassLoader.getSystemResource(configFile);
			if (confURL == null)
				confURL = ClassLoader.getSystemResource("/" + configFile);
			String url = "";
			if (confURL == null) {
				url = System.getProperty("user.dir");

				url += "/" + configFile;
				log.debug("Pool Config file:" + System.getProperty("user.dir")
						+ "/" + configFile);
			} else
				url = confURL.toString();
			this.handler = new ConfigParser(url, this.dbnamespace,
					this.filterdbname);

			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(false);
			factory.setValidating(false);
			SAXParser parser = factory.newSAXParser();
			parser.parse(url, handler);
			this.adaptors = handler.getAdaptors();
			this.datasources = handler.getDataSourceProperties();
			this.genericObjects = handler.getGenericProperties();
		} else {
			initpooltemplates();
			String poolconfig = BBossVelocityUtil.evaluate(context, "",
					pooltemplates);
			InputStream in = null;
			ByteArrayInputStream sr = null;
			try {

				this.handler = new ConfigParser(
						PoolManConstants.XML_CONFIG_FILE_TEMPLATE,
						this.dbnamespace, this.filterdbname);

				SAXParserFactory factory = SAXParserFactory.newInstance();
				factory.setNamespaceAware(false);
				factory.setValidating(false);
				SAXParser parser = factory.newSAXParser();
				sr = new ByteArrayInputStream(poolconfig.getBytes());
				in = new java.io.BufferedInputStream(sr);
				parser.parse(in, handler);
				this.adaptors = handler.getAdaptors();
				this.datasources = handler.getDataSourceProperties();
				this.genericObjects = handler.getGenericProperties();
			} finally {
				if (sr != null) {
					try {
						sr.close();
					} catch (Exception e2) {

					}
				}
				if (in != null) {
					try {
						in.close();
					} catch (Exception e2) {

					}
				}
			}
		}

	}

	private static ClassLoader getTCL() throws IllegalAccessException,
			InvocationTargetException {
		Method method = null;
		try {
			method = (java.lang.Thread.class).getMethod(
					"getContextClassLoader", null);
		} catch (NoSuchMethodException e) {
			return null;
		}
		return (ClassLoader) method.invoke(Thread.currentThread(), null);
	}

	private ArrayList parseProperties(String propsfilename) throws Exception {

		// backwards compatibility is a b****

		Hashtable datasources = new Hashtable();
		this.getClass().getClassLoader().getParent().getSystemClassLoader()
				.getResource(null);
		InputStream is = null;
		Properties p = null;

		try {
			is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(propsfilename);
			p = new Properties();
			p.load(is);
		} catch (Exception e) {
			if (propsfilename.equals(PoolManConstants.PROPS_CONFIG_FILE)) {
				try {
					is = Thread.currentThread().getContextClassLoader()
							.getResourceAsStream(
									PoolManConstants.OLDPROPS_CONFIG_FILE);
					p = new Properties();
					p.load(is);
				} catch (Exception e2) {
					throw new Exception(
							"Unable to find and read a valid poolman.xml config file. "
									+ "Please ensure that '"
									+ PoolManConstants.XML_CONFIG_FILE
									+ "' is in a directory that is in your CLASSPATH.\n");
				}
				System.out
						.println("\nPLEASE NOTE: You should replace the pool.props file with a valid poolman.xml file\n");
			} else {
				throw new Exception(
						"ERROR: Unable to find and read a valid PoolMan properties file.\n"
								+ "Please ensure that "
								+ PoolManConstants.XML_CONFIG_FILE
								+ " or at least "
								+ propsfilename
								+ " is in a directory that is in your CLASSPATH.\n");
			}
		}

		// now have props with each set of datasource entries delimited by a
		// number
		// split those into an ArrayList of Properties objects

		Properties theseProps = null;
		String entrySetNumber = null;

		for (Enumeration enum_ = p.keys(); enum_.hasMoreElements();) {

			String key = (String) enum_.nextElement();

			// get rid of the "_"
			String adjustedKey = key;
			if (key.indexOf("_") != -1) {
				StringBuffer sb = new StringBuffer(key.substring(0, key
						.indexOf("_")));
				sb.append(key.substring(key.indexOf("_") + 1, key.length()));
				adjustedKey = sb.toString();
				if ((!adjustedKey.toLowerCase().equals("dbname"))
						&& (adjustedKey.startsWith("db"))) {
					adjustedKey = adjustedKey
							.substring(2, adjustedKey.length());
				}
			}
			if (adjustedKey.toLowerCase().startsWith("cacherefresh")) {
				adjustedKey = "cacherefreshinterval"
						+ adjustedKey.substring(12, adjustedKey.length());
			}

			try {
				entrySetNumber = key.substring(key.indexOf('.') + 1, key
						.length());
			} catch (StringIndexOutOfBoundsException sbe) {
				throw new Exception("Unnumbered property in poolman.props: "
						+ key);
			}

			if (datasources.containsKey(entrySetNumber))
				theseProps = (Properties) datasources.get(entrySetNumber);
			else
				theseProps = new Properties();

			theseProps.setProperty(adjustedKey.substring(0, adjustedKey
					.indexOf('.')), p.getProperty(key));

			datasources.put(entrySetNumber, theseProps);

		}

		ArrayList finalDataSourceList = new ArrayList();
		for (Iterator iter = datasources.keySet().iterator(); iter.hasNext();) {
			Properties testp = (Properties) datasources.get(iter.next());
			finalDataSourceList.add(testp);
		}

		return finalDataSourceList;
	}

	/**
	 * @return the adaptors
	 */
	public Map<String, String> getAdaptors() {
		return adaptors;
	}

	/**
	 * @param adaptors
	 *            the adaptors to set
	 */
	public void setAdaptors(Map<String, String> adaptors) {
		this.adaptors = adaptors;
	}

}
