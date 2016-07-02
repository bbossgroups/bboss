// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2005 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/parserapplications/filterbuilder/SubFilterList.java,v $
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

package org.htmlparser.parserapplications.filterbuilder;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import org.htmlparser.NodeFilter;
import org.htmlparser.parserapplications.filterbuilder.layouts.VerticalLayoutManager;

/**
 * A helper class for lists of filters within filters.
 */
public class SubFilterList
    extends
        JPanel
{
    /**
     * Padding for the drop target.
     */
    protected int mExtra = 25; // for now

    /**
     * The drop target spacer at the bottom of the list.
     */
    protected Component mSpacer;

    /**
     * The parent filter wrapper.
     */
    protected Filter mHome;

    /**
     * The textual title for the list.
     */
    protected String mTitle;

    /**
     * The number of filters limit.
     */
    protected int mMax;

    /**
     * Creates a container panel.
     * Set the panel minimum size to the same width as the container
     * but with a bit of extra length.
     * @param home The filter we belong to.
     * @param title The border title.
     * @param max The maximum number of filters in the list (0 for no limit).
     */
    public SubFilterList (Filter home, String title, int max)
    {
        mHome = home;
        mTitle = title;
        mMax = max;

        // not quite:
        // new BoxLayout (this, BoxLayout.Y_AXIS));
        setLayout (new VerticalLayoutManager ());
	    addSpacer ();
	    setSelected (false);
    }

    /**
     * Set the 'selected look' for the component.
     * @param selected If <code>true</code>, 'select' this component,
     * otherwise 'deselect' it.
     */
    public void setSelected (boolean selected)
    {
        if (selected)
    	    setBorder (
    		        new CompoundBorder (
    		            new TitledBorder (
    		                null,
    		                mTitle,
    	                    TitledBorder.LEFT,
    	                    TitledBorder.TOP),
    	                    new CompoundBorder (
	                            new LineBorder (Color.green, 2),
	                            new EmptyBorder (1, 1, 1, 1))));
        else
    	    setBorder (
    		        new CompoundBorder (
    		            new TitledBorder (
    		                null,
    		                mTitle,
    	                    TitledBorder.LEFT,
    	                    TitledBorder.TOP),
    	                new EmptyBorder (3,3,3,3)));
    }

    /**
     * Stuff a spacer component at the end of the list.
     */
    protected void addSpacer ()
    {
        Dimension dimension;
        Insets insets;
        
        // set the command area size by adding a rigid area
        dimension = mHome.getSize ();
        insets = mHome.getInsets ();
        // todo: this should resize with the container
        dimension.setSize (dimension.width - insets.left - insets.right, mExtra);
        mSpacer = Box.createRigidArea (dimension);
        add (mSpacer);
    }

    /**
     * Remove the spacer component at the end of the list.
     */
    protected void removeSpacer ()
    {
        remove (mSpacer);
        mSpacer = null;
    }

    /**
     * Get the components in which to drop commands.
     * @return The component to act as a drop target.
     */
    public Component[] getDropTargets ()
    {
        return (new Component[] { this });
    }

    /**
     * Add a filter to the container contents.
     * @param filter The command to add to the container.
     */
    public void addFilter (Filter filter)
    {
        int count;
        
        count = getComponentCount ();
        if (null != mSpacer)
            count--; // insert before the spacer
        addFilter (filter, count);
    }

    /**
     * Add a filter to the container at a specific position.
     * @param filter The filter to add to the container.
     * @param index The index at which to add it.
     */
    public void addFilter (Filter filter, int index)
    {
        NodeFilter[] before;
        NodeFilter[] after;
        int offset;

        add (filter, index);
        before = mHome.getSubNodeFilters ();
        after = new NodeFilter[before.length + 1];
        offset = 0;
        for (int i = 0; i < after.length; i++)
            after[i] = (i == index) ? filter : before[offset++];
        mHome.setSubNodeFilters (after);
        if ((null != mSpacer) && (0 != mMax) && (after.length >= mMax))
            removeSpacer ();
    }        

    /**
     * Remove a filter from the container.
     * @param filter The filter to remove from the container.
     */
    public void removeFilter (Filter filter)
    {
        Filter[] filters;
        int index;
        
        filters = getFilters ();
        index = -1;
        for (int i = 0; ((-1 == index) && (i < filters.length)); i++)
            if (filter == filters[i])
                index = i;
        if (-1 != index)
            removeFilter (index);
    }

    /**
     * Remove a filter from the container.
     * @param index The index of the filter to remove from the container.
     */
    public void removeFilter (int index)
    {
        NodeFilter[] before;
        NodeFilter[] after;
        int offset;

        remove (index);
        before = mHome.getSubNodeFilters ();
        if (0 != before.length)
        {
	        after = new NodeFilter[before.length - 1];
	        offset = 0;
	        for (int i = 0; i < before.length; i++)
	            if (i != index)
	                after[offset++] = before[i];
	        mHome.setSubNodeFilters (after);
	        if ((null == mSpacer) && (0 != mMax) && (after.length < mMax))
	            addSpacer ();
        }
    }

    /**
     * Return the list of filters in this container.
     * @return The list of contained filters.
     */
    public Filter[] getFilters ()
    {
        NodeFilter[] list;
        Filter[] ret;

        list = mHome.getSubNodeFilters ();
        ret = new Filter[list.length];
        System.arraycopy (list, 0, ret, 0, list.length);

        return (ret);
    }

    /**
     * Check if our maximum number of filters limit has been reached.
     * @return <code>true</code> if the sublist can accept one more filter,
     * <code>false</code> otherwise.
     */
    public boolean canAccept ()
    {
        int count;
        boolean ret;

        if (0 == mMax)
            ret = true;
        else
        {
	        count = getComponentCount ();
	        if (null != mSpacer)
	            count--;
	        ret = count < mMax;
        }

        return (ret);
    }

    /**
     * Get the bytes for this command as a String.
     * @param indent The number of spaces to indent a block.
     * @param level The current indentation level.
     * The first non-whitespace character should be at
     * indented <code>indent</code> * <code>level</code> spaces.
     * @return The string representing this command.
     */
    public String toString (int indent, int level)
    {
        Filter[] filters;
        StringBuffer ret;
        
        ret = new StringBuffer ();

        filters = getFilters ();
        for (int i = 0; i < filters.length; i++)
        {
            ret.append (filters[i].toString ());
            if (i + 1 != filters.length)
                ret.append ("\n");
        }
        
        return (ret.toString ());
    }
    
}
