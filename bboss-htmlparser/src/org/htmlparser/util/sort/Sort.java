// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/util/sort/Sort.java,v $
// $Author: derrickoswald $
// $Date: 2004/01/14 02:53:47 $
// $Revision: 1.12 $
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

package org.htmlparser.util.sort;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * A quick sort algorithm to sort Lists or arrays.
 * Provides sort and binary search capabilities.
 *<p>
 * This all goes away in JDK 1.2.
 * <p>
 * @author James Gosling
 * @author Kevin A. Smith
 * @author Derrick Oswald
 * @version 1.4, 11 June, 1997
 */
public class Sort
{
    /**
     * No object of this class need ever be instantiated.
     * All methods are static.
     */
    private Sort ()
    {
    }

    /**
     * This is a generic version of C.A.R Hoare's Quick Sort algorithm.
     * This will handle Lists that are already
     * sorted, and Lists with duplicate keys.
     * Equivalent to:
     * <pre>
     * QuickSort (v, 0, v.size () - 1);
     * </pre>
     * @param v A <code>List</code> of <code>Ordered</code> items.
     * @exception ClassCastException If the List contains objects that
     * are not <code>Ordered</code>.
     */
    public static void QuickSort (List v) throws ClassCastException
    {
        QuickSort (v, 0, v.size () - 1);
    }

    /**
     * This is a generic version of C.A.R Hoare's Quick Sort algorithm.
     * This will handle Lists that are already
     * sorted, and Lists with duplicate keys.
     * <p>
     * If you think of a one dimensional List as going from
     * the lowest index on the left to the highest index on the right
     * then the parameters to this function are lowest index or
     * left and highest index or right.
     * @param v A <code>List</code> of <code>Ordered</code> items.
     * @param lo0 Left boundary of List partition.
     * @param hi0 Right boundary of List partition.
     * @exception ClassCastException If the List contains objects that
     * are not <code>Ordered</code>.
     */
    public static void QuickSort (List v, int lo0, int hi0) throws ClassCastException
    {
        int lo = lo0;
        int hi = hi0;
        Ordered mid;

        if ( hi0 > lo0)
        {   // arbitrarily establish partition element as the midpoint of the vector
            mid = (Ordered)v.get((lo0 + hi0) / 2);

            // loop through the vector until indices cross
            while (lo <= hi)
            {
                // find the first element that is greater than or equal to
                // the partition element starting from the left index
                while ((lo < hi0) && (0 > ((Ordered)v.get (lo)).compare (mid)))
                    ++lo;

                // find an element that is smaller than or equal to
                // the partition element starting from the right index
                while ((hi > lo0) && (0 < ((Ordered)v.get (hi)).compare (mid)))
                    --hi;

                // if the indexes have not crossed, swap
                if (lo <= hi)
                    swap (v, lo++, hi--);
            }

            // if the right index has not reached the left side of array
            // must now sort the left partition
            if (lo0 < hi)
                QuickSort (v, lo0, hi);

            // if the left index has not reached the right side of array
            // must now sort the right partition
            if (lo < hi0)
                QuickSort (v, lo, hi0);
        }
    }

    private static void swap (List v, int i, int j)
    {
        Object o;

        o = v.get (i);
        v.set (i,v.get (j));
        v.set (j,o);
    }

    /**
     * This is a generic version of C.A.R Hoare's Quick Sort algorithm.
     * This will handle arrays that are already sorted,
     * and arrays with duplicate keys.
     * <p>
     * Equivalent to:
     * <pre>
     * QuickSort (a, 0, a.length - 1);
     * </pre>
     * @param a An array of <code>Ordered</code> items.
     */
    public static void QuickSort (Ordered[] a)
    {
        QuickSort (a, 0, a.length - 1);
    }

    /**
     * This is a generic version of C.A.R Hoare's Quick Sort algorithm.
     * This will handle arrays that are already sorted,
     * and arrays with duplicate keys.
     * <p>
     * If you think of a one dimensional array as going from
     * the lowest index on the left to the highest index on the right
     * then the parameters to this function are lowest index or
     * left and highest index or right.
     * @param a An array of <code>Ordered</code> items.
     * @param lo0 Left boundary of array partition.
     * @param hi0 Right boundary of array partition.
     */
    public static void QuickSort (Ordered[] a, int lo0, int hi0)
    {
        int lo = lo0;
        int hi = hi0;
        Ordered mid;

        if ( hi0 > lo0)
        {
            // arbitrarily establish partition element as the midpoint of the array
            mid = a[(lo0 + hi0) / 2];

            // loop through the vector until indices cross
            while (lo <= hi)
            {
                // find the first element that is greater than or equal to
                // the partition element starting from the left index
                while ((lo < hi0) && (0 > a[lo].compare (mid)))
                    ++lo;

                // find an element that is smaller than or equal to
                // the partition element starting from the right Index.
                while ((hi > lo0) && (0 < a[hi].compare (mid)))
                    --hi;

                // if the indexes have not crossed, swap
                if (lo <= hi)
                    swap (a, lo++, hi--);
            }

            // if the right index has not reached the left side of array
            // must now sort the left partition
            if (lo0 < hi)
                QuickSort (a, lo0, hi);

            // if the left index has not reached the right side of array
            // must now sort the right partition
            if (lo < hi0)
                QuickSort (a, lo, hi0);
        }
    }

