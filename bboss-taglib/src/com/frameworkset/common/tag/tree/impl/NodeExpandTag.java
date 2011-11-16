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

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.tree.itf.ITreeIteratorElement;

/**
 *@deprecated Use NodeMatchTag instead.
 */
public class NodeExpandTag extends NodeBaseTag{

    protected String target = null;
    protected String expandParam = null;

    public String getTarget(){
        return this.target;
    }

    public void setTarget(String target){
        this.target = target;
    }

    public String getExpandParam(){
        if(this.expandParam == null){
            return "expand";
        }
        return this.expandParam;
    }

    public boolean hasExpandHandle(ITreeIteratorElement element){
       return (!element.isExpanded()) && (element.getNode().hasChildren());
    }

    public int doStartTag() throws JspException{
        ITreeIteratorElement element = getElement();
        if(hasExpandHandle(element)){
            write("<a href=\"");
            write(getTarget());
            write("?");
            write(getExpandParam());
            write("=");
            write(element.getNode().getId());
            write("\">");
            return EVAL_BODY_INCLUDE;
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException{
        ITreeIteratorElement element = getElement();
        if(hasExpandHandle(element)){
            write("</a>");
        }
        return SKIP_BODY;
    }
  }
