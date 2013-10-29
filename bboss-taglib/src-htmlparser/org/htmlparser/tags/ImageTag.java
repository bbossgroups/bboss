// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Somik Raha
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/tags/ImageTag.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/10 23:20:45 $
// $Revision: 1.49 $
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

import java.util.Locale;
import java.util.Vector;

import org.htmlparser.Attribute;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.ParserUtils;

/**
 * Identifies an image tag.
 */
public class ImageTag
    extends
        TagNode
{
    /**
     * The set of names handled by this tag.
     */
    private static final String[] mIds = new String[] {"IMG"};

    /**
     * Holds the set value of the SRC attribute, since this can differ
     * from the attribute value due to relative references resolved by
     * the scanner.
     */
    protected String imageURL;

    /**
     * Create a new image tag.
     */
    public ImageTag ()
    {
        imageURL = null;
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
    * Extract the location of the image
    * Given the tag (with attributes), and the url of the html page in which
    * this tag exists, perform best effort to extract the 'intended' URL.
    * Attempts to handle such attributes as:
    * <pre>
    * &lt;IMG SRC=http://www.redgreen.com&gt; - normal
    * &lt;IMG SRC =http://www.redgreen.com&gt; - space between attribute name and equals sign
    * &lt;IMG SRC= http://www.redgreen.com&gt; - space between equals sign and attribute value
    * &lt;IMG SRC = http://www.redgreen.com&gt; - space both sides of equals sign
    * </pre>
    * @return The relative URL for the image.
    */
    public String extractImageLocn ()
    {
        Vector attributes;
        int size;
        Attribute attribute;
        String string;
        String data;
        int state;
        String name;
        String ret;
    
        // TODO: move this logic into the lexer?

        ret = "";
        state = 0;
        attributes = getAttributesEx ();
        size = attributes.size ();
        for (int i = 0; (i < size) && (state < 3); i++)
        {
            attribute = (Attribute)attributes.elementAt (i);
            string = attribute.getName ();
            data = attribute.getValue ();
            switch (state)
            {
                case 0: // looking for 'src'
                    if (null != string)
                    {
                        name = string.toUpperCase (Locale.ENGLISH);
                        if (name.equals ("SRC"))
                        {
                            state = 1;
                            if (null != data)
                            {
                                if ("".equals (data))
                                    state = 2; // empty attribute, SRC= 
                                else
                                {
                                    ret = data;
                                    i = size; // exit fast
                                }
                            }

                        }
                        else if (name.startsWith ("SRC"))
                        {
                            // missing equals sign
                            string = string.substring (3);
                            // remove any double quotes from around string
                            if (string.startsWith ("\"") && string.endsWith ("\"") && (1 < string.length ()))
                                string = string.substring (1, string.length () - 1);
                            // remove any single quote from around string
                            if (string.startsWith ("'") && string.endsWith ("'") && (1 < string.length ()))
                                string = string.substring (1, string.length () - 1);
                            ret = string;
                            state = 0; // go back to searching for SRC
                            // because, maybe we found SRCXXX
                            // where XXX isn't a URL
                        }
                    }
                    break;
                case 1: // looking for equals sign
                    if (null != string)
                    {
                        if (string.startsWith ("="))
                        {
                            state = 2;
                            if (1 < string.length ())
                            {
                                ret = string.substring (1);
                                state = 0; // keep looking ?
                            }
                            else if (null != data)
                            {
                                ret = string.substring (1);
                                state = 0; // keep looking ?
                            }
                        }
                    }
                    break;
                case 2: // looking for a valueless attribute that could be a relative or absolute URL
                    if (null != string)
                    {
                        if (null == data)
                            ret = string;
                        state = 0; // only check first non-whitespace item
                        // not every valid attribute after an equals
                    }
                    break;
                default:
                    throw new IllegalStateException ("we're not supposed to in state " + state);
            }
        }
        ret = ParserUtils.removeChars (ret, '\n');
        ret = ParserUtils.removeChars (ret, '\r');
        
        return (ret);
    }

    /**
     * Returns the location of the image.
     * @return The absolute URL for this image.
     */
    public String getImageURL()
    {
        if (null == imageURL)
            if (null != getPage ())
                imageURL = getPage ().getAbsoluteURL (extractImageLocn ());

        return (imageURL);
    }

    /**
     * Set the <code>SRC</code> attribute.
     * @param url The new value of the <code>SRC</code> attribute.
     */
    public void setImageURL (String url)
    {
        imageURL = url;
        setAttribute ("SRC", imageURL);
    }
}
