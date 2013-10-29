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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.frameworkset.common.tag.sort.ListSort;
/**
 * 包装java.util.ArrayList类，限定只能容纳ClassData类型的对象
 * 
 * 提供对所有的ClassData对象的排序功能 
 * @author biaoping.yin
 * 2005-3-25
 * version 1.0
 */
 
 
public class ClassDataList  
{
	ListSort sort = new ListSort();    
    /**
     * 容纳ClassData对象的List     */
    
    public List theClassData = new ArrayList();    
    
    public ClassDataList() 
    {
		//loca.createRegistry(1190);		
    }    
    
    /**
     * @param classData
     */
    public void add(ClassData classData) 
    {
		theClassData.add(classData);
    }
    
    /**
     * @param index - 获取一条记录
   
     */
    public ClassData get(int index) 
    {
     	return (ClassData)theClassData.get(index);
    }
    
    /**
     * @return int
     */
    public int size() 
    {
     	return theClassData.size();
    }
    
    /**
     * @return boolean
     */
    public boolean isEmpty() 
    {
     	return theClassData.isEmpty();
    }
    
    /**
     * @param arg0
     * @return boolean
     */
    public boolean contains(ClassData arg0) 
    {
     	return theClassData.contains(arg0);
    }
    
    /**
     * @return Iterator
     */
    public Iterator iterator() 
    {
     	return theClassData.iterator();
    }
    ClassData[] arrays = null;
    /**
     * @return Object[]
     */
    public ClassData[] toArray() 
    {
    	if(arrays == null)
    	{
    		synchronized(arrays)
    		{
    			if(arrays != null)
    				return arrays;

	    		ClassData[] ret = new ClassData[this.theClassData.size()];
	    		for(int i = 0; i < ret.length; i ++)
	    		{
	    			ret[i] = (ClassData)theClassData.get(i);
	    		}
	    		arrays = ret;
    		}
    		
    		
    	}
		
		   	
		return arrays;
     	
    }
    
    /**
     * @param arg0
     * @return Object[]
     */
    public ClassData[] toArray(Object[] arg0) 
    {
    	Object[] temp = theClassData.toArray(arg0);
    	ClassData[] ret = new ClassData[temp.length];
    	for(int i = 0; i < temp.length; i ++)
    	{
    		ret[i] = (ClassData)temp[i];
    	}    	
     	return ret; 
    }
    
    /**
     * @param arg0
     * @return boolean
     */
    public boolean remove(ClassData arg0) 
    {
     	return theClassData.remove(arg0);
    }
    
    /**
     * @param arg0
     * @return boolean
     */
    public boolean containsAll(Collection arg0) 
    {
     	return theClassData.containsAll(arg0);
    }
    
    /**
     * @param arg0
     * @return boolean
     */
    public boolean addAll(Collection arg0) 
    {
     	return theClassData.addAll(arg0);
    }
    
    /**
     * @param arg0
     * @param arg1
     * @return boolean
     */
    public boolean addAll(int arg0, Collection arg1) 
    {
     	return theClassData.addAll(arg0,arg1);
    }
    
    /**
     * @param arg0
     * @return boolean
     */
    public boolean removeAll(Collection arg0) 
    {
     	return theClassData.removeAll(arg0);
    }
    
    /**
     * @param arg0
     * @return boolean
     */
    public boolean retainAll(Collection arg0) 
    {
     	return theClassData.retainAll(arg0);
    }
    
    public void clear() 
    {
		theClassData.clear();
    }
    
    /**
     * @param arg0
     * @return boolean
     */
    public boolean equals(Object arg0) 
    {
     	return theClassData.equals(arg0);
    }
    
    /**
     * @return int
     */
    public int hashCode() 
    {
     	return theClassData.hashCode();
    }
    
    /**
     * @param arg0
     * @param arg1
     */
    public void add(int arg0, ClassData arg1) 
    {
		theClassData.add(arg0,arg1);
    }
    
    /**
     * @param arg0
     * @param arg1
     * @return com.westerasoft.khcerp.tag.pager.ClassData
     */
    public ClassData set(int arg0, ClassData arg1) 
    {
     	return (ClassData)theClassData.set(arg0,arg1);
    }
    
    /**
     * @param arg0
     * @return ClassObject
     */
    public ClassData remove(int arg0) 
    {
     	return (ClassData)theClassData.remove(arg0);
    }
    
    /**
     * @param arg0
     * @return int
     */
    public int indexOf(ClassData arg0) 
    {
     	return theClassData.indexOf(arg0);
    }
    
    /**
     * @param arg0
     * @return int
     */
    public int lastIndexOf(ClassData arg0) 
    {
     	return theClassData.lastIndexOf(arg0);
    }
    
    /**
     * @return java.util.ListIterator
     */
    public ListIterator listIterator() 
    {
     return theClassData.listIterator();
    }
    
    /**
     * @param arg0
     * @return java.util.ListIterator
     */
    public ListIterator listIterator(int arg0) 
    {
     return theClassData.listIterator(arg0);
    }
    
    /**
     * @param arg0
     * @param arg1
     * @return List
     */
    public List subList(int arg0, int arg1) 
    {
     	return theClassData.subList(arg0,arg1);
    }
    
    
    /**
     * 对list中的对象排序，方法中创建ListSort的实例lsort，调用lsort.sort(list,sortKey,d
     * esc)完成排序
     * desc参数决定排序的秩序：
     * true :降序
     * false:升序
     * 
     * sortKey参数：排序字段
     * 
     * @param sortKey - 排序关键字
     * @param desc
     * @return void
     */
    public void sortBy(String sortKey, boolean desc) 
    {
		sort.sort(theClassData,sortKey,desc);    	
    }
}
