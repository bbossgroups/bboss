// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2003 Somik Raha
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/scanners/CompositeTagScanner.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/10 23:20:44 $
// $Revision: 1.90 $
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

package org.htmlparser.scanners;

import java.util.Vector;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.scanners.Scanner;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * The main scanning logic for nested tags.
 * When asked to scan, this class gathers nodes into a heirarchy of tags.
 */
public class CompositeTagScanner extends TagScanner
{
    /**
     * Determine whether to use JVM or NodeList stack.
     * This can be set to true to get the original behaviour of
     * recursion into composite tags on the JVM stack.
     * This may lead to StackOverFlowException problems in some cases
     * i.e. Windows.
     */
    private static final boolean mUseJVMStack = false;

    /**
     * Determine whether unexpected end tags should cause stack roll-up.
     * This can be set to true to get the original behaviour of gathering
     * end tags into whatever tag is open.
     * This can be expensive, but should only be needed in the presence of
     * bad HTML.
     */
    private static final boolean mLeaveEnds = false;

    /**
     * Create a composite tag scanner.
     */
    public CompositeTagScanner ()
    {
    }

    /**
     * Collect the children.
     * <p>An initial test is performed for an empty XML tag, in which case
     * the start tag and end tag of the returned tag are the same and it has
     * no children.<p>
     * If it's not an empty XML tag, the lexer is repeatedly asked for
     * subsequent nodes until an end tag is found or a node is encountered
     * that matches the tag ender set or end tag ender set.
     * In the latter case, a virtual end tag is created.
     * Each node found that is not the end tag is added to
     * the list of children. The end tag is special and not a child.<p>
     * Nodes that also have a CompositeTagScanner as their scanner are
     * recursed into, which provides the nested structure of an HTML page.
     * This method operates in two possible modes, depending on a private boolean.
     * It can recurse on the JVM stack, which has caused some overflow problems
     * in the past, or it can use the supplied stack argument to nest scanning
     * of child tags within itself. The former is left as an option in the code,
     * mostly to help subsequent modifiers visualize what the internal nesting
     * is doing.
     * @param tag The tag this scanner is responsible for.
     * @param lexer The source of subsequent nodes.
     * @param stack The parse stack. May contain pending tags that enclose
     * this tag.
     * @return The resultant tag (may be unchanged).
     */
    public Tag scan (Tag tag, Lexer lexer, NodeList stack) throws ParserException
    {
        Node node;
        Tag next;
        String name;
        Scanner scanner;
        Tag ret;
        
        ret = tag;

        if (ret.isEmptyXmlTag ())
            ret.setEndTag (ret);
        else
            do
            {
                node = lexer.nextNode (false);
                if (null != node)
                {
                    if (node instanceof Tag)
                    {
                        next = (Tag)node;
                        name = next.getTagName ();
                        // check for normal end tag
                        if (next.isEndTag () && name.equals (ret.getTagName ()))
                        {
                            ret.setEndTag (next);
                            node = null;
                        }
                        else if (isTagToBeEndedFor (ret, next)) // check DTD
                        {
                            // backup one node. insert a virtual end tag later
                            lexer.setPosition (next.getStartPosition ());
                            node = null;
                        }
                        else if (!next.isEndTag ())
                        {
                            // now recurse if there is a scanner for this type of tag
                            scanner = next.getThisScanner ();
                            if (null != scanner)
                            {
                                if (mUseJVMStack)
                                {   // JVM stack recursion
                                    node = scanner.scan (next, lexer, stack);
                                    addChild (ret, node);
                                }
                                else
                                {
                                    // fake recursion:
                                    if (scanner == this)
                                    {
                                        if (next.isEmptyXmlTag ())
                                        {
                                            next.setEndTag (next);
                                            finishTag (next, lexer);
                                            addChild (ret, next);
                                        }
                                        else
                                        {
                                            stack.add (ret);
                                            ret = next;
                                        }
                                    }
                                    else
                                    {   // normal recursion if switching scanners
                                        node = scanner.scan (next, lexer, stack);
                                        addChild (ret, node);
                                    }
                                }
                            }
                            else
                                addChild (ret, next);
                        }
                        else
                        {
                            if (!mUseJVMStack && !mLeaveEnds)
                            {
                                // Since all non-end tags are consumed by the
                                // previous clause, we're here because we have an
                                // end tag with no opening tag... this could be bad.
                                // There are two cases...
                                // 1) The tag hasn't been registered, in which case
                                // we just add it as a simple child, like it's
                                // opening tag
                                // 2) There may be an opening tag further up the
                                // parse stack that needs closing.
                                // So, we ask the factory for a node like this one
                                // (since end tags never have scanners) and see
                                // if it's scanner is a composite tag scanner.
                                // If it is we walk up the parse stack looking for
                                // something that needs this end tag to finish it.
                                // If there is something, we close off all the tags
                                // walked over and continue on as if nothing
                                // happened.
                                Vector attributes = new Vector ();
                                attributes.addElement (new Attribute (name, null));
                                Tag opener = lexer.getNodeFactory ().createTagNode (
                                    lexer.getPage (), next.getStartPosition (), next.getEndPosition (),
                                    attributes);

                                scanner = opener.getThisScanner ();
                                if ((null != scanner) && (scanner == this))
                                {
                                    // uh-oh
                                    int index = -1;
                                    for (int i = stack.size () - 1; (-1 == index) && (i >= 0); i--)
                                    {
                                        // short circuit here... assume everything on the stack has this as it's scanner
                                        // we'll need to stop if either of those conditions isn't met
                                        Tag boffo = (Tag)stack.elementAt (i);
                                        if (name.equals (boffo.getTagName ()))
                                            index = i;
                                        else if (isTagToBeEndedFor (boffo, next)) // check DTD
                                            index = i;
                                    }
                                    if (-1 != index)
                                    {
                                        // finish off the current one first
                                        finishTag (ret, lexer);
                                        addChild ((Tag)stack.elementAt (stack.size () - 1), ret);
                                        for (int i = stack.size () - 1; i > index; i--)
                                        {
                                            Tag fred = (Tag)stack.remove (i);
                                            finishTag (fred, lexer);
                                            addChild ((Tag)stack.elementAt (i - 1), fred);
                                        }
                                        ret = (Tag)stack.remove (index);
                                        node = null;
                                    }
                                    else
                                        addChild (ret, next); // default behaviour
                                }
                                else
                                    addChild (ret, next); // default behaviour
                            }
                            else
                                addChild (ret, next);
                        }
                    }
                    else
                    {
                        addChild (ret, node);
                        node.doSemanticAction ();
                    }
                }

                if (!mUseJVMStack)
                {
                    // handle coming out of fake recursion
                    if (null == node)
                    {
                        int depth = stack.size ();
                        if (0 != depth)
                        {
                            node = stack.elementAt (depth - 1);
                            if (node instanceof Tag)
                            {
                                Tag precursor = (Tag)node;
                                scanner = precursor.getThisScanner ();
                                if (scanner == this)
                                {
                                    stack.remove (depth - 1);
                                    finishTag (ret, lexer);
                                    addChild (precursor, ret);
                                    ret = precursor;
                                }
                                else
                                    node = null; // normal recursion
                            }
                            else
                                node = null; // normal recursion
                        }
                    }
                }
            }
            while (null != node);

        finishTag (ret, lexer);

        return (ret);
    }

