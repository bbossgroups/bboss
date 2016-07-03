// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/util/sort/Ordered.java,v $
// $Author: derrickoswald $
// $Date: 2004/01/02 16:24:58 $
// $Revision: 1.11 $
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

/**
 * Describes an object that knows about ordering.
 * Implementors must have a comparison function,
 * which imposes a partial ordering on some
 * collection of objects. Ordered objects can be passed to a
 * sort method (such as org.htmlparser.util.sort.Sort) to allow precise control
 * over the sort order.
 * <p>
 * An set of elements S is partially ordered
 * if and only if <code>e1.compare(e2)==0</code> implies that
 * <code>e1.equals(e2)</code> for every e1 and e2 in S.
 * <p>
 * This all goes away in JDK 1.2.
 * <p>
 * For use with java.lang.Comparable from JDK 1.2:
 * <pre>
 * public int compare (Object o1, Object o2)
 * {
 *     return (((Ordered)o1).compare (o2));
 * }
 * </pre>
 * @see Sort
 */
public interface Ordered
{
    /**
     * Compares this object with another for order.
     * Returns a negative integer, zero, or a positive integer
     * as this object is less than, equal to, or greater
     * than the second.
     * <p>
     * The implementor must ensure that
     * <code>sgn(x.compare(y)) == -sgn(y.compare(x))</code>
     * for all x and y. (This implies that <code>x.compare(y)</code>
     * must throw an exception if and only if <code>y.compare(x)</code>
     * throws an exception.)
     * <p>
     * The implementor must also ensure that the relation is transitive:
     * <code>((x.compare(y)>0) && (y.compare(z)>0))</code>
     * implies <code>x.compare(z)>0</code>.
     * <p>
     * Finally, the implementer must ensure that
     * <code>x.compare(y)==0</code> implies that
     * <code>sgn(x.compare(z))==sgn(y.compare(z))</code>
     * for all z.
     * @param that The object to compare this object against.
     * @return A negative integer, zero, or a positive
     * integer as this object is less than, equal to,
     * or greater than the second.
     * @exception ClassCastException The arguments type prevents it
     * from being compared by this Ordered.
     */
    public int compare (Object that);
}
