// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2005 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/parserapplications/filterbuilder/HtmlTreeCellRenderer.java,v $
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

import java.awt.Component;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.lexer.Cursor;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.Translate;

/**
 * Renderer for tree view of a NodeList.
 */
public class HtmlTreeCellRenderer
    extends
        DefaultTreeCellRenderer
    implements
        TreeCellRenderer
{
    /**
     * Create a new tree cell renderer for Nodes.
     */
    public HtmlTreeCellRenderer ()
    {
        setLeafIcon (null);
        setClosedIcon (null);
        setOpenIcon (null);
    }

    /**
     * Render the tag as HTML.
     * This is different from the tag's normal toHtml() method in that it
     * doesn't process children or end tags, just the initial tag, and
     * it also wraps the tag in html a label would expect.
     * @see org.htmlparser.Node#toHtml()
     * @param tag The tag to convert to HTML.
     * @return A string suitable for rendering the tag.
     */
    public String toHtml (TagNode tag)
    {
        int length;
        int size;
        Vector attributes;
        Attribute attribute;
        String s;
        boolean children;
        StringBuffer ret;

        length = 2;
        attributes = tag.getAttributesEx ();
        size = attributes.size ();
        for (int i = 0; i < size; i++)
        {
            attribute = (Attribute)attributes.elementAt (i);
            length += attribute.getLength ();
        }
        ret = new StringBuffer (length);
        ret.append ("<");
        for (int i = 0; i < size; i++)
        {
            attribute = (Attribute)attributes.elementAt (i);
            attribute.toString (ret);
        }
        ret.append (">");
        s = Translate.encode (ret.toString ());
        children = null != tag.getChildren ();
        ret = new StringBuffer (s.length () + 13 + (children ? 16 : 0));
        ret.append ("<html>");
        if (children)
            ret.append ("<font color=\"blue\">");
        ret.append (s);
        if (children)
            ret.append ("</font>");
        ret.append ("</html>");

        return (ret.toString ());
    }
    
    /**
     * Express this string node as a printable string
     * This is suitable for display in a debugger or output to a printout.
     * Control characters are replaced by their equivalent escape
     * sequence and contents is truncated to 80 characters.
     * @param node The node to render.
     * @return A string representation of the string node.
     */
    public String toText (TextNode node)
    {
        int startpos;
        int endpos;
        String s;
        char c;
        StringBuffer ret;

        startpos = node.getStartPosition ();
        endpos = node.getEndPosition ();
        ret = new StringBuffer (endpos - startpos + 20);
        s = node.toHtml ();
        for (int i = 0; i < s.length (); i++)
        {
            c = s.charAt (i);
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

        return (ret.toString ());
    }

    /**
     * Render the node for the tree cell.
     * @see TreeCellRenderer#getTreeCellRendererComponent(JTree, Object, boolean, boolean, boolean, int, boolean)
     * @param tree {@inheritDoc}
     * @param value {@inheritDoc}
     * @param selected {@inheritDoc}
     * @param expanded {@inheritDoc}
     * @param leaf {@inheritDoc}
     * @param row {@inheritDoc}
     * @param hasFocus {@inheritDoc}
     * @return {@inheritDoc}
     */
    public Component getTreeCellRendererComponent (JTree tree, Object value,
            boolean selected, boolean expanded, boolean leaf, int row,
            boolean hasFocus)
    {
        Node node;

        super.getTreeCellRendererComponent (tree, value, selected, expanded, leaf, row, hasFocus);
        node = (Node)value;
        if (node instanceof TagNode)
            setText (toHtml ((TagNode)node));
        else if (node instanceof TextNode)
            setText (toText ((TextNode)node));
        else
            setText (node.toHtml ());

        return (this);
    }

}
