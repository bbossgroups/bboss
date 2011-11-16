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
package com.frameworkset.common.tag.tree.itf;

import java.util.List;

/**
 * Instances of this interface wraps the <code>ITreeNode</code> instances, when
 * the TreeIterator iterates the tree, from within the TreeTag.
 *
 * <br/><br/>
 * This interface is primarily used by the TreeIterator and the TreeTag classes when
 * iterating the tree nodes. During normal work with the ITree interface and related
 * classes, you will not come across this interface.
 */
public interface ITreeIteratorElement {

    /**
     * Returns the node wrapped by this <code>ITreeIteratorElement</code> instance.
     * @return A <code>ITreeNode</code> instance.
     */
    public ITreeNode getNode();

    /**
     * Returns the id of the wrapped node.
     * @return The id of the wrapped node.
     */
    public String    getId();

    /**
     * Returns the name of the wrapped node
     * @return String
     */ 
    public String    getName();

    /**
     * Returns the child level of this node. The child level tells the indentation
     * tag how much this node should be indented. Thus, if a node is a child of the
     * root node, it's child level is 1.
     * @return The child level (indentation level) of this node.
     * @deprecated Use the getIndendationProfile() method instead.
     */
    public int       childLevel();

    /**
     * Returns the indentation profile of the wrapped node. The indendation profile
     * is a <code>List</code> of <code>Boolean</code> instances, containing one
     * instance for each ancestor of the wrapped node.
     * The <code>Boolean</code> instances tells whether that particular ancestor
     * was the last child of it's parent or not. If the ancestor was not the
     * last child of it's parent, a vertical line can be drawn from this ancestor
     * to it's next sibling. Else a blank space can be drawn.
     *
     * @return A <code>List</code> of <code>Boolean</code> instances, telling whether to
     * connect ancestors of the wrapped node, to succeding ancestor sibblings with vertical
     * lines.
     */
    public List      getIndendationProfile();


    /**
     * Returns true if the wrapped node is expanded, false if not.
     * @return True if the wrapped node is expanded, false if not.
     */
    public boolean   isExpanded();

    /**
     * Returns true if the wrapped node is selected, false if not.
     * @return True if the wrapped node is selected, false if not.
     */
    public boolean   isSelected();

    /**
     * Returns true if the wrapped node is the first child of it's parent,
     * false if not.
     * @return True if the wrapped node is the first child of it's parent,
     * false if not.
     */
    public boolean   isFirstChild();

    /**
     * Returns true if the wrapped node is the last child of it's parent,
     * false if not.
     * @return True if the wrapped node is the last child of it's parent,
     * false if not.
     */
    public boolean   isLastChild();
}
