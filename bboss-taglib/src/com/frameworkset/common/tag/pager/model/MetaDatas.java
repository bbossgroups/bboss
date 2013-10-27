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

import java.util.ArrayList;
import java.util.List;

/**
 * 定义页面上要显示的所有字段域
 * @author biaoping.yin
 * created on 2005-5-25
 * version 1.0
 */
public class MetaDatas extends UniqueHelper implements ModelObject{
    private List metafields = new ArrayList();
    private List dataModels = new ArrayList();
    
    /**
     * 当输出无记录时，提供无记录的提示信息
     */
    private Notification notification;

    /**
     * 添加页面的显示字段
     * @param field
     */
    public void addMetaField(Field field)
    {
        metafields.add(field);
    }

    public Field getMetaField(String fieldName)
    	throws FieldException
    {

        Field field = null;
        for(int i = 0; i < metafields.size(); i ++)
        {
            field = (Field)metafields.get(i);
            if(field.getField().equals(fieldName))
                return field;

        }
        throw new FieldException("fieldName：" + fieldName + " not found!");

    }

    public DataModel addDataModel(DataModel dataSet)
    {
        dataModels.add(dataSet);
        return dataSet;
    }

    public DataModel getDataModel(String fieldName)
    	throws DataModelException
    {
        DataModel field = null;
        for(int i = 0; i < metafields.size(); i ++)
        {
            field = (DataModel)metafields.get(i);
            if(field.getField().equals(fieldName))
                return field;

        }
        throw new DataModelException("DataModel：" + fieldName + " not found!");

    }

    /**
     * 判断是否有dataSet模型
     * @return boolean
     */
    public boolean hasDataModel()
    {
        if(dataModels == null )
            return false;
        return dataModels.size() != 0;
    }

    /**
     * 判断是否有字段元数据
     * @return boolean
     */
    public boolean hasMetaField()
    {
        if(metafields == null )
            return false;
        return metafields.size() != 0;
    }


    /**
     * @return Returns the notification.
     */
    public Notification getNotification() {
        return notification;
    }
    /**
     * @param notification The notification to set.
     */
    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
