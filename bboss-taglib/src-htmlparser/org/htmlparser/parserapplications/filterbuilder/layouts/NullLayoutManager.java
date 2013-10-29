// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2005 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/parserapplications/filterbuilder/layouts/NullLayoutManager.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/12 11:27:42 $
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

package org.htmlparser.parserapplications.filterbuilder.layouts;

import java.awt.*;
import java.io.*;

/**
 * A layout manager that doesn't move things around.
 * Attempts to set the bounds of components to accomodate them
 * but doesn't move them.
 */
public class NullLayoutManager
    implements
        LayoutManager2,
        Serializable
{ 
    /**
     * Constructs a NullLayoutManager object.
     */
    public NullLayoutManager ()
    {
    }
  
    /**
     * Calculates the minimum size dimensions for the specified
     * panel given the components in the specified parent container.
     * @param target The component to be laid out.
     * @return The minimum size.
     * @see #preferredLayoutSize
     */
    public Dimension minimumLayoutSize (Container target)
    {
        return (preferredLayoutSize (target));
    }
  
    /**
     * Calculates the preferred size dimensions for the specified
     * panel given the components in the specified parent container.
     * @see #minimumLayoutSize
     * @param target The component to be laid out.
     * @return A size deemed suitable for laying out the container.
     */
    public Dimension preferredLayoutSize (Container target)
    {
        int count;
        Container parent;
        Component component;
        Point point;
        Dimension dimension;
        Insets insets;
        Dimension ret;
        
        synchronized (target.getTreeLock ())
        {
            count = target.getComponentCount ();
            if (0 == count)
            {
                // be the same size unless we have a parent
                ret = target.getSize ();
                parent = target.getParent ();
                if (null != parent)
                {
                    insets = parent.getInsets ();
                    ret = parent.getSize ();
                    ret.setSize (
                        ret.width - insets.left - insets.right,
                        ret.height - insets.top - insets.bottom);
                }
            }
            else
            {
                ret = new Dimension (0, 0);
                for (int i = 0 ; i < count ; i++)
                {
                    component = target.getComponent (i);
                    if (component.isVisible ())
                    {
                        point = component.getLocation ();
                        dimension = component.getPreferredSize();
                        ret.width = Math.max (ret.width, point.x + dimension.width);
                        ret.height = Math.max (ret.height, point.y + dimension.height);
                    }
                }
                insets = target.getInsets ();
                ret.width += insets.left + insets.right;
                ret.height += insets.top + insets.bottom;
            }
        }

        return (ret);
    }
  
    /**
     * Returns the maximum size of this component.
     * @param target The component to be laid out.
     * @return The maximum size for the container.
     * @see #preferredLayoutSize
     */
    public Dimension maximumLayoutSize (Container target)
    {
        return (preferredLayoutSize (target));
    }
  
    //
    // LayoutManager Interface
    //
  
    /**
     * Adds the specified component with the specified name to
     * the layout.
     * @param name the component name
     * @param comp the component to be added
     */
    public void addLayoutComponent (String name, Component comp)
    {
    }        
  
    /**
     * Removes the specified component from the layout.
     * @param comp the component ot be removed
     */
    public void removeLayoutComponent (Component comp)
    {
    }
  
    /**
     * Lays out the container.
     * @param target The container which needs to be laid out.
     */
    public void layoutContainer (Container target)
    {
        int count;
        Component component;
        Dimension dimension;
        
        synchronized (target.getTreeLock ())
        {
            count = target.getComponentCount ();
            for (int i = 0 ; i < count ; i++)
            {
                component = target.getComponent (i);
                if (component.isVisible ())
                {
                    dimension = component.getPreferredSize();
                    component.setSize (dimension.width, dimension.height);
                }
            }
        }
    }
  
    //
    // LayoutManager2 Interface
    //
  
    /**
     * Adds the specified component to the layout, using the specified
     * constraint object.
     * @param comp the component to be added
     * @param constraints  where/how the component is added to the layout.
     */
    public void addLayoutComponent (Component comp, Object constraints)
    {
    }
  
    /**
     * Returns the alignment along the x axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     * @param target The target container.
     * @return The X-axis alignment.
     */
    public float getLayoutAlignmentX (Container target)
    {
        return (0.0f);
    }
  
    /**
     * Returns the alignment along the y axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     * @param target The target container.
     * @return The Y-axis alignment.
     */
    public float getLayoutAlignmentY (Container target)
    {
        return (0.0f);
    }
  
    /**
     * Invalidates the layout, indicating that if the layout manager
     * has cached information it should be discarded.
     * @param target The target container.
     */
    public void invalidateLayout (Container target)
    {
    }
}
