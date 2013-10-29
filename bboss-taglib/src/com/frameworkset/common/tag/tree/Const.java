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

package com.frameworkset.common.tag.tree;

import java.io.Serializable;

/**
 * 
 * @author biaoping.yin
 * created on 2005-3-13
 * version 1.0
 */
 
public class Const implements Serializable
{
	/**
	 * 定义结点类型
	 */

	public final static int NODE_ROOT = 1;//根结点
	public final static int NODE_DIRECTORY = 2;//目录结点
	public final static int NODE_FILE = 3;//文件结点

	public final static int NODE_SON = 4;//儿子结点
	public final static int NODE_LEAF = 5;//叶子结点

	public final static int PRODUCT_CLASS_TREE = 0; //产品分类树

	//public final static int SECURITY_MANAGER_TREE = 2; //安全管理树

	//public final static int SYSTEM_MANAGER_TREE = 3; //安全管理树

	public final static int MATERIAL_CLASS_TREE = 1; //物资分类树

	public final static int PRODUCTION_OILWORKSTREE_CLASS_TREE = 2; //生产采油厂分类树

}