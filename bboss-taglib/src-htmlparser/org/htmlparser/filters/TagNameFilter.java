// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2003 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/filters/TagNameFilter.java,v $
// $Author: derrickoswald $
// $Date: 2005/05/15 11:49:04 $
// $Revision: 1.6 $
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

import java.util.Locale;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Tag;

/**
 * This class accepts all tags matching the tag name.
 */
public class TagNameFilter
    implements
        NodeFilter
{
    /**
     * The tag name to match.
     */
    protected String mName;

    /**
     * Creates a new instance of TagNameFilter.
     * With no name, this would always return <code>false</code>
     * from {@link #accept}.
     */
    public TagNameFilter ()
    {
        this ("");
    }

    /**
     * Creates a TagNameFilter that accepts tags with the given name.
     * @param name The tag name to match.
     */
    public TagNameFilter (String name)
    {
        mName = name.toUpperCase (Locale.ENGLISH);
    }

    /**
     * Get the tag name.
     * @return Returns the name of acceptable tags.
     */
    public String getName ()
    {
        return (mName);
    }

    /**
     * Set the tag name.
     * @param name The name of the tag to accept.
     */
    public void setName (String name)
    {
        mName = name;
    }

    /**
     * Accept nodes that are tags and have a matching tag name.
     * This discards non-tag nodes and end tags.
     * The end tags are available on the enclosing non-end tag.
     * @param node The node to check.
     * @return <code>true</code> if the tag name matches,
     * <code>false</code> otherwise.
     */
    public boolean accept (Node node)
    {
        return ((node instanceof Tag)
                && !((Tag)node).isEndTag ()
                && ((Tag)node).getTagName ().equals (mName));
    }
}
