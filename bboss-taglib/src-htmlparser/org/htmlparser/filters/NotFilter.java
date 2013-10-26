// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2003 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/filters/NotFilter.java,v $
// $Author: derrickoswald $
// $Date: 2005/05/15 11:49:04 $
// $Revision: 1.5 $
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
 * Accepts all nodes not acceptable to it's predicate filter.
 */
public class NotFilter implements NodeFilter
{
    /**
     * The filter to gainsay.
     */
    protected NodeFilter mPredicate;

    /**
     * Creates a new instance of a NotFilter.
     * With no predicates, this would always return <code>false</code>
     * from {@link #accept}.
     * @see #setPredicate
     */
    public NotFilter ()
    {
        setPredicate (null);
    }

    /**
     * Creates a NotFilter that accepts nodes not acceptable to the predicate.
     * @param predicate The filter to consult.
     */
    public NotFilter (NodeFilter predicate)
    {
        setPredicate (predicate);
    }

    /**
     * Get the predicate used by this NotFilter.
     * @return The predicate currently in use.
     */
    public NodeFilter getPredicate ()
    {
        return (mPredicate);
    }

    /**
     * Set the predicate for this NotFilter.
     * @param predicate The predidcate to use in {@link #accept}.
     */
    public void setPredicate (NodeFilter predicate)
    {
        mPredicate = predicate;
    }

    //
    // NodeFilter interface
    //

    /**
     * Accept nodes that are not acceptable to the predicate filter.
     * @param node The node to check.
     * @return <code>true</code> if the node is not acceptable to the
     * predicate filter, <code>false</code> otherwise.
     */
    public boolean accept (Node node)
    {
        return ((null != mPredicate) && !mPredicate.accept (node));
    }
}
