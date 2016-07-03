// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Somik Raha
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/visitors/TextExtractingVisitor.java,v $
// $Author: derrickoswald $
// $Date: 2004/05/24 16:18:36 $
// $Revision: 1.42 $
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

import org.htmlparser.Text;
import org.htmlparser.Tag;
import org.htmlparser.util.Translate;


/**
 * Extracts text from a web page.
 * Usage:
 * <code>
 * Parser parser = new Parser(...);
 * TextExtractingVisitor visitor = new TextExtractingVisitor();
 * parser.visitAllNodesWith(visitor);
 * String textInPage = visitor.getExtractedText();
 * </code>
 */
public class TextExtractingVisitor extends NodeVisitor {
    private StringBuffer textAccumulator;
    private boolean preTagBeingProcessed;

    public TextExtractingVisitor() {
        textAccumulator = new StringBuffer();
        preTagBeingProcessed = false;
    }

    public String getExtractedText() {
        return textAccumulator.toString();
    }

    public void visitStringNode(Text stringNode) {
        String text = stringNode.getText();
        if (!preTagBeingProcessed) {
            text = Translate.decode(text);
            text = replaceNonBreakingSpaceWithOrdinarySpace(text);
        }
        textAccumulator.append(text);
    }

    private String replaceNonBreakingSpaceWithOrdinarySpace(String text) {
        return text.replace('\u00a0',' ');
    }

    public void visitTag(Tag tag)
    {
        if (isPreTag(tag))
            preTagBeingProcessed = true;
    }

    public void visitEndTag(Tag tag)
    {
        if (isPreTag(tag))
            preTagBeingProcessed = false;
    }

    private boolean isPreTag(Tag tag) {
        return tag.getTagName().equals("PRE");
    }

}
