/*
 * Created on 2005-3-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.common.tag;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * @author biaoping.yin
 * created on 2005-3-30
 * version 1.0 
 */
public class TestTagExtraInfo extends TagExtraInfo {
    public VariableInfo[] getVariableInfo(TagData tagData)
    {        
        
//        VariableInfo(name,
//				java.lang.Integer.class.getName(),
//				true, VariableInfo.NESTED)
        VariableInfo[] vinfo = new VariableInfo[] {new VariableInfo("data",
				java.lang.String.class.getName(),
				false, VariableInfo.AT_END)};
        return vinfo;
    }
}
