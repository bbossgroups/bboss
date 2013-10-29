/*****************************************************************************
 *                                                                           *
 *  This file is part of the frameworkset distribution.                      *
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
 *  Code is biaoping.yin. Portions created by biaoping.yin are Copyright     *
 *  (C) 2004.  All Rights Reserved.                                          *
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
 * 一个元数据只能在元模型中添加一次，因此通过本抽象类来控制确保所有的元数据只添加一次
 * @author biaoping.yin
 * created on 2005-5-27
 * version 1.0 
 */
public abstract class UniqueHelper implements ModelObject{
    /**
     * 控制变量如果已经添加到元模型中，则不添加
     * true，已经添加，
     * false，未添加
     */
    protected boolean hasAdded = false; 

    /**
     * @return Returns the hasAdded.
     */
    public boolean isHasAdded() {
        return hasAdded;
    }
    /**
     * @param hasAdded The hasAdded to set.
     */
    public void setHasAdded(boolean hasAdded) {
        this.hasAdded = hasAdded;
    }
}
