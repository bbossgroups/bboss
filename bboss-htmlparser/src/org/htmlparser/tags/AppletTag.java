// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Somik Raha
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/tags/AppletTag.java,v $
// $Author: derrickoswald $
// $Date: 2004/07/02 00:49:28 $
// $Revision: 1.41 $
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

/**
 * AppletTag represents an &lt;Applet&gt; tag.
 * It extends a basic tag by providing accessors to the class, codebase,
 * archive and parameters.
 */
public class AppletTag
    extends
        CompositeTag
{
    /**
     * The set of names handled by this tag.
     */
    private static final String[] mIds = new String[] {"APPLET"};

    /**
     * The set of end tag names that indicate the end of this tag.
     */
    private static final String[] mEndTagEnders = new String[] {"BODY", "HTML"};

    /**
     * Create a new applet tag.
     */
    public AppletTag ()
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
     * Return the set of end tag names that cause this tag to finish.
     * @return The names of following end tags that stop further scanning.
     */
    public String[] getEndTagEnders ()
    {
        return (mEndTagEnders);
    }

    /**
     * Extract the applet <code>PARAM</code> tags from the child list.
     * @return The list of applet parameters (keys and values are String objects).
     */
    public HashMap createAppletParamsTable ()
    {
        NodeList kids;
        Node node;
        Tag tag;
        String paramName;
        String paramValue;
        HashMap ret;

        ret = new HashMap ();
        kids = getChildren ();
        if (null != kids)
            for (int i = 0; i < kids.size (); i++)
            {
                node = children.elementAt(i);
                if (node instanceof Tag)
                {
                    tag = (Tag)node;
                    if (tag.getTagName().equals ("PARAM"))
                    {
                        paramName = tag.getAttribute ("NAME");
                        if (null != paramName && 0 != paramName.length ())
                        {
                            paramValue = tag.getAttribute ("VALUE");
                            ret.put (paramName,paramValue);
                        }
                    }
                }
            }

        return (ret);
    }

    /**
     * Get the class name of the applet.
     * @return The value of the <code>CODE</code> attribute.
     */
    public String getAppletClass ()
    {
        return (getAttribute ("CODE"));
    }

    /**
     * Get the applet parameters.
     * @return The list of parameter values (keys and values are String objects).
     */
    public HashMap getAppletParams ()
    {
        return (createAppletParamsTable ());
    }

    /**
     * Get the jar file of the applet.
     * @return The value of the <code>ARCHIVE</code> attribute, or <code>null</code> if it wasn't specified.
     */
    public String getArchive()
    {
        return (getAttribute ("ARCHIVE"));
    }

    /**
     * Get the code base of the applet.
     * @return The value of the <code>CODEBASE</code> attribute, or <code>null</code> if it wasn't specified.
     */
    public String getCodeBase()
    {
        return (getAttribute ("CODEBASE"));
    }

    /**
     * Get the <code>PARAM<code> tag with the given name.
     * <em>NOTE: This was called (erroneously) getAttribute() in previous versions.</em>
     * @param key The applet parameter name to get.
     * @return The value of the parameter or <code>null</code> if there is no parameter of that name.
     */
    public String getParameter (String key)
    {
        return ((String)(getAppletParams ().get (key)));
    }

    /**
     * Get an enumeration over the (String) parameter names.
     * @return An enumeration of the <code>PARAM<code> tag <code>NAME<code> attributes.
     */
    public Iterator getParameterNames ()
    {
        return (getAppletParams ().keySet().iterator());
    }

    /**
     * Set the <code>CODE<code> attribute.
     * @param newAppletClass The new applet class.
     */
    public void setAppletClass (String newAppletClass)
    {
        setAttribute ("CODE", newAppletClass);
    }

    /**
     * Set the enclosed <code>PARM<code> children.
     * @param newAppletParams The new parameters.
     */
    public void setAppletParams (HashMap newAppletParams)
    {
        NodeList kids;
        Node node;
        Tag tag;
        String paramName;
        String paramValue;
        List attributes;
        Text string;

        kids = getChildren ();
        if (null == kids)
            kids = new NodeList ();
        else
            // erase appletParams from kids
            for (int i = 0; i < kids.size (); )
            {
                node = kids.elementAt (i);
                if (node instanceof Tag)
                    if (((Tag)node).getTagName ().equals ("PARAM"))
                    {
                        kids.remove (i);
                        // remove whitespace too
                        if (i < kids.size ())
                        {
                            node = kids.elementAt (i);
                            if (node instanceof Text)
                            {
                                string = (Text)node;
                                if (0 == string.getText ().trim ().length ())
                                    kids.remove (i);
                            }   
                        }
                    }
                    else
                        i++;
                else
                    i++;
            }

        // add newAppletParams to kids
        for (Iterator e = newAppletParams.entrySet().iterator(); e.hasNext(); )
        {
        	Map.Entry entry = (Entry) e.next();
            attributes = new ArrayList (); // should the tag copy the attributes?
            paramName = (String)entry.getKey();
            paramValue = (String)entry.getValue();
            attributes.add (new Attribute ("PARAM", null));
            attributes.add (new Attribute (" "));
            attributes.add (new Attribute ("VALUE", paramValue, '"'));
            attributes.add (new Attribute (" "));
            attributes.add (new Attribute ("NAME", paramName, '"'));
            tag = new TagNode (null, 0, 0, attributes);
            kids.add (tag);
        }

        //set kids as new children
        setChildren (kids);
    }

    /**
     * Set the <code>ARCHIVE<code> attribute.
     * @param newArchive The new archive file.
     */
    public void setArchive (String newArchive)
    {
        setAttribute ("ARCHIVE", newArchive);
    }

    /**
     * Set the <code>CODEBASE<code> attribute.
     * @param newCodeBase The new applet code base.
     */
    public void setCodeBase (String newCodeBase)
    {
        setAttribute ("CODEBASE", newCodeBase);
    }

    /**
     * Output a string representing this applet tag.
     * @return A string showing the contents of the applet tag.
     */
    public String toString ()
    {
        HashMap parameters;
        Iterator params;
        String paramName;
        String paramValue;
        boolean found;
        Node node;
        StringBuffer ret;

        ret = new StringBuffer(500);
        ret.append ("Applet Tag\n");
        ret.append ("**********\n");
        ret.append ("Class Name = ");
        ret.append (getAppletClass ());
        ret.append ("\n");
        ret.append ("Archive = ");
        ret.append (getArchive ());
        ret.append ("\n");
        ret.append ("Codebase = ");
        ret.append (getCodeBase ());
        ret.append ("\n");
        parameters = getAppletParams ();
        params = parameters.entrySet().iterator();
        if (null == params)
            ret.append ("No Params found.\n");
        else
            for (int cnt = 0; params.hasNext(); cnt++)
            {
            	Map.Entry entry = (Entry) params.next();
                paramName = (String)entry.getKey();
                paramValue = (String)entry.getValue();
                ret.append (cnt);
                ret.append (": Parameter name = ");
                ret.append (paramName);
                ret.append (", Parameter value = ");
                ret.append (paramValue);
                ret.append ("\n");
            }
        found = false;
        for (SimpleNodeIterator e = children (); e.hasMoreNodes ();)
        {
            node = e.nextNode ();
            if (node instanceof Tag)
                if (((Tag)node).getTagName ().equals ("PARAM"))
                    continue;
            if (!found)
                ret.append ("Miscellaneous items :\n");
            else
                ret.append (" ");
            found = true;
            ret.append (node.toString ());
        }
        if (found)
            ret.append ("\n");
        ret.append ("End of Applet Tag\n");
        ret.append ("*****************\n");

        return (ret.toString ());
    }
}
