// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Somik Raha
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/util/NodeList.java,v $
// $Author: derrickoswald $
// $Date: 2005/03/12 13:39:47 $
// $Revision: 1.58 $
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

package org.htmlparser.util;

import java.io.Serializable;
import java.util.NoSuchElementException;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.visitors.NodeVisitor;

public class NodeList implements Serializable {
    private static final int INITIAL_CAPACITY=10;
    //private static final int CAPACITY_INCREMENT=20;
    private Node nodeData[];
    private int size;
    private int capacity;
    private int capacityIncrement;
    private int numberOfAdjustments;

    public NodeList() {
        size = 0;
        capacity = INITIAL_CAPACITY;
        nodeData = newNodeArrayFor(capacity);
        capacityIncrement = capacity*2;
        numberOfAdjustments = 0;
    }

    /**
     * Create a one element node list.
     * @param node The initial node to add.
     */
    public NodeList(Node node)
    {
        this ();
        add (node);
    }
        
    public void add(Node node) {
        if (size==capacity)
            adjustVectorCapacity();
        nodeData[size++]=node;
    }

    /**
     * Add another node list to this one.
     * @param list The list to add.
     */
    public void add (NodeList list)
    {
        for (int i = 0; i < list.size; i++)
            add (list.nodeData[i]);
    }

    /**
     * Insert the given node at the head of the list.
     * @param node The new first element.
     */
    public void prepend(Node node)
    {
        if (size==capacity)
            adjustVectorCapacity();
        System.arraycopy (nodeData, 0, nodeData, 1, size);
        size++;
        nodeData[0]=node;
    }

    private void adjustVectorCapacity() {
        capacity += capacityIncrement;
        capacityIncrement *= 2;
        Node oldData [] = nodeData;
        nodeData = newNodeArrayFor(capacity);
        System.arraycopy(oldData, 0, nodeData, 0, size);
        numberOfAdjustments++;
    }

    private Node[] newNodeArrayFor(int capacity) {
        return new Node[capacity];
    }

    public int size() {
        return size;
    }

    public Node elementAt(int i) {
        return nodeData[i];
    }

    public int getNumberOfAdjustments() {
        return numberOfAdjustments;
    }

    public SimpleNodeIterator elements() {
        return new SimpleNodeIterator() {
            int count = 0;

            public boolean hasMoreNodes() {
                return count < size;
            }

            public Node nextNode() {
            synchronized (NodeList.this) {
                if (count < size) {
                return nodeData[count++];
                }
            }
            throw new NoSuchElementException("Vector Enumeration");
            }
        };
    }

    public Node [] toNodeArray() {
        Node [] nodeArray = newNodeArrayFor(size);
        System.arraycopy(nodeData, 0, nodeArray, 0, size);
        return nodeArray;
    }

    public void copyToNodeArray(Node[] array) {
        System.arraycopy(nodeData, 0, array, 0, size);
    }

    public String asString() {
        StringBuffer buff = new StringBuffer();
        for (int i=0;i<size;i++)
            buff.append(nodeData[i].toPlainTextString());
        return buff.toString();
    }

    /**
     * Convert this nodelist into the equivalent HTML.
     * @deprecated Use {@link #toHtml}.
     * @return The contents of the list as HTML text.
     */
    public String asHtml()
    {
        return (toHtml ());
    }

    /**
     * Convert this nodelist into the equivalent HTML.
     * @return The contents of the list as HTML text.
     */
    public String toHtml()
    {
        StringBuffer buff = new StringBuffer();
        for (int i=0;i<size;i++)
            buff.append(nodeData[i].toHtml());
        return buff.toString();
    }

    public Node remove(int index) {
        Node ret;
        ret = nodeData[index];
        System.arraycopy(nodeData, index+1, nodeData, index, size-index-1);
        nodeData[size-1] = null;
        size--;
        return (ret);
    }

    public void removeAll() {
        size = 0;
        capacity = INITIAL_CAPACITY;
        nodeData = newNodeArrayFor(capacity);
        capacityIncrement = capacity*2;
        numberOfAdjustments = 0;
    }

    /**
     * Return the contents of the list as a string.
     * Suitable for debugging.
     * @return A string representation of the list. 
     */
    public String toString()
    {
        StringBuffer text = new StringBuffer();
        for (int i=0;i<size;i++)
            text.append (nodeData[i]);
        return (text.toString ());
    }

    /**
     * Filter the list with the given filter non-recursively.
     * @param filter The filter to use.
     * @return A new node array containing the nodes accepted by the filter.
     * This is a linear list and preserves the nested structure of the returned
     * nodes only.
     */
    public NodeList extractAllNodesThatMatch (NodeFilter filter)
    {
        return (extractAllNodesThatMatch (filter, false));
    }

    /**
     * Filter the list with the given filter.
     * @param filter The filter to use.
     * @param recursive If <code>true<code> digs into the children recursively.
     * @return A new node array containing the nodes accepted by the filter.
     * This is a linear list and preserves the nested structure of the returned
     * nodes only.
     */
    public NodeList extractAllNodesThatMatch (NodeFilter filter, boolean recursive)
    {
        Node node;
        NodeList children;
        NodeList ret;

        ret = new NodeList ();
        for (int i = 0; i < size; i++)
        {
            node = nodeData[i];
            if (filter.accept (node))
                ret.add (node);
            if (recursive)
            {
                children = node.getChildren ();
                if (null != children)
                    ret.add (children.extractAllNodesThatMatch (filter, recursive));
            }
        }

        return (ret);
    }

    /**
     * Remove nodes not matching the given filter non-recursively.
     * @param filter The filter to use.
     */
    public void keepAllNodesThatMatch (NodeFilter filter)
    {
        keepAllNodesThatMatch (filter, false);
    }

    /**
     * Remove nodes not matching the given filter.
     * @param filter The filter to use.
     * @param recursive If <code>true<code> digs into the children recursively.
     */
    public void keepAllNodesThatMatch (NodeFilter filter, boolean recursive)
    {
        Node node;
        NodeList children;

        for (int i = 0; i < size; )
        {
            node = nodeData[i];
            if (!filter.accept (node))
                remove (i);
            else
            {
                if (recursive)
                {
                    children = node.getChildren ();
                    if (null != children)
                        children.keepAllNodesThatMatch (filter, recursive);
                }
                i++;
            }
        }
    }

    /**
     * Convenience method to search for nodes of the given type non-recursively.
     * @param classType The class to search for.
     */
    public NodeList searchFor (Class classType)
    {
        return (searchFor (classType, false));
    }

    /**
     * Convenience method to search for nodes of the given type.
     * @param classType The class to search for.
     * @param recursive If <code>true<code> digs into the children recursively.
     */
    public NodeList searchFor (Class classType, boolean recursive)
    {
        return (extractAllNodesThatMatch (new NodeClassFilter (classType), recursive));
    }

    /**
     * Utility to apply a visitor to a node list.
     * Provides for a visitor to modify the contents of a page and get the
     * modified HTML as a string with code like this:
     * <pre>
     * Parser parser = new Parser ("http://whatever");
     * NodeList list = parser.parse (null); // no filter
     * list.visitAllNodesWith (visitor);
     * System.out.println (list.toHtml ());
     * </pre>
     */
    public void visitAllNodesWith (NodeVisitor visitor)
        throws
            ParserException
    {
        Node node;

        visitor.beginParsing ();
        for (int i = 0; i < size; i++)
            nodeData[i].accept (visitor);
        visitor.finishedParsing ();
    }
}
