// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Somik Raha
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/util/IteratorImpl.java,v $
// $Author: derrickoswald $
// $Date: 2005/03/13 14:51:46 $
// $Revision: 1.43 $
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

package org.htmlparser.util;

import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.lexer.Cursor;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.scanners.Scanner;
import org.htmlparser.util.NodeIterator;

public class IteratorImpl implements NodeIterator
{
    Lexer mLexer;
    ParserFeedback mFeedback;
    Cursor mCursor;

    public IteratorImpl (Lexer lexer, ParserFeedback fb)
    {
        mLexer = lexer;
        mFeedback = fb;
        mCursor = new Cursor (mLexer.getPage (), 0);
    }

    /**
     * Check if more nodes are available.
     * @return <code>true</code> if a call to <code>nextNode()</code> will succeed.
     */
    public boolean hasMoreNodes() throws ParserException
    {
        boolean ret;

        mCursor.setPosition (mLexer.getPosition ());
        ret = Page.EOF != mLexer.getPage ().getCharacter (mCursor); // more characters?

        return (ret);
    }

    /**
     * Get the next node.
     * @return The next node in the HTML stream, or null if there are no more nodes.
     * @exception ParserException If an unrecoverable error occurs.
     */
    public Node nextNode () throws ParserException
    {
        Tag tag;
        Scanner scanner;
        NodeList stack;
        Node ret;

        try
        {
            ret = mLexer.nextNode ();
            if (null != ret)
            {
                // kick off recursion for the top level node
                if (ret instanceof Tag)
                {
                    tag = (Tag)ret;
                    if (!tag.isEndTag ())
                    {
                        // now recurse if there is a scanner for this type of tag
                        scanner = tag.getThisScanner ();
                        if (null != scanner)
                        {
                            stack = new NodeList ();
                            ret = scanner.scan (tag, mLexer, stack);
                        }
                    }
                }
            }
        }
        catch (ParserException pe)
        {
            throw pe; // no need to wrap an existing ParserException
        }
        catch (Exception e)
        {
            StringBuffer msgBuffer = new StringBuffer ();
            msgBuffer.append ("Unexpected Exception occurred while reading ");
            msgBuffer.append (mLexer.getPage ().getUrl ());
            msgBuffer.append (", in nextNode");
            // TODO: appendLineDetails (msgBuffer);
            ParserException ex = new ParserException (msgBuffer.toString (), e);
            mFeedback.error (msgBuffer.toString (), ex);
            throw ex;
        }
        
        return (ret);
    }
}
