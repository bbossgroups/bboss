// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Somik Raha
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/parserapplications/StringExtractor.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/12 11:27:42 $
// $Revision: 1.48 $
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

package org.htmlparser.parserapplications;

import org.htmlparser.beans.StringBean;
import org.htmlparser.util.ParserException;

/**
 * Extract plaintext strings from a web page.
 * Illustrative program to gather the textual contents of a web page.
 * Uses a {@link org.htmlparser.beans.StringBean StringBean} to accumulate
 * the user visible text (what a browser would display) into a single string.
 */
public class StringExtractor
{
    private String resource;

    /**
     * Construct a StringExtractor to read from the given resource.
     * @param resource Either a URL or a file name.
     */
    public StringExtractor (String resource)
    {
        this.resource = resource;
    }

    /**
     * Extract the text from a page.
     * @return The textual contents of the page.
     * @param links if <code>true</code> include hyperlinks in output.
     * @exception ParserException If a parse error occurs.
     */
    public String extractStrings (boolean links)
        throws
            ParserException
    {
        StringBean sb;

        sb = new StringBean ();
        sb.setLinks (links);
        sb.setURL (resource);

        return (sb.getStrings ());
    }

    /**
     * Mainline.
     * @param args The command line arguments.
     */
    public static void main (String[] args)
    {
        boolean links;
        String url;
        StringExtractor se;

        links = false;
        url = null;
        for (int i = 0; i < args.length; i++)
            if (args[i].equalsIgnoreCase ("-links"))
                links = true;
            else
                url = args[i];
        if (null != url)
        {
            se = new StringExtractor (url);
            try
            {
                System.out.println (se.extractStrings (links));
            }
            catch (ParserException e)
            {
                e.printStackTrace ();
            }
        }
        else
            System.out.println ("Usage: java -classpath htmlparser.jar org.htmlparser.parserapplications.StringExtractor [-links] url");
    }
}
