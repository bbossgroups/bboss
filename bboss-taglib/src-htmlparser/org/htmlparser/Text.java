// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/Text.java,v $
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

/**
 * This interface represents a piece of the content of the HTML document.
 */
public interface Text
    extends
        Node
{
    /**
     * Accesses the textual contents of the node.
     * @return The text of the node.
     */
    String getText ();

    /**
     * Sets the contents of the node.
     * @param text The new text for the node.
     */
    void setText (String text);

    //
    // Node interface
    //

//    public void accept (NodeVisitor visitor)
//    {
//    }
//
//    public void collectInto (.NodeList collectionList, NodeFilter filter)
//    {
//    }
//
//    public void doSemanticAction () throws ParserException
//    {
//    }
//
//    public NodeList getChildren ()
//    {
//    }
//
//    public int getEndPosition ()
//    {
//    }
//
//    public Node getParent ()
//    {
//    }
//
//    public int getStartPosition ()
//    {
//    }
//
//    public String getText ()
//    {
//    }
//
//    public void setChildren (NodeList children)
//    {
//    }
//
//    public void setEndPosition (int position)
//    {
//    }
//
//    public void setParent (Node node)
//    {
//    }
//
//    public void setStartPosition (int position)
//    {
//    }
//
//    public void setText (String text)
//    {
//    }
//
//    public String toHtml ()
//    {
//    }
//
//    public String toPlainTextString ()
//    {
//    }
}
