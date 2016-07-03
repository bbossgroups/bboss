// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Somik Raha
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/tags/FrameTag.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/10 23:20:45 $
// $Revision: 1.38 $
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

import org.htmlparser.nodes.TagNode;

/**
 * Identifies a frame tag
 */
public class FrameTag
    extends
        TagNode
{
    /**
     * The set of names handled by this tag.
     */
    private static final String[] mIds = new String[] {"FRAME","IFRAME"};

    /**
     * Create a new frame tag.
     */
    public FrameTag ()
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
     * Returns the location of the frame.
     * @return The contents of the SRC attribute converted to an absolute URL.
     */
    public String getFrameLocation ()
    {
        String ret;
        
        ret = getAttribute ("SRC");
        if (null == ret)
            ret = "";
        else if (null != getPage ())
            ret = getPage ().getAbsoluteURL (ret);
        
        return (ret);
    }

    /**
     * Sets the location of the frame.
     * @param url The new frame location.
     */
    public void setFrameLocation (String url)
    {
        setAttribute ("SRC", url);
    }

    /**
     * Get the <code>NAME</code> attribute, if any.
     * @return The value of the <code>NAME</code> attribute,
     * or <code>null</code> if the attribute doesn't exist.
     */
    public String getFrameName()
    {
        return (getAttribute ("NAME"));
    }

    /**
     * Return a string representation of the contents of this <code>FRAME</code> tag suitable for debugging.
     * @return A string with this tag's contents.
     */
    public String toString()
    {
        return "FRAME TAG : Frame " +getFrameName() + " at "+getFrameLocation()+"; begins at : "+getStartPosition ()+"; ends at : "+getEndPosition ();
    }
}