    /**
     * Add a child to the given tag.
     * @param parent The parent tag.
     * @param child The child node.
     */
    protected void addChild (Tag parent, Node child)
    {
        if (null == parent.getChildren ())
            parent.setChildren (new NodeList ());
        child.setParent (parent);
        parent.getChildren ().add (child);
    }

    /**
     * Finish off a tag.
     * Perhap add a virtual end tag.
     * Set the end tag parent as this tag.
     * Perform the semantic acton.
     * @param tag The tag to finish off.
     * @param lexer A lexer positioned at the end of the tag.
     */
    protected void finishTag (Tag tag, Lexer lexer)
        throws
            ParserException
    {
        if (null == tag.getEndTag ())
            tag.setEndTag (createVirtualEndTag (tag, lexer, lexer.getPage (), lexer.getCursor ().getPosition ()));
        tag.getEndTag ().setParent (tag);
        tag.doSemanticAction ();
    }

    /**
     * Creates an end tag with the same name as the given tag.
     * @param tag The tag to end.
     * @param lexer The object containg the node factory.
     * @param page The page the tag is on (virtually).
     * @param position The offset into the page at which the tag is to
     * be anchored.
     * @return An end tag with the name '"/" + tag.getTagName()' and a start
     * and end position at the given position. The fact these positions are
     * equal may be used to distinguish it as a virtual tag later on.
     */
    protected Tag createVirtualEndTag (Tag tag, Lexer lexer, Page page, int position)
        throws
            ParserException
    {
        Tag ret;
        String name;
        Vector attributes;
        
        name = "/" + tag.getRawTagName ();
        attributes = new Vector ();
        attributes.addElement (new Attribute (name, (String)null));
        ret = lexer.getNodeFactory ().createTagNode (
                                    page, position, position, attributes);
        
        return (ret);
    }

    /**
     * Determine if the current tag should be terminated by the given tag.
     * Examines the 'enders' or 'end tag enders' lists of the current tag
     * for a match with the given tag. Which list is chosen depends on whether
     * tag is an end tag ('end tag enders') or not ('enders').
     * @param current The tag that might need to be ended.
     * @param tag The candidate tag that might end the current one.
     * @return <code>true</code> if the name of the given tag is a member of
     * the appropriate list.
     */
    public final boolean isTagToBeEndedFor (Tag current, Tag tag)
    {
        String name;
        String[] ends;
        boolean ret;

        ret = false;

        name = tag.getTagName ();
        if (tag.isEndTag ())
            ends = current.getEndTagEnders ();
        else
            ends = current.getEnders ();
        for (int i = 0; i < ends.length; i++)
            if (name.equalsIgnoreCase (ends[i]))
            {
                ret = true;
                break;
            }
        
        return (ret);
    }
}
