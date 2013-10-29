// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2005 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/parserapplications/filterbuilder/wrappers/NodeClassFilterWrapper.java,v $
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

package org.htmlparser.parserapplications.filterbuilder.wrappers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JComboBox;
//import javax.swing.event.DocumentEvent;
//import javax.swing.event.DocumentListener;
//import javax.swing.text.BadLocationException;
//import javax.swing.text.Document;

import org.htmlparser.Node;
import org.htmlparser.NodeFactory;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.Tag;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.parserapplications.filterbuilder.Filter;

/**
 * Wrapper for NodeClassFilters.
 */
public class NodeClassFilterWrapper
    extends
        Filter
    implements
        ActionListener
//        ,
//        DocumentListener
{
    /**
     * The underlying filter.
     */
    protected NodeClassFilter mFilter;

    /**
     * Combo box for strategy.
     */
    protected JComboBox mClass;

    /**
     * Create a wrapper over a new NodeClassFilter.
     */ 
    public NodeClassFilterWrapper ()
    {
        mFilter = new NodeClassFilter ();

        // add the strategy choice
        mClass = new JComboBox ();
        mClass.addItem ("");
        add (mClass);
        mClass.addActionListener (this);
    }

    //
    // Filter overrides and concrete implementations
    //

    /**
     * Get the name of the filter.
     * @return A descriptive name for the filter.
     */
    public String getDescription ()
    {
        return ("Nodes of class");
    }

    /**
     * Get the resource name for the icon.
     * @return The icon resource specification.
     */
    public String getIconSpec ()
    {
        return ("images/NodeClassFilter.gif");
    }

    /**
     * Get the underlying node filter object.
     * @return The node filter object suitable for serialization.
     */
    public NodeFilter getNodeFilter ()
    {
        NodeClassFilter ret;
        
        ret = new NodeClassFilter ();
        ret.setMatchClass (mFilter.getMatchClass ());
            
        return (ret);
    }

    /**
     * Assign the underlying node filter for this wrapper.
     * @param filter The filter to wrap.
     * @param context The parser to use for conditioning this filter.
     * Some filters need contextual information to provide to the user,
     * i.e. for tag names or attribute names or values,
     * so the Parser context is provided. 
     */
    public void setNodeFilter (NodeFilter filter, Parser context)
    {
        NodeFactory factory;
        PrototypicalNodeFactory proto;
        Set names;
        String name;
        Tag tag;

        mFilter = (NodeClassFilter)filter;

        factory = context.getNodeFactory ();
        if (factory instanceof PrototypicalNodeFactory)
        {
            proto = (PrototypicalNodeFactory)factory;
            // iterate over the classes
            names = proto.getTagNames ();
            for (Iterator iterator = names.iterator (); iterator.hasNext (); )
            {
                name = (String)iterator.next ();
                tag = proto.get (name);
                mClass.addItem (tag.getClass ().getName ());
            }
        }
        mClass.setSelectedItem (mFilter.getMatchClass ().getName ());
    }

    /**
     * Get the underlying node filter's subordinate filters.
     * @return The node filter object's contained filters.
     */
    public NodeFilter[] getSubNodeFilters ()
    {
        return (new NodeFilter[0]);
    }

    /**
     * Assign the underlying node filter's subordinate filters.
     * @param filters The filters to insert into the underlying node filter.
     */
    public void setSubNodeFilters (NodeFilter[] filters)
    {
        // should we complain?
    }

    /**
     * Convert this filter into Java code.
     * Output whatever text necessary and return the variable name.
     * @param out The output buffer.
     * @param context Three integers as follows:
     * <li>indent level - the number of spaces to insert at the beginning of each line</li>
     * <li>filter number - the next available filter number</li>
     * <li>filter array number - the next available array of filters number</li>
     * @return The variable name to use when referencing this filter (usually "filter" + context[1]++) 
     */
    public String toJavaCode (StringBuffer out, int[] context)
    {
        String ret;

        ret = "filter" + context[1]++;
        spaces (out, context[0]);
        out.append ("NodeClassFilter ");
        out.append (ret);
        out.append (" = new NodeClassFilter ();");
        newline (out);
        spaces (out, context[0]);
        out.append ("try { ");
        out.append (ret);
        out.append (".setMatchClass (Class.forName (\"");
        out.append (mFilter.getMatchClass ().getName ());
        out.append ("\")); } catch (ClassNotFoundException cnfe) { cnfe.printStackTrace (); }");
        newline (out);
        
        return (ret);
    }

    //
    // NodeFilter interface
    //

    /**
     * Predicate to determine whether or not to keep the given node.
     * The behaviour based on this outcome is determined by the context
     * in which it is called. It may lead to the node being added to a list
     * or printed out. See the calling routine for details.
     * @return <code>true</code> if the node is to be kept, <code>false</code>
     * if it is to be discarded.
     * @param node The node to test.
     */
    public boolean accept (Node node)
    {
        return (mFilter.accept (node));
    }

    //
    // ActionListener interface
    //

    /**
     * Invoked when an action occurs on the combo box.
     * @param event Details about the action event.
     */
    public void actionPerformed (ActionEvent event)
    {
        Object source;

        source = event.getSource ();
        if (source == mClass)
            try
            {
                mFilter.setMatchClass (Class.forName ((String)mClass.getSelectedItem ()));
            }
            catch (ClassNotFoundException cnfe)
            {
                cnfe.printStackTrace ();
            }
    }

//    //
//    // DocumentListener interface
//    //
//
//    public void insertUpdate (DocumentEvent e)
//    {
//        Document doc;
//        
//        doc = e.getDocument ();
//        try
//        {
//            mFilter.setPattern (doc.getText (0, doc.getLength ()));
//        }
//        catch (BadLocationException ble)
//        {
//            ble.printStackTrace ();
//        }
//    }
//
//    public void removeUpdate (DocumentEvent e)
//    {
//        Document doc;
//        
//        doc = e.getDocument ();
//        try
//        {
//            mFilter.setPattern (doc.getText (0, doc.getLength ()));
//        }
//        catch (BadLocationException ble)
//        {
//            ble.printStackTrace ();
//        }
//    }
//
//    public void changedUpdate (DocumentEvent e)
//    {
//        // plain text components don't fire these events
//    }
}
