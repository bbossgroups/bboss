// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/util/SpecialHashtable.java,v $
// $Author: derrickoswald $
// $Date: 2004/01/02 16:24:58 $
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

package org.htmlparser.util;

import java.util.Hashtable;

/**
 * Acts like a regular HashTable, except some values are translated in get(String).
 * Specifically, <code>Tag.NULLVALUE</code> is translated to <code>null</code> and
 * <code>Tag.NOTHING</code> is translated to <code>""</code>.
 * This is done for backwards compatibility, users are expecting a HashTable,
 * but Tag.toHTML needs to know when there is no attribute value (&lt;<TAG ATTRIBUTE&gt;)
 * and when the value was not present (&lt;<TAG ATTRIBUTE=&gt;).
 */
public class SpecialHashtable extends Hashtable
{
    /**
     * Special key for the tag name.
     */
    public final static String TAGNAME = "$<TAGNAME>$";

    /**
     * Special value for a null attribute value.
     */
    public final static String NULLVALUE = "$<NULL>$";

    /**
     * Special value for an empty attribute value.
     */
    public final static String NOTHING = "$<NOTHING>$";

    /**
     * Constructs a new, empty hashtable with a default initial capacity (11)
     * and load factor, which is 0.75.
     */
    public SpecialHashtable ()
    {
        super ();
    }

    /**
     * Constructs a new, empty hashtable with the specified initial capacity
     * and default load factor, which is 0.75.
     */
    public SpecialHashtable (int initialCapacity)
    {
        super (initialCapacity);
    }

    /**
     * Constructs a new, empty hashtable with the specified initial capacity
     * and the specified load factor.
     */
    public SpecialHashtable (int initialCapacity, float loadFactor)
    {
        super (initialCapacity, loadFactor);
    }

    /**
     * Returns the value to which the specified key is mapped in this hashtable.
     * This is translated to provide backwards compatibility.
     * @return The translated value of the attribute. <em>This will be
     * <code>null</code> if the attribute is a stand-alone attribute.</em>
     */
    public Object get (Object key)
    {
        Object ret;

        ret = getRaw (key);
        if (NULLVALUE == ret)
            ret = null;
        else if (NOTHING == ret)
            ret = "";

        return (ret);
    }

    /**
     * Returns the raw (untranslated) value to which the specified key is
     * mapped in this hashtable.
     */
    public Object getRaw (Object key)
    {
        return (super.get (key));
    }
}
