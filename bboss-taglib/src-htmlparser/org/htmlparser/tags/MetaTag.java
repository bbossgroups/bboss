// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Somik Raha
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/tags/MetaTag.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/10 23:20:45 $
// $Revision: 1.39 $
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

import org.htmlparser.Attribute;
import org.htmlparser.lexer.Page;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.ParserException;

/**
 * A Meta Tag
 */
public class MetaTag
    extends
        TagNode
{
    /**
     * The set of names handled by this tag.
     */
    private static final String[] mIds = new String[] {"META"};

    /**
     * Create a new meta tag.
     */
    public MetaTag ()
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
     * Get the <code>HTTP-EQUIV</code> attribute, if any.
     * @return The value of the <code>HTTP-EQUIV</code> attribute,
     * or <code>null</code> if the attribute doesn't exist.
     */
    public String getHttpEquiv ()
    {
        return (getAttribute ("HTTP-EQUIV"));
    }

    /**
     * Get the <code>CONTENT</code> attribute, if any.
     * @return The value of the <code>CONTENT</code> attribute,
     * or <code>null</code> if the attribute doesn't exist.
     */
    public String getMetaContent ()
    {
        return (getAttribute ("CONTENT"));
    }

    /**
     * Get the <code>NAME</code> attribute, if any.
     * @return The value of the <code>NAME</code> attribute,
     * or <code>null</code> if the attribute doesn't exist.
     */
    public String getMetaTagName ()
    {
        return (getAttribute ("NAME"));
    }

    /**
     * Set the <code>HTTP-EQUIV</code> attribute.
     * @param httpEquiv The new value of the <code>HTTP-EQUIV</code> attribute.
     */
    public void setHttpEquiv (String httpEquiv)
    {
        Attribute equiv;
        equiv = getAttributeEx ("HTTP-EQUIV");
        if (null != equiv)
            equiv.setValue (httpEquiv);
        else
            getAttributesEx ().add (new Attribute ("HTTP-EQUIV", httpEquiv));
    }

    /**
     * Set the <code>CONTENT</code> attribute.
     * @param metaTagContents The new value of the <code>CONTENT</code> attribute.
     */
    public void setMetaTagContents (String metaTagContents)
    {
        Attribute content;
        content = getAttributeEx ("CONTENT");
        if (null != content)
            content.setValue (metaTagContents);
        else
            getAttributesEx ().add (new Attribute ("CONTENT", metaTagContents));
    }

    /**
     * Set the <code>NAME</code> attribute.
     * @param metaTagName The new value of the <code>NAME</code> attribute.
     */
    public void setMetaTagName (String metaTagName)
    {
        Attribute name;
        name = getAttributeEx ("NAME");
        if (null != name)
            name.setValue (metaTagName);
        else
            getAttributesEx ().add (new Attribute ("NAME", metaTagName));
    }
    
    /**
     * Perform the META tag semantic action.
     * Check for a charset directive, and if found, set the charset for the page.
     * @exception ParserException If setting the encoding fails.
     */
    public void doSemanticAction ()
        throws
            ParserException
    {
        String httpEquiv;
        String charset;

        httpEquiv = getHttpEquiv ();
        if ("Content-Type".equalsIgnoreCase (httpEquiv))
        {
            charset = Page.getCharset (getAttribute ("CONTENT"));
            getPage ().setEncoding (charset);
        }
    }
}
