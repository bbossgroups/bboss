// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2003 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/filters/AndFilter.java,v $
// $Author: derrickoswald $
// $Date: 2005/06/14 10:37:33 $
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

package org.htmlparser.filters;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;

/**
 * Accepts nodes matching all of it's predicate filters (AND operation).
 */
public class AndFilter
    implements
        NodeFilter
{
    /**
     * The predicates that are to be and'ed together;
     */
    protected NodeFilter[] mPredicates;

    /**
     * Creates a new instance of an AndFilter.
     * With no predicates, this would always answer <code>true</code>
     * to {@link #accept}.
     * @see #setPredicates
     */
    public AndFilter ()
    {
        setPredicates (null);
    }

    /**
     * Creates an AndFilter that accepts nodes acceptable to both filters.
     * @param left One filter.
     * @param right The other filter.
     */
    public AndFilter (NodeFilter left, NodeFilter right)
    {
        NodeFilter[] predicates;

        predicates = new NodeFilter[2];
        predicates[0] = left;
        predicates[1] = right;
        setPredicates (predicates);
    }

    /**
     * Get the predicates used by this AndFilter.
     * @return The predicates currently in use.
     */
    public NodeFilter[] getPredicates ()
    {
        return (mPredicates);
    }

    /**
     * Set the predicates for this AndFilter.
     * @param predicates The list of predidcates to use in {@link #accept}.
     */
    public void setPredicates (NodeFilter[] predicates)
    {
        if (null == predicates)
            predicates = new NodeFilter[0];
        mPredicates = predicates;
    }

    //
    // NodeFilter interface
    //

    /**
     * Accept nodes that are acceptable to all of it's predicate filters.
     * @param node The node to check.
     * @return <code>true</code> if all the predicate filters find the node
     * is acceptable, <code>false</code> otherwise.
     */
    public boolean accept (Node node)
    {
        boolean ret;

        ret = true;

        for (int i = 0; ret && (i < mPredicates.length); i++)
            if (!mPredicates[i].accept (node))
                ret = false;

        return (ret);
    }
}
