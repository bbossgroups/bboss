// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Joshua Kerievsky
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/Node.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/24 17:48:27 $
// $Revision: 1.53 $
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

package org.htmlparser;

import org.htmlparser.lexer.Page;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

/**
 * Specifies the minimum requirements for nodes returned by the Lexer or Parser.
 * There are three types of nodes in HTML: text, remarks and tags. You may wish
 * to define your own nodes to be returned by the
 * {@link org.htmlparser.lexer.Lexer} or {@link Parser}, but each of the types
 * must support this interface.
 * More specific interface requirements for each of the node types are specified
 * by the {@link Text}, {@link Remark} and {@link Tag} interfaces.
 */
public interface Node
    extends
        Cloneable
{
    /**
     * A string representation of the node.
     * This is an important method, it allows a simple string transformation
     * of a web page, regardless of a node. For a Text node this is obviously
     * the textual contents itself. For a Remark node this is the remark
     * contents (sic). For tags this is the text contents of it's children
     * (if any). Because multiple nodes are combined when presenting
     * a page in a browser, this will not reflect what a user would see.
     * See HTML specification section 9.1 White space
     * <a href="http://www.w3.org/TR/html4/struct/text.html#h-9.1">
     * http://www.w3.org/TR/html4/struct/text.html#h-9.1</a>.<br>
     * Typical application code (for extracting only the text from a web page)
     * would be:<br>
     * <pre>
     * for (Enumeration e = parser.elements (); e.hasMoreElements ();)
     *     // or do whatever processing you wish with the plain text string
     *     System.out.println ((Node)e.nextElement ()).toPlainTextString ());
     * </pre>
     * @return The text of this node including it's children.
     */
    String toPlainTextString ();

    /**
     * Return the HTML for this node.
     * This should be the exact sequence of characters that were encountered by
     * the parser that caused this node to be created. Where this breaks down is
     * where broken nodes (tags and remarks) have been encountered and fixed.
     * Applications reproducing html can use this method on nodes which are to
     * be used or transferred as they were received or created.
     * @return The (exact) sequence of characters that would cause this node
     * to be returned by the parser or lexer.
     */
    String toHtml ();

    /**
     * Return the string representation of the node.
     * The return value may not be the entire contents of the node, and non-
     * printable characters may be translated in order to make them visible.
     * This is typically to be used in
     * the manner<br>
     * <pre>
     * System.out.println (node);
     * </pre>
     * or within a debugging environment.
     * @return A string representation of this node suitable for printing,
     * that isn't too large.
     */
    String toString ();

    /**
     * Collect this node and its child nodes into a list, provided the node
     * satisfies the filtering criteria.
     * <p>This mechanism allows powerful filtering code to be written very
     * easily, without bothering about collection of embedded tags separately.
     * e.g. when we try to get all the links on a page, it is not possible to
     * get it at the top-level, as many tags (like form tags), can contain
     * links embedded in them. We could get the links out by checking if the
     * current node is a {@link org.htmlparser.tags.CompositeTag}, and going
     * through its children. So this method provides a convenient way to do
     * this.</p>
     * <p>Using collectInto(), programs get a lot shorter. Now, the code to
     * extract all links from a page would look like:
     * <pre>
     * NodeList list = new NodeList ();
     * NodeFilter filter = new TagNameFilter ("A");
     * for (NodeIterator e = parser.elements (); e.hasMoreNodes ();)
     *      e.nextNode ().collectInto (list, filter);
     * </pre>
     * Thus, <code>list</code> will hold all the link nodes, irrespective of how
     * deep the links are embedded.</p>
     * <p>Another way to accomplish the same objective is:
     * <pre>
     * NodeList list = new NodeList ();
     * NodeFilter filter = new TagClassFilter (LinkTag.class);
     * for (NodeIterator e = parser.elements (); e.hasMoreNodes ();)
     *      e.nextNode ().collectInto (list, filter);
     * </pre>
     * This is slightly less specific because the LinkTag class may be
     * registered for more than one node name, e.g. &lt;LINK&gt; tags too.
     * @param list The list to collect nodes into.
     * @param filter The criteria to use when deciding if a node should
     * be added to the list.</p>
     */
    void collectInto (NodeList list, NodeFilter filter);

    /**
     * Gets the starting position of the node.
     * This is the character (not byte) offset of this node in the page.
     * @return The start position.
     */
    int getStartPosition ();

    /**
     * Sets the starting position of the node.
     * @param position The new start position.
     */
    void setStartPosition (int position);

    /**
     * Gets the ending position of the node.
     * This is the character (not byte) offset of the character following this
     * node in the page.
     * @return The end position.
     */
    int getEndPosition ();

    /**
     * Sets the ending position of the node.
     * @param position The new end position.
     */
    void setEndPosition (int position);

    /**
     * Get the page this node came from.
     * @return The page that supplied this node.
     */
    Page getPage ();

    /**
     * Set the page this node came from.
     * @param page The page that supplied this node.
     */
    void setPage (Page page);

    /**
     * Apply the visitor to this node.
     * @param visitor The visitor to this node.
     */
    void accept (NodeVisitor visitor);

    /**
     * Get the parent of this node.
     * This will always return null when parsing with the
     * {@link org.htmlparser.lexer.Lexer}.
     * Currently, the object returned from this method can be safely cast to a
     * {@link org.htmlparser.tags.CompositeTag}, but this behaviour should not
     * be expected in the future.
     * @return The parent of this node, if it's been set, <code>null</code>
     * otherwise.
     */
    Node getParent ();

    /**
     * Sets the parent of this node.
     * @param node The node that contains this node.
     */
    void setParent (Node node);

    /**
     * Get the children of this node.
     * @return The list of children contained by this node, if it's been set,
     * <code>null</code> otherwise.
     */
    NodeList getChildren ();

    /**
     * Set the children of this node.
     * @param children The new list of children this node contains.
     */
    void setChildren (NodeList children);

    /**
     * Returns the text of the node.
     * @return The contents of the string or remark node, and in the case of
     * a tag, the contents of the tag less the enclosing angle brackets.
     */
    String getText ();

    /**
     * Sets the string contents of the node.
     * @param text The new text for the node.
     */
    void setText (String text);

    /**
     * Perform the meaning of this tag.
     * This is defined by the tag, for example the bold tag &lt;B&gt; may switch
     * bold text on and off.
     * Only a few tags have semantic meaning to the parser. These have to do
     * with the character set to use (&lt;META&gt;) and the base URL to use
     * (&lt;BASE&gt;). Other than that, the semantic meaning is up to the
     * application and it's custom nodes.<br>
     * The semantic action is performed when the node has been parsed. For
     * composite nodes (those that contain other nodes), the children will have
     * already been parsed and will be available via {@link #getChildren}.
     * @exception ParserException If a problem is encountered performing the
     * semantic action.
     */
    void doSemanticAction ()
        throws
            ParserException;

    //
    // Cloneable interface
    //

    /**
     * Allow cloning of nodes.
     * Creates and returns a copy of this object.  The precise meaning
     * of "copy" may depend on the class of the object. The general
     * intent is that, for any object <tt>x</tt>, the expression:
     * <blockquote>
     * <pre>
     * x.clone() != x</pre></blockquote>
     * will be true, and that the expression:
     * <blockquote>
     * <pre>
     * x.clone().getClass() == x.getClass()</pre></blockquote>
     * will be <tt>true</tt>, but these are not absolute requirements.
     * While it is typically the case that:
     * <blockquote>
     * <pre>
     * x.clone().equals(x)</pre></blockquote>
     * will be <tt>true</tt>, this is not an absolute requirement.
     * <p>
     * By convention, the returned object should be obtained by calling
     * <tt>super.clone</tt>.  If a class and all of its superclasses (except
     * <tt>Object</tt>) obey this convention, it will be the case that
     * <tt>x.clone().getClass() == x.getClass()</tt>.
     * <p>
     * By convention, the object returned by this method should be independent
     * of this object (which is being cloned).  To achieve this independence,
     * it may be necessary to modify one or more fields of the object returned
     * by <tt>super.clone</tt> before returning it.  Typically, this means
     * copying any mutable objects that comprise the internal "deep structure"
     * of the object being cloned and replacing the references to these
     * objects with references to the copies.  If a class contains only
     * primitive fields or references to immutable objects, then it is usually
     * the case that no fields in the object returned by <tt>super.clone</tt>
     * need to be modified.
     * <p>
     * The method <tt>clone</tt> for class <tt>Object</tt> performs a
     * specific cloning operation. First, if the class of this object does
     * not implement the interface <tt>Cloneable</tt>, then a
     * <tt>CloneNotSupportedException</tt> is thrown. Note that all arrays
     * are considered to implement the interface <tt>Cloneable</tt>.
     * Otherwise, this method creates a new instance of the class of this
     * object and initializes all its fields with exactly the contents of
     * the corresponding fields of this object, as if by assignment; the
     * contents of the fields are not themselves cloned. Thus, this method
     * performs a "shallow copy" of this object, not a "deep copy" operation.
     * <p>
     * The class <tt>Object</tt> does not itself implement the interface
     * <tt>Cloneable</tt>, so calling the <tt>clone</tt> method on an object
     * whose class is <tt>Object</tt> will result in throwing an
     * exception at run time.
     *
     * @return     a clone of this instance.
     * @exception  CloneNotSupportedException  if the object's class does not
     *               support the <code>Cloneable</code> interface. Subclasses
     *               that override the <code>clone</code> method can also
     *               throw this exception to indicate that an instance cannot
     *               be cloned.
     * @see java.lang.Cloneable
     */
    Object clone ()
        throws
            CloneNotSupportedException;
    boolean isResource();
    void setResource(boolean isresource);
    
}
