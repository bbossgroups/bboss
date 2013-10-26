// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2003 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/PrototypicalNodeFactory.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/24 17:48:27 $
// $Revision: 1.15 $
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

package org.htmlparser;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.htmlparser.lexer.Page;
import org.htmlparser.nodes.RemarkNode;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.AppletTag;
import org.htmlparser.tags.BaseHrefTag;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.Bullet;
import org.htmlparser.tags.BulletList;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.DoctypeTag;
import org.htmlparser.tags.FormTag;
import org.htmlparser.tags.FrameSetTag;
import org.htmlparser.tags.FrameTag;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.Html;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.JspTag;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.tags.ObjectTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.ResourceTag;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.StyleTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableHeader;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.tags.TextareaTag;
import org.htmlparser.tags.TitleTag;

/**
 * A node factory based on the prototype pattern.
 * This factory uses the prototype pattern to generate new nodes.
 * These are cloned as needed to form new {@link Text}, {@link Remark} and
 * {@link Tag} nodes.</p>
 * <p>Text and remark nodes are generated from prototypes accessed
 * via the {@link #setTextPrototype(Text) textPrototype} and
 * {@link #setRemarkPrototype(Remark) remarkPrototype} properties respectively.
 * Tag nodes are generated as follows:
 * <p>Prototype tags, in the form of undifferentiated tags, are held in a hash
 * table. On a request for a tag, the attributes are examined for the name
 * of the tag to be created. If a prototype of that name has been registered
 * (exists in the hash table), it is cloned and the clone is given the
 * characteristics ({@link Attribute Attributes}, start and end position)
 * of the requested tag.</p>
 * <p>In the case that no tag has been registered under that name,
 * a generic tag is created from the prototype acessed via the
 * {@link #setTagPrototype(Tag) tagPrototype} property.</p>
 * <p>The hash table of registered tags can be automatically populated with
 * all the know tags from the {@link org.htmlparser.tags} package when
 * the factory is constructed, or it can start out empty and be populated
 * explicitly.</p>
 * <p>Here is an example of how to override all text issued from
 * {@link org.htmlparser.nodes.TextNode#toPlainTextString()
 * Text.toPlainTextString()},
 * in this case decoding (converting character references),
 * which illustrates the use of setting the text prototype:
 * <pre>
 * PrototypicalNodeFactory factory = new PrototypicalNodeFactory ();
 * factory.setTextPrototype (
 *     // create a inner class that is a subclass of TextNode
 *     new TextNode () {
 *         public String toPlainTextString()
 *         {
 *             String original = super.toPlainTextString ();
 *             return (org.htmlparser.util.Translate.decode (original));
 *         }
 *     });
 * Parser parser = new Parser ();
 * parser.setNodeFactory (factory);
 * </pre></p>
 * <p>Here is an example of using a custom link tag, in this case just
 * printing the URL, which illustrates registering a tag:
 * <pre>
 *
 * class PrintingLinkTag extends LinkTag
 * {
 *     public void doSemanticAction ()
 *         throws
 *             ParserException
 *     {
 *         System.out.println (getLink ());
 *     }
 * }
 * PrototypicalNodeFactory factory = new PrototypicalNodeFactory ();
 * factory.registerTag (new PrintingLinkTag ());
 * Parser parser = new Parser ();
 * parser.setNodeFactory (factory);
 * </pre></p>
 */
