// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/scanners/StyleScanner.java,v $
// $Author: derrickoswald $
// $Date: 2005/03/07 02:18:46 $
// $Revision: 1.39 $
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
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * The StyleScanner handles style elements.
 * It gathers all interior nodes into one undifferentiated string node.
 */
public class StyleScanner extends CompositeTagScanner
{
    /**
     * Create a style scanner.
     */
    public StyleScanner ()
    {
    }

    /**
     * Scan for style definitions.
     * Accumulates text from the page, until &lt;/[a-zA-Z] is encountered.
     * @param tag The tag this scanner is responsible for.
     * @param lexer The source of CDATA.
     * @param stack The parse stack, <em>not used</em>.
     */
    public Tag scan (Tag tag, Lexer lexer, NodeList stack)
        throws ParserException
    {
        Node content;
        int position;
        Node node;
        Attribute attribute;
        Vector vector;

//        content = lexer.parseCDATA ();
        content = lexer.parseStyleCDATA(false);
        position = lexer.getPosition ();
        node = lexer.nextNode (false);
        if (null != node)
            if (!(node instanceof Tag) || !(   ((Tag)node).isEndTag ()
                && ((Tag)node).getTagName ().equals (tag.getIds ()[0])))
            {
                lexer.setPosition (position);
                node = null;
            }

        // build new end tag if required
        if (null == node)
        {
            attribute = new Attribute ("/style", null);
            vector = new Vector ();
            vector.addElement (attribute);
            node = lexer.getNodeFactory ().createTagNode (
                lexer.getPage (), position, position, vector);
        }
        tag.setEndTag ((Tag)node);
        if (null != content)
        {
            tag.setChildren (new NodeList (content));
            System.out.println(content.getText());
            content.setParent (tag);
        }
        node.setParent (tag);
        tag.doSemanticAction ();

        return (tag);
    }
}
