// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/beans/HTMLTextBean.java,v $
// $Author: derrickoswald $
// $Date: 2005/05/15 11:49:03 $
// $Revision: 1.24 $
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

package org.htmlparser.beans;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.net.URLConnection;

import javax.swing.JTextArea;

/**
 * Display the textual URL contents.
 * @author Derrick Oswald
 * Created on December 24, 2002, 3:49 PM
 */
public class HTMLTextBean
    extends
        JTextArea
    implements
        Serializable,
        PropertyChangeListener
{
    /**
     * The underlying bean that provides our htmlparser specific properties.
     */
    protected StringBean mBean;

    /**
     * Creates a new HTMLTextBean.
     * This uses an underlying StringBean and displays the text.
     */
    public HTMLTextBean ()
    {
        getBean ().addPropertyChangeListener (this);
    }

    /**
     * Return the minimum dimension for this visible bean.
     * @return a minimum bounding box for this bean.
     */
    public Dimension getMinimumSize ()
    {
        FontMetrics met;
        int width;
        int height;

        met = getFontMetrics (getFont ());
        width = met.stringWidth ("Hello World");
        height = met.getLeading () + met.getHeight () + met.getDescent ();

        return (new Dimension (width, height));
    }

    /**
     * Add a PropertyChangeListener to the listener list.
     * The listener is registered for all properties.
     * <p><em>Delegates to the underlying StringBean</em>
     * @param listener The PropertyChangeListener to be added.
     */
    public void addPropertyChangeListener (PropertyChangeListener listener)
    {
        super.addPropertyChangeListener (listener);
        getBean ().addPropertyChangeListener (listener);
    }

    /**
     * Remove a PropertyChangeListener from the listener list.
     * This removes a registered PropertyChangeListener.
     * <p><em>Delegates to the underlying StringBean</em>
     * @param listener The PropertyChangeListener to be removed.
     */
    public void removePropertyChangeListener (PropertyChangeListener listener)
    {
        super.addPropertyChangeListener (listener);
        getBean ().removePropertyChangeListener (listener);
    }

    //
    // Properties
    //

    /**
     * Return the underlying bean object.
     * Creates a new one if it hasn't been initialized yet.
     * @return The StringBean this bean uses to fetch text.
     */
    public StringBean getBean ()
    {
        if (null == mBean)
            mBean = new StringBean ();

        return (mBean);
    }

    /**
     * Getter for property strings.
     * <p><em>Delegates to the underlying StringBean</em>
     * @return Value of property strings.
     */
    public String getStrings ()
    {
        return (getBean ().getStrings ());
    }

    /**
     * Getter for property links.
     * <p><em>Delegates to the underlying StringBean</em>
     * @return Value of property links.
     */
    public boolean getLinks ()
    {
        return (getBean ().getLinks ());
    }

    /**
     * Setter for property links.
     * <p><em>Delegates to the underlying StringBean</em>
     * @param links New value of property links.
     */
    public void setLinks (boolean links)
    {
        getBean ().setLinks (links);
    }

    /**
     * Getter for property URL.
     * <p><em>Delegates to the underlying StringBean</em>
     * @return Value of property URL.
     */
    public String getURL ()
    {
        return (getBean ().getURL ());
    }

    /**
     * Setter for property URL.
     * <p><em>Delegates to the underlying StringBean</em>
     * @param url New value of property URL.
     */
    public void setURL (String url)
    {
        getBean ().setURL (url);
    }

    /**
     * Get the current 'replace non breaking spaces' state.
     * @return The <code>true</code> if non-breaking spaces (character '\u00a0',
     * numeric character reference &160; or character entity reference &nbsp;)
     * are to be replaced with normal spaces (character '\u0020').
     */
    public boolean getReplaceNonBreakingSpaces ()
    {
        return (getBean ().getReplaceNonBreakingSpaces ());
    }

    /**
     * Set the 'replace non breaking spaces' state.
     * @param replace <code>true</code> if non-breaking spaces (character '\u00a0',
     * numeric character reference &160; or character entity reference &nbsp;)
     * are to be replaced with normal spaces (character '\u0020').
     */
    public void setReplaceNonBreakingSpaces (boolean replace)
    {
        getBean ().setReplaceNonBreakingSpaces (replace);
    }

    /**
     * Get the current 'collapse whitespace' state.
     * If set to <code>true</code> this emulates the operation of browsers
     * in interpretting text where auser agents should collapse input white
     * space sequences when producing output inter-word space.
     * See HTML specification section 9.1 White space
     * http://www.w3.org/TR/html4/struct/text.html#h-9.1
     * @return <code>true</code> if sequences of whitespace (space '\u0020',
     * tab '\u0009', form feed '\u000C', zero-width space '\u200B',
     * carriage-return '\r' and newline '\n') are to be replaced with a single
     * space.
     */
    public boolean getCollapse ()
    {
        return (getBean ().getCollapse ());
    }

    /**
     * Set the current 'collapse whitespace' state.
     * @param collapse If <code>true</code>, sequences of whitespace
     * will be reduced to a single space.
     */
    public void setCollapse (boolean collapse)
    {
        getBean ().setCollapse (collapse);
    }

    /**
     * Getter for property Connection.
     * @return Value of property Connection.
     */
    public URLConnection getConnection ()
    {
        return (getBean ().getConnection ());
    }

    /**
     * Setter for property Connection.
     * @param connection New value of property Connection.
     */
    public void setConnection (URLConnection connection)
    {
        getBean ().setConnection (connection);
    }

    //
    // PropertyChangeListener inteface
    //

    /**
     * Responds to changes in the underlying bean's properties.
     * @param event The event triggering this listener method call.
     */
    public void propertyChange (PropertyChangeEvent event)
    {
        if (event.getPropertyName ().equals (StringBean.PROP_STRINGS_PROPERTY))
        {
            setText (getBean ().getStrings ());
            setCaretPosition (0);
        }
    }

//    /**
//     * Unit test.
//     */
//    public static void main (String[] args)
//    {
//        HTMLTextBean tb = new HTMLTextBean ();
//        tb.setURL ("http://cbc.ca");
//        // tb.setLinks (true);
//        javax.swing.JFrame frame = new javax.swing.JFrame ();
//        frame.getContentPane ().setLayout (new BorderLayout ());
//        frame.getContentPane ().add (new JScrollPane (tb),
//            BorderLayout.CENTER);
//        frame.addWindowListener (new java.awt.event.WindowListener () {
//            public void windowOpened (java.awt.event.WindowEvent e) {}
//            public void windowClosing (java.awt.event.WindowEvent e)
//            {
//                System.exit (0);
//            }
//            public void windowClosed (java.awt.event.WindowEvent e) {}
//            public void windowDeiconified (java.awt.event.WindowEvent e) {}
//            public void windowIconified (java.awt.event.WindowEvent e) {}
//            public void windowActivated (java.awt.event.WindowEvent e) {}
//            public void windowDeactivated (java.awt.event.WindowEvent e) {}
//        });
//        frame.setBounds (100, 100, 640, 480);
//        frame.show ();
//    }
}
