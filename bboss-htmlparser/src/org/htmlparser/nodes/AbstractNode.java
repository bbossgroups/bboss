// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Somik Raha
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/nodes/AbstractNode.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/10 23:20:44 $
// $Revision: 1.4 $
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

package org.htmlparser.nodes;

import java.io.Serializable;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

/**
 * The concrete base class for all types of nodes (tags, text remarks).
 * This class provides basic functionality to hold the {@link Page}, the
 * starting and ending position in the page, the parent and the list of
 * {@link NodeList children}.
 */
public abstract class AbstractNode implements Node, Serializable
{
	protected boolean isresource = false;
    /**
     * The page this node came from.
     */
    protected Page mPage;

    /**
     * The beginning position of the tag in the line
     */
    protected int nodeBegin;

    /**
     * The ending position of the tag in the line
     */
    protected int nodeEnd;

    /**
     * The parent of this node.
     */
    protected Node parent;

    /**
     * The children of this node.
     */
    protected NodeList children;

    /**
     * Create an abstract node with the page positions given.
     * Remember the page and start & end cursor positions.
     * @param page The page this tag was read from.
     * @param start The starting offset of this node within the page.
     * @param end The ending offset of this node within the page.
     */
    public AbstractNode (Page page, int start, int end)
    {
        mPage = page;
        nodeBegin = start;
        nodeEnd = end;
        parent = null;
        children = null;
    }

    /**
     * Clone this object.
     * Exposes java.lang.Object clone as a public method.
     * @return A clone of this object.
     * @exception CloneNotSupportedException This shouldn't be thrown since
     * the {@link Node} interface extends Cloneable.
     */
    public Object clone() throws CloneNotSupportedException
    {
        return (super.clone ());
    }

    /**
     * Returns a string representation of the node.
     * It allows a simple string transformation
     * of a web page, regardless of node type.<br>
     * Typical application code (for extracting only the text from a web page)
     * would then be simplified to:<br>
     * <pre>
     * Node node;
     * for (Enumeration e = parser.elements (); e.hasMoreElements (); )
     * {
     *     node = (Node)e.nextElement();
     *     System.out.println (node.toPlainTextString ());
     *     // or do whatever processing you wish with the plain text string
     * }
     * </pre>
     * @return The 'browser' content of this node.
     */
    public abstract String toPlainTextString ();

    /**
     * Return the HTML that generated this node.
     * This method will make it easier when using html parser to reproduce html
     * pages (with or without modifications).
     * Applications reproducing html can use this method on nodes which are to
     * be used or transferred as they were recieved, with the original html.
     * @return The HTML code for this node.
     */
    public abstract String toHtml ();

    /**
     * Return a string representation of the node.
     * Subclasses must define this method, and this is typically to be used in the manner<br>
     * <pre>System.out.println(node)</pre>
     * @return A textual representation of the node suitable for debugging
     */
    public abstract String toString ();

    /**
     * Collect this node and its child nodes (if-applicable) into the collectionList parameter, provided the node
     * satisfies the filtering criteria.<P>
     * 
     * This mechanism allows powerful filtering code to be written very easily,
     * without bothering about collection of embedded tags separately.
     * e.g. when we try to get all the links on a page, it is not possible to
     * get it at the top-level, as many tags (like form tags), can contain
     * links embedded in them. We could get the links out by checking if the
     * current node is a {@link org.htmlparser.tags.CompositeTag}, and going through its children.
     * So this method provides a convenient way to do this.<P>
     * 
     * Using collectInto(), programs get a lot shorter. Now, the code to
     * extract all links from a page would look like:
     * <pre>
     * NodeList collectionList = new NodeList();
     * NodeFilter filter = new TagNameFilter ("A");
     * for (NodeIterator e = parser.elements(); e.hasMoreNodes();)
     *      e.nextNode().collectInto(collectionList, filter);
     * </pre>
     * Thus, collectionList will hold all the link nodes, irrespective of how
     * deep the links are embedded.<P>
     * 
     * Another way to accomplish the same objective is:
     * <pre>
     * NodeList collectionList = new NodeList();
     * NodeFilter filter = new TagClassFilter (LinkTag.class);
     * for (NodeIterator e = parser.elements(); e.hasMoreNodes();)
     *      e.nextNode().collectInto(collectionList, filter);
     * </pre>
     * This is slightly less specific because the LinkTag class may be
     * registered for more than one node name, e.g. &lt;LINK&gt; tags too.
     * @param list The node list to collect acceptable nodes into.
     * @param filter The filter to determine which nodes are retained.
     */
    public void collectInto (NodeList list, NodeFilter filter)
    {
        if (filter.accept (this))
            list.add (this);
    }

    /**
     * Get the page this node came from.
     * @return The page that supplied this node.
     */
    public Page getPage ()
    {
        return (mPage);
    }
    
    public boolean isResource()
    {
    	return this.isresource;
    }
    public void setResource(boolean isresource)
    {
    	this.isresource = isresource;
    }

    /**
     * Set the page this node came from.
     * @param page The page that supplied this node.
     */
    public void setPage (Page page)
    {
        mPage = page;
    }

    /**
     * Gets the starting position of the node.
     * @return The start position.
     */
    public int getStartPosition ()
    {
        return (nodeBegin);
    }

    /**
     * Sets the starting position of the node.
     * @param position The new start position.
     */
    public void setStartPosition (int position)
    {
        nodeBegin = position;
    }

    /**
     * Gets the ending position of the node.
     * @return The end position.
     */
    public int getEndPosition ()
    {
        return (nodeEnd);
    }

    /**
     * Sets the ending position of the node.
     * @param position The new end position.
     */
    public void setEndPosition (int position)
    {
        nodeEnd = position;
    }

    /**
     * Visit this node.
     * @param visitor The visitor that is visiting this node.
     */
    public abstract void accept (NodeVisitor visitor);

    /**
     * Get the parent of this node.
     * This will always return null when parsing without scanners,
     * i.e. if semantic parsing was not performed.
     * The object returned from this method can be safely cast to a <code>CompositeTag</code>.
     * @return The parent of this node, if it's been set, <code>null</code> otherwise.
     */
    public Node getParent ()
    {
        return (parent);
    }

    /**
     * Sets the parent of this node.
     * @param node The node that contains this node. Must be a <code>CompositeTag</code>.
     */
    public void setParent (Node node)
    {
        parent = node;
    }

    /**
     * Get the children of this node.
     * @return The list of children contained by this node, if it's been set, <code>null</code> otherwise.
     */
    public NodeList getChildren ()
    {
        return (children);
    }

    /**
     * Set the children of this node.
     * @param children The new list of children this node contains.
     */
    public void setChildren (NodeList children)
    {
        this.children = children;
    }

    /**
     * Returns the text of the node.
     * @return The text of this node. The default is <code>null</code>.
     */
    public String getText ()
    {
        return null;
    }

    /**
     * Sets the string contents of the node.
     * @param text The new text for the node.
     */
    public void setText(String text)
    {
    }

    /**
     * Perform the meaning of this tag.
     * The default action is to do nothing.
     * @exception ParserException <em>Not used.</em> Provides for subclasses
     * that may want to indicate an exceptional condition.
     */
    public void doSemanticAction ()
        throws
            ParserException
    {
    }
}
