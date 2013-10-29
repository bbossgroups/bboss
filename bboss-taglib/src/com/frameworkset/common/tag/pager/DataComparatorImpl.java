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
package com.frameworkset.common.tag.pager;

import java.io.Serializable;
import java.util.Comparator;

import com.frameworkset.util.CompareUtil;



/**
 * 继承java.util.Comparator接口，针对ClassData的，根据desc的值来确定排序的顺序，根?
 * sortKey来确定排序的字段 
 * @author biaoping.yin
 * 2005-3-25
 * version 1.0
 */
 
public class DataComparatorImpl implements Comparator ,Serializable
{
    
    /**
     * 决定排序规则
     * false:升序
     * true :降序
     */
    private boolean desc = true;	
    
    /**
     * 排序关键字，为具体Field对象的名称
     */
    private String sortKey = "";
    private static DataComparatorImpl dataComparatorImpl;
    
    /**
     * 构建器，参数sortKey为排序的关键字
     */
    private DataComparatorImpl() 
    {
     
    }
    
    /**
     * Determines if the desc property is true.
     * 
     * @return   <code>true<code> if the desc property is true
     */
    public boolean getDesc() 
    {
        return desc;     
    }
    
    /**
     * Sets the value of the desc property.
     * 
     * @param aDesc the new value of the desc property
     */
    public void setDesc(boolean aDesc) 
    {
        desc = aDesc;     
    }
    
    /**
     * Access method for the sortKey property.
     * 
     * @return   the current value of the sortKey property
     */
    public String getSortKey() 
    {
        return sortKey;     
    }
    
    /**
     * Sets the value of the sortKey property.
     * 
     * @param aSortKey the new value of the sortKey property
     */
    public void setSortKey(String aSortKey) 
    {
        sortKey = aSortKey;     
    }    
    

    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.
     * @param left
     * @param right
     * @return int
     */
    private int compareValue(Object left, Object right) 
    {	
    	return CompareUtil.compareValue(left, right,desc);
    }    
    
    /**
     * @return com.westerasoft.khcerp.tag.pager.DataComparatorImpl
     */
    public static DataComparatorImpl newInstance() 
    {
    	if(dataComparatorImpl == null)
			dataComparatorImpl = new DataComparatorImpl();
     	return dataComparatorImpl;
    }
    
    /**
     * @param arg0
     * @param arg1
     * @return int
     */
    public int compare(Object arg0, Object arg1) 
    {
		ClassData tleft = (ClassData)arg0;		
		ClassData tright = (ClassData)arg1;		
		//获取值对象中field名称为sortKey的值
		Object lvalue =  tleft.getValue(sortKey);
		Object rvalue =  tright.getValue(sortKey);		
		return compareValue(lvalue,rvalue);	
     	//return 0;
    }
    
      
    
    
    
    /**
     * 重置sortKey和desc的值
     */
    public void reset() 
    {
     	this.desc 	 = false;
     	this.sortKey = "";     	
    }
}
