// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/sax/XMLReader.java,v $
// $Author: derrickoswald $
// $Date: 2005/05/13 10:44:15 $
// $Revision: 1.3 $
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

package org.htmlparser.sax;

import java.io.IOException;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.NamespaceSupport;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Remark;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.util.DefaultParserFeedback;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.ParserFeedback;

/**
 * SAX parser.
 * Generates callbacks on the {@link ContentHandler} based on encountered nodes.
 * <br><em>Preliminary</em>.
 * <pre>
 * org.xml.sax.XMLReader reader = org.xml.sax.helpers.XMLReaderFactory.createXMLReader ("org.htmlparser.sax.XMLReader");
 * org.xml.sax.ContentHandler content = new MyContentHandler ();
 * reader.setContentHandler (content);
 * org.xml.sax.ErrorHandler errors = new MyErrorHandler ();
 * reader.setErrorHandler (errors);
 * reader.parse ("http://cbc.ca");
 * </pre>
 */
public class XMLReader
    implements
        org.xml.sax.XMLReader
{
    /**
     * Determines if namespace handling is on.
     * All XMLReaders are required to recognize the feature names:
     * <ul>
     * <li><code>http://xml.org/sax/features/namespaces</code> -
     *     a value of "true" indicates namespace URIs and unprefixed
     *     local names for element and attribute names will be available</li>
     * <li><code>http://xml.org/sax/features/namespace-prefixes</code> -
     *     a value of "true" indicates that XML qualified names (with
     *     prefixes) and attributes (including xmlns* attributes) will
     *     be available.
     * </ul>
     */
    protected boolean mNameSpaces; // namespaces

    /**
     * Determines if namespace prefix handling is on.
     * @see #mNameSpaces
     */
    protected boolean mNameSpacePrefixes; // namespace-prefixes

    /**
     * <em> not implemented</em>
     */
    protected EntityResolver mEntityResolver;

    /**
     * <em> not implemented</em>
     */
    protected DTDHandler mDTDHandler;

    /**
     * The content callback object.
     */
    protected ContentHandler mContentHandler;

    /**
     * The error handler object.
     */
    protected ErrorHandler mErrorHandler;

    /**
     * The underlying DOM parser.
     */
    protected Parser mParser;

    /**
     * Namspace utility object.
     */
    protected NamespaceSupport mSupport;

    /**
     * Qualified name parts.
     */
    protected String mParts[];

    /**
     * Create an SAX parser.
     */
    public XMLReader ()
    {
        mNameSpaces = true;
        mNameSpacePrefixes = false;
        
        mEntityResolver = null;
        mDTDHandler = null;
        mContentHandler = null;
        mErrorHandler = null;

        mSupport = new NamespaceSupport ();
        mSupport.pushContext ();
        mSupport.declarePrefix ("", "http://www.w3.org/TR/REC-html40");
        // todo:
        //    xmlns:html='http://www.w3.org/TR/REC-html40'
        // or xmlns:html='http://www.w3.org/1999/xhtml'
        mParts = new String[3];
    }
    
    ////////////////////////////////////////////////////////////////////
    // Configuration.
    ////////////////////////////////////////////////////////////////////


    /**
     * Look up the value of a feature flag.
     *
     * <p>The feature name is any fully-qualified URI.  It is
     * possible for an XMLReader to recognize a feature name but
     * temporarily be unable to return its value.
     * Some feature values may be available only in specific
     * contexts, such as before, during, or after a parse.
     * Also, some feature values may not be programmatically accessible.
     * (In the case of an adapter for SAX1 {@link Parser}, there is no
     * implementation-independent way to expose whether the underlying
     * parser is performing validation, expanding external entities,
     * and so forth.) </p>
     *
     * <p>All XMLReaders are required to recognize the
     * http://xml.org/sax/features/namespaces and the
     * http://xml.org/sax/features/namespace-prefixes feature names.</p>
     *
     * <p>Typical usage is something like this:</p>
     *
     * <pre>
     * XMLReader r = new MySAXDriver();
     *
     *                         // try to activate validation
     * try {
     *   r.setFeature("http://xml.org/sax/features/validation", true);
     * } catch (SAXException e) {
     *   System.err.println("Cannot activate validation."); 
     * }
     *
     *                         // register event handlers
     * r.setContentHandler(new MyContentHandler());
     * r.setErrorHandler(new MyErrorHandler());
     *
     *                         // parse the first document
     * try {
     *   r.parse("http://www.foo.com/mydoc.xml");
     * } catch (IOException e) {
     *   System.err.println("I/O exception reading XML document");
     * } catch (SAXException e) {
     *   System.err.println("XML exception reading document.");
     * }
     * </pre>
     *
     * <p>Implementors are free (and encouraged) to invent their own features,
     * using names built on their own URIs.</p>
     *
     * @param name The feature name, which is a fully-qualified URI.
     * @return The current value of the feature (true or false).
     * @exception org.xml.sax.SAXNotRecognizedException If the feature
     *            value can't be assigned or retrieved.
     * @exception org.xml.sax.SAXNotSupportedException When the
     *            XMLReader recognizes the feature name but 
     *            cannot determine its value at this time.
     * @see #setFeature
     */
    public boolean getFeature (String name)
        throws SAXNotRecognizedException, SAXNotSupportedException
    {
        boolean ret;

        if (name.equals ("http://xml.org/sax/features/namespaces"))
            ret = mNameSpaces;
        else if (name.equals ("http://xml.org/sax/features/namespace-prefixes"))
            ret = mNameSpacePrefixes;
        else
            throw new SAXNotSupportedException (name + " not yet understood");

        return (ret);
    }


    /**
     * Set the value of a feature flag.
     *
     * <p>The feature name is any fully-qualified URI.  It is
     * possible for an XMLReader to expose a feature value but
     * to be unable to change the current value.
     * Some feature values may be immutable or mutable only 
     * in specific contexts, such as before, during, or after 
     * a parse.</p>
     *
     * <p>All XMLReaders are required to support setting
     * http://xml.org/sax/features/namespaces to true and
     * http://xml.org/sax/features/namespace-prefixes to false.</p>
     *
     * @param name The feature name, which is a fully-qualified URI.
     * @param value The requested value of the feature (true or false).
     * @exception org.xml.sax.SAXNotRecognizedException If the feature
     *            value can't be assigned or retrieved.
     * @exception org.xml.sax.SAXNotSupportedException When the
     *            XMLReader recognizes the feature name but 
     *            cannot set the requested value.
     * @see #getFeature
     */
    public void setFeature (String name, boolean value)
	throws SAXNotRecognizedException, SAXNotSupportedException
    {
        if (name.equals ("http://xml.org/sax/features/namespaces"))
            mNameSpaces = value;
        else if (name.equals ("http://xml.org/sax/features/namespace-prefixes"))
            mNameSpacePrefixes = value;
        else
            throw new SAXNotSupportedException (name + " not yet understood");
    }


    /**
     * Look up the value of a property.
     *
     * <p>The property name is any fully-qualified URI.  It is
     * possible for an XMLReader to recognize a property name but
     * temporarily be unable to return its value.
     * Some property values may be available only in specific
     * contexts, such as before, during, or after a parse.</p>
     *
     * <p>XMLReaders are not required to recognize any specific
     * property names, though an initial core set is documented for
     * SAX2.</p>
     *
     * <p>Implementors are free (and encouraged) to invent their own properties,
     * using names built on their own URIs.</p>
     *
     * @param name The property name, which is a fully-qualified URI.
     * @return The current value of the property.
     * @exception org.xml.sax.SAXNotRecognizedException If the property
     *            value can't be assigned or retrieved.
     * @exception org.xml.sax.SAXNotSupportedException When the
     *            XMLReader recognizes the property name but 
     *            cannot determine its value at this time.
     * @see #setProperty
     */
    public Object getProperty (String name)
	throws SAXNotRecognizedException, SAXNotSupportedException
    {
        throw new SAXNotSupportedException (name + " not yet understood");
    }


    /**
     * Set the value of a property.
     *
     * <p>The property name is any fully-qualified URI.  It is
     * possible for an XMLReader to recognize a property name but
     * to be unable to change the current value.
     * Some property values may be immutable or mutable only 
     * in specific contexts, such as before, during, or after 
     * a parse.</p>
     *
     * <p>XMLReaders are not required to recognize setting
     * any specific property names, though a core set is defined by 
     * SAX2.</p>
     *
     * <p>This method is also the standard mechanism for setting
     * extended handlers.</p>
     *
     * @param name The property name, which is a fully-qualified URI.
     * @param value The requested value for the property.
     * @exception org.xml.sax.SAXNotRecognizedException If the property
     *            value can't be assigned or retrieved.
     * @exception org.xml.sax.SAXNotSupportedException When the
     *            XMLReader recognizes the property name but 
     *            cannot set the requested value.
     */
    public void setProperty (String name, Object value)
	throws SAXNotRecognizedException, SAXNotSupportedException
    {
        throw new SAXNotSupportedException (name + " not yet understood");
    }

    ////////////////////////////////////////////////////////////////////
    // Event handlers.
    ////////////////////////////////////////////////////////////////////


    /**
     * Allow an application to register an entity resolver.
     *
     * <p>If the application does not register an entity resolver,
     * the XMLReader will perform its own default resolution.</p>
     *
     * <p>Applications may register a new or different resolver in the
     * middle of a parse, and the SAX parser must begin using the new
     * resolver immediately.</p>
     *
     * @param resolver The entity resolver.
     * @see #getEntityResolver
     */
    public void setEntityResolver (EntityResolver resolver)
    {
        mEntityResolver = resolver;
    }


    /**
     * Return the current entity resolver.
     *
     * @return The current entity resolver, or null if none
     *         has been registered.
     * @see #setEntityResolver
     */
    public EntityResolver getEntityResolver ()
    {
        return (mEntityResolver);
    }


    /**
     * Allow an application to register a DTD event handler.
     *
     * <p>If the application does not register a DTD handler, all DTD
     * events reported by the SAX parser will be silently ignored.</p>
     *
     * <p>Applications may register a new or different handler in the
     * middle of a parse, and the SAX parser must begin using the new
     * handler immediately.</p>
     *
     * @param handler The DTD handler.
     * @see #getDTDHandler
     */
    public void setDTDHandler (DTDHandler handler)
    {
        mDTDHandler = handler;
    }


    /**
     * Return the current DTD handler.
     *
     * @return The current DTD handler, or null if none
     *         has been registered.
     * @see #setDTDHandler
     */
    public DTDHandler getDTDHandler ()
    {
        return (mDTDHandler);
    }


    /**
     * Allow an application to register a content event handler.
     *
     * <p>If the application does not register a content handler, all
     * content events reported by the SAX parser will be silently
     * ignored.</p>
     *
     * <p>Applications may register a new or different handler in the
     * middle of a parse, and the SAX parser must begin using the new
     * handler immediately.</p>
     *
     * @param handler The content handler.
     * @see #getContentHandler
     */
    public void setContentHandler (ContentHandler handler)
    {
        mContentHandler = handler;
    }


    /**
     * Return the current content handler.
     *
     * @return The current content handler, or null if none
     *         has been registered.
     * @see #setContentHandler
     */
    public ContentHandler getContentHandler ()
    {
        return (mContentHandler);
    }


    /**
     * Allow an application to register an error event handler.
     *
     * <p>If the application does not register an error handler, all
     * error events reported by the SAX parser will be silently
     * ignored; however, normal processing may not continue.  It is
     * highly recommended that all SAX applications implement an
     * error handler to avoid unexpected bugs.</p>
     *
     * <p>Applications may register a new or different handler in the
     * middle of a parse, and the SAX parser must begin using the new
     * handler immediately.</p>
     *
     * @param handler The error handler.
     * @see #getErrorHandler
     */
    public void setErrorHandler (ErrorHandler handler)
    {
        mErrorHandler = handler;
    }


    /**
     * Return the current error handler.
     *
     * @return The current error handler, or null if none
     *         has been registered.
     * @see #setErrorHandler
     */
    public ErrorHandler getErrorHandler ()
    {
        return (mErrorHandler);
    }


    ////////////////////////////////////////////////////////////////////
    // Parsing.
    ////////////////////////////////////////////////////////////////////

    /**
     * Parse an XML document.
     *
     * <p>The application can use this method to instruct the XML
     * reader to begin parsing an XML document from any valid input
     * source (a character stream, a byte stream, or a URI).</p>
     *
     * <p>Applications may not invoke this method while a parse is in
     * progress (they should create a new XMLReader instead for each
     * nested XML document).  Once a parse is complete, an
     * application may reuse the same XMLReader object, possibly with a
     * different input source.
     * Configuration of the XMLReader object (such as handler bindings and
     * values established for feature flags and properties) is unchanged
     * by completion of a parse, unless the definition of that aspect of
     * the configuration explicitly specifies other behavior.
     * (For example, feature flags or properties exposing
     * characteristics of the document being parsed.)
     * </p>
     *
     * <p>During the parse, the XMLReader will provide information
     * about the XML document through the registered event
     * handlers.</p>
     *
     * <p>This method is synchronous: it will not return until parsing
     * has ended.  If a client application wants to terminate 
     * parsing early, it should throw an exception.</p>
     *
     * @param input The input source for the top-level of the
     *        XML document.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @exception java.io.IOException An IO exception from the parser,
     *            possibly from a byte stream or character stream
     *            supplied by the application.
     * @see org.xml.sax.InputSource
     * @see #parse(java.lang.String)
     * @see #setEntityResolver
     * @see #setDTDHandler
     * @see #setContentHandler
     * @see #setErrorHandler 
     */
    public void parse (InputSource input)
	throws IOException, SAXException
    {
        Locator locator;
        ParserFeedback feedback;

        if (null != mContentHandler)
            try
            {
                mParser = new Parser (
                    new Lexer (
                        new Page (
                            input.getByteStream (),
                            input.getEncoding ())));
                locator = new Locator (mParser);
                if (null != mErrorHandler)
                    feedback = new Feedback (mErrorHandler, locator);
                else
                    feedback = new DefaultParserFeedback (0);
                mParser.setFeedback (feedback);
                mContentHandler.setDocumentLocator (locator);
                try
                {
                    mContentHandler.startDocument ();
                    for (NodeIterator iterator = mParser.elements ();
                                        iterator.hasMoreNodes ();
                        doSAX (iterator.nextNode ()));
                    mContentHandler.endDocument ();
                }
                catch (SAXException se)
                {
                    if (null != mErrorHandler)
                        mErrorHandler.fatalError (new SAXParseException (
                            "contentHandler threw me", locator, se));
                }
            }
            catch (ParserException pe)
            {
                if (null != mErrorHandler)
                    mErrorHandler.fatalError (new SAXParseException (
                        pe.getMessage (), "", "", 0, 0));
            }
    }

    /**
     * Parse an XML document from a system identifier (URI).
     *
     * <p>This method is a shortcut for the common case of reading a
     * document from a system identifier.  It is the exact
     * equivalent of the following:</p>
     *
     * <pre>
     * parse(new InputSource(systemId));
     * </pre>
     *
     * <p>If the system identifier is a URL, it must be fully resolved
     * by the application before it is passed to the parser.</p>
     *
     * @param systemId The system identifier (URI).
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @exception java.io.IOException An IO exception from the parser,
     *            possibly from a byte stream or character stream
     *            supplied by the application.
     * @see #parse(org.xml.sax.InputSource)
     */
    public void parse (String systemId)
	throws IOException, SAXException
    {
        Locator locator;
        ParserFeedback feedback;

        if (null != mContentHandler)
            try
            {
                mParser = new Parser (systemId);
                locator = new Locator (mParser);
                if (null != mErrorHandler)
                    feedback = new Feedback (mErrorHandler, locator);
                else
                    feedback = new DefaultParserFeedback (DefaultParserFeedback.QUIET);
                mParser.setFeedback (feedback);

                // OK, try a simplistic parse
                mContentHandler.setDocumentLocator (locator);
                try
                {
                    mContentHandler.startDocument ();
                    for (NodeIterator iterator = mParser.elements (); iterator.hasMoreNodes (); )
                        doSAX (iterator.nextNode ());
                    mContentHandler.endDocument ();
                }
                catch (SAXException se)
                {
                    if (null != mErrorHandler)
                        mErrorHandler.fatalError (
                            new SAXParseException ("contentHandler threw me", locator, se));
                }
            }
            catch (ParserException pe)
            {
                if (null != mErrorHandler)
                    mErrorHandler.fatalError (
                        new SAXParseException (pe.getMessage (), "", systemId, 0, 0));

            }
    }

    /**
     * Process nodes recursively on the DocumentHandler.
     * Calls methods on the handler based on the type and whether it's an end tag.
     * Processes composite tags recursively.
     * Does rudimentary namespace processing according to the state of {@link #mNameSpaces}
     * and {@link #mNameSpacePrefixes}. 
     * @param node The htmlparser node to traverse.
     * @exception ParserException If a parse error occurs.
     * @exception SAXException If a SAX error occurs.
     */
    protected void doSAX (Node node)
        throws
            ParserException,
            SAXException
    {
        Tag tag;
        Tag end;

        if (node instanceof Remark)
        {
            String text = mParser.getLexer ().getPage ().getText (node.getStartPosition (), node.getEndPosition ());
            mContentHandler.ignorableWhitespace (text.toCharArray (), 0, text.length ());
        }
        else if (node instanceof Text)
        {
            String text = mParser.getLexer ().getPage ().getText (node.getStartPosition (), node.getEndPosition ());
            mContentHandler.characters (text.toCharArray (), 0, text.length ());
        }
        else if (node instanceof Tag)
        {
            tag = (Tag)node;
            if (mNameSpaces)
                mSupport.processName (tag.getTagName (), mParts, false);
            else
            {
                mParts[0] = "";
                mParts[1] = "";
            }
            if (mNameSpacePrefixes)
                mParts[2] = tag.getTagName ();
            else if (mNameSpaces)
                mParts[2] = "";
            else
                mParts[2] = tag.getTagName ();

            mContentHandler.startElement (
                mParts[0], // uri
                mParts[1], // local
                mParts[2], // raw
                new Attributes (tag, mSupport, mParts));
            NodeList children = tag.getChildren ();
            if (null != children)
                for (int i = 0; i < children.size (); i++)
                    doSAX (children.elementAt (i));
            end = tag.getEndTag ();
            if (null != end)
            {
                if (mNameSpaces)
                    mSupport.processName (end.getTagName (), mParts, false);
                else
                {
                    mParts[0] = "";
                    mParts[1] = "";
                }
                if (mNameSpacePrefixes)
                    mParts[2] = end.getTagName ();
                else if (mNameSpaces)
                    mParts[2] = "";
                else
                    mParts[2] = end.getTagName ();
                mContentHandler.endElement (
                    mParts[0], // uri
                    mParts[1], // local
                    mParts[2]); // raw
            }
        }
    }
}

