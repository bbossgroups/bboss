// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Enrico Triolo
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/tags/ObjectTag.java,v $
// $Author: derrickoswald $
// $Date: 2004/07/02 00:49:29 $
// $Revision: 1.2 $
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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

/**
 * ObjectTag represents an &lt;Object&gt; tag.
 * It extends a basic tag by providing accessors to the
 * type, codetype, codebase, classid, data, height, width, standby attributes and parameters.
 */
public class ObjectTag extends CompositeTag
{
    /**
     * The set of names handled by this tag.
     */
    private static final String[] mIds = new String[] {"OBJECT"};

    /**
     * The set of end tag names that indicate the end of this tag.
     */
    private static final String[] mEndTagEnders = new String[] {"BODY", "HTML"};

    /**
     * Create a new object tag.
     */
    public ObjectTag ()
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
     * Extract the object <code>PARAM</code> tags from the child list.
     * @return The list of object parameters (keys and values are String objects).
     */
    public Hashtable createObjectParamsTable ()
    {
        NodeList kids;
        Node node;
        Tag tag;
        String paramName;
        String paramValue;
        Hashtable ret;

        ret = new Hashtable ();
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
                            ret.put (paramName.toUpperCase(),paramValue);
                        }
                    }
                }
            }

        return (ret);
    }

    /**
     * Get the classid of the object.
     * @return The value of the <code>CLASSID</code> attribute.
     */
    public String getObjectClassId ()
    {
        return getAttribute ("CLASSID");
    }

    /**
     * Get the codebase of the object.
     * @return The value of the <code>CODEBASE</code> attribute.
     */
    public String getObjectCodeBase ()
    {
        return getAttribute ("CODEBASE");
    }
    
    /**
     * Get the codetype of the object.
     * @return The value of the <code>CODETYPE</code> attribute.
     */
    public String getObjectCodeType ()
    {
        return getAttribute ("CODETYPE");
    }
    
    /**
     * Get the data of the object.
     * @return The value of the <code>DATA</code> attribute.
     */
    public String getObjectData ()
    {
        return getAttribute ("DATA");
    }
    
    /**
     * Get the height of the object.
     * @return The value of the <code>HEIGHT</code> attribute.
     */
    public String getObjectHeight ()
    {
        return getAttribute ("HEIGHT");
    }
    
    /**
     * Get the standby of the object.
     * @return The value of the <code>STANDBY</code> attribute.
     */
    public String getObjectStandby ()
    {
        return getAttribute ("STANDBY");
    }
    
    /**
     * Get the type of the object.
     * @return The value of the <code>TYPE</code> attribute.
     */
    public String getObjectType ()
    {
        return getAttribute ("TYPE");
    }
    
    /**
     * Get the width of the object.
     * @return The value of the <code>WIDTH</code> attribute.
     */
    public String getObjectWidth ()
    {
        return getAttribute ("WIDTH");
    }
    
    /**
     * Get the object parameters.
     * @return The list of parameter values (keys and values are String objects).
     */
    public Hashtable getObjectParams ()
    {
        return createObjectParamsTable ();
    }
    
    /**
     * Get the <code>PARAM<code> tag with the given name.
     * @param key The object parameter name to get.
     * @return The value of the parameter or <code>null</code> if there is no parameter of that name.
     */
    public String getParameter (String key)
    {
        return ((String)(getObjectParams ().get (key.toUpperCase ())));
    }
    
    /**
     * Get an enumeration over the (String) parameter names.
     * @return An enumeration of the <code>PARAM<code> tag <code>NAME<code> attributes.
     */
    public Enumeration getParameterNames ()
    {
        return getObjectParams ().keys ();
    }
    
    /**
     * Set the <code>CLASSID<code> attribute.
     * @param newClassId The new classid.
     */
    public void setObjectClassId (String newClassId)
    {
        setAttribute ("CLASSID", newClassId);
    }
    
    /**
     * Set the <code>CODEBASE<code> attribute.
     * @param newCodeBase The new codebase.
     */
    public void setObjectCodeBase (String newCodeBase)
    {
        setAttribute ("CODEBASE", newCodeBase);
    }
    
    /**
     * Set the <code>CODETYPE<code> attribute.
     * @param newCodeType The new codetype.
     */
    public void setObjectCodeType (String newCodeType)
    {
        setAttribute ("CODETYPE", newCodeType);
    }
    
    /**
     * Set the <code>DATA<code> attribute.
     * @param newData The new data.
     */
    public void setObjectData (String newData)
    {
        setAttribute ("DATA", newData);
    }
    
    /**
     * Set the <code>HEIGHT<code> attribute.
     * @param newHeight The new height.
     */
    public void setObjectHeight (String newHeight)
    {
        setAttribute ("HEIGHT", newHeight);
    }
    
    /**
     * Set the <code>STANDBY<code> attribute.
     * @param newStandby The new standby.
     */
    public void setObjectStandby (String newStandby)
    {
        setAttribute ("STANDBY", newStandby);
    }
    
    /**
     * Set the <code>TYPE<code> attribute.
     * @param newType The new type.
     */
    public void setObjectType (String newType)
    {
        setAttribute ("TYPE", newType);
    }
    
    /**
     * Set the <code>WIDTH<code> attribute.
     * @param newWidth The new width.
     */
    public void setObjectWidth (String newWidth)
    {
        setAttribute ("WIDTH", newWidth);
    }
    
    /**
     * Set the enclosed <code>PARAM<code> children.
     * @param newObjectParams The new parameters.
     */
    public void setObjectParams (Hashtable newObjectParams)
    {
        NodeList kids;
        Node node;
        Tag tag;
        String paramName;
        String paramValue;
        Vector attributes;
        TextNode string;
        
        kids = getChildren ();
        if (null == kids)
            kids = new NodeList ();
        else
            // erase objectParams from kids
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
                            if (node instanceof TextNode)
                            {
                                string = (TextNode)node;
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
        
        // add newObjectParams to kids
        for (Enumeration e = newObjectParams.keys (); e.hasMoreElements (); )
        {
            attributes = new Vector (); // should the tag copy the attributes?
            paramName = (String)e.nextElement ();
            paramValue = (String)newObjectParams.get (paramName);
            attributes.addElement (new Attribute ("PARAM", null));
            attributes.addElement (new Attribute (" "));
            attributes.addElement (new Attribute ("VALUE", paramValue, '"'));
            attributes.addElement (new Attribute (" "));
            attributes.addElement (new Attribute ("NAME", paramName.toUpperCase (), '"'));
            tag = new TagNode (null, 0, 0, attributes);
            kids.add (tag);
        }
        
        //set kids as new children
        setChildren (kids);
    }
    
    /**
     * Output a string representing this object tag.
     * @return A string showing the contents of the object tag.
     */
    public String toString ()
    {
        Hashtable parameters;
        Enumeration params;
        String paramName;
        String paramValue;
        boolean found;
        Node node;
        StringBuffer ret;
        
        ret = new StringBuffer (500);
        ret.append ("Object Tag\n");
        ret.append ("**********\n");
        ret.append ("ClassId = ");
        ret.append (getObjectClassId ());
        ret.append ("\n");
        ret.append ("CodeBase = ");
        ret.append (getObjectCodeBase ());
        ret.append ("\n");
        ret.append ("CodeType = ");
        ret.append (getObjectCodeType ());
        ret.append ("\n");
        ret.append ("Data = ");
        ret.append (getObjectData ());
        ret.append ("\n");
        ret.append ("Height = ");
        ret.append (getObjectHeight ());
        ret.append ("\n");
        ret.append ("Standby = ");
        ret.append (getObjectStandby ());
        ret.append ("\n");
        ret.append ("Type = ");
        ret.append (getObjectType ());
        ret.append ("\n");
        ret.append ("Width = ");
        ret.append (getObjectWidth ());
        ret.append ("\n");
        parameters = getObjectParams ();
        params = parameters.keys ();
        if (null == params)
            ret.append ("No Params found.\n");
        else
            for (int cnt = 0; params.hasMoreElements (); cnt++)
            {
                paramName = (String)params.nextElement ();
                paramValue = (String)parameters.get (paramName);
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
        ret.append ("End of Object Tag\n");
        ret.append ("*****************\n");
        
        return (ret.toString ());
    }
}
