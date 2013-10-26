// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2003 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/NodeFactory.java,v $
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

package org.htmlparser;

import java.util.Vector;

import org.htmlparser.lexer.Page;
import org.htmlparser.util.ParserException;

/**
 * This interface defines the methods needed to create new nodes.
 * <p>The factory is used when lexing to generate the nodes passed
 * back to the caller. By implementing this interface, and setting
 * that concrete object as the node factory for the
 * {@link org.htmlparser.lexer.Lexer#setNodeFactory lexer} (perhaps via the
 * {@link Parser#setNodeFactory parser}), the way that nodes are generated
 * can be customized.</p>
 * <p>In general, replacing the factory with a custom factory is not required
 * because of the flexibility of the {@link PrototypicalNodeFactory}.</p>
 * <p>Creation of Text and Remark nodes is straight forward, because essentially
 * they are just sequences of characters extracted from the page. Creation of a
 * Tag node requires that the attributes from the tag be remembered as well.
 * @see PrototypicalNodeFactory
 */
public interface NodeFactory
{
    /**
     * Create a new text node.
     * @param page The page the node is on.
     * @param start The beginning position of the string.
     * @param end The ending positiong of the string.
     * @throws ParserException If there is a problem encountered
     * when creating the node.
     * @return A text node comprising the indicated characters from the page.
     */
    Text createStringNode (Page page, int start, int end)
        throws
            ParserException;

    /**
     * Create a new remark node.
     * @param page The page the node is on.
     * @param start The beginning position of the remark.
     * @param end The ending positiong of the remark.
     * @throws ParserException If there is a problem encountered
     * when creating the node.
     * @return A remark node comprising the indicated characters from the page.
     */
    Remark createRemarkNode (Page page, int start, int end)
        throws
            ParserException;

    /**
     * Create a new tag node.
     * Note that the attributes vector contains at least one element,
     * which is the tag name (standalone attribute) at position zero.
     * This can be used to decide which type of node to create, or
     * gate other processing that may be appropriate.
     * @param page The page the node is on.
     * @param start The beginning position of the tag.
     * @param end The ending positiong of the tag.
     * @param attributes The attributes contained in this tag.
     * @throws ParserException If there is a problem encountered
     * when creating the node.
     * @return A tag node comprising the indicated characters from the page.
     */
    Tag createTagNode (Page page, int start, int end, Vector attributes)
        throws
            ParserException;
}
