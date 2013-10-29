// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/beans/FilterBean.java,v $
// $Author: derrickoswald $
// $Date: 2005/05/15 11:49:03 $
// $Revision: 1.3 $
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.net.URLConnection;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.EncodingChangeException;

/**
 * Extract nodes from a URL using a filter.
 * <pre>
 * <code>
 *     FilterBean fb = new FilterBean ("http://cbc.ca");
 *     fb.setFilters (new NodeFilter[] { new TagNameFilter ("META") });
 *     fb.setURL ("http://cbc.ca");
 *     System.out.println (fb.getNodes ().toHtml ());
 * </code>
 * </pre>
 */
public class FilterBean
    implements
        Serializable
{
    /**
     * Property name in event where the URL contents changes.
     */
    public static final String PROP_NODES_PROPERTY = "nodes";

    /**
     * Property name in event where the URL contents changes.
     */
    public static final String PROP_TEXT_PROPERTY = "text";

    /**
     * Property name in event where the URL changes.
     */
    public static final String PROP_URL_PROPERTY = "URL";

    /**
     * Property name in event where the connection changes.
     */
    public static final String PROP_CONNECTION_PROPERTY = "connection";

    /**
     * Bound property support.
     */
    protected PropertyChangeSupport mPropertySupport;

    /**
     * The parser used to filter.
     */
    protected Parser mParser;

    /**
     * The filter set.
     */
    protected NodeFilter[] mFilters;

    /**
     * The nodes extracted from the URL.
     */
    protected NodeList mNodes;

   /**
     * Create a FilterBean object.
     */
    public FilterBean ()
    {
        mPropertySupport = new PropertyChangeSupport (this);
        mParser = new Parser ();
        mFilters = null;
        mNodes = null;
    }

    //
    // internals
    //

    /**
     * Assign the <code>Nodes</code> property, firing the property change.
     * @param nodes The new value of the <code>Nodes</code> property.
     */
    protected void updateNodes (NodeList nodes)
    {
        NodeList oldValue;
        String oldText;
        String newText;

        if ((null == mNodes) || !mNodes.equals (nodes))
        {
            oldValue = mNodes;
            if (null != oldValue)
                oldText = getText ();
            else
                oldText = "";
            if (null == oldText)
                oldText = "";
            mNodes = nodes;
            if (null != mNodes) // TODO: fix this null problem
                newText = getText ();
            else // StringBean finds no nodes
                newText = "";
            if (null == newText)
                newText = "";
            mPropertySupport.firePropertyChange (
                PROP_NODES_PROPERTY, oldValue, nodes);
            if (!newText.equals (oldText))
                mPropertySupport.firePropertyChange (
                    PROP_TEXT_PROPERTY, oldText, newText);
        }
    }

    /**
     * Apply each of the filters.
     * The first filter is applied to the parser.
     * Subsequent filters are applied to the output of the prior filter.
     * @return A list of nodes passed through all filters.
     * @throws ParserException If an encoding change occurs
     * or there is some other problem.
     */
    protected NodeList applyFilters ()
        throws
            ParserException
    {
        NodeList ret;

        ret = new NodeList ();

        if (null != getFilters ())
            for (int i = 0; i < getFilters ().length; i++)
                if (0 == i)
                    ret = mParser.parse (getFilters ()[0]);
                else
                    ret = ret.extractAllNodesThatMatch (getFilters ()[i]);

        return (ret);
    }

    /**
     * Fetch the URL contents and filter it.
     * Only do work if there is a valid parser with it's URL set.
     */
    protected void setNodes ()
    {
        NodeList list;

        if (null != getURL ())
            try
            {
                list = applyFilters ();
                updateNodes (list);
            }
            catch (EncodingChangeException ece)
            {
                try
                {   // try again with the encoding now in force
                    mParser.reset ();
                    list = applyFilters ();
                    updateNodes (list);
                }
                catch (ParserException pe)
                {
                    updateNodes (new NodeList ());
                }
             }
            catch (ParserException pe)
            {
                updateNodes (new NodeList ());
            }
    }

    //
    // Property change support.
    //

    /**
     * Add a PropertyChangeListener to the listener list.
     * The listener is registered for all properties.
     * @param listener The PropertyChangeListener to be added.
     */
    public void addPropertyChangeListener (PropertyChangeListener listener)
    {
        mPropertySupport.addPropertyChangeListener (listener);
    }

    /**
     * Remove a PropertyChangeListener from the listener list.
     * This removes a registered PropertyChangeListener.
     * @param listener The PropertyChangeListener to be removed.
     */
    public void removePropertyChangeListener (PropertyChangeListener listener)
    {
        mPropertySupport.removePropertyChangeListener (listener);
    }

    //
    // Properties
    //

    /**
     * Return the nodes of the URL matching the filter.
     * This is the primary output of the bean.
     * @return The nodes from the URL matching the current filter.
     */
    public NodeList getNodes ()
    {
        if (null == mNodes)
            setNodes ();

        return (mNodes);
    }

    /**
     * Get the current URL.
     * @return The URL from which text has been extracted, or <code>null</code>
     * if this property has not been set yet.
     */
    public String getURL ()
    {
         return ((null != mParser) ? mParser.getURL () : null);
    }

    /**
     * Set the URL to extract strings from.
     * The text from the URL will be fetched, which may be expensive, so this
     * property should be set last.
     * @param url The URL that text should be fetched from.
     */
    public void setURL (String url)
    {
        String old;
        URLConnection conn;

        old = getURL ();
        conn = getConnection ();
        if (((null == old) && (null != url)) || ((null != old)
            && !old.equals (url)))
        {
            try
            {
                if (null == mParser)
                    mParser = new Parser (url);
                else
                    mParser.setURL (url);
                mPropertySupport.firePropertyChange (
                    PROP_URL_PROPERTY, old, getURL ());
                mPropertySupport.firePropertyChange (
                    PROP_CONNECTION_PROPERTY, conn, mParser.getConnection ());
                setNodes ();
            }
            catch (ParserException pe)
            {
                updateNodes (new NodeList ());
            }
        }
    }

    /**
     * Get the current connection.
     * @return The connection that the parser has or <code>null</code> if it
     * hasn't been set or the parser hasn't been constructed yet.
     */
    public URLConnection getConnection ()
    {
        return ((null != mParser) ? mParser.getConnection () : null);
    }

    /**
     * Set the parser's connection.
     * The text from the URL will be fetched, which may be expensive, so this
     * property should be set last.
     * @param connection New value of property Connection.
     */
    public void setConnection (URLConnection connection)
    {
        String url;
        URLConnection conn;

        url = getURL ();
        conn = getConnection ();
        if (((null == conn) && (null != connection)) || ((null != conn)
            && !conn.equals (connection)))
        {
            try
            {
                if (null == mParser)
                    mParser = new Parser (connection);
                else
                    mParser.setConnection (connection);
                mPropertySupport.firePropertyChange (
                    PROP_URL_PROPERTY, url, getURL ());
                mPropertySupport.firePropertyChange (
                    PROP_CONNECTION_PROPERTY, conn, mParser.getConnection ());
                setNodes ();
            }
            catch (ParserException pe)
            {
                updateNodes (new NodeList ());
            }
        }
    }

    /**
     * Get the current filter set.
     * @return The current filters.
     */
    public NodeFilter[] getFilters ()
    {
        return (mFilters);
    }

    /**
     * Set the filters for the bean.
     * If the parser has been set, it is reset and
     * the nodes are refetched with the new filters.
     * @param filters The filter set to use.
     */
    public void setFilters (NodeFilter[] filters)
    {
        mFilters = filters;
        if (null != getParser ())
        {
            getParser ().reset ();
            setNodes ();
        }
    }

    /**
     * Get the parser used to fetch nodes.
     * @return The parser used by the bean.
     */
    public Parser getParser ()
    {
        return (mParser);
    }

    /**
     * Set the parser for the bean.
     * The parser is used immediately to fetch the nodes,
     * which for a null filter means all the nodes
     * @param parser The parser to use.
     */
    public void setParser (Parser parser)
    {
        mParser = parser;
        if (null != getFilters ())
            setNodes ();
    }

    /**
     * Convenience method to apply a {@link StringBean} to the filter results.
     * This may yield duplicate or multiple text elements if the node list
     * contains nodes from two or more levels in the same nested tag heirarchy,
     * but if the node list contains only one tag, it provides access to the
     * text within the node.
     * @return The textual contents of the nodes that pass through the filter set,
     * as collected by the StringBean. 
     */
    public String getText ()
    {
        NodeList list;
        StringBean sb;
        String ret;

        list = getNodes ();
        if (0 != list.size ())
        {
            sb = new StringBean ();
            for (int i = 0; i < list.size (); i++)
                list.elementAt (i).accept (sb);
            ret = sb.getStrings ();
        }
        else
            ret = "";
        
        return (ret);
    }

    /**
     * Unit test.
     * @param args Pass arg[0] as the URL to process,
     * and optionally a node name for filtering.
     */
    public static void main (String[] args)
    {
        if (0 >= args.length)
            System.out.println ("Usage: java -classpath htmlparser.jar org.htmlparser.beans.FilterBean <http://whatever_url> [node name]");
        else
        {
            FilterBean fb = new FilterBean ();
            if (1 < args.length)
                fb.setFilters (new NodeFilter[] { new org.htmlparser.filters.TagNameFilter (args[1]) });
            fb.setURL (args[0]);
            //System.out.println (fb.getNodes ().toHtml ());
            System.out.println (fb.getText ());
        }
    }
}
