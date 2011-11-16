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

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.frameworkset.common.tag.BaseTag;
import com.frameworkset.common.tag.tree.itf.ITreeIteratorElement;

public class NodeBaseTag extends BaseTag {

    protected String node = null;

     public String getNode(){
         return this.node;
     }

     public void setNode(String node){
         this.node = node;
     }

     protected void validateAttributes() throws JspException{
         if(getNode() == null)
             throw new JspException("Attribute node must not be null");
     }

     protected ITreeIteratorElement getElement() throws JspException{
         validateAttributes();
         ITreeIteratorElement element = null;
         if(this.getSession() != null )
        	 element = (ITreeIteratorElement) this.getSession().getAttribute(getNode());
         if(element == null){
             throw new JspException("Node retrieved from session was null");
         }
         return element;
     }

     protected void write(String text) throws JspException{
        try{
            this.getJspWriter().write(text);
        } catch (IOException e) {
            throw new JspException("Could not write to JspWriter", e);
        }
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
