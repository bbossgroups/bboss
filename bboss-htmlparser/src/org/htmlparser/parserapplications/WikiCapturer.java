// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2003 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/parserapplications/WikiCapturer.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/12 11:27:42 $
// $Revision: 1.3 $
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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NotFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;

/**
 * Save a wikiwikiweb locally.
 * Illustrative program to save a wiki locally.
 */
public class WikiCapturer
    extends
        SiteCapturer
{
    /**
     * Create a wikicapturer.
     */
    public WikiCapturer ()
    {
    }

    /**
     * Returns <code>true</code> if the link is one we are interested in.
     * @param link The link to be checked.
     * @return <code>true</code> if the link has the source URL as a prefix
     * and doesn't contain '?' or '#'; the former because we won't be able to
     * handle server side queries in the static target directory structure and
     * the latter because presumably the full page with that reference has
     * already been captured previously. This performs a case insensitive
     * comparison, which is cheating really, but it's cheap.
     */
    protected boolean isToBeCaptured (String link)
    {
        boolean ret;
        
        ret = super.isToBeCaptured (link);

        // eliminate PhpWiki specific pages
        if (ret)
            if (link.endsWith ("PhpWikiAdministration"))
                ret = false;
            else if (link.endsWith ("PhpWikiDocumentation"))
                ret = false;
            else if (link.endsWith ("TextFormattingRules"))
                ret = false;
            else if (link.endsWith ("NewMarkupTestPage"))
                ret = false;
            else if (link.endsWith ("OldMarkupTestPage"))
                ret = false;
            else if (link.endsWith ("OldTextFormattingRules"))
                ret = false;
            else if (link.endsWith ("PgsrcTranslation"))
                ret = false;
            else if (link.endsWith ("HowToUseWiki"))
                ret = false;
            else if (link.endsWith ("MoreAboutMechanics"))
                ret = false;
            else if (link.endsWith ("AddingPages"))
                ret = false;
            else if (link.endsWith ("WikiWikiWeb"))
                ret = false;
            else if (link.endsWith ("UserPreferences"))
                ret = false;
            else if (link.endsWith ("PhpWiki"))
                ret = false;
            else if (link.endsWith ("WabiSabi"))
                ret = false;
            else if (link.endsWith ("EditText"))
                ret = false;
            else if (link.endsWith ("FindPage"))
                ret = false;
            else if (link.endsWith ("RecentChanges"))
                ret = false;
            else if (link.endsWith ("RecentEdits"))
                ret = false;
            else if (link.endsWith ("RecentVisitors"))
                ret = false;
            else if (link.endsWith ("SteveWainstead"))
                ret = false;

        return (ret);
    }

    /**
     * Mainline to capture a web site locally.
     * @param args The command line arguments.
     * There are three arguments the web site to capture, the local directory
     * to save it to, and a flag (true or false) to indicate whether resources
     * such as images and video are to be captured as well.
     * These are requested via dialog boxes if not supplied.
     * @exception MalformedURLException If the supplied URL is invalid.
     * @exception IOException If an error occurs reading the pages or resources.
     */
    public static void main (String[] args)
        throws
            MalformedURLException,
            IOException
    {
        WikiCapturer worker;
        String url;
        JFileChooser chooser;
        URL source;
        String path;
        File target;
        Boolean capture;
        int ret;
        
        worker = new WikiCapturer ();
        if (0 >= args.length)
        {
            url = (String)JOptionPane.showInputDialog (
                null,
                "Enter the URL to capture:",
                "Web Site",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "http://htmlparser.sourceforge.net/wiki");
            if (null != url)
                worker.setSource (url);
            else
                System.exit (1);
        }
        else
            worker.setSource (args[0]);
        if (1 >= args.length)
        {
            url = worker.getSource ();
            source = new URL (url);
            path = new File (new File ("." + File.separator), source.getHost () + File.separator).getCanonicalPath ();
            target = new File (path);
            chooser = new JFileChooser (target);
            chooser.setDialogType (JFileChooser.SAVE_DIALOG);
            chooser.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY);
            chooser.setSelectedFile (target); // this doesn't frickin' work
            chooser.setMultiSelectionEnabled (false);
            chooser.setDialogTitle ("Target Directory");
            ret = chooser.showSaveDialog (null);
            if (ret == JFileChooser.APPROVE_OPTION)
                worker.setTarget (chooser.getSelectedFile ().getAbsolutePath ());
            else
                System.exit (1);
        }
        else
            worker.setTarget (args[1]);
        if (2 >= args.length)
        {
            capture = (Boolean)JOptionPane.showInputDialog (
                null,
                "Should resources be captured:",
                "Capture Resources",
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[] { Boolean.TRUE, Boolean.FALSE},
                Boolean.TRUE);
            if (null != capture)
                worker.setCaptureResources (capture.booleanValue ());
            else
                System.exit (1);
        }
        else
            worker.setCaptureResources ((Boolean.valueOf (args[2]).booleanValue ()));
        worker.setFilter (
            new NotFilter (
                new OrFilter (
                    new AndFilter (
                        new TagNameFilter ("DIV"),
                        new HasAttributeFilter ("id", "navbar")), 
                    new OrFilter (
                        new AndFilter (
                            new TagNameFilter ("DIV"),
                            new HasAttributeFilter ("id", "actionbar")),
                        new AndFilter (
                            new TagNameFilter ("DIV"),
                            new HasAttributeFilter ("id", "xhtml-validator"))))));
        worker.capture ();
        
        System.exit (0);
    }
}
