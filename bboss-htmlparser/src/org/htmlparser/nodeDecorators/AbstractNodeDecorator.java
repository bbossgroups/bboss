// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Joshua Kerievsky
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/nodeDecorators/AbstractNodeDecorator.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/10 23:20:43 $
// $Revision: 1.23 $
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

package org.htmlparser.nodeDecorators;

import org.htmlparser.Node;
import org.htmlparser.Text;
import org.htmlparser.NodeFilter;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

/**
 * Node wrapping base class.
 * @deprecated Use direct subclasses or dynamic proxies instead.
 * <p>Use either direct subclasses of the appropriate node and set them on the
 * {@link org.htmlparser.PrototypicalNodeFactory PrototypicalNodeFactory},
 * or use a dynamic proxy implementing the required node type interface.
 * In the former case this avoids the wrapping and delegation, while the latter
 * case handles the wrapping and delegation without this class.</p>
 * <p>Here is an example of how to use dynamic proxies to accomplish the same
 * effect as using decorators to wrap Text nodes:
 * <pre>
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.Text;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.ParserException;

public class TextProxy
    implements
        InvocationHandler
{
    protected Object mObject;

    public static Object newInstance (Object object)
    {
        Class cls;
        
        cls = object.getClass ();
	return (Proxy.newProxyInstance (
	    cls.getClassLoader (),
	    cls.getInterfaces (),
	    new TextProxy (object)));
    }

    private TextProxy (Object object)
    {
	mObject = object;
    }

    public Object invoke (Object proxy, Method m, Object[] args)
	throws Throwable
    {
        Object result;
        String name;
	try
        {
	    result = m.invoke (mObject, args);
            name = m.getName ();
            if (name.equals ("clone"))
                result = newInstance (result); // wrap the cloned object
            else if (name.equals ("doSemanticAction")) // or other methods
               System.out.println (mObject); // do the needful on the TextNode
        }
        catch (InvocationTargetException e)
        {
	    throw e.getTargetException ();
        }
        catch (Exception e)
        {
	    throw new RuntimeException ("unexpected invocation exception: " +
				       e.getMessage());
	}
        finally
        {
	}
        
	return (result);
    }

    public static void main (String[] args)
        throws
            ParserException
    {
        // create the wrapped text node and set it as the prototype
        Text text = (Text) TextProxy.newInstance (new TextNode (null, 0, 0));
        PrototypicalNodeFactory factory = new PrototypicalNodeFactory ();
        factory.setTextPrototype (text);
        // perform the parse
        Parser parser = new Parser (args[0]);
        parser.setNodeFactory (factory);
        parser.parse (null);
    }
}
 * </pre>
 * </p>
 */
public abstract class AbstractNodeDecorator implements Text
{
    protected Text delegate;

    protected AbstractNodeDecorator(Text delegate)
    {
        this.delegate = delegate;
    }

    /**
     * Clone this object.
     * Exposes java.lang.Object clone as a public method.
     * @return A clone of this object.
     * @exception CloneNotSupportedException This shouldn't be thrown since
     * the {@link Node} interface extends Cloneable.
     */
    public Object clone() throws CloneNotSupportedException
    {
        return (super.clone ());
    }

    public void accept (NodeVisitor visitor)
    {
        delegate.accept (visitor);
    }

    public void collectInto(NodeList list, NodeFilter filter) {
        delegate.collectInto(list, filter);
    }

    /**
     * Gets the starting position of the node.
     * @return The start position.
     */
    public int getStartPosition ()
    {
        return (delegate.getStartPosition ());
    }

    /**
     * Sets the starting position of the node.
     * @param position The new start position.
     */
    public void setStartPosition (int position)
    {
        delegate.setStartPosition (position);
    }

    /**
     * Gets the ending position of the node.
     * @return The end position.
     */
    public int getEndPosition ()
    {
        return (delegate.getEndPosition ());
    }

    /**
     * Sets the ending position of the node.
     * @param position The new end position.
     */
    public void setEndPosition (int position)
    {
        delegate.setEndPosition (position);
    }
    
    /**
     * Get the page this node came from.
     * @return The page that supplied this node.
     */
    public Page getPage ()
    {
        return (delegate.getPage ());
    }

    /**
     * Set the page this node came from.
     * @param page The page that supplied this node.
     */
    public void setPage (Page page)
    {
        delegate.setPage (page);
    }

    public boolean equals(Object arg0)
    {
        return delegate.equals(arg0);
    }

    public Node getParent() {
        return delegate.getParent();
    }

    public String getText() {
        return delegate.getText();
    }

    public void setParent(Node node) {
        delegate.setParent(node);
    }

    /**
     * Get the children of this node.
     * @return The list of children contained by this node, if it's been set, <code>null</code> otherwise.
     */
    public NodeList getChildren ()
    {
        return (delegate.getChildren ());
    }

    /**
     * Set the children of this node.
     * @param children The new list of children this node contains.
     */
    public void setChildren (NodeList children)
    {
        delegate.setChildren (children);
    }

    public void setText(String text) {
        delegate.setText(text);
    }

    public String toHtml() {
        return delegate.toHtml();
    }

    public String toPlainTextString() {
        return delegate.toPlainTextString();
    }

    public String toString() {
        return delegate.toString();
    }

    public void doSemanticAction () throws ParserException {
        delegate.doSemanticAction ();
    }
    
    public boolean isResource()
    {
    	return delegate.isResource();
    }
    public void setResource(boolean isresource)
    {
    	this.delegate.setResource(isresource);
    }
}
