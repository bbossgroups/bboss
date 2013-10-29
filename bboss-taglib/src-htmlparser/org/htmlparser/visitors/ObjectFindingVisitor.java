// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Joshua Kerievsky
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/visitors/ObjectFindingVisitor.java,v $
// $Author: derrickoswald $
// $Date: 2004/05/24 00:38:19 $
// $Revision: 1.40 $
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

import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.util.NodeList;

public class ObjectFindingVisitor extends NodeVisitor {
    private Class classTypeToFind;
    private NodeList tags;

    public ObjectFindingVisitor(Class classTypeToFind) {
        this(classTypeToFind,true);
    }

    public ObjectFindingVisitor(Class classTypeToFind,boolean recurse) {
        super(recurse, true);
        this.classTypeToFind = classTypeToFind;
        this.tags = new NodeList();
    }

    public int getCount() {
        return (tags.size ());
    }

    public void visitTag(Tag tag) {
        if (tag.getClass().equals(classTypeToFind))
            tags.add(tag);
    }

    public Node[] getTags() {
        return tags.toNodeArray();
    }
}
