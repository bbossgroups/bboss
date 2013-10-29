// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2003 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/parserapplications/LinkExtractor.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/12 11:27:41 $
// $Revision: 1.52 $
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

import javax.swing.JOptionPane;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * LinkExtractor extracts all the links from the given webpage
 * and prints them on standard output.
 */
public class LinkExtractor
{
    /**
     * Run the link extractor.
     * @param args [0] Optional url to extract links from.
     * An input dialog is displayed if it is not supplied.
     */
    public static void main (String[] args)
    {
        String url;
        Parser parser;
        NodeFilter filter;
        NodeList list;

        if (0 >= args.length)
        {
            url = (String)JOptionPane.showInputDialog (
                null,
                "Enter the URL to extract links from:",
                "Web Site",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "http://htmlparser.sourceforge.net/wiki/");
            if (null == url)
                System.exit (1);
        }
        else
            url = args[0];
        filter = new NodeClassFilter (LinkTag.class);
        if ((1 < args.length) && args[1].equalsIgnoreCase ("-maillinks"))
            filter = new AndFilter (
                filter,
                new NodeFilter ()
                {
                    public boolean accept (Node node)
                    {
                        return (((LinkTag)node).isMailLink ());
                    }
                }
            );
        try
        {
            parser = new Parser (url);
            list = parser.extractAllNodesThatMatch (filter);
            for (int i = 0; i < list.size (); i++)
                System.out.println (list.elementAt (i).toHtml ());
        }
        catch (ParserException e)
        {
            e.printStackTrace ();
        }
        System.exit (0);
    }
}

