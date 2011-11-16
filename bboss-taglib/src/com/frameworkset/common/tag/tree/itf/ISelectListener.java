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


package com.frameworkset.common.tag.tree.itf;

import java.io.Serializable;


/**
 * Select listeners are used to listen for selected nodes in the tree. Whenever a node
 * is selected (the ITree.select(...) method is called), all select listeners are notified.
 *
 * @author Jakob Jenkov,  Jenkov Development
 */
public interface ISelectListener  extends Serializable{

    /**
     * This method is called whenever a node is selected in the tree
     * @param node The node that was selected
     * @param tree The tree this node was selected in.
     */
    public void nodeSelected(ITreeNode node, ITree tree,boolean needNotifier);
}
