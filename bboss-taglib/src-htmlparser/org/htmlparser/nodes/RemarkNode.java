// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/nodes/RemarkNode.java,v $
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

import org.htmlparser.Remark;
import org.htmlparser.lexer.Cursor;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

/**
 * The remark tag is identified and represented by this class.
 */
public class RemarkNode
    extends
        AbstractNode
    implements
        Remark
{
    /**
     * The contents of the remark node, or override text.
     */
    protected String mText;

    /**
     * Constructor takes in the text string.
     * @param text The string node text. For correct generation of HTML, this
     * should not contain representations of tags (unless they are balanced).
     */
    public RemarkNode (String text)
    {
        super (null, 0, 0);
        setText (text);
    }

    /**
     * Constructor takes in the page and beginning and ending posns.
     * @param page The page this remark is on.
     * @param start The beginning position of the remark.
     * @param end The ending positiong of the remark.
     */
    public RemarkNode (Page page, int start, int end)
    {
        super (page, start, end);
        mText = null;
    }

    /**
     * Returns the text contents of the comment tag.
     * @return The contents of the text inside the comment delimiters.
     */
    public String getText()
    {
        int start;
        int end;
        String ret;

        if (null == mText)
        {
            start = getStartPosition () + 4; // <!--
            end = getEndPosition () - 3; // -->
            if (start >= end)
                ret = "";
            else
                ret = mPage.getText (start, end);
        }
        else
            ret = mText;

        return (ret);
    }

    /**
     * Sets the string contents of the node.
     * If the text has the remark delimiters (&lt;!-- --&gt;), these are stripped off.
     * @param text The new text for the node.
     */
    public void setText (String text)
    {
        mText = text;
        if (text.startsWith ("<!--") && text.endsWith ("-->"))
            mText = text.substring (4, text.length () - 3);
        nodeBegin = 0;
        nodeEnd = mText.length ();
    }

    /**
     * Return the remark text.
     * @return The HTML comment.
     */
    public String toPlainTextString ()
    {
        return (getText());
    }

    /**
     * Return The full HTML remark.
     * @return The comment, i.e. {@.html <!-- this is a comment -->}.
     */
    public String toHtml ()
    {
        StringBuffer buffer;
        String ret;
        
        if (null == mText)
        {
        	if(!this.isresource)
        		ret = mPage.getText (getStartPosition (), getEndPosition ());
        	else
        	{
        		buffer = new StringBuffer();
        		buffer.append("<").append(mPage.getText (getStartPosition ()+1, getEndPosition () -1)).append(">");
        		ret = buffer.toString();
        	}
        }
        else
        {
            buffer = new StringBuffer (mText.length () + 7);
            buffer.append ("<!--");
            buffer.append (mText);
            buffer.append ("-->");
            ret = buffer.toString ();
        }

        return (ret);
    }

    /**
     * Print the contents of the remark tag.
     * This is suitable for display in a debugger or output to a printout.
     * Control characters are replaced by their equivalent escape
     * sequence and contents is truncated to 80 characters.
     * @return A string representation of the remark node.
     */
    public String toString()
    {
        int startpos;
        int endpos;
        Cursor start;
        Cursor end;
        char c;
        StringBuffer ret;

        startpos = getStartPosition ();
        endpos = getEndPosition ();
        ret = new StringBuffer (endpos - startpos + 20);
        if (null == mText)
        {
            start = new Cursor (getPage (), startpos);
            end = new Cursor (getPage (), endpos);
            ret.append ("Rem (");
            ret.append (start);
            ret.append (",");
            ret.append (end);
            ret.append ("): ");
            start.setPosition (startpos + 4); // <!--
            endpos -= 3; // -->
            while (start.getPosition () < endpos)
            {
                try
                {
                    c = mPage.getCharacter (start);
                    switch (c)
                    {
                        case '\t':
                            ret.append ("\\t");
                            break;
                        case '\n':
                            ret.append ("\\n");
                            break;
                        case '\r':
                            ret.append ("\\r");
                            break;
                        default:
                            ret.append (c);
                    }
                }
                catch (ParserException pe)
                {
                    // not really expected, but we're only doing toString, so ignore
                }
                if (77 <= ret.length ())
                {
                    ret.append ("...");
                    break;
                }
            }
        }
        else
        {
            ret.append ("Rem (");
            ret.append (startpos);
            ret.append (",");
            ret.append (endpos);
            ret.append ("): ");
            for (int i = 0; i < mText.length (); i++)
            {
                c = mText.charAt (i);
                switch (c)
                {
                    case '\t':
                        ret.append ("\\t");
                        break;
                    case '\n':
                        ret.append ("\\n");
                        break;
                    case '\r':
                        ret.append ("\\r");
                        break;
                    default:
                        ret.append (c);
                }
                if (77 <= ret.length ())
                {
                    ret.append ("...");
                    break;
                }
            }
        }

        return (ret.toString ());
    }

    /**
     * Remark visiting code.
     * @param visitor The <code>NodeVisitor</code> object to invoke 
     * <code>visitRemarkNode()</code> on.
     */
    public void accept (NodeVisitor visitor)
    {
        visitor.visitRemarkNode (this);
    }
}
