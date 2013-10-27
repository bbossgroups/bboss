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
 *                                                                           *
 *****************************************************************************/
package com.frameworkset.common.tag.pager.tags;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * 导出变量
 * @author biaoping.yin
 * created on 2005-3-30
 * version 1.0
 */
public class PagerDataSetExtraInfo extends TagExtraInfo {
    public VariableInfo[] getVariableInfo(TagData tagData)
    {
        Object declareo = tagData.getAttribute("declare");//这个属性只有在标签上，才会有值
        
//        if(true)
//        	return null;
        boolean declare = declareo == null?true: new Boolean(declareo.toString().trim()).booleanValue();
//        if(!declare )
//        	return null;
  
//    boolean flag = true;
//    if(flag)
//    	throw new RuntimeException();

//        if(!id.equals(""))
//        {
//	        VariableInfo[] vinfo = new VariableInfo[] {new VariableInfo("dataSet_" + id,
//	                								                     PagerDataSet.class.getName(),
//																		true, VariableInfo.NESTED),
//														new VariableInfo("rowid_" + id,
//												              String.class.getName(),
//																true, VariableInfo.NESTED)
//											};
//	        return vinfo;
//        }
//        else
        {
        	String dataSetName = tagData.getAttributeString("dataSetName");
        	if(dataSetName == null)
        		dataSetName = "dataSet";
        	String rowidName = tagData.getAttributeString("rowidName");
        	if(rowidName == null)
        		rowidName = "rowid";

            VariableInfo[] vinfo = new VariableInfo[] {new VariableInfo(dataSetName,
	                PagerDataSet.class.getName(),
	                declare, VariableInfo.NESTED),
					new VariableInfo(rowidName,
			                String.class.getName(),
			                declare, VariableInfo.NESTED)};
	        return vinfo;
        }


    }

}
