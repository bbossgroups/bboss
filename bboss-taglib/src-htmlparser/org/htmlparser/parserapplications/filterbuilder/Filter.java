// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2005 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/parserapplications/filterbuilder/Filter.java,v $
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

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.*;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.parserapplications.filterbuilder.layouts.VerticalLayoutManager;

/**
 * Base class for all filters.
 * Provides common functionality applicable to all filters.
 */
public abstract class Filter
    extends
        JComponent
    implements
        NodeFilter
{
    /**
     * Create a new filter from the class name.
     * @param class_name The class to instatiate.
     * @return The constructed filter object.
     */
    public static Filter instantiate (String class_name)
    {
        Filter ret;
        
        ret = null;
        try
        {
            Class cls = Class.forName (class_name);
            ret = (Filter)cls.newInstance ();
            mWrappers.put (ret.getNodeFilter ().getClass ().getName (), class_name);
        }
        catch (ClassNotFoundException cnfe)
        {
            System.out.println ("can't find class " + class_name);
        }
        catch (InstantiationException ie)
        {
            System.out.println ("can't instantiate class " + class_name);
        }
        catch (IllegalAccessException ie)
        {
            System.out.println ("class " + class_name + " has no public constructor");
        }
        catch (ClassCastException cce)
        {
            System.out.println ("class " + class_name + " is not a Filter");
        }
        
        return (ret);
    }

    /**
     * Map from cilter class to wrapper.
     * Populated as part of each wrapper being loaded.
     */
    protected static Hashtable mWrappers = new Hashtable ();

    /**
     * Create a filter.
     * Set up the default display.
     * Only a border with the label of the filter name,
     * returned by <code>getDescription()</code>,
     * and an icon, returned by <code>getIcon()</code>.
     */
    public Filter ()
    {
        JLabel label;
        Dimension dimension;
        Insets insets;

        setToolTipText (getDescription ());
        // none of these quite does it:
        // new BoxLayout (this, BoxLayout.Y_AXIS));
        // new GridLayout (0, 1));
        setLayout (new VerticalLayoutManager ());
        setSelected (false);
        label = new JLabel (getDescription (), getIcon (), SwingConstants.LEFT);
        label.setBackground (Color.green);
        label.setAlignmentX (Component.LEFT_ALIGNMENT);
        label.setHorizontalAlignment (SwingConstants.LEFT);
        add (label);
        dimension = label.getMaximumSize ();
        insets = getInsets ();
        dimension.setSize (dimension.width + insets.left + insets.right, dimension.height + insets.top + insets.bottom);
        setSize (dimension);
    }

    /**
     * Get the name of the filter.
     * @return A descriptive name for the filter.
     */
    public abstract String getDescription ();

    /**
     * Get the underlying node filter object.
     * @return The node filter object suitable for serialization.
     */
    public abstract NodeFilter getNodeFilter ();

    /**
     * Assign the underlying node filter for this wrapper.
     * @param filter The filter to wrap.
     * @param context The parser to use for conditioning this filter.
     * Some filters need contextual information to provide to the user,
     * i.e. for tag names or attribute names or values,
     * so the Parser context is provided. 
     */
    public abstract void setNodeFilter (NodeFilter filter, Parser context);

    /**
     * Get the underlying node filter's subordinate filters.
     * @return The node filter object's contained filters.
     */
    public abstract NodeFilter[] getSubNodeFilters ();

    /**
     * Assign the underlying node filter's subordinate filters.
     * @param filters The filters to insert into the underlying node filter.
     */
    public abstract void setSubNodeFilters (NodeFilter[] filters);

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
    public abstract String toJavaCode (StringBuffer out, int[] context);

    /**
     * Get the icon for the filter.
     * Loads the resource specified by 
     * <code>getIconSpec()</code> as an icon.
     * @return The icon or null if it was not found.
     */
    public Icon getIcon ()
    {
        ImageIcon ret;
        
        ret = null;
        try
        {
            ret = new ImageIcon (getClass ().getResource (getIconSpec ()));
        }
        catch (NullPointerException npe)
        {
            System.err.println ("can't find icon " + getIconSpec ());
        }
        
        return (ret);
    }
    
    /**
     * Get the resource name for the icon.
     * @return The icon resource specification.
     */
    public abstract String getIconSpec ();

    //
    // Component overrides
    //

    /**
     * Returns a string representation of this component and its values.
     * @return A string representation of this component.
     */
    public String toString ()
    {
        return (getDescription () + " [" + this.getClass ().getName () + "]");
    }

    //
    // utilities
    //

    /**
     * Serialize an object to a byte array.
     * @param object The object to be pickled.
     * @return The serialized object.
     * @exception IOException If the output stream complains (unlikely).
     */
    public static byte[] pickle (Object object)
        throws
            IOException
    {
        ByteArrayOutputStream bos;
        ObjectOutputStream oos;
        byte[] ret;

        bos = new ByteArrayOutputStream ();
        oos = new ObjectOutputStream (bos);
        oos.writeObject (object);
        oos.close ();
        ret = bos.toByteArray ();

        return (ret);
    }

    /**
     * Reconstitute a serialized object.
     * @param data The pickled object.
     * @return The reconstituted object.
     * @exception IOException If the input stream complains. 
     * @exception ClassNotFoundException If the serialized object class cannot
     * be located.
     */
    public static Object unpickle (byte[] data)
        throws
            IOException,
            ClassNotFoundException
    {
        ByteArrayInputStream bis;
        ObjectInputStream ois;
        Object ret;

        bis = new ByteArrayInputStream (data);
        ois = new ObjectInputStream (bis);
        ret = ois.readObject ();
        ois.close ();

        return (ret);
    }

    /**
     * Serialize a byte array to a String.
     * Convert each byte from the serialized object into a couple of hexadecimal
     * characters.
     * @param data The serialized object as a byte array.
     * @return The string representing the serialized object.
     */
    public static String serialize (byte[] data)
    {
        String string;
        StringBuffer ret;
        
        ret = new StringBuffer (data.length * 2);

        for (int i = 0; i < data.length; i++)
        {
            string = Integer.toString (0xff & data[i], 16);
            if (string.length () < 2)
                ret.append ("0");
            ret.append (string);
        }
        
        return (ret.toString ());
    }

    /**
     * Convert a sequence of hexadecimal characters back into a byte array.
     * @param string The string to convert (must be correct hex characters).
     * @return The bytes as an array.
     */
    public static byte[] deserialize (String string)
    {
        byte[] ret;
        
        ret = new byte[string.length () / 2];
        
        for (int i = 0; i < string.length (); i += 2)
            ret[i/2] = (byte)Integer.parseInt (string.substring (i, i + 2), 16); // todo: hopelessly inefficient
        
        return (ret);
    }

    /**
     * Returns a string serialization of the filters.
     * @param filters The list of filters to serialize.
     * @return A string representation of the filters.
     * @exception IOException If serialization fails.
     */
    public static String deconstitute (Filter[] filters) throws IOException
    {
        StringBuffer ret;

        ret = new StringBuffer (1024);
        for (int i = 0; i < filters.length; i++)
        {
            ret.append ("[");
            ret.append (serialize (pickle (filters[i].getNodeFilter ())));
            ret.append ("]");
        }

        return (ret.toString ());
    }

    /**
     * Returns the filters represented by the string.
     * @param string The string with serialized node filters.
     * @param context The context from which to extract meaningful values
     * for GUI choices (which aren't serialized).
     * @return The filters gleaned from the string.
     * @see #wrap
     */
    public static Filter[] reconstitute (String string, Parser context)
    {
        Filter[] ret;
        Vector vector;
        int index;
        String code;
        Object object;
        Filter filter;
        
        vector = new Vector ();
        try
        {
            while (string.startsWith ("["))
            {
                index = string.indexOf (']');
                if (-1 != index)
                {
                    code = string.substring (1, index);
                    string = string.substring (index + 1);
                    object = unpickle (deserialize (code));
                    if (object instanceof NodeFilter)
                    {
                        filter = wrap ((NodeFilter)object, context);
                        if (null != filter)
                            vector.addElement (filter);
                    }
                    else
                        break;
                }
                else
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace ();
        }

        ret = new Filter[vector.size ()];
        vector.copyInto (ret);

        return (ret);
    }

    /**
     * Get the enclosed sub filter list if any.
     * Todo: rationalize with FilterBuilder's method(s) of the same name.
     * @param component The component that's supposedly enclosing the list.
     * @return The enclosed component or <code>null</code> otherwise.
     */
    protected static SubFilterList getEnclosed (Component component)
    {
        Component[] list;

        if (component instanceof Container)
        {
            list = ((Container)component).getComponents  ();
            for (int i = 0; i < list.length; i++)
                if (list[i] instanceof SubFilterList)
                    return ((SubFilterList)list[i]);
        }

        return (null);
    }

    /**
     * Returns a wrapped filter.
     * @param filter A filter to be wrapped by GUI components.
     * @param context The context within which to wrap the object.
     * Some wrappers need context to set up useful choices for the user.
     * @return The filter to wrap.
     */
    public static Filter wrap (NodeFilter filter, Parser context)
    {
        String class_name;
        NodeFilter[] filters;
        SubFilterList list;
        Filter ret;

        ret = null;
        
        class_name = filter.getClass ().getName ();
        class_name = (String)mWrappers.get (class_name);
        if (null != class_name)
        {
            try
            {
                ret = Filter.instantiate (class_name);
                ret.setNodeFilter (filter, context);
                // recurse into subfilters
                filters = ret.getSubNodeFilters ();
                if (0 != filters.length)
                {
                    list = getEnclosed (ret);
                    if (null != list)
                    {
                        ret.setSubNodeFilters (new NodeFilter[0]); // clean out the unwrapped filters
                        for (int i = 0; i < filters.length; i++)
                            list.addFilter (wrap (filters[i], context));
                    }
                    else
                        throw new IllegalStateException ("filter can't have subnodes without a SubFilterList on the wrapper");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace ();
            }
        }
        else
            System.out.println (class_name + " is not registered for wrapping.");
        
        return (ret);
    }

    /**
     * Set the 'selected look' for the component.
     * @param selected If <code>true</code>, 'select' this component,
     * otherwise 'deselect' it.
     */
    public void setSelected (boolean selected)
    {
        if (selected)
            setBorder (
                new CompoundBorder (
                    new EtchedBorder (),
                    new CompoundBorder (
                        new LineBorder(Color.blue, 2),
                        new EmptyBorder (1, 1, 1, 1))));
        else
            setBorder (
                new CompoundBorder (
                    new EtchedBorder (),
                    new EmptyBorder (3,3,3,3)));
    }
    
    /**
     * Set the expanded state for the component.
     * This sets invisible all but the JLabel component in the
     * comand component.
     * @param expanded If <code>true</code>, 'expand' this component,
     * otherwise 'collapse' it.
     */
    public void setExpanded (boolean expanded)
    {
        Component[] components;
        
        components = getComponents ();
        for (int i = 0; i < components.length; i++)
            if (!(components[i] instanceof JLabel))
                components[i].setVisible (expanded);
    }
    
    /**
     * Append count spaces to the buffer.
     * @param out The buffer to append to.
     * @param count The number of spaces to append.
     */
    public static void spaces (StringBuffer out, int count)
    {
        for (int i = 0; i < count; i++)
            out.append (' ');
    }
    
    /**
     * Append a newline to the buffer.
     * @param out The buffer to append to.
     */
    public static void newline (StringBuffer out)
    {
        out.append ('\n');
    }
}
