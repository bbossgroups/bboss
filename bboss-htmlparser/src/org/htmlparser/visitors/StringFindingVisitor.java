// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Joshua Kerievsky
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/visitors/StringFindingVisitor.java,v $
// $Author: derrickoswald $
// $Date: 2004/05/24 16:18:36 $
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

import java.util.Locale;

import org.htmlparser.Text;

public class StringFindingVisitor extends NodeVisitor
{
    private String stringToFind;
    private int foundCount;
    private boolean multipleSearchesWithinStrings;
    private Locale locale;

    public StringFindingVisitor(String stringToFind)
    {
        this (stringToFind, null);
    }

    public StringFindingVisitor(String stringToFind, Locale locale)
    {
        this.locale = (null == locale) ? Locale.ENGLISH : locale;
        this.stringToFind = stringToFind.toUpperCase (this.locale);
        foundCount = 0;
        multipleSearchesWithinStrings = false;
    }

    public void doMultipleSearchesWithinStrings()
    {
        multipleSearchesWithinStrings = true;
    }

    public void visitStringNode(Text stringNode)
    {
        String stringToBeSearched = stringNode.getText().toUpperCase(locale);
        if (!multipleSearchesWithinStrings &&
            stringToBeSearched.indexOf(stringToFind) != -1) {
            foundCount++;
        } else if (multipleSearchesWithinStrings) {
            int index = -1;
            do {
                index = stringToBeSearched.indexOf(stringToFind, index+1);
                if (index!=-1)
                    foundCount++;
            } while (index != -1);
        }
    }

    public boolean stringWasFound()
    {
        return (0 != stringFoundCount());
    }

    public int stringFoundCount()
    {
        return foundCount;
    }

}
