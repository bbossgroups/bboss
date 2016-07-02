// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2005 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/parserapplications/filterbuilder/wrappers/StringFilterWrapper.java,v $
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
import java.util.Locale;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.StringFilter;
import org.htmlparser.parserapplications.filterbuilder.Filter;

/**
 * Wrapper for StringFilters.
 */
public class StringFilterWrapper
    extends
        Filter
    implements
        ActionListener,
        DocumentListener,
        Runnable
{
    /**
     * The underlying filter.
     */
    protected StringFilter mFilter;

    /**
     * Text to check for.
     */
    protected JTextArea mPattern;

    /**
     * The check box for case sensitivity.
     */
    protected JCheckBox mCaseSensitivity;

    /**
     * Combo box for locale.
     */
    protected JComboBox mLocale;

    /**
     * Cached locales.
     */
    protected static Locale[] mLocales = null;

    /**
     * Create a wrapper over a new StringFilter.
     */ 
    public StringFilterWrapper ()
    {
        Thread thread;

        mFilter = new StringFilter ();
        mFilter.setCaseSensitive (true);

        // add the text pattern
        mPattern = new JTextArea (2, 20);
        mPattern.setBorder (new BevelBorder (BevelBorder.LOWERED));
        add (mPattern);
        mPattern.getDocument ().addDocumentListener (this);
        mPattern.setText (mFilter.getPattern ());

        // add the case sensitivity flag
        mCaseSensitivity = new JCheckBox ("Case Sensitive");
        add (mCaseSensitivity);
        mCaseSensitivity.addActionListener (this);
        mCaseSensitivity.setSelected (mFilter.getCaseSensitive ());

        // add the locales choice
        mLocale = new JComboBox ();
        synchronized (mLocale)
        {
            mLocale.addItem (mFilter.getLocale ().getDisplayName ());
            thread = new Thread (this);
            thread.setName  ("locale_getter");
            thread.setPriority (Thread.MIN_PRIORITY);
            thread.run ();
        }
        add (mLocale);
        mLocale.addActionListener (this);
        mLocale.setSelectedIndex (0);
        mLocale.setVisible (!mFilter.getCaseSensitive ());
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
        return ("Nodes containing string");
    }

    /**
     * Get the resource name for the icon.
     * @return The icon resource specification.
     */
    public String getIconSpec ()
    {
        return ("images/StringFilter.gif");
    }

    /**
     * Get the underlying node filter object.
     * @return The node filter object suitable for serialization.
     */
    public NodeFilter getNodeFilter ()
    {
        StringFilter ret;
        
        ret = new StringFilter ();
        ret.setCaseSensitive (mFilter.getCaseSensitive ());
        ret.setLocale (mFilter .getLocale ());
        ret.setPattern (mFilter.getPattern ());
            
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
        mFilter = (StringFilter)filter;
        mPattern.setText (mFilter.getPattern ());
        mCaseSensitivity.setSelected (mFilter.getCaseSensitive ());
        mLocale.setVisible (!mFilter.getCaseSensitive ());
        mLocale.setSelectedItem (mFilter.getLocale ().getDisplayName ());
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
        out.append ("StringFilter ");
        out.append (ret);
        out.append (" = new StringFilter ();");
        newline (out);
        spaces (out, context[0]);
        out.append (ret);
        out.append (".setCaseSensitive (");
        out.append (mFilter.getCaseSensitive () ? "true" : "false");
        out.append (");");
        newline (out);
        spaces (out, context[0]);
        out.append (ret);
        out.append (".setLocale (new java.util.Locale (\"");
        out.append (mFilter .getLocale ().getLanguage ());
        out.append ("\", \"");
        out.append (mFilter .getLocale ().getCountry ());
        out.append ("\", \"");
        out.append (mFilter .getLocale ().getVariant ());
        out.append ("\"));");
        newline (out);
        spaces (out, context[0]);
        out.append (ret);
        out.append (".setPattern (\"");
        out.append (mFilter.getPattern ());
        out.append ("\");");
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
        boolean sensitive;
        Object[] selection;
        String locale;

        source = event.getSource ();
        if (source == mCaseSensitivity)
        {
            sensitive = mCaseSensitivity.isSelected ();
            mFilter.setCaseSensitive (sensitive);
            mLocale.setVisible (!sensitive);
            mLocale.setSelectedItem (mFilter.getLocale ().getDisplayName ());
        }
        else if (source == mLocale)
        {
            synchronized (mLocale)
            {
                selection = mLocale.getSelectedObjects ();
                if ((null != selection) && (0 != selection.length))
                {
                    locale = (String)selection[0];
                    for (int i = 0; i < mLocales.length; i++)
                        if (locale.equals (mLocales[i].getDisplayName ()))
                            mFilter.setLocale (mLocales[i]);
                }
            }
        }
    }

    //
    // Runnable interface
    //

    /**
     * Background thread task to get the available locales.
     */
    public void run ()
    {
        String locale;

        synchronized (mLocale)
        {
            mLocales = Locale.getAvailableLocales ();
            locale = mFilter.getLocale ().getDisplayName ();
            for (int i = 0; i < mLocales.length; i++)
                if (!locale.equals (mLocales[i].getDisplayName ()))
                    mLocale.addItem (mLocales[i].getDisplayName ());
            mLocale.invalidate ();
        }
    }
        
    //
    // DocumentListener interface
    //

    /**
     * Handle an insert update event.
     * @param e Details about the insert event.
     */
    public void insertUpdate (DocumentEvent e)
    {
        Document doc;
        
        doc = e.getDocument ();
        try
        {
            mFilter.setPattern (doc.getText (0, doc.getLength ()));
        }
        catch (BadLocationException ble)
        {
            ble.printStackTrace ();
        }
    }

    /**
     * Handle a remove update event.
     * @param e Details about the remove event.
     */
    public void removeUpdate (DocumentEvent e)
    {
        Document doc;
        
        doc = e.getDocument ();
        try
        {
            mFilter.setPattern (doc.getText (0, doc.getLength ()));
        }
        catch (BadLocationException ble)
        {
            ble.printStackTrace ();
        }
    }

    /**
     * Handle a change update event.
     * @param e Details about the change event.
     */
    public void changedUpdate (DocumentEvent e)
    {
        // plain text components don't fire these events
    }
}
