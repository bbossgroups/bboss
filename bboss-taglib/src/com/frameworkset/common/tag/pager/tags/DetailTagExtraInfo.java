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
package com.frameworkset.common.tag.pager.tags;

import java.io.Serializable;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 *
 * @author biaoping.yin
 * created on 2005.03.30
 * version 1.0
 */
public class DetailTagExtraInfo extends TagExtraInfo implements Serializable {
	public VariableInfo[] getVariableInfo(TagData tagData)
    {
        Object declareo = tagData.getAttribute("declare");

    	boolean declare = declareo == null?true: new Boolean(declareo.toString().trim()).booleanValue();
    	String beaninfoName= tagData.getAttributeString("beaninfoName");
    	if(beaninfoName == null || beaninfoName.equals(""))
    		beaninfoName = "beaninfo";
        //String index = (String)tagData.getAttribute("index");
    	//int id = ((Integer)tagData.getAttribute("id")).intValue();
//    	String id = (String)tagData.getAttribute("id");
//    	if(id == null)
//    	    id = "";
//        id = id.trim();
        {
	//        VariableInfo(name,
	//				java.lang.Integer.class.getName(),
	//				true, VariableInfo.NESTED)
//	        VariableInfo[] vinfo = new VariableInfo[] {new VariableInfo("beaninfo" + (id.equals("")?"":("_" + id)),
            VariableInfo[] vinfo = new VariableInfo[] {new VariableInfo(beaninfoName,
	                DetailTag.class.getName(),
	                declare, VariableInfo.NESTED)};
//					,
//					new VariableInfo("rowid_" + id,
//			                String.class.getName(),
//							true, VariableInfo.NESTED)};
	        return vinfo;
        }

//        {
//            VariableInfo[] vinfo = new VariableInfo[] {new VariableInfo("dataSet",
//                    PagerDataSet.class.getName(),
//					true, VariableInfo.NESTED),
//					new VariableInfo("rowid",
//			                String.class.getName(),
//							true, VariableInfo.NESTED)};
//	        return vinfo;
//        }
    }


}
