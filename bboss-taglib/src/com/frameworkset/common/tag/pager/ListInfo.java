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

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 封装数据信息
 * items存放数据总数
 * datas存放所需的数据集合
 * @author biaoping.yin
 * 2005-3-25
 * version 1.0
 */

public class ListInfo extends com.frameworkset.util.ListInfo
{
    /**前台后台通用的分页数据封装对象*/
    private com.frameworkset.util.ListInfo listInfo;


    public ListInfo()
    {

    }

    public ListInfo(com.frameworkset.util.ListInfo listInfo)
    {
        this.listInfo = listInfo;
    }

    /**
     * Access method for the items property.
     *
     * @return   the current value of the items property
     */
    public int getItems()
    {
        return (int)super.getTotalSize();
    }

    /**
     * Sets the value of the items property.
     *
     * @param aItems the new value of the items property
     */
    public void setItems(int aItems)
    {
        if(listInfo == null)
            super.setTotalSize(aItems);
        else
            listInfo.setTotalSize(aItems);
    }

    /**
     * Access method for the datas property.
     *
     * @return   the current value of the datas property
     */
    public List getDatas()
    {
        if(listInfo == null)
            return super.getDatas();
        return listInfo.getDatas();
    }

    /**
     * Sets the value of the datas property.
     *
     * @param aDatas the new value of the datas property
     */
    public void setDatas(List aDatas)
    {
        if(listInfo == null)
            super.setDatas(aDatas);
        else
            listInfo.setDatas(aDatas);
    }

    public String toString()
    {
        return "ListInfo:"+ this.getClass() +
                "\r\ndatas:"
                + (listInfo == null ? super.getDatas():listInfo.getDatas())
                + "\r\ntotalSize:"
                + (listInfo == null ? super.getTotalSize():listInfo.getTotalSize());
    }
    /**
     * @return Returns the dbDatas.
     */
    public Object[] getArrayDatas() {
        return super.getArrayDatas();
    }
    /**
     * @param dbDatas The dbDatas to set.
     */
    public void setArrayDatas(Object[] dbDatas) {
        super.setArrayDatas(dbDatas);
    }
}
