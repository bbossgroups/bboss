// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Somik Raha
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/tags/TableTag.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/10 23:20:45 $
// $Revision: 1.41 $
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

package org.htmlparser.tags;

import org.htmlparser.NodeFilter;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.IsEqualFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.NotFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.util.NodeList;

/**
 * A table tag.
 */
public class TableTag extends CompositeTag
{
    /**
     * The set of names handled by this tag.
     */
    private static final String[] mIds = new String[] {"TABLE"};

    /**
     * The set of end tag names that indicate the end of this tag.
     */
    private static final String[] mEndTagEnders = new String[] {"BODY", "HTML"};

    /**
     * Create a new table tag.
     */
    public TableTag ()
    {
    }

    /**
     * Return the set of names handled by this tag.
     * @return The names to be matched that create tags of this type.
     */
    public String[] getIds ()
    {
        return (mIds);
    }

    /**
     * Return the set of end tag names that cause this tag to finish.
     * @return The names of following end tags that stop further scanning.
     */
    public String[] getEndTagEnders ()
    {
        return (mEndTagEnders);
    }

    /**
     * Get the row tags within this table.
     * @return The rows directly contained by this table.
     */
    public TableRow[] getRows ()
    {
        NodeList kids;
        NodeClassFilter cls;
        HasParentFilter recursion;
        NodeFilter filter;
        TableRow[] ret;

        kids = getChildren ();
        if (null != kids)
        {
            cls = new NodeClassFilter (TableTag.class);
            recursion = new HasParentFilter (null);
            filter = new OrFilter (
                        new AndFilter (
                            cls, 
                            new IsEqualFilter (this)),
                        new AndFilter ( // recurse up the parent chain
                            new NotFilter (cls), // but not past the first table
                            recursion));
            recursion.setParentFilter (filter);
            kids = kids.extractAllNodesThatMatch (
                // it's a row, and has this table as it's enclosing table
                new AndFilter (
                    new NodeClassFilter (TableRow.class),
                    filter), true);
            ret = new TableRow[kids.size ()];
            kids.copyToNodeArray (ret);
        }
        else
            ret = new TableRow[0];
        
        return (ret);
    }

    /**
     * Get the number of rows in this table.
     * @return The number of rows in this table.
     * <em>Note: this is a a simple count of the number of {@.html <TR>} tags and
     * may be incorrect if the {@.html <TR>} tags span multiple rows.</em>
     */
    public int getRowCount ()
    {
        return (getRows ().length);
    }

    /**
     * Get the row at the given index.
     * @param index The row number (zero based) to get. 
     * @return The row for the given index.
     */
    public TableRow getRow (int index)
    {
        TableRow[] rows;
        TableRow ret;

        rows = getRows ();
        if (index < rows.length)
            ret = rows[index];
        else
            ret = null;
        
        return (ret);
    }

    /**
     * Return a string suitable for debugging display.
     * @return The table as HTML, sorry.
     */
    public String toString()
    {
        return
            "TableTag\n" +
            "********\n"+
            toHtml();
    }

}
