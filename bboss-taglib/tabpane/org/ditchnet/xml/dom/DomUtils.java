/*
 * The contents of this file are subject to the GNU Lesser General Public
 * License Version 2.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/lesser.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Developer:
 * Todd Ditchendorf, todd@ditchnet.org
 *
 */

/**
 *	@author Todd Ditchendorf
 *	@since 2005-03-13
 *
 */
package org.ditchnet.xml.dom;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import org.w3c.dom.*;
import org.ditchnet.xml.Xhtml;

/**
 *	A Utility class that may not be instanciated (private constructor) that
 *	contains static utility methods that ease DOM traversal pain.
 *	@author Todd Ditchendorf
 *
 *
 */
public class DomUtils {
	
	/**
	 *	Private constructor -- class may not be instanciated.
	 */
	private DomUtils() { }
	
	/**
	 *	Test <code>Element</code> param to see if it's HTML <code>id</code>
	 *	attribute matches <code>id</code>.
	 *	@param	target	The Element to test.
	 *	@param	id		Check target for this value in it's HTML
	 *					<code>id</code> attribute.
	 */
	public static boolean hasId(final Element target,final String id) {
		return target.getAttribute(Xhtml.Attr.ID.toString()).equals(id);
	}
	
	/**
	 *	Test the <code>target</code> param to see if it's HTML <code>class</code>
	 *	attribute contains <code>className</code>. Keep in mind that the HTML
	 *	4.01 spec allows an element's HTML <code>class</code> attribute value
	 *	to consist of multiple space-separated values. This method takes that
	 *	fact into account, and tests <code>target</code>'s HTML class attribute
	 *	for the existence of <code>className</code> anywhere in the string.</p>
	 *
	 *	<p>So for example, say you had an <code>org.w3c.dom.Element</code>
	 *	reference to the following HTML element in a variable named
	 *	<code>target</code>:</p>
	 *	<p><code>&lt;div class="ditch-tab-pane ditch-focused"&gt;&lt;/div&gt;
	 *	</code></p>
	 *	<p>note that this attribute value is taking advantage of the
	 *	ability to list multiple space separated values.</p>
	 *	<p><code>DomUtils.hasClassName(target,"ditch-tab-pane");</code></p>
	 *	<p>Would return <code>true</code>.</p>
	 *	<p><code>DomUtils.hasClassName(target,"ditch-tab");</code></p>
	 *	<p>Would return <code>false</code>.</p>
	 *	
	 *	@param	target		The Element to test.
	 *	@param	className	Check target for this value in it's HTML
	 *						<code>class</code> attribute.
	 */
	//*	@throws IllegalArgumentException
	public static boolean hasClassName(final Element target,
									   String className) {
		className = className.trim();
		/*if (className.indexOf(" ") > -1) {
			throw new IllegalArgumentException("Whitespace is not allowed " +
				   "in the className arg. hasClassName() accepts only one " +
				   "class name at a time");
		}*/
		String cn = target.getAttribute(Xhtml.Attr.CLASS.toString());
		if (null == cn || 0 == cn.length()) {
			return false;
		}
		cn = cn.trim();
		if (cn.equals(className)) {
			return true;
		}
		if (cn.indexOf(className + " ") > -1) {
			return true;
		}
		if (_isLastOfMultpleClassNames(cn,className)) {
			return true;
		}
		return false;
	}
	
	private static boolean _isLastOfMultpleClassNames(final String all,
											  final String className) {
		int spaceBefore = all.lastIndexOf(className)-1;
		return all.endsWith(className) && 
			all.substring(spaceBefore,spaceBefore+1).equals(" ");
	}
	
	/**
	 *	Tests to see if <code>target</code>'s <code>getNodeType()</code> 
	 *	method returns <code>Node.ELEMENT_NODE</code>.
	 *	
	 */
	public static boolean isElementNode(final Node target) {
		return Node.ELEMENT_NODE == target.getNodeType();
	}
	
	/**
	 *	Search up the DOM tree for the first ancestor element of <code>
	 *	target</code> with an HTML <code>class</code> attribute value of
	 *	<code>className</code>.
	 */
	public static Element getFirstAncestorByClassName(final Node target,
													  final String className) {
		Element parent = (Element)target;
		while ((parent = (Element)parent.getParentNode()) != null) {
			if (hasClassName(parent,className)) {
				return parent;
			}
		}
		return null;
	}
	
