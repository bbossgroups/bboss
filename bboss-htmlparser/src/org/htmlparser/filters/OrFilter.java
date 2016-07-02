// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2003 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/filters/OrFilter.java,v $
// $Author: derrickoswald $
// $Date: 2005/05/15 11:49:04 $
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
 * Accepts nodes matching any of it's predicates filters (OR operation).
 */
public class OrFilter implements NodeFilter
{
    /**
     * The predicates that are to be or'ed together;
     */
    protected NodeFilter[] mPredicates;

    /**
     * Creates a new instance of an OrFilter.
     * With no predicates, this would always answer <code>false</code>
     * to {@link #accept}.
     * @see #setPredicates
     */
    public OrFilter ()
    {
        setPredicates (null);
    }

    /**
     * Creates an OrFilter that accepts nodes acceptable to either filter.
     * @param left One filter.
     * @param right The other filter.
     */
    public OrFilter (NodeFilter left, NodeFilter right)
    {
        NodeFilter[] predicates;

        predicates = new NodeFilter[2];
        predicates[0] = left;
        predicates[1] = right;
        setPredicates (predicates);
    }

    /**
     * Get the predicates used by this OrFilter.
     * @return The predicates currently in use.
     */
    public NodeFilter[] getPredicates ()
    {
        return (mPredicates);
    }

    /**
     * Set the predicates for this OrFilter.
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
     * Accept nodes that are acceptable to any of it's predicate filters.
     * @param node The node to check.
     * @return <code>true</code> if any of the predicate filters find the node
     * is acceptable, <code>false</code> otherwise.
     */
    public boolean accept (Node node)
    {
        boolean ret;

        ret = false;

        for (int i = 0; !ret && (i < mPredicates.length); i++)
            if (mPredicates[i].accept (node))
                ret = true;

        return (ret);
    }
}
