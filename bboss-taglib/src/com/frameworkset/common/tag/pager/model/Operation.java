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
package com.frameworkset.common.tag.pager.model;

/**
 *
 * @author biaoping.yin
 * created on 2005-5-21
 * version 1.0
 */
public class Operation implements ModelObject{
	/**
	 * 基本操作，加（+）、减(-)、乘(*)、整除(/)、求余(%)
	 */
	public final static int ADD = 0;
	public final static String ADD_TOKEN = "+";
	public final static int SUB = 1;
	public final static String SUB_TOKEN = "-";
	public final static int MUTI = 2;
	public final static String MUTI_TOKEN = "*";
	public final static int DIV = 3;
	public final static String DIV_TOKEN = "/";
	public final static int MOD = 4;
	public final static String MOD_TOKEN = "%";


	/**
	 * 聚合操作:求和（sum），计数（count）
	 */
	public final static int SUM = 5;
	public final static String SUM_TOKEN = "sum";

	public final static int COUNT = 6;
	public final static String COUNT_TOKEN = "count";

	/**
	 * 从左到右顺序执行
	 * ()优先级为0 最高
	 * sum,count优先级为1次高
	 * *、/、%优先级为2，次高
	 */











}