public class PrototypicalNodeFactory
    implements
        Serializable,
        NodeFactory
{
    /**
     * The prototypical text node.
     */
    protected Text mText;

    /**
     * The prototypical remark node.
     */
    protected Remark mRemark;

    /**
     * The prototypical tag node.
     */
    protected Tag mTag;

    /**
     * The list of tags to return.
     * The list is keyed by tag name.
     */
    protected Map mBlastocyst;

    /**
     * Create a new factory with all tags registered.
     * Equivalent to
     * {@link #PrototypicalNodeFactory() PrototypicalNodeFactory(false)}.
     */
    public PrototypicalNodeFactory ()
    {
        this (false);
    }

    /**
     * Create a new factory.
     * @param empty If <code>true</code>, creates an empty factory,
     * otherwise create a new factory with all tags registered.
     */
    public PrototypicalNodeFactory (boolean empty)
    {
        clear ();
        mText = new TextNode (null, 0, 0);
        mRemark = new RemarkNode (null, 0, 0);
        mTag = new TagNode (null, 0, 0, null);
        if (!empty)
            registerTags ();
    }

    /**
     * Create a new factory with the given tag as the only registered tag.
     * @param tag The single tag to register in the otherwise empty factory.
     */
    public PrototypicalNodeFactory (Tag tag)
    {
        this (true);
        registerTag (tag);
    }

    /**
     * Create a new factory with the given tags registered.
     * @param tags The tags to register in the otherwise empty factory.
     */
    public PrototypicalNodeFactory (Tag[] tags)
    {
        this (true);
        for (int i = 0; i < tags.length; i++)
            registerTag (tags[i]);
    }

    /**
     * Adds a tag to the registry.
     * @param id The name under which to register the tag.
     * <strong>For proper operation, the id should be uppercase so it
     * will be matched by a Map lookup.</strong>
     * @param tag The tag to be returned from a {@link #createTagNode} call.
     * @return The tag previously registered with that id if any,
     * or <code>null</code> if none.
     */
    public Tag put (String id, Tag tag)
    {
        return ((Tag)mBlastocyst.put (id, tag));
    }

    /**
     * Gets a tag from the registry.
     * @param id The name of the tag to return.
     * @return The tag registered under the <code>id</code> name,
     * or <code>null</code> if none.
     */
    public Tag get (String id)
    {
        return ((Tag)mBlastocyst.get (id));
    }

    /**
     * Remove a tag from the registry.
     * @param id The name of the tag to remove.
     * @return The tag that was registered with that <code>id</code>,
     * or <code>null</code> if none.
     */
    public Tag remove (String id)
    {
        return ((Tag)mBlastocyst.remove (id));
    }

    /**
     * Clean out the registry.
     */
    public void clear ()
    {
        mBlastocyst = new Hashtable ();
    }

    /**
     * Get the list of tag names.
     * @return The names of the tags currently registered.
     */
    public Set getTagNames ()
    {
        return (mBlastocyst.keySet ());
    }

    /**
     * Register a tag.
     * Registers the given tag under every {@link Tag#getIds() id} that the
     * tag has (i.e. all names returned by {@link Tag#getIds() tag.getIds()}.
     * <p><strong>For proper operation, the ids are converted to uppercase so
     * they will be matched by a Map lookup.</strong>
     * @param tag The tag to register.
     */
    public void registerTag (Tag tag)
    {
        String[] ids;

        ids = tag.getIds ();
        for (int i = 0; i < ids.length; i++)
            put (ids[i].toUpperCase (Locale.ENGLISH), tag);
    }

    /**
     * Unregister a tag.
     * Unregisters the given tag from every {@link Tag#getIds() id} the tag has.
     * <p><strong>The ids are converted to uppercase to undo the operation
     * of registerTag.</strong>
     * @param tag The tag to unregister.
     */
    public void unregisterTag (Tag tag)
    {
        String[] ids;

        ids = tag.getIds ();
        for (int i = 0; i < ids.length; i++)
            remove (ids[i].toUpperCase (Locale.ENGLISH));
    }

    /**
     * Register all known tags in the tag package.
     * Registers tags from the {@link org.htmlparser.tags tag package} by
     * calling {@link #registerTag(Tag) registerTag()}.
     * @return 'this' nodefactory as a convenience.
     */
    public PrototypicalNodeFactory registerTags ()
    {
        registerTag (new AppletTag ());
        registerTag (new BaseHrefTag ());
        registerTag (new Bullet ());
        registerTag (new BulletList ());
        registerTag (new DoctypeTag ());
        registerTag (new FormTag ());
        registerTag (new FrameSetTag ());
        registerTag (new FrameTag ());
        registerTag (new ImageTag ());
        registerTag (new InputTag ());
        registerTag (new JspTag ());
        registerTag (new LabelTag ());
        registerTag (new LinkTag ());
        registerTag (new MetaTag ());
        registerTag (new ObjectTag ());
        registerTag (new OptionTag ());
        registerTag (new ScriptTag ());
        registerTag (new SelectTag ());
        registerTag (new StyleTag ());
        registerTag (new TableColumn ());
        registerTag (new TableHeader ());
        registerTag (new TableRow ());
        registerTag (new TableTag ());
        registerTag (new TextareaTag ());
        registerTag (new TitleTag ());
        registerTag (new Div ());
        registerTag (new Span ());
        registerTag (new BodyTag ());
        registerTag (new HeadTag ());
        registerTag (new Html ());
        registerTag (new ResourceTag ());

        return (this);
    }

    /**
     * Get the object that is cloned to generate text nodes.
     * @return The prototype for {@link Text} nodes.
     */
    public Text getTextPrototype ()
    {
        return (mText);
    }

    /**
     * Set the object to be used to generate text nodes.
     * @param text The prototype for {@link Text} nodes.
     * If <code>null</code> the prototype is set to the default
     * ({@link TextNode}).
     */
    public void setTextPrototype (Text text)
    {
        if (null == text)
            mText = new TextNode (null, 0, 0);
        else
            mText = text;
    }

    /**
     * Get the object that is cloned to generate remark nodes.
     * @return The prototype for {@link Remark} nodes.
     */
    public Remark getRemarkPrototype ()
    {
        return (mRemark);
    }

    /**
     * Set the object to be used to generate remark nodes.
     * @param remark The prototype for {@link Remark} nodes.
     * If <code>null</code> the prototype is set to the default
     * ({@link RemarkNode}).
     */
    public void setRemarkPrototype (Remark remark)
    {
        if (null == remark)
            mRemark = new RemarkNode (null, 0, 0);
        else
            mRemark = remark;
    }

    /**
     * Get the object that is cloned to generate tag nodes.
     * Clones of this object are returned from {@link #createTagNode} when no
     * specific tag is found in the list of registered tags.
     * @return The prototype for {@link Tag} nodes.
     */
    public Tag getTagPrototype ()
    {
        return (mTag);
    }

    /**
     * Set the object to be used to generate tag nodes.
     * Clones of this object are returned from {@link #createTagNode} when no
     * specific tag is found in the list of registered tags.
     * @param tag The prototype for {@link Tag} nodes.
     * If <code>null</code> the prototype is set to the default
     * ({@link TagNode}).
     */
    public void setTagPrototype (Tag tag)
    {
        if (null == tag)
            mTag = new TagNode (null, 0, 0, null);
        else
            mTag = tag;
    }

    //
    // NodeFactory interface
    //

    /**
     * Create a new string node.
     * @param page The page the node is on.
     * @param start The beginning position of the string.
     * @param end The ending position of the string.
     * @return A text node comprising the indicated characters from the page.
     */
    public Text createStringNode (Page page, int start, int end)
    {
        Text ret;

        try
        {
            ret = (Text)(getTextPrototype ().clone ());
            ret.setPage (page);
            ret.setStartPosition (start);
            ret.setEndPosition (end);
        }
        catch (CloneNotSupportedException cnse)
        {
            ret = new TextNode (page, start, end);
        }

        return (ret);
    }

    /**
     * Create a new remark node.
     * @param page The page the node is on.
     * @param start The beginning position of the remark.
     * @param end The ending positiong of the remark.
     * @return A remark node comprising the indicated characters from the page.
     */
    public Remark createRemarkNode (Page page, int start, int end)
    {
        Remark ret;

        try
        {
            ret = (Remark)(getRemarkPrototype ().clone ());
            ret.setPage (page);
            ret.setStartPosition (start);
            ret.setEndPosition (end);
        }
        catch (CloneNotSupportedException cnse)
        {
            ret = new RemarkNode (page, start, end);
        }

        return (ret);
    }

    /**
     * Create a new tag node.
     * Note that the attributes vector contains at least one element,
     * which is the tag name (standalone attribute) at position zero.
     * This can be used to decide which type of node to create, or
     * gate other processing that may be appropriate.
     * @param page The page the node is on.
     * @param start The beginning position of the tag.
     * @param end The ending positiong of the tag.
     * @param attributes The attributes contained in this tag.
     * @return A tag node comprising the indicated characters from the page.
     */
    public Tag createTagNode (Page page, int start, int end, Vector attributes)
    {
        Attribute attribute;
        String id;
        Tag prototype;
        Tag ret;

        ret = null;

        if (0 != attributes.size ())
        {
            attribute = (Attribute)attributes.elementAt (0);
            id = attribute.getName ();
            if (null != id)
            {
                try
                {
                    id = id.toUpperCase (Locale.ENGLISH);
                    if (!id.startsWith ("/"))
                    {
                        if (id.endsWith ("/"))
                            id = id.substring (0, id.length () - 1);
                        prototype = (Tag)mBlastocyst.get (id);
                        if (null != prototype)
                        {
                            ret = (Tag)prototype.clone ();
                            ret.setPage (page);
                            ret.setStartPosition (start);
                            ret.setEndPosition (end);
                            ret.setAttributesEx (attributes);
                        }
                    }
                }
                catch (CloneNotSupportedException cnse)
                {
                    // default to creating a generic one
                }
            }
        }
        if (null == ret)
        {   // generate a generic node
            try
            {
                ret = (Tag)getTagPrototype ().clone ();
                ret.setPage (page);
                ret.setStartPosition (start);
                ret.setEndPosition (end);
                ret.setAttributesEx (attributes);
            }
            catch (CloneNotSupportedException cnse)
            {
                ret = new TagNode (page, start, end, attributes);
            }
        }

        return (ret);
    }
}
