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

/**
 * @version $revision$
 * @author Jakob Jenkov
 */
package com.frameworkset.common.tag.tree.impl;

import java.util.Iterator;

import javax.servlet.jsp.JspException;

public class NodeIndentTag extends NodeBaseTag{

    protected Iterator  indentationProfileIterator   = null;
    protected String    indentationTypeAttributeName = "indentationType";

    public String getIndentationType() throws JspException{
        return this.indentationTypeAttributeName;
    }

    public void setIndentationType(String name){
        this.indentationTypeAttributeName = name;
    }




    public int doStartTag() throws JspException{
        this.indentationProfileIterator = getElement().getIndendationProfile().iterator();

        if(this.indentationProfileIterator.hasNext()){
            this.getHttpServletRequest().setAttribute(getIndentationType(),
                    this.indentationProfileIterator.next());
            return EVAL_BODY_INCLUDE;
        }
        return SKIP_BODY;
    }


    public int doAfterBody() throws JspException{
        if(this.indentationProfileIterator.hasNext()){
        	this.getHttpServletRequest().setAttribute(getIndentationType(),
                    this.indentationProfileIterator.next());

            return EVAL_BODY_AGAIN;
        }

        return SKIP_BODY;
    }
}
