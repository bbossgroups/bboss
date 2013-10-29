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
package com.frameworkset.util;

/**
 * @author biaoping.yin
 * 对象序列化和反序列化类 
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serializer implements Serializable{

	 private Serializer()
	 {
	 }
	 
	 /**
	  * Description:将对象字节流转换成一个对象
	  * @param buf
	  * @return Object
	  * @throws Exception
	  * Object
	  */
	 public static Object toObject(byte buf[])
		 throws Exception
	 {
		 ObjectInputStream ois = null;
		 Object obj = null;
		 try
		 {
			 ByteArrayInputStream bin = new ByteArrayInputStream(buf);
			 ois = new ObjectInputStream(bin);
			 obj = ois.readObject();
		 }
		 finally
		 {
			 if(ois != null)
				 ois.close();
		 }
		 return obj;
	 }
	
	 /**
	  * Description:将一个对象转换为一个字节数组
	  * @param obj
	  * @return byte[]
	  * @throws Exception
	  * 
	  */
	 public static byte[] toByteArray(Object obj)
		 throws Exception
	 {
		 ObjectOutputStream oos = null;
		 byte buf[] = null;
		 try
		 {
			 ByteArrayOutputStream bos = new ByteArrayOutputStream();
			 oos = new ObjectOutputStream(bos);
			 oos.writeObject(obj);
			 oos.flush();
			 buf = bos.toByteArray();
		 }
		 finally
		 {
			 if(oos != null)
				 oos.close();
		 }
		 return buf;
	 }
	
	 /**
	  * Description:实现对象的完整拷贝
	  * @param oldObj
	  * @return Object
	  * @throws Exception
	  * 
	  */
	 public static Object deepCopy(Object oldObj)
		 throws Exception
	 {
		 ObjectOutputStream oos = null;
		 ObjectInputStream ois = null;
		 Object obj;
		 try
		 {
			 ByteArrayOutputStream bos = new ByteArrayOutputStream();
			 oos = new ObjectOutputStream(bos);
			 oos.writeObject(oldObj);
			 oos.flush();
			 ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
			 ois = new ObjectInputStream(bin);
			 obj = ois.readObject();
		 }
		 finally
		 {
			 if(oos != null)
				 oos.close();
			 if(ois != null)
				 ois.close();
		 }
		 return obj;
	 }
}
