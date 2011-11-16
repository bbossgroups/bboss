/*
 * Created on 2005-3-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.common.tag;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author biaoping.yin
 * created on 2005-3-30
 * version 1.0 
 */
public class TestTag extends BaseTag {
    
    public int doStartTag()
    {
        this.pageContext.setAttribute("data","testdata");
        return this.EVAL_BODY_INCLUDE;
    }
    
    public static void main(String[] args)
    {
        String n = "1233";
        String f = "##0.#####E0";
        NumberFormat numerFormat = new DecimalFormat(f);
        System.out.println(numerFormat.format(1233));
    }

}