	/**
	 *	Search up the DOM tree for the first ancestor element of <code>
	 *	target</code> with an HTML <code>class</code> attribute value of
	 *	<code>className</code>.
	 */
	public static Element getFirstAncestorOrSelfByClassName(
													final Node target,
													final String className) {
		Element parent = (Element)target;
		do {
			if (hasClassName(parent,className)) {
				return parent;
			}
		} while ((parent = (Element)parent.getParentNode()) != null);
		return null;
	}
	
	/**
	 *	Search <code>target</code>'s children for the first element node
	 *	with an HTML <code>class</code> attribute value of
	 *	<code>className</code>.
	 */
	public static Element getFirstChildByClassName(
												final Node target,
												final String className) {
		Node childNode;
		for (int i = 0; i < target.getChildNodes().getLength(); i++) {
			childNode = target.getChildNodes().item(i);
			if ( Node.ELEMENT_NODE != childNode.getNodeType() ) {
				continue;
			}
			if (hasClassName(((Element)childNode),className)) {
				return (Element)childNode;
			}
		}
		return null;
	}
	
	/**
	 *	Retreives <code>target</code>'s first descendant element with an
	 *	HTML <code>class</code> attribute value that includes <code>
	 *	className</code> using a breadth-first algorithm.
	 */
	public static Element getFirstDescendantByClassNameBreadthFirst(
												final Node target,
												final String className) {
		Element result;
		if ((result = getFirstChildByClassName(target,className)) != null) {
			return result;
		}
		for (int i = 0; i < target.getChildNodes().getLength(); i++) {
			result = getFirstDescendantByClassNameBreadthFirst(
											target.getChildNodes().item(i),
											className );
			if (null != result) {
				return result;
			}
		}
		return null;
	}
	
	/**
	 *	Retreives <code>target</code>'s first descendant element with an
	 *	HTML <code>class</code> attribute value that includes <code>
	 *	className</code> using a depth-first algorithm.
	 */
	public static Element getFirstDescendantByClassNameDepthFirst(
												  final Node target,
												  final String className) {
		Node child;
		Element result;
		for (int i = 0; i < target.getChildNodes().getLength(); i++) {
			child = target.getChildNodes().item(i);
			if (isElementNode(child) && hasClassName(((Element)child),className)) {
				return (Element)child;
			}
			result = getFirstDescendantByClassNameDepthFirst(
													target.getChildNodes().item(i),
													className );
			if (null != result) {
				return result;
			}
		}
		return null;
	}
	
	/**
	 *	Returns all child elements of <code>target</code> with HTML <code>
	 *	class</code> attribute values that contain <code>className</code>
	 *	as an array of type {@link java.util.List}. NOTE that unlike the
	 *	algorithms specified in the DOM spec, a <code>NodeList</code> is NOT
	 *	returned. This method searches only one level deep... <code>
	 *	target</code>'s child nodes.
	 */
	public static List getChildrenByClassName(final Node target,
												   final String className) {
		List result = new ArrayList();
		Node child;
		for (int i = 0; i < target.getChildNodes().getLength(); i++) {
			child = target.getChildNodes().item(i);
		 	if (isElementNode(child) && hasClassName(((Element)child),className)) {
				result.add(child);
			}
		}
		return result;
	}
	
	/**
	 *	Returns all descendant elements of <code>target</code> with HTML <code>
	 *	class</code> attribute values that contain <code>className</code>
	 *	as an array of type {@link java.util.List}. NOTE that unlike the
	 *	algorithms specified in the DOM spec, a <code>NodeList</code> is NOT
	 *	returned. This method searched for all descendants of <code>target
	 *	</code> meeting the criteria using a breadth-first algorithm.
	 */
	public static List getDescendantsByClassName(final Node target,
												final String className) {
		List result = new ArrayList();
		result.addAll(getChildrenByClassName(target,className));
		for (int i = 0; i < target.getChildNodes().getLength(); i++) {
			result.addAll(getDescendantsByClassName(
											target.getChildNodes().item(i),
											className));
		}
		return result;
	}
	
	
	
}
