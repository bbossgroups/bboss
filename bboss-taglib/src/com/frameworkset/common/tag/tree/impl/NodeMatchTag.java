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

public class NodeMatchTag extends NodeBaseTag{

    protected String type        = null;
    protected String name        = null;
    protected String id          = null;
    protected String expanded    = null;
    protected String selected    = null;
    protected String hasChildren = null;
    protected String isFirstChild= null;
    protected String isLastChild = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType(){
        return this.type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getExpanded(){
        return this.expanded;
    }

    public void setExpanded(String expanded){
        this.expanded = expanded;
    }

    public String getSelected() {
          return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(String hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getIsFirstChild(){
        return this.isFirstChild;
    }

    public void setIsFirstChild(String isFirstChild){
        this.isFirstChild = isFirstChild;
    }

    public String getIsLastChild(){
        return this.isLastChild;
    }

    public void setIsLastChild(String isLastChild){
        this.isLastChild = isLastChild;
    }

    public boolean attributeNotSet(String attributeValue){
        return attributeValue == null;
    }
    public boolean valueNotSet(String attributeValue){
        return attributeValue == null;
    }

    protected boolean matchesWithWildCard(String attribute, String value){
        int index = attribute.indexOf("*");
        if(index == -1) return false;
        if(attribute.equals("*")) return true;

        if(attribute.startsWith("*")){
            if(value.endsWith(attribute.substring(1, attribute.length()))) return true;
        }
        if(attribute.endsWith("*")){
            if(value.startsWith(attribute.substring(0,attribute.length() -1))) return true;
        }
        String start = attribute.substring(0,index);
        String end   = attribute.substring(index + 1, attribute.length());
        if(value.startsWith(start) && value.endsWith(end)) return true;

        return false;
    }

    protected boolean matches(String attribute, String value){
        if(attributeNotSet(attribute)) return true;
        if(valueNotSet(value))         return false;
        if(attribute.equals(value))    return true;
        if(matchesWithWildCard(attribute, value)) return true;
        return false;
    }

    protected boolean matchesBoolean(String attribute, boolean booleanValue) throws JspException{
        if(attribute == null) return true;
        if(!attribute.equals("true") && !attribute.equals("false")){
            throw new JspException("boolean values must be either true or false (lower case only)");
        }
        if(attribute.equals("true")  && booleanValue == true)   return true;
        if(attribute.equals("false") && booleanValue == false) return true;

        return false;
    }

    public int doStartTag() throws JspException{
        ITreeIteratorElement element = getElement();
        if(element == null) throw new JspException("null element");
        if(element.getNode() == null) throw new JspException("null node");
        if(element.getNode().getType() == null) throw new JspException("null type");

        if(!matches(getType(), element.getNode().getType()))    return SKIP_BODY;
        if(!matches(getId()  , element.getNode().getId()))      return SKIP_BODY;
        if(!matches(getName(), element.getNode().getName()))    return SKIP_BODY;

        if(!matchesBoolean(getExpanded()    , element.isExpanded()) )  return SKIP_BODY;
        if(!matchesBoolean(getSelected()    , element.isSelected()) )  return SKIP_BODY;
        if(!matchesBoolean(getHasChildren() , element.getNode().hasChildren()) ) return SKIP_BODY;
        if(!matchesBoolean(getIsFirstChild(), element.isFirstChild())){ return SKIP_BODY;}
        if(!matchesBoolean(getIsLastChild() , element.isLastChild())) { return SKIP_BODY;}


        return EVAL_BODY_INCLUDE;
    }
}
