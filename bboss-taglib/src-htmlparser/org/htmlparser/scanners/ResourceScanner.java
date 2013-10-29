/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package org.htmlparser.scanners;

import java.util.Vector;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * <p>Title: ResourceScanner.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2013-9-24
 * @author biaoping.yin
 * @version 1.0
 */
public class ResourceScanner  extends CompositeTagScanner
{
    /**
     * Create a style scanner.
     */
    public ResourceScanner ()
    {
    }
    
    private boolean inIds(String tagname,String[] ids)
    {
    	for(String id:ids)
    	{
    		if(tagname.equals(id))
    			return true;
    	}
    	return false;
    }

    /**
     * Scan for style definitions.
     * Accumulates text from the page, until &lt;/[a-zA-Z] is encountered.
     * @param tag The tag this scanner is responsible for.
     * @param lexer The source of CDATA.
     * @param stack The parse stack, <em>not used</em>.
     */
    public Tag scan (Tag tag, Lexer lexer, NodeList stack)
        throws ParserException
    {
    	String tagname = tag.getTagName();
    	int len = tagname.length();
    	Node content;
        int position;
        Node node;
        Attribute attribute;
        Vector vector;

//        content = lexer.parseCDATA ();
        content = lexer.parseCDATA ();
//        content = lexer.parseCompositeCDATA_(false, tagname.toLowerCase() + "]", len);
        position = lexer.getPosition ();
        node = lexer.nextNode (false);
        if (null != node)
            if (!(node instanceof Tag) || !(   ((Tag)node).isEndTag ()
                && ((Tag)node).getTagName ().equals (tagname)))
            {
                lexer.setPosition (position);
                node = null;
            }

        // build new end tag if required
        if (null == node)
        {
            attribute = new Attribute ("/" + tagname.toLowerCase(), null);
            vector = new Vector ();
            vector.addElement (attribute);
            node = lexer.getNodeFactory ().createTagNode (
                lexer.getPage (), position, position, vector);
        }
        tag.setEndTag ((Tag)node);
        if (null != content)
        {
            tag.setChildren (new NodeList (content));
            content.setParent (tag);
        }
        node.setParent (tag);
        tag.doSemanticAction ();

        return (tag);
    }
}
