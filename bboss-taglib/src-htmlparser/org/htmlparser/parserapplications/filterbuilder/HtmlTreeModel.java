// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2005 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/parserapplications/filterbuilder/HtmlTreeModel.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/12 11:27:42 $
// $Revision: 1.2 $
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
//
                                                                                                  
package org.htmlparser.parserapplications.filterbuilder;

import java.util.Vector;

import javax.swing.tree.*;
import javax.swing.event.*;

import org.htmlparser.Node;
import org.htmlparser.tags.Html;
import org.htmlparser.util.NodeList;

/**
 * Quick and dirty tree model for HTML nodes.
 */
public class HtmlTreeModel implements TreeModel
{
    /**
     * The list of tree listeners.
     */
    protected Vector mTreeListeners;
    
    /**
     * The root {@link Node}.
     */
    protected Node mRoot;
    
    /**
     * Create an HTML tree view.
     * @param root The nodes at the root of the tree
     * (the nodes are wrapped in an Html node that is never seen
     * because it's the root, but this makes all downstream processing
     * super-simple because every tree node is then a {@link Node},
     * not sometimes a {@link NodeList} at the root).
     */
    public HtmlTreeModel (NodeList root)
    {
        mTreeListeners = new Vector ();
        // for simplicity we encapsulate the nodelist in a Html tag
        mRoot = new Html ();
        mRoot.setChildren (root);
    }    

    //
    // TreeModel interface
    //

    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     * @param l {@inheritDoc}
     */
    public void addTreeModelListener (TreeModelListener l)
    {
        synchronized (mTreeListeners)
        {
            if (!mTreeListeners.contains(l))
                mTreeListeners.addElement(l);
        }        
    }        

    /**
     * Removes a listener previously added with addTreeModelListener().
     * @param l {@inheritDoc}
     */
    public void removeTreeModelListener(TreeModelListener l)
    {
        synchronized (mTreeListeners)
        {
            mTreeListeners.removeElement (l);
        }    
    }

    /**
     * Returns the child of parent at index index in the parent's child array.
     * @param parent {@inheritDoc}
     * @param index {@inheritDoc}
     * @return {@inheritDoc}
     */
    public Object getChild (Object parent, int index)
    {
        Node node;
        NodeList list;
        Object ret;

        node = (Node)parent;
        list = node.getChildren ();
        if (null == list)
            throw new IllegalArgumentException ("invalid parent for getChild()");
        else
            ret = list.elementAt (index);
        
        return (ret);
    }

    /**
     * Returns the number of children of parent.
     * @param parent {@inheritDoc}
     * @return {@inheritDoc}
     */
    public int getChildCount (Object parent)
    {
        Node node;
        NodeList list;
        int ret;

        ret = 0;

        node = (Node)parent;
        list = node.getChildren ();
        if (null != list)
            ret = list.size ();
        
        return (ret);
    }


    /**
     * Returns the index of child in parent.
     * @param parent {@inheritDoc}
     * @param child {@inheritDoc}
     * @return {@inheritDoc}
     */
    public int getIndexOfChild (Object parent, Object child)
    {
        Node node;
        NodeList list;
        int count;
        int ret;

        ret = -1;

        node = (Node)parent;
        list = node.getChildren ();
        if (null != list)
        {
            count = list.size ();
            for (int i = 0; i < count; i++)
                if (child == list.elementAt (i))
                {
                    ret = i;
                	break;
                }
        }
        else
            throw new IllegalArgumentException ("invalid parent for getIndexOfChild()");

        if (0 > ret)
            throw new IllegalArgumentException ("child not found in getIndexOfChild()");

        return (ret);
    }

    /**
     * Returns the root of the tree.
     * @return {@inheritDoc}
     */
    public Object getRoot ()
    {
        return (mRoot);
    }    

    /**
     * Returns true if node is a leaf.
     * @param node {@inheritDoc}
     * @return {@inheritDoc}
     */
    public boolean isLeaf (Object node)
    {
        NodeList list;
        boolean ret;

        list = ((Node)node).getChildren ();
        if (null == list)
            ret = true;
        else
            ret = 0 == list.size ();

        return (ret);
    }    

    /**
     * Messaged when the user has altered the value for the item identified by path to newValue.
     * @param path {@inheritDoc}
     * @param newValue {@inheritDoc}
     */
    public void valueForPathChanged (TreePath path, Object newValue)
    {
        TreeModelEvent event;
        Vector v;

        event = new TreeModelEvent (this, path);
        synchronized (mTreeListeners)
        {
            v = (Vector)mTreeListeners.clone ();
        }
        
        for (int i = 0; i < v.size (); i++)
        {
            TreeModelListener listener = (TreeModelListener)v.elementAt (i);
            listener.treeStructureChanged (event);
        }
    }
}
