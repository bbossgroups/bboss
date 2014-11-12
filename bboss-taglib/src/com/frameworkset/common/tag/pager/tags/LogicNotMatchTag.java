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
package com.frameworkset.common.tag.pager.tags;


/**
 * 如果对象属性值与给定value值不匹配时输出本标签得内容，否则不输出
 * @author biaoping.ying
 * @version 1.0
 * 2004-9-14
 */
public class LogicNotMatchTag extends MatchTag {



	/**
	 *  Description: 不等于
	 * @return boolean
	 * @see com.frameworkset.common.tag.pager.tags.MatchTag#match()
	 */
//	protected boolean match() {
//		if(actualValue == null && getValue() == null)
//			return true;
//		if(actualValue == null || getValue() == null)
//			return true;
//		if(String.valueOf(actualValue).compareTo(String.valueOf(getValue())) != 0)
//			return true;
//		else
//			return false;
//	}
	protected boolean match() {
	
//		if(ValueObjectUtil.typecompare(actualValue,getValue()) != 0)
//			return true;
//		else
//			return false;
		
		return !equalCompare();
	}
	

}
