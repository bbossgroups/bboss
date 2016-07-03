// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Somik Raha
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/StringNodeFactory.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/10 23:20:42 $
// $Revision: 1.13 $
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

import java.io.Serializable;
import org.htmlparser.lexer.Page;

import org.htmlparser.nodeDecorators.DecodingNode;
import org.htmlparser.nodeDecorators.EscapeCharacterRemovingNode;
import org.htmlparser.nodeDecorators.NonBreakingSpaceConvertingNode;

/**
 * @deprecated Use PrototypicalNodeFactory#setTextPrototype(Text)
 * <p>A more efficient implementation of affecting all string nodes, is to replace
 * the Text node prototype in the {@link PrototypicalNodeFactory} with a
 * custom TextNode that performs the required operation.</p>
 * <p>For example, if you were using:
 * <pre>
 * StringNodeFactory factory = new StringNodeFactory();
 * factory.setDecode(true);
 * </pre>
 * to decode all text issued from
 * {@link org.htmlparser.nodes.TextNode#toPlainTextString() Text.toPlainTextString()},
 * you would instead create a subclass of {@link org.htmlparser.nodes.TextNode TextNode}
 * and set it as the prototype for text node generation:
 * <pre>
 * PrototypicalNodeFactory factory = new PrototypicalNodeFactory ();
 * factory.setTextPrototype (new TextNode () {
 *     public String toPlainTextString()
 *     {
 *         return (org.htmlparser.util.Translate.decode (super.toPlainTextString ()));
 *     }
 * });
 * </pre>
 * Similar constructs apply to removing escapes and converting non-breaking
 * spaces, which were the examples previously provided.</p>
 * <p>Using a subclass avoids the wrapping and delegation inherent in the
 * decorator pattern, with subsequent improvements in processing speed
 * and memory usage.</p>
 */
public class StringNodeFactory
    extends
        PrototypicalNodeFactory
    implements
        Serializable
{
    /**
     * Flag to tell the parser to decode strings returned by StringNode's toPlainTextString.
     * Decoding occurs via the method, org.htmlparser.util.Translate.decode()
     */
    protected boolean mDecode;


    /**
     * Flag to tell the parser to remove escape characters, like \n and \t, returned by StringNode's toPlainTextString.
     * Escape character removal occurs via the method, org.htmlparser.util.ParserUtils.removeEscapeCharacters()
     */
    protected boolean mRemoveEscapes;

    /**
     * Flag to tell the parser to convert non breaking space (from \u00a0 to a space " ").
     * If true, this will happen inside StringNode's toPlainTextString.
     */
    protected boolean mConvertNonBreakingSpaces;
    
    public StringNodeFactory ()
    {
        mDecode = false;
        mRemoveEscapes = false;
        mConvertNonBreakingSpaces = false;
    }

    //
    // NodeFactory interface override
    //

    /**
     * Create a new string node.
     * @param page The page the node is on.
     * @param start The beginning position of the string.
     * @param end The ending positiong of the string.
     */
    public Text createStringNode (Page page, int start, int end)
    {
        Text ret;
        
        ret = super.createStringNode (page, start, end);
        if (getDecode ())
            ret = new DecodingNode (ret);
        if (getRemoveEscapes ())
            ret = new EscapeCharacterRemovingNode (ret);
        if (getConvertNonBreakingSpaces ())
            ret = new NonBreakingSpaceConvertingNode (ret);

        return (ret);
    }

    /**
     * Set the decoding state.
     * @param decode If <code>true</code>, string nodes decode text using {@link org.htmlparser.util.Translate#decode}.
     */
    public void setDecode (boolean decode)
    {
        mDecode = decode;
    }

    /**
     * Get the decoding state.
     * @return <code>true</code> if string nodes decode text.
     */
    public boolean getDecode ()
    {
        return (mDecode);
    }

    /**
     * Set the escape removing state.
     * @param remove If <code>true</code>, string nodes remove escape characters.
     */
    public void setRemoveEscapes (boolean remove)
    {
        mRemoveEscapes = remove;
    }

    /**
     * Get the escape removing state.
     * @return The removing state.
     */
    public boolean getRemoveEscapes ()
    {
        return (mRemoveEscapes);
    }

    /**
     * Set the non-breaking space replacing state.
     * @param convert If <code>true</code>, string nodes replace &semi;nbsp; characters with spaces.
     */
    public void setConvertNonBreakingSpaces (boolean convert)
    {
        mConvertNonBreakingSpaces = convert;
    }

    /**
     * Get the non-breaking space replacing state.
     * @return The replacing state.
     */
    public boolean getConvertNonBreakingSpaces ()
    {
        return (mConvertNonBreakingSpaces);
    }
}
