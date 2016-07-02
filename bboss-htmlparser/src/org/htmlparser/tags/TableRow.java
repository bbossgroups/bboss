// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Somik Raha
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/tags/TableRow.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/10 23:20:45 $
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
 * A table row tag.
 */
public class TableRow extends CompositeTag
{
    /**
     * The set of names handled by this tag.
     */
    private static final String[] mIds = new String[] {"TR"};

    /**
     * The set of end tag names that indicate the end of this tag.
     */
    private static final String[] mEndTagEnders = new String[] {"TABLE"};

    /**
     * Create a new table row tag.
     */
    public TableRow ()
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
     * Return the set of tag names that cause this tag to finish.
     * @return The names of following tags that stop further scanning.
     */
    public String[] getEnders ()
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
     * Get the column tags within this <code>TR</code> (table row) tag.
     * @return The {@.html <TD>} tags contained by this tag.
     */
    public TableColumn[] getColumns ()
    {
        NodeList kids;
        NodeClassFilter cls;
        HasParentFilter recursion;
        NodeFilter filter;
        TableColumn[] ret;

        kids = getChildren ();
        if (null != kids)
        {
            cls = new NodeClassFilter (TableRow.class);
            recursion = new HasParentFilter (null);
            filter = new OrFilter (
                        new AndFilter (
                            cls, 
                            new IsEqualFilter (this)),
                        new AndFilter ( // recurse up the parent chain
                            new NotFilter (cls), // but not past the first row
                            recursion));
            recursion.setParentFilter (filter);
            kids = kids.extractAllNodesThatMatch (
                // it's a column, and has this row as it's enclosing row
                new AndFilter (
                    new NodeClassFilter (TableColumn.class),
                    filter), true);
            ret = new TableColumn[kids.size ()];
            kids.copyToNodeArray (ret);
        }
        else
            ret = new TableColumn[0];
        
        return (ret);
    }

    /**
     * Get the number of columns in this row.
     * @return The number of columns in this row.
     * <em>Note: this is a a simple count of the number of {@.html <TD>} tags and
     * may be incorrect if the {@.html <TD>} tags span multiple columns.</em>
     */
    public int getColumnCount ()
    {
        return (getColumns ().length);
    }

    /**
     * Get the header of this table
     * @return Table header tags contained in this row.
     */
    public TableHeader[] getHeaders ()
    {
        NodeList kids;
        NodeClassFilter cls;
        HasParentFilter recursion;
        NodeFilter filter;
        TableHeader[] ret;

        kids = getChildren ();
        if (null != kids)
        {
            cls = new NodeClassFilter (TableRow.class);
            recursion = new HasParentFilter (null);
            filter = new OrFilter (
                        new AndFilter (
                            cls, 
                            new IsEqualFilter (this)),
                        new AndFilter ( // recurse up the parent chain
                            new NotFilter (cls), // but not past the first row
                            recursion));
            recursion.setParentFilter (filter);
            kids = kids.extractAllNodesThatMatch (
                // it's a header, and has this row as it's enclosing row
                new AndFilter (
                    new NodeClassFilter (TableHeader.class),
                    filter), true);
            ret = new TableHeader[kids.size ()];
            kids.copyToNodeArray (ret);
        }
        else
            ret = new TableHeader[0];
        
        return (ret);
    }

    /**
     * Get the number of headers in this row.
     * @return The count of header tags in this row.
     */
    public int getHeaderCount ()
    {
        return (getHeaders ().length);
    }

    /**
     * Checks if this table has a header
     * @return <code>true</code> if there is a header tag.
     */
    public boolean hasHeader ()
    {
        return (0 != getHeaderCount ());
    }
}