    /**
     * Swaps two elements of an array.
     * @param a The array of elements.
     * @param i The index of one item to swap.
     * @param j The index of the other item to swap.
     */
    private static void swap (Object[] a, int i, int j)
    {
        Object o;
        o = a[i];
        a[i] = a[j];
        a[j] = o;
    }

    /**
     * This is a string version of C.A.R Hoare's Quick Sort algorithm.
     * This will handle arrays that are already sorted,
     * and arrays with duplicate keys.
     * <p>
     * Equivalent to:
     * <pre>
     * QuickSort (a, 0, a.length - 1);
     * </pre>
     * @param a An array of <code>String</code> items.
     */
    public static void QuickSort (String[] a)
    {
        QuickSort (a, 0, a.length - 1);
    }

    /**
     * This is a string version of C.A.R Hoare's Quick Sort algorithm.
     * This will handle arrays that are already sorted,
     * and arrays with duplicate keys.
     * <p>
     * If you think of a one dimensional array as going from
     * the lowest index on the left to the highest index on the right
     * then the parameters to this function are lowest index or
     * left and highest index or right.
     * @param a An array of <code>String</code> items.
     * @param lo0 Left boundary of array partition.
     * @param hi0 Right boundary of array partition.
     */
    public static void QuickSort (String[] a, int lo0, int hi0)
    {
        int lo = lo0;
        int hi = hi0;
        String mid;

        if ( hi0 > lo0)
        {
            // arbitrarily establish partition element as the midpoint of the array
            mid = a[(lo0 + hi0) / 2];

            // loop through the vector until indices cross
            while (lo <= hi)
            {
                // find the first element that is greater than or equal to
                // the partition element starting from the left index
                while ((lo < hi0) && (0 > a[lo].compareTo (mid)))
                    ++lo;

                // find an element that is smaller than or equal to
                // the partition element starting from the right Index.
                while ((hi > lo0) && (0 < a[hi].compareTo (mid)))
                    --hi;

                // if the indexes have not crossed, swap
                if (lo <= hi)
                    swap (a, lo++, hi--);
            }

            // if the right index has not reached the left side of array
            // must now sort the left partition
            if (lo0 < hi)
                QuickSort (a, lo0, hi);

            // if the left index has not reached the right side of array
            // must now sort the right partition
            if (lo < hi0)
                QuickSort (a, lo, hi0);
        }
    }

    /**
     * This is a generic version of C.A.R Hoare's Quick Sort algorithm.
     * This will handle Sortable objects that are already
     * sorted, and Sortable objects with duplicate keys.
     * <p>
     * @param sortable A <code>Sortable</code> object.
     * @param lo0 Left boundary of partition.
     * @param hi0 Right boundary of partition.
     */
    public static void QuickSort (Sortable sortable, int lo0, int hi0)
    {
        int lo = lo0;
        int hi = hi0;
        Ordered mid;
        Ordered test;

        if ( hi0 > lo0)
        {   // arbitrarily establish partition element as the midpoint of the vector
            mid = sortable.fetch ((lo0 + hi0) / 2, null);
            test = null;

            // loop through the vector until indices cross
            while (lo <= hi)
            {
                // find the first element that is greater than or equal to
                // the partition element starting from the left index
                while ((lo < hi0) && (0 > (test = sortable.fetch (lo, test)).compare (mid)))
                    ++lo;

                // find an element that is smaller than or equal to
                // the partition element starting from the right index
                while ((hi > lo0) && (0 < (test = sortable.fetch (hi, test)).compare (mid)))
                    --hi;

                // if the indexes have not crossed, swap
                if (lo <= hi)
                    sortable.swap (lo++, hi--);
            }

            // if the right index has not reached the left side of array
            // must now sort the left partition
            if (lo0 < hi)
                QuickSort (sortable, lo0, hi);

            // if the left index has not reached the right side of array
            // must now sort the right partition
            if (lo < hi0)
                QuickSort (sortable, lo, hi0);
        }
    }

    /**
     * This is a generic version of C.A.R Hoare's Quick Sort algorithm.
     * This will handle Sortable objects that are already
     * sorted, and Sortable objects with duplicate keys.
     * <p>
     * Equivalent to:
     * <pre>
     * QuickSort (sortable, sortable.first (), sortable.last ());
     * </pre>
     * @param sortable A <code>Sortable</code> object.
     */
    public static void QuickSort (Sortable sortable)
    {
        QuickSort (sortable, sortable.first (), sortable.last ());
    }

