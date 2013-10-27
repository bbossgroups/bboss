/*****************************************************************************
 *                                                                           *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     *
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
 *  Author of Learning Java 						     					 *
 *                                                                           *
 *****************************************************************************/
package com.frameworkset.common.util;

/**
 * @author biaoping.yin
 * 文件处理实用类
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Properties;
public class FileUtil {
	public FileUtil() {
	}

	/**
	 * Description:读取文件的内容，将其保存在StringBuffer对象中返回，
	 * @param file
	 * @return StringBuffer
	 * @throws Exception
	 * StringBuffer
	 */
	public static StringBuffer read(String file) throws Exception {
		BufferedReader in = null;
		StringBuffer sb = new StringBuffer();
		String s = null;
		StringBuffer stringbuffer;
		try {
			in = new BufferedReader(new FileReader(file));
			while ((s = in.readLine()) != null)
				sb.append(s).append('\n');
			stringbuffer = sb;
		} finally {
			if (in != null)
				in.close();
		}
		return stringbuffer;
	}

	/**
	 * Description:获取属性文件的类容
	 * @param propsFile
	 * @return  Properties
	 * @throws Exception
	 * Properties
	 */
	public static Properties getProperties(String propsFile) throws Exception {
		return getProperties(propsFile, false);
	}
	/** 
	 * Description:获取属性文件的内容，并且根据addToSystemProps的值是否装载系统属性
	 * @param propsFile
	 * @param addToSystemProps true:装载系统属性，false不装载系统属性
	 * @return Properties
	 * @throws Exception
	 * Properties
	 */
	public static Properties getProperties(
		String propsFile,
		boolean addToSystemProps)
		throws Exception {
		FileInputStream fis = null;
		Properties props = null;
		try {
			fis = new FileInputStream(propsFile);
			props =
				addToSystemProps
					? new Properties(System.getProperties())
					: new Properties();
			props.load(fis);
			fis.close();
		} finally {
			if (fis != null)
				fis.close();
		}
		return props;
	}	

	//	   private static final Category cat;
	//
	//	   static 
	//	   {
	//		   cat = Category.getInstance(com.pow2.util.FileUtil.class);
	//	   }

}
