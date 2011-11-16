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

import java.util.List;

import com.frameworkset.common.tag.tree.itf.ITreeIteratorElement;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

/**
 * 
 * @author biaoping.yin 
 * @date 2006-12-19 8:54:08
 * @version v1.0
 * @company chinacreator.com
 */
public class TreeIteratorElement implements ITreeIteratorElement,java.io.Serializable {

    protected ITreeNode node    = null;
    protected List      indentationProfile = null;
    protected boolean   isExpanded = false;
    protected boolean   isSelected = false;
    protected boolean   isFirstChild = false;
    protected boolean   isLastChild  = false;
    /**
     * @deprecated
     */
    protected int       level   = 0 ;

    /**
     * @deprecated
     * @param node
     * @param level
     * @param isExpanded
     * @param isSelected
     * @param isFirstChild
     * @param isLastChild
     */
    public TreeIteratorElement(ITreeNode node, int level,
                               boolean isExpanded  , boolean isSelected,
                               boolean isFirstChild, boolean isLastChild){
        this.node = node;
        this.level= level;
        this.isExpanded   = isExpanded;
        this.isSelected   = isSelected;
        this.isFirstChild = isFirstChild;
        this.isLastChild  = isLastChild;
    }

    public TreeIteratorElement(ITreeNode node, List indentationProfile,
                               boolean isExpanded  , boolean isSelected,
                               boolean isFirstChild, boolean isLastChild){
        this.node = node;
        this.indentationProfile = indentationProfile;
        this.isExpanded   = isExpanded;
        this.isSelected   = isSelected;
        this.isFirstChild = isFirstChild;
        this.isLastChild  = isLastChild;
    }

    public ITreeNode getNode() {
        return this.node;
    }

    public String getId() {
        return getNode().getId();
    }

    public String getName() {
        return getNode().getName();
    }

    public int childLevel() {
        return this.level;
    }

    public List getIndendationProfile() {
        return this.indentationProfile;
    }

    public boolean isExpanded(){
        return this.isExpanded;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public boolean isFirstChild() {
        return this.isFirstChild;
    }

    public boolean isLastChild() {
        return this.isLastChild;
    }
}
