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
package org.htmlparser.tags;

import org.htmlparser.Node;
import org.htmlparser.scanners.ResourceScanner;
import org.htmlparser.util.SimpleNodeIterator;
import org.htmlparser.visitors.NodeVisitor;

/**
 * <p>Title: ResourceTag.java</p>
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
public class ResourceTag extends CompositeTag{
	protected boolean changed = false;
	protected String resourceText; 
	protected boolean shouldRecurseChildren = true;
	   /**
     * The set of names handled by this tag.
     */
    private static final String[] mIds = new String[] {"CFILE","CIMAGE","CFLASHPLAYER","CMEDIA"};
    private static final String[] mEndTagEnders = new String[] {"BODY", "HTML"};
    /**
     * Return the set of names handled by this tag.
     * @return The names to be matched that create tags of this type.
     */
    public String[] getIds ()
    {
        return (mIds);
    }
    
    public ResourceTag()
    {
    	 setThisScanner (new ResourceScanner());
    }
    

    /**
     * Return the set of end tag names that cause this tag to finish.
     * @return The names of following end tags that stop further scanning.
     */
    public String[] getEndTagEnders ()
    {
        return (mEndTagEnders);
    }

	public String getResourceText() {
		return resourceText;
	}

	public void setResourceText(String resourceText) {
		this.changed = true;
		this.resourceText = resourceText;
	}
	public boolean isChanged()
	{
		return this.changed;
	}
	
	 /**
     * Tag visiting code.
     * Invokes <code>accept()</code> on the start tag and then
     * walks the child list invoking <code>accept()</code> on each
     * of the children, finishing up with an <code>accept()</code>
     * call on the end tag. If <code>shouldRecurseSelf()</code>
     * returns true it then asks the visitor to visit itself.
     * @param visitor The <code>NodeVisitor</code> object to be signalled
     * for each child and possibly this tag.
     */
    public void accept (NodeVisitor visitor)
    {
        SimpleNodeIterator children;
        Node child;

        if (visitor.shouldRecurseSelf ())
            visitor.visitTag (this);
        if (visitor.shouldRecurseChildren () && shouldRecurseChildren)
        {
            if (null != getChildren ())
            {
                children = children ();
                while (children.hasMoreNodes ())
                {
                    child = children.nextNode ();
                    child.accept (visitor);
                }
            }
            if ((null != getEndTag ()) && (this != getEndTag ())) // 2nd guard handles <tag/>
                getEndTag ().accept (visitor);
        }
    }

	public boolean isShouldRecurseChildren() {
		return shouldRecurseChildren;
	}

	public void setShouldRecurseChildren(boolean shouldRecurseChildren) {
		this.shouldRecurseChildren = shouldRecurseChildren;
	}

}
