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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 实现各种类型数据的比较操作，实现各种对象中属性值的比较操作 
 * @author biaoping.yin
 * @version 1.0
 */
public final class CompareUtil implements Serializable{
	
	
	
	
	/**	 
	 * Description: 比较两个对象的大小
	 * @param left 第一个对象
	 * @param right 第二个对象
	 * true：降序
	 * false:升序
	 * @return int 0 -表示两个对象相等
	 * 				-1-表示left比right小
	 * 				1 -表示left比right大
	 */
	public static int compareValue(Object left, Object right)
	{				
		return compareValue(left, right,true);
	}
	
	/**	 
	 * Description: 比较两个对象的大小
	 * @param left 第一个对象
	 * @param right 第二个对象
	 * @param desc 确定比较逻辑顺序
	 * 				true：降序
	 * 				false:升序
	 * @return int 0 -表示两个对象相等
	 * 				-1-表示left比right小
	 * 				1 -表示left比right大
	 */
	public static int compareValue(Object left, Object right,boolean desc) 
	{    	
		
		if(left == null && right == null)
			return 0;
		if(left == null && right != null)
			return lessThan(desc);
		if(left != null && right == null)
			return moreThan(desc);
		if(int.class.isInstance(left) || Integer.class.isInstance(left))
		{
			return compareInt(((Integer)left).intValue(),((Integer)right).intValue(),desc);
		}
	
		if(short.class.isInstance(left) || Short.class.isInstance(left))
		{
			return compareShort(((Short)left).shortValue(),((Short)right).shortValue(),desc);
		}
	
		if(char.class.isInstance(left) || Character.class.isInstance(left))
		{
			return compareChar(((Character)left).charValue(),((Character)right).charValue(),desc);
		}
		if(long.class.isInstance(left) || Long.class.isInstance(left))
		{
			return compareLong(((Long)left).longValue(),((Long)right).longValue(),desc);
		}
	
		if(double.class.isInstance(left) || Double.class.isInstance(left))
		{
			return compareDouble(((Double)left).doubleValue(),((Double)right).doubleValue(),desc);
		}		
	
		if(String.class.isInstance(left))
		{
			return compareString((String)left,(String)right,desc);
		}
	
		if(boolean.class.isInstance(left) || Boolean.class.isInstance(left))
		{
			return compareBoolean(((Boolean)left).booleanValue(),((Boolean)right).booleanValue(),desc);
		}	
	
		if(Date.class.isInstance(left))
		{
			return compareDate(((Date)left),((Date)right),desc);
		}	
		if(float.class.isInstance(left) || Float.class.isInstance(left))
		{
			return compareFloat(((Float)left).floatValue(),((Float)right).floatValue(),desc);
		} 
		if(BigDecimal.class.isInstance(left))
		{
			return compareBigDecimal(((BigDecimal)left),((BigDecimal)right),desc);
		}
		
		
		return 0;
	}
	
	private static int compareBigDecimal(BigDecimal bigDecimal,
			BigDecimal bigDecimal2, boolean desc) {
		int v = bigDecimal.compareTo(bigDecimal2);
		if(v < 0)
			return lessThan(desc);
		else if(v > 0)
			return moreThan(desc);
		else
			return 0;
		
		
	}

	/** 
	 * Description:比较两个布尔变量的大小
	 * @param left
	 * @param right
	 * @param desc 排序顺序，true降序，false升序
	 * @return
	 * int
	 */
	public static int compareBoolean(boolean left,boolean right,boolean desc)
	{
		if(left && right)
			return 0;
		else if(left && !right)
			return moreThan(desc);
		else if(!left && right)
			return lessThan(desc);
		else
			return 0;		
	}

	/**
	 * 比较两个字符串的大小
	 * @param left
	 * @param right
	 * @param desc 排序顺序，true降序，false升序
	 * @return int
	 */
	public static int compareString(String left, String right,boolean desc) 
	{
		int ret = left.compareTo(right);
		if(ret > 0)
			return moreThan(desc);
		else if(ret < 0)
			return lessThan(desc);
		else
			return 0;
	}

	/**
	 * 比较两个整数的大小
	 * @param left
	 * @param right
	 * @return int
	 */
	public static int compareInt(int left, int right,boolean desc) 
	{
		if(left < right)
			return lessThan(desc);
		else if(left == right)
			return 0;
		else
			return moreThan(desc);     	
	}

