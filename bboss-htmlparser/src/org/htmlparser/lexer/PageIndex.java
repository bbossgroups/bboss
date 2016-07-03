// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/lexer/PageIndex.java,v $
// $Author: derrickoswald $
// $Date: 2005/05/15 11:49:04 $
// $Revision: 1.18 $
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

package org.htmlparser.lexer;

import java.io.Serializable;
import org.htmlparser.util.sort.Ordered;
import org.htmlparser.util.sort.Sort;
import org.htmlparser.util.sort.Sortable;

/**
 * A sorted array of integers, the positions of the first characters of each line.
 * To facilitate processing the first element should be maintained at position 0.
 * Facilities to add, remove, search and determine row and column are provided.
 * This class provides similar functionality to a Vector but
 * does not incur the overhead of an <code>Integer</code> object per element.
 */
public class PageIndex
    implements
        Serializable,
        Sortable
{
    /**
     * Starting increment for allocations.
     */
    protected static final int mStartIncrement = 100;

    /**
     * Increment for allocations.
     */
    protected int mIncrement;

    /**
     * The number of valid elements.
     */
    protected int mCount;

    /**
     * The elements.
     */
    protected int[] mIndices;

    /**
     * The page associated with this index.
     */
    protected Page mPage;

    /**
     * Create an empty index.
     * @param page The page associated with this index.
     */
    public PageIndex (Page page)
    {
        mPage = page;
        mIndices = new int[mIncrement];
        mCount = 0;
        mIncrement = mStartIncrement * 2;
    }

    /**
     * Create an index with the one element given.
     * @param page The page associated with this index.
     * @param cursor The single element for the new index.
     */
    public PageIndex (Page page, int cursor)
    {
        this (page);
        mIndices[0] = cursor;
        mCount = 1;
    }

    /**
     * Create an index with the elements given.
     * @param page The page associated with this index.
     * @param cursors The initial elements of the index.
     * NOTE: The list must be sorted in ascending order.
     */
    public PageIndex (Page page, int[] cursors)
    {
        mPage = page;
        mIndices = cursors;
        mCount = cursors.length;
    }

    /**
     * Get this index's page.
     * @return The page associated with this index.
     */
    public Page getPage ()
    {
        return (mPage);
    }

    /**
     * Get the count of elements.
     * @return The number of valid elements.
     */
    public int size ()
    {
        return (mCount);
    }

    /**
     * Get the capacity for elements without reallocation.
     * @return The number of spaces for elements.
     */
    public int capacity ()
    {
        return (mIndices.length);
    }

    /**
     * Add an element to the list
     * @param cursor The element to add.
     * @return The position at which the element was inserted or
     * the index of the existing element if it is a duplicate.
     */
    public int add (Cursor cursor)
    {
        int position;
        int last;
        int ret;

        position = cursor.getPosition ();
        if (0 == mCount)
        {
            ret = 0;
            insertElementAt (position, ret);
        }
        else
        {
            last = mIndices[mCount - 1];
            if (position == last)
                ret = mCount - 1;
            else
                if (position > last)
                {
                    ret = mCount;
                    insertElementAt (position, ret);
                }
                else
                {
        	        // find where it goes
        	        ret = Sort.bsearch (this, cursor);

	                // insert, but not twice
	                if (!((ret < size ()) && (position == mIndices[ret])))
	                    insertElementAt (position, ret);
                }
        }

        return (ret);
    }

    /**
     * Add an element to the list
     * @param cursor The element to add.
     * @return The position at which the element was inserted or
     * the index of the existing element if it is a duplicate.
     */
    public int add (int cursor)
    {
        return (add (new Cursor (getPage (), cursor)));
    }

    /**
     * Remove an element from the list
     * @param cursor The element to remove.
     */
    public void remove (Cursor cursor)
    {
        int i;

        // find it
        i = Sort.bsearch (this, cursor);

        // remove
        if ((i < size ()) && (cursor.getPosition () == mIndices[i]))
            removeElementAt (i);
    }

    /**
     * Remove an element from the list
     * @param cursor The element to remove.
     */
    public void remove (int cursor)
    {
        remove (new Cursor (getPage (), cursor));
    }

    /**
     * Get an element from the list.
     * @param index The index of the element to get.
     * @return The element.
     */
    public int elementAt (int index)
    {
        if (index >= mCount) // negative index is handled by array.. below
            throw new IndexOutOfBoundsException ("index " + index + " beyond current limit");
        else
            return (mIndices[index]);
    }

    /**
     * Get the line number for a cursor.
     * @param cursor The character offset into the page.
     * @return The line number the character is in.
     */
    public int row (Cursor cursor)
    {
        int ret;

        ret = Sort.bsearch (this, cursor);
        // handle line transition, the search returns the index if it matches
        // exactly one of the line end positions, so we advance one line if
        // it's equal to the offset at the row index, since that position is
        // actually the beginning of the next line
        if ((ret < mCount) && (cursor.getPosition () == mIndices[ret]))
            ret++;

        return (ret);
    }

    /**
     * Get the line number for a position.
     * @param cursor The character offset into the page.
     * @return The line number the character is in.
     */
    public int row (int cursor)
    {
        return (row (new Cursor (getPage (), cursor)));
    }

    /**
     * Get the column number for a cursor.
     * @param cursor The character offset into the page.
     * @return The character offset into the line this cursor is on.
     */
    public int column (Cursor cursor)
    {
        int row;
        int previous;

        row = row (cursor);
        if (0 != row)
            previous = this.elementAt (row - 1);
        else
            previous = 0;

        return (cursor.getPosition () - previous);
    }

    /**
     * Get the column number for a position.
     * @param cursor The character offset into the page.
     * @return The character offset into the line this cursor is on.
     */
    public int column (int cursor)
    {
        return (column (new Cursor (getPage (), cursor)));
    }

    /**
     * Get the elements as an array of int.
     * @return A new array containing the elements,
     * i.e. a snapshot of the index.
     */
    public int[] get ()
    {
        int[] ret = new int[size ()];
        System.arraycopy (mIndices, 0, ret, 0, size ());

        return (ret);
    }

    /**
     * Binary search for the element.
     * @param cursor The element to search for.
     * @return The index at which the element was found or is to be inserted.
     */
    protected int bsearch (int cursor)
    {
        return (Sort.bsearch (this, new Cursor (getPage (), cursor)));
    }

    /**
     * Binary search for the element.
     * @param cursor The element to search for.
     * @param first The index to start at.
     * @param last The index to stop at.
     * @return The index at which the element was found or is to be inserted.
     */
    protected int bsearch (int cursor, int first, int last)
    {
        return (Sort.bsearch (this, new Cursor (getPage (), cursor), first, last));
    }

    /**
     * Inserts an element into the list.
     * The index must be a value greater than or equal to 0 and less than
     * or equal to the current size of the array.
     * @param cursor The element to insert.
     * @param index The index in the list to insert it at.
     */
    protected void insertElementAt (int cursor, int index)
    {
        if ((index >= capacity ()) || (size () == capacity ()))
        {   // allocate more space
            int[] new_values = new int[Math.max (capacity () + mIncrement, index + 1)];
            mIncrement *= 2;
            if (index < capacity ())
            {
                // copy and shift up in two pieces
                System.arraycopy (mIndices, 0, new_values, 0, index);
                System.arraycopy (mIndices, index, new_values, index + 1, capacity () - index);
            }
            else
                System.arraycopy (mIndices, 0, new_values, 0, capacity ());
            mIndices = new_values;
        }
        else if (index < size ())
            // shift up
            System.arraycopy (mIndices, index, mIndices, index + 1, capacity () - (index + 1));
        mIndices[index] = cursor;
        mCount++;
    }

    /**
     * Remove an element from the list.
     * @param index The index of the item to remove.
     */
    protected void removeElementAt (int index)
    {
        // shift
        System.arraycopy (mIndices, index + 1, mIndices, index, capacity () - (index + 1));
        mIndices[capacity() - 1] = 0;
        mCount--;
    }

    //
    // Sortable interface
    //

    /**
     * Returns the first index of the Sortable.
     * @return The index of the first element.
     */
    public int first ()
    {
        return (0);
    }

    /**
     * Returns the last index of the Sortable.
     * @return The index of the last element.
     * If this were an array object this would be (object.length - 1).
     * For an empty index this will return -1.
     */
    public int last ()
    {
        return (mCount - 1);
    }

    /**
     * Fetch the object at the given index.
     * @param index The item number to get.
     * @param reuse If this argument is not null, it is an object
     * acquired from a previous fetch that is no longer needed and
     * may be returned as the result if it makes mores sense to alter
     * and return it than to fetch or create a new element. That is, the
     * reuse object is garbage and may be used to avoid allocating a new
     * object if that would normally be the strategy.
     * @return The Ordered object at that index.
     */
    public Ordered fetch (int index, Ordered reuse)
    {
        Cursor ret;

        if (null != reuse)
        {
            ret = (Cursor)reuse;
            ret.mPosition = mIndices[index];
            ret.mPage = getPage (); // redundant
        }
        else
            ret = new Cursor (getPage (), mIndices[index]);

        return (ret);
    }

    /**
     * Swaps the elements at the given indicies.
     * @param i One index.
     * @param j The other index.
     */
    public void swap (int i, int j)
    {
        int temp = mIndices[i];
        mIndices[i] = mIndices[j];
        mIndices[j] = temp;
    }
}
