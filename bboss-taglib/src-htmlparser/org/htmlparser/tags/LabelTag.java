// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Dhaval Udani
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/tags/LabelTag.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/10 23:20:45 $
// $Revision: 1.36 $
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

/**
 * A label tag.
 */
public class LabelTag extends CompositeTag
{
    /**
     * The set of names handled by this tag.
     */
    private static final String[] mIds = new String[] {"LABEL"};

    /**
     * Create a new label tag.
     */
    public LabelTag ()
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
     * Returns the text contained inside this label tag.
     * @return The textual contents between the {@.html <LABEL></LABEL>} pair.
     */
    public String getLabel()
    {
        return toPlainTextString();
    }

    /**
     * Returns a string representation of this label tag suitable for debugging.
     * @return A string representing this label.
     */
    public String toString()
    {
        return "LABEL: "+ getLabel();
    }
}