    /**
     * Sort a Hashtable.
     * @param h A HashMap with String or Ordered keys.
     * @return A sorted array of the keys.
     * @exception ClassCastException If the keys of the HashMap
     * are not <code>Ordered</code>.
     */
    public static Object[] QuickSort (HashMap h) throws ClassCastException
    {
        Iterator e;
        boolean are_strings;
        Object[] ret;

        // make the array
        ret = new Ordered[h.size ()];
        e = h.keySet().iterator();
        are_strings = true; // until proven otherwise
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = e.next ();
            if (are_strings && !(ret[i] instanceof String))
                are_strings = false;
        }

        // sort it
        if (are_strings )
        {
        	if(ret instanceof String[])
        		QuickSort ((String[])ret);
        }
        else
        {
        	if(ret instanceof Ordered[])
        		QuickSort ((Ordered[])ret);
        }

        return (ret);
    }

    /**
     * Binary search for an object
     * @param set The collection of <code>Ordered</code> objects.
     * @param ref The name to search for.
     * @param lo The lower index within which to look.
     * @param hi The upper index within which to look.
     * @return The index at which reference was found or is to be inserted.
     */
    public static int bsearch (Sortable set, Ordered ref, int lo, int hi)
    {   int num;
        int mid;
        Ordered ordered;
        int half;
        int result;
        int ret;

        ret = -1;

        num = (hi - lo) + 1;
        ordered = null;
        while ((-1 == ret) && (lo <= hi))
        {
            half = num / 2;
            mid = lo + ((0 != (num & 1)) ? half : half - 1);
            ordered = set.fetch (mid, ordered);
            result = ref.compare (ordered);
            if (0 == result)
                ret = mid;
            else if (0 > result)
            {
                hi = mid - 1;
                num = ((0 != (num & 1)) ? half : half - 1);
            }
            else
            {
                lo = mid + 1;
                num = half;
            }
        }
        if (-1 == ret)
            ret = lo;

        return (ret);
    }

    /**
     * Binary search for an object
     * @param set The collection of <code>Ordered</code> objects.
     * @param ref The name to search for.
     * @return The index at which reference was found or is to be inserted.
     */
    public static int bsearch (Sortable set, Ordered ref)
    {
        return (bsearch (set, ref, set.first (), set.last ()));
    }

    /**
     * Binary search for an object
     * @param vector The vector of <code>Ordered</code> objects.
     * @param ref The name to search for.
     * @param lo The lower index within which to look.
     * @param hi The upper index within which to look.
     * @return The index at which reference was found or is to be inserted.
     */
    public static int bsearch (List vector, Ordered ref, int lo, int hi)
    {   int num;
        int mid;
        int half;
        int result;
        int ret;

        ret = -1;

        num = (hi - lo) + 1;
        while ((-1 == ret) && (lo <= hi))
        {
            half = num / 2;
            mid = lo + ((0 != (num & 1)) ? half : half - 1);
            result = ref.compare (vector.get (mid));
            if (0 == result)
                ret = mid;
            else if (0 > result)
            {
                hi = mid - 1;
                num = ((0 != (num & 1)) ? half : half - 1);
            }
            else
            {
                lo = mid + 1;
                num = half;
            }
        }
        if (-1 == ret)
            ret = lo;

        return (ret);
    }

    /**
     * Binary search for an object
     * @param vector The vector of <code>Ordered</code> objects.
     * @param ref The name to search for.
     * @return The index at which reference was found or is to be inserted.
     */
    public static int bsearch (List vector, Ordered ref)
    {
        return (bsearch (vector, ref, 0, vector.size () - 1));
    }

    /**
     * Binary search for an object
     * @param array The array of <code>Ordered</code> objects.
     * @param ref The name to search for.
     * @param lo The lower index within which to look.
     * @param hi The upper index within which to look.
     * @return The index at which reference was found or is to be inserted.
     */
    public static int bsearch (Ordered[] array, Ordered ref, int lo, int hi)
    {   int num;
        int mid;
        int half;
        int result;
        int ret;

        ret = -1;

        num = (hi - lo) + 1;
        while ((-1 == ret) && (lo <= hi))
        {
            half = num / 2;
            mid = lo + ((0 != (num & 1)) ? half : half - 1);
            result = ref.compare (array[mid]);
            if (0 == result)
                ret = mid;
            else if (0 > result)
            {
                hi = mid - 1;
                num = ((0 != (num & 1)) ? half : half - 1);
            }
            else
            {
                lo = mid + 1;
                num = half;
            }
        }
        if (-1 == ret)
            ret = lo;

        return (ret);
    }

    /**
     * Binary search for an object
     * @param array The array of <code>Ordered</code> objects.
     * @param ref The name to search for.
     * @return The index at which reference was found or is to be inserted.
     */
    public static int bsearch (Ordered[] array, Ordered ref)
    {
        return (bsearch (array, ref, 0, array.length - 1));
    }
}

