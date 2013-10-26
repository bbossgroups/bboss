// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Somik Raha
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/tags/LinkTag.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/10 23:20:45 $
// $Revision: 1.54 $
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

import org.htmlparser.Node;
import org.htmlparser.util.ParserUtils;
import org.htmlparser.util.SimpleNodeIterator;

/**
 * Identifies a link tag.
 */
public class LinkTag extends CompositeTag
{
    /**
     * The set of names handled by this tag.
     */
    private static final String[] mIds = new String[] {"A"};

    /**
     * The set of tag names that indicate the end of this tag.
     * marked by biaoping.yin
     */
//    private static final String[] mEnders = new String[] {"A", "P", "DIV", "TD", "TR", "FORM", "LI"};
    private static final String[] mEnders = new String[] {};

    /**
     * The set of end tag names that indicate the end of this tag.
     * marked by biaoping.yin
     */
//    private static final String[] mEndTagEnders = new String[] {"P", "DIV", "TD", "TR", "FORM", "LI", "BODY", "HTML"};
    
    private static final String[] mEndTagEnders = new String[] {};

    /**
     * The URL where the link points to
     */
    protected String mLink;

    /**
     * Set to true when the link was a mailto: URL.
     */
    private boolean mailLink;

    /**
     * Set to true when the link was a javascript: URL.
     */
    private boolean javascriptLink;

    /**
     * Constructor creates an LinkTag object, which basically stores the location
     * where the link points to, and the text it contains.
     * <p>
     * In order to get the contents of the link tag, use the method linkData(),
     * which returns an enumeration of nodes encapsulated within the link.
     * <p>
     * The following code will get all the images inside a link tag.
     * <pre>
     * Node node ;
     * ImageTag imageTag;
     * for (Enumeration e=linkTag.linkData();e.hasMoreElements();) {
     *      node = (Node)e.nextElement();
     *      if (node instanceof ImageTag) {
     *          imageTag = (ImageTag)node;
     *          // Process imageTag
     *      }
     * }
     * </pre>
     */
    public LinkTag ()
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
        return (mEnders);
    }

    /**
     * Return the set of end tag names that cause this tag to finish.
     * @return The names of following end tags that stop further scanning.
     */
    public String[] getEndTagEnders ()
    {
        return (mEndTagEnders);
    }

    /**
     * Get the <code>ACCESSKEY</code> attribute, if any.
     * @return The value of the <code>ACCESSKEY</code> attribute,
     * or <code>null</code> if the attribute doesn't exist.
     */
    public String getAccessKey()
    {
        return (getAttribute("ACCESSKEY"));
    }

    /**
     * Returns the url as a string, to which this link points.
     * This string has had the "mailto:" and "javascript:" protocol stripped
     * off the front (if those predicates return <code>true</code>) but not
     * for other protocols. Don't ask me why, it's a legacy thing.
     * @return The URL for this <code>A</code> tag.
     */
    public String getLink()
    {
        if (null == mLink)
        {
            mailLink=false;
            javascriptLink = false;
            mLink = extractLink ();

            int mailto = mLink.indexOf("mailto");
            if (mailto==0)
            {
                // yes it is
                mailto = mLink.indexOf(":");
                mLink = mLink.substring(mailto+1);
                mailLink = true;
            }
            int javascript = mLink.indexOf("javascript:");
            if (javascript == 0)
            {
                mLink = mLink.substring(11); // this magic number is "javascript:".length()
                javascriptLink = true;
            }
        }
        return (mLink);
    }

    /**
     * Returns the text contained inside this link tag.
     * @return The textual contents between the {@.html <A></A>} pair.
     */
    public String getLinkText()
    {
        String ret;

        if (null != getChildren ())
            ret = getChildren ().asString ();
        else
            ret = "";

        return (ret);
    }

    /**
     * Is this a mail address
     * @return boolean true/false
     */
    public boolean isMailLink()
    {
        getLink (); // force an evaluation of the booleans
        return (mailLink);
    }

    /**
     * Tests if the link is javascript
     * @return flag indicating if the link is a javascript code
     */
    public boolean isJavascriptLink()
    {
        getLink (); // force an evaluation of the booleans
        return (javascriptLink);
    }

    /**
     * Tests if the link is an FTP link.
     *
     * @return flag indicating if this link is an FTP link
     */
    public boolean isFTPLink() {
        return getLink ().indexOf("ftp://")==0;
    }

    /**
     * Tests if the link is an IRC link.
     * @return flag indicating if this link is an IRC link
     */
    public boolean isIRCLink() {
        return getLink ().indexOf("irc://")==0;
    }

    /**
     * Tests if the link is an HTTP link.
     *
     * @return flag indicating if this link is an HTTP link
     */
    public boolean isHTTPLink()
    {
        return (!isFTPLink() && !isHTTPSLink() && !isJavascriptLink() && !isMailLink() && !isIRCLink());
    }

    /**
     * Tests if the link is an HTTPS link.
     *
     * @return flag indicating if this link is an HTTPS link
     */
    public boolean isHTTPSLink() {
            return getLink ().indexOf("https://")==0;
    }

        /**
     * Tests if the link is an HTTP link or one of its variations (HTTPS, etc.).
     *
     * @return flag indicating if this link is an HTTP link or one of its variations (HTTPS, etc.)
     */
    public boolean isHTTPLikeLink() {
            return isHTTPLink() || isHTTPSLink();
    }


    /**
     * Insert the method's description here.
     * Creation date: (8/3/2001 1:49:31 AM)
     * @param newMailLink boolean
     */
    public void setMailLink(boolean newMailLink)
    {
        mailLink = newMailLink;
    }

    /**
     * Set the link as a javascript link.
     *
     * @param newJavascriptLink flag indicating if the link is a javascript code
     */
    public void setJavascriptLink(boolean newJavascriptLink)
    {
        javascriptLink = newJavascriptLink;
    }

    /**
     * Return the contents of this link node as a string suitable for debugging.
     * @return A string representation of this node.
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("Link to : "+ getLink() + "; titled : "+getLinkText ()+"; begins at : "+getStartPosition ()+"; ends at : "+getEndPosition ()+ ", AccessKey=");
        if (getAccessKey ()==null)
            sb.append("null\n");
        else
            sb.append(getAccessKey ()+"\n");
        if (null != getChildren ())
        {
            sb.append("  "+"LinkData\n");
            sb.append("  "+"--------\n");

            Node node;
            int i = 0;
            for (SimpleNodeIterator e=children();e.hasMoreNodes();)
            {
                node = e.nextNode();
                sb.append("   "+(i++)+ " ");
                sb.append(node.toString()+"\n");
            }
        }
        sb.append("  "+"*** END of LinkData ***\n");
        return sb.toString();
    }

    /**
     * Set the <code>HREF</code> attribute.
     * @param link The new value of the <code>HREF</code> attribute.
     */
    public void setLink(String link)
    {
        mLink = link;
        setAttribute ("HREF", link);
    }

    /**
     * This method returns an enumeration of data that it contains
     * @return Enumeration
     * @deprecated Use children() instead.
     */
    public SimpleNodeIterator linkData() {
        return children();
    }

    /**
     * Extract the link from the HREF attribute.
     * @return The URL from the HREF attibute. This is absolute if the tag has
     * a valid page.
     */
    public String extractLink ()
    {
        String ret;

        ret =  getAttribute ("HREF");
        if (null != ret)
        {
            ret = ParserUtils.removeChars (ret,'\n');
            ret = ParserUtils.removeChars (ret,'\r');
        }
        if (null != getPage ())
            ret = getPage ().getAbsoluteURL (ret);

        return (ret);
    }
}
