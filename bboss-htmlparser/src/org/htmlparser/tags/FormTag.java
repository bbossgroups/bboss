// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Somik Raha
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/tags/FormTag.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/10 23:20:45 $
// $Revision: 1.50 $
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

import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

/**
 * Represents a FORM tag.
 * @author ili
 */
public class FormTag extends CompositeTag
{
    /**
     * The {@value} method.
     * @see #getFormMethod
     */
    public static final String POST = "POST";

    /**
     * The {@value} method.
     * @see #getFormMethod
     */
    public static final String GET = "GET";
    
    /**
     * This is the derived form location, based on action.
     */
    protected String mFormLocation;

    /**
     * The set of names handled by this tag.
     */
    private static final String[] mIds = new String[] {"FORM"};

    /**
     * The set of end tag names that indicate the end of this tag.
     */
    private static final String[] mEndTagEnders = new String[] {"HTML", "BODY", "TABLE"};

    /**
     * Create a new form tag.
     */
    public FormTag ()
    {
        mFormLocation = null;
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
     * Get the list of input fields.
     * @return Input elements in the form.
     */
    public NodeList getFormInputs()
    {
        return (searchFor (InputTag.class, true));
    }

    /**
     * Get the list of text areas.
     * @return Textarea elements in the form.
     */
    public NodeList getFormTextareas()
    {
        return (searchFor (TextareaTag.class, true));
    }

    /**
     * Get the value of the action attribute.
     * @return The submit url of the form.
     */
    public String getFormLocation()
    {
        if (null == mFormLocation)
            // ... is it true that without an ACTION the default is to send it back to the same page?
            mFormLocation = extractFormLocn ();

        return (mFormLocation);
    }

    /**
     * Set the form location. Modification of this element will cause the HTML rendering
     * to change as well (in a call to toHTML()).
     * @param url The new FORM location
     */
    public void setFormLocation(String url)
    {
        mFormLocation = url;
        setAttribute ("ACTION", url);
    }

    /**
     * Returns the method of the form, GET or POST.
     * @return String The method of the form (GET if nothing is specified).
     */
    public String getFormMethod()
    {
        String ret;
        
        ret = getAttribute("METHOD");
        if (null == ret)
            ret = GET;

        return (ret);
    }

    /**
     * Get the input tag in the form corresponding to the given name
     * @param name The name of the input tag to be retrieved
     * @return Tag The input tag corresponding to the name provided
     */
    public InputTag getInputTag (String name)
    {
        InputTag inputTag;
        boolean found;
        String inputTagName;
        
        inputTag = null;
        found = false;
        for (SimpleNodeIterator e = getFormInputs().elements();e.hasMoreNodes() && !found;)
        {
            inputTag = (InputTag)e.nextNode();
            inputTagName = inputTag.getAttribute("NAME");
            if (inputTagName!=null && inputTagName.equalsIgnoreCase(name))
                found=true;
        }
        if (found)
            return (inputTag);
        else
            return (null);
    }

    /**
     * Get the value of the name attribute.
     * @return String The name of the form
     */
    public String getFormName()
    {
        return (getAttribute("NAME"));
    }

    /**
     * Find the textarea tag matching the given name
     * @param name Name of the textarea tag to be found within the form.
     * @return The <code>TEXTAREA</code> tag with the matching name.
     */
    public TextareaTag getTextAreaTag(String name)
    {
        TextareaTag textareaTag=null;
        boolean found = false;
        for (SimpleNodeIterator e=getFormTextareas ().elements();e.hasMoreNodes() && !found;)
        {
            textareaTag = (TextareaTag)e.nextNode();
            String textAreaName = textareaTag.getAttribute("NAME");
            if (textAreaName!=null && textAreaName.equals(name))
                found = true;
        }
        if (found)
            return (textareaTag);
        else
            return (null);
    }

    /**
     * Return a string representation of the contents of this <code>FORM</code> tag suitable for debugging.
     * @return A textual representation of the form tag.
     */
    public String toString()
    {
        return "FORM TAG : Form at "+getFormLocation()+"; begins at : "+getStartPosition ()+"; ends at : "+getEndPosition ();
    }
    
    /**
     * Extract the <code>ACTION</code> attribute as an absolute URL.
     * @return The URL the form is to be submitted to.
     */
    public String extractFormLocn ()
    {
        String ret;
        
        ret = getAttribute("ACTION");
        if (null == ret)
            ret = "";
        else if (null != getPage ())
            ret = getPage ().getAbsoluteURL (ret);
        
        return (ret);
    }
}
