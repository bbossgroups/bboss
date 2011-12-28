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
package com.frameworkset.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import org.frameworkset.util.ClassUtils;
import org.frameworkset.util.PropertiesPersister;



/**
 * <p>Title: DefaultPropertiesPersister.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultPropertiesPersister  implements PropertiesPersister {

	// Determine whether Properties.load(Reader) is available (on JDK 1.6+)
	private static final boolean loadFromReaderAvailable =
			ClassUtils.hasMethod(Properties.class, "load", new Class[] {Reader.class});
	private static final boolean storeToWriterAvailable =
		ClassUtils.hasMethod(Properties.class, "store", new Class[] {Writer.class, String.class});
	private static Method loadMethodfor16; 
	private static Method writerMethodfor16; 
	static
	{
		if(loadFromReaderAvailable)
		{
			try {
				loadMethodfor16 = Properties.class.getMethod("load", new Class[] {Reader.class});
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				writerMethodfor16 = Properties.class.getMethod("store", new Class[] {Writer.class, String.class});
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	// Determine whether Properties.store(Writer, String) is available (on JDK 1.6+)
	


	public void load(Properties props, InputStream is) throws IOException {
		props.load(is);
	}

	public void load(Properties props, Reader reader) throws IOException {
		if (loadFromReaderAvailable) {
			// On JDK 1.6+
			try {
				loadMethodfor16.invoke(props, new Object[]{reader});
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			props.load(reader);
		}
		else {
			// Fall back to manual parsing.
			doLoad(props, reader);
		}
	}

	protected void doLoad(Properties props, Reader reader) throws IOException {
		BufferedReader in = new BufferedReader(reader);
		while (true) {
			String line = in.readLine();
			if (line == null) {
				return;
			}
			line = SimpleStringUtil.trimLeadingWhitespace(line);
			if (line.length() > 0) {
				char firstChar = line.charAt(0);
				if (firstChar != '#' && firstChar != '!') {
					while (endsWithContinuationMarker(line)) {
						String nextLine = in.readLine();
						line = line.substring(0, line.length() - 1);
						if (nextLine != null) {
							line += SimpleStringUtil.trimLeadingWhitespace(nextLine);
						}
					}
					int separatorIndex = line.indexOf("=");
					if (separatorIndex == -1) {
						separatorIndex = line.indexOf(":");
					}
					String key = (separatorIndex != -1 ? line.substring(0, separatorIndex) : line);
					String value = (separatorIndex != -1) ? line.substring(separatorIndex + 1) : "";
					key = SimpleStringUtil.trimTrailingWhitespace(key);
					value = SimpleStringUtil.trimLeadingWhitespace(value);
					props.put(unescape(key), unescape(value));
				}
			}
		}
	}

	protected boolean endsWithContinuationMarker(String line) {
		boolean evenSlashCount = true;
		int index = line.length() - 1;
		while (index >= 0 && line.charAt(index) == '\\') {
			evenSlashCount = !evenSlashCount;
			index--;
		}
		return !evenSlashCount;
	}

	protected String unescape(String str) {
		StringBuffer outBuffer = new StringBuffer(str.length());
		for (int index = 0; index < str.length();) {
			char c = str.charAt(index++);
			if (c == '\\') {
				c = str.charAt(index++);
				if (c == 't') {
					c = '\t';
				}
				else if (c == 'r') {
					c = '\r';
				}
				else if (c == 'n') {
					c = '\n';
				}
				else if (c == 'f') {
					c = '\f';
				}
			}
			outBuffer.append(c);
		}
		return outBuffer.toString();
	}


	public void store(Properties props, OutputStream os, String header) throws IOException {
		props.store(os, header);
	}

	public void store(Properties props, Writer writer, String header) throws IOException {
		if (storeToWriterAvailable) {
			// On JDK 1.6+
			// On JDK 1.6+
			try {
				writerMethodfor16.invoke(props, new Object[]{writer,header});
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			props.store(writer, header);
		}
		else {
			// Fall back to manual parsing.
			doStore(props, writer, header);
		}
	}

	protected void doStore(Properties props, Writer writer, String header) throws IOException {
		BufferedWriter out = new BufferedWriter(writer);
		if (header != null) {
			out.write("#" + header);
			out.newLine();
		}
		out.write("#" + new Date());
		out.newLine();
		for (Enumeration keys = props.keys(); keys.hasMoreElements();) {
			String key = (String) keys.nextElement();
			String val = props.getProperty(key);
			out.write(escape(key, true) + "=" + escape(val, false));
			out.newLine();
		}
		out.flush();
	}

	protected String escape(String str, boolean isKey) {
		int len = str.length();
		StringBuffer outBuffer = new StringBuffer(len * 2);
		for (int index = 0; index < len; index++) {
			char c = str.charAt(index);
			switch (c) {
				case ' ':
					if (index == 0 || isKey) {
						outBuffer.append('\\');
					}
					outBuffer.append(' ');
					break;
				case '\\':
					outBuffer.append("\\\\");
					break;
				case '\t':
					outBuffer.append("\\t");
					break;
				case '\n':
					outBuffer.append("\\n");
					break;
				case '\r':
					outBuffer.append("\\r");
					break;
				case '\f':
					outBuffer.append("\\f");
					break;
				default:
					if ("=: \t\r\n\f#!".indexOf(c) != -1) {
						outBuffer.append('\\');
					}
					outBuffer.append(c);
			}
		}
		return outBuffer.toString();
	}


	public void loadFromXml(Properties props, InputStream is) throws IOException {
		try {
			props.loadFromXML(is);
		}
		catch (NoSuchMethodError err) {
			throw new IOException("Cannot load properties XML file - not running on JDK 1.5+: " + err.getMessage());
		}
	}

	public void storeToXml(Properties props, OutputStream os, String header) throws IOException {
		try {
			props.storeToXML(os, header);
		}
		catch (NoSuchMethodError err) {
			throw new IOException("Cannot store properties XML file - not running on JDK 1.5+: " + err.getMessage());
		}
	}

	public void storeToXml(Properties props, OutputStream os, String header, String encoding) throws IOException {
		try {
			props.storeToXML(os, header, encoding);
		}
		catch (NoSuchMethodError err) {
			throw new IOException("Cannot store properties XML file - not running on JDK 1.5+: " + err.getMessage());
		}
	}

}
