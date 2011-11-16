 /*
    The Jenkov JSP Tree Tag provides extra tasks for Apaches Ant build tool

    Copyright (C) 2003 Jenkov Development

    Jenkov JSP Tree Tag is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    Jenkov JSP Tree Tag is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


    Contact Details:
    ---------------------------------
    Company:     Jenkov Development - www.jenkov.com
    Project URL: http://www.jenkov.dk/projects/treetag/treetag.jsp
    Email:       info@jenkov.com
 */

package com.frameworkset.common.tag.tree.impl;

import java.io.OutputStream;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import com.frameworkset.common.tag.BaseTag;

/**
 * @author Jakob Jenkov,  Jenkov Development
 */
public class NodeIndentBaseTag extends BaseTag{

    protected String indentationTypeAttributeName = "indentationType";

    public String getIndentationType() throws JspException{
        return this.indentationTypeAttributeName;
    }

    public void setIndentationType(String name){
        this.indentationTypeAttributeName = name;
    }

    protected boolean getIndentationTypeAsBoolean() throws JspException{
        Boolean type = (Boolean)
                this.getHttpServletRequest().getAttribute(this.indentationTypeAttributeName);

        if(type == null) throw new JspException("No indentation type found for name: "
            + this.indentationTypeAttributeName);

        return type.booleanValue();
    }

	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.BaseTag#generateContent()
	 */
	public String generateContent() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.BaseTag#write(java.io.OutputStream)
	 */
	public void write(OutputStream output) {
		// TODO Auto-generated method stub
		
	}
}
