// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Somik Raha
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/visitors/HtmlPage.java,v $
// $Author: derrickoswald $
// $Date: 2004/05/24 00:38:19 $
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

package org.htmlparser.visitors;

import org.htmlparser.Parser;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.TableTag;
import org.htmlparser.Tag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;

public class HtmlPage extends NodeVisitor {
    private String title;
    private NodeList nodesInBody;
    private NodeList tables;

    public HtmlPage(Parser parser) {
        super(true);
        title = "";
        nodesInBody = new NodeList();
        tables = new NodeList();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void visitTag(Tag tag)
    {
        if (isTable(tag))
            tables.add(tag);
        else if (isBodyTag(tag))
            nodesInBody = tag.getChildren ();
        else if (isTitleTag(tag))
            title = ((TitleTag)tag).getTitle();
    }

    private boolean isTable(Tag tag)
    {
        return (tag instanceof TableTag);
    }

    private boolean isBodyTag(Tag tag)
    {
        return (tag instanceof BodyTag);
    }

    private boolean isTitleTag(Tag tag)
    {
        return (tag instanceof TitleTag);
    }

    public NodeList getBody() {
        return nodesInBody;
    }

    public TableTag [] getTables()
    {
        TableTag [] tableArr = new TableTag[tables.size()];
        tables.copyToNodeArray (tableArr);
        return tableArr;
    }
}