	/**
	 * 比较两个日期的大小
	 * @param left
	 * @param right
	 * @param desc 排序顺序，true降序，false升序 
	 * @return int
	 */
	public static int compareDate(Date left, Date right,boolean desc) 
	{
		int ret = left.compareTo(right);    	
		if(ret > 0)
			return moreThan(desc);
		else if(ret < 0)
			return lessThan(desc);
		else
			return ret;
	}

	/**
	 * 比较两个长整数的大小
	 * @param left
	 * @param right
	 * @param desc 排序顺序，true降序，false升序
	 * @return int
	 */
	public static int compareLong(long left, long right,boolean desc) 
	{
		if(left < right)
			return lessThan(desc);
		else if(left > right)
			return moreThan(desc);
		else
			return 0;
	}

	/**
	 * 比较两个短整数的大小
	 * @param left
	 * @param right
	 * @param desc 排序顺序，true降序，false升序
	 * @return int
	 */
	public static int compareShort(short left, short right,boolean desc) 
	{
		if(left < right)
			return lessThan(desc);
		else if(left > right)
			return moreThan(desc);
		else
			return 0;
	}

	/**
	 * 比较两个双精度数的大小
	 * @param left
	 * @param right
	 * @param desc 排序顺序，true降序，false升序
	 * @return int
	 */
	public static int compareDouble(double left, double right,boolean desc) 
	{
		if(left < right)
		   return lessThan(desc);
		else if(left > right)
		   return moreThan(desc);
		else
		   return 0;
	}

	/**
	 * 表较两个浮点数的大小
	 * @param left
	 * @param right
	 * @param desc 排序顺序，true降序，false升序
	 * @return int
	 */
	public static int compareFloat(float left, float right,boolean desc) 
	{
		if(left < right)
			return lessThan(desc);
		else if(left > right)
			return moreThan(desc);
		else
			return 0;
	}

	/**
	 * 比较两个字符的大小
	 * @param left
	 * @param right 
	 * @param desc 排序顺序，true降序，false升序
	 * @return int
	 */
	public static int compareChar(char left, char right,boolean desc) 
	{    	
		if(left < right)
			return lessThan(desc);
		else if(left > right)
			return moreThan(desc);
		else
			return 0;
	}

	

	/**
	 * Description:根据desc的值，处理比较结果为小于时的返回结果
	 * @param desc 排序顺序，true降序，false升序
	 * @return int
	 */
	private static int lessThan(boolean desc)
	{
		return desc?1:-1; 
	}
   
	/**
	 * Description:根据desc的值，处理比较结果为大于时的返回结果
     * @param desc 排序顺序，true降序，false升序
	 * @return
	 * int
	 */
	private static int moreThan(boolean desc)
	{
		return desc?-1:1;
	} 
	
	/** 
	 * Description:比较对象obj中字段field的值与compareValue的大小
	 * @param obj
	 * @param field
	 * @param compareValue
	 * @return
	 * int
	 */
	public static int compareField(Object obj,String field,Object compareValue)
	{				
		return compareField(obj,field,compareValue,true);				
	}
	
	/** 
	 * Description:比较对象obj中字段field的值与compareValue的大小
	 * @param obj
	 * @param field
	 * @param compareValue
	 * @param desc 排序顺序，true降序，false升序
	 * @return
	 * int
	 */
	public static int compareField(Object obj,String field,Object compareValue,boolean desc)
	{
		Object value = ValueObjectUtil.getValue(obj,field.trim());		
		return compareValue(value,compareValue,desc);				
	}
	
	/**
	 * Description:比较对象obj的字段field与对象obj1的字段field1的大小
	 * @param obj
	 * @param field
	 * @param obj1
	 * @param field1
	 * @param desc 排序顺序，true降序，false升序
	 * @return
	 * int
	 */
	public static int compareField(Object obj,String field,Object obj1,String field1,boolean desc)
	{
		Object value = ValueObjectUtil.getValue(obj,field.trim());		
		Object value1 = ValueObjectUtil.getValue(obj1,field1.trim());
		return compareValue(value,value1,desc);				
	}
	
	/**
	 * Description:比较对象obj的字段field与对象obj1的字段field1的大小
	 * @param obj
	 * @param field
	 * @param obj1
	 * @param field1	
	 * @return
	 * int
	 */
	public static int compareField(Object obj,String field,Object obj1,String field1)
	{
		
		return compareField(obj,field,obj1,field1,true);				
	}
}
