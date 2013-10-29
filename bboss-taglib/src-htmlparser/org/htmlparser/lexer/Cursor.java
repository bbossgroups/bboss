// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/lexer/Cursor.java,v $
// $Author: derrickoswald $
// $Date: 2005/05/15 11:49:04 $
// $Revision: 1.20 $
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

package org.htmlparser.lexer;

import java.io.Serializable;
import org.htmlparser.util.sort.Ordered;

/**
 * A bookmark in a page.
 * This class remembers the page it came from and its position within the page.
 */
public class Cursor
    implements
        Serializable,
        Ordered,
        Cloneable
{
    /**
     * This cursor's position.
     */
    protected int mPosition;

    /**
     * This cursor's page.
     */
    protected Page mPage;

    /**
     * Construct a <code>Cursor</code> from the page and position given.
     * @param page The page this cursor is on.
     * @param offset The character offset within the page.
     */
    public Cursor (Page page, int offset)
    {
        mPage = page;
        mPosition = offset;
    }

    /**
     * Get this cursor's page.
     * @return The page associated with this cursor.
     */
    public Page getPage ()
    {
        return (mPage);
    }

    /**
     * Get the position of this cursor.
     * @return The cursor position.
     */
    public int getPosition ()
    {
        return (mPosition);
    }

    /**
     * Set the position of this cursor.
     * @param position The new cursor position.
     */
    public void setPosition (int position)
    {
        mPosition = position;
    }

    /**
     * Move the cursor position ahead one character.
     */
    public void advance ()
    {
        mPosition++;
    }

    /**
     * Move the cursor position back one character.
     */
    public void retreat ()
    {
        mPosition--;
        if (0 > mPosition)
            mPosition = 0;
    }

    /**
     * Make a new cursor just like this one.
     * @return The new cursor positioned where <code>this</code> one is,
     * and referring to the same page.
     */
    public Cursor dup ()
    {
        try
        {
            return ((Cursor)clone ());
        }
        catch (CloneNotSupportedException cnse)
        {
            return (new Cursor (getPage (), getPosition ()));
        }
    }

    /**
     * Return a string representation of this cursor
     * @return A string of the form "n[r,c]", where n is the character position,
     * r is the row (zero based) and c is the column (zero based) on the page.
     */
    public String toString ()
    {
        StringBuffer ret;

        ret = new StringBuffer (9 * 3 + 3); // three ints and delimiters
        ret.append (getPosition ());
        ret.append ("[");
        if (null != mPage)
            ret.append (mPage.row (this));
        else
            ret.append ("?");
        ret.append (",");
        if (null != mPage)
            ret.append (mPage.column (this));
        else
            ret.append ("?");
        ret.append ("]");

        return (ret.toString ());
    }

    //
    // Ordered interface
    //

    /**
     * Compare one reference to another.
     * @param that The object to compare this to.
     * @return A negative integer, zero, or a positive
     * integer as this object is less than, equal to,
     * or greater than that object.
     * @see org.htmlparser.util.sort.Ordered
     */
    public int compare (Object that)
    {
        Cursor r = (Cursor)that;
        return (getPosition () - r.getPosition ());
    }
}
