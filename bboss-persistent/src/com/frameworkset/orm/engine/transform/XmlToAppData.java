/*
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
package com.frameworkset.orm.engine.transform;

/*
 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.Stack;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.frameworkset.orm.engine.EngineException;
import com.frameworkset.orm.engine.model.Column;
import com.frameworkset.orm.engine.model.Database;
import com.frameworkset.orm.engine.model.Domain;
import com.frameworkset.orm.engine.model.ForeignKey;
import com.frameworkset.orm.engine.model.Index;
import com.frameworkset.orm.engine.model.Table;
import com.frameworkset.orm.engine.model.Unique;

/**
 * A Class that is used to parse an input xml schema file and creates a Database
 * java structure.
 *
 * @author <a href="mailto:leon@opticode.co.za">Leon Messerschmidt</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:mpoeschl@marmot.at">Martin Poeschl</a>
 * @author <a href="mailto:dlr@collab.net">Daniel Rall</a>
 * @version $Id: XmlToAppData.java,v 1.14 2004/02/22 06:27:19 jmcnally Exp $
 */
public class XmlToAppData extends DefaultHandler  implements Serializable 
{
    /** Logging class from commons.logging */
    private static Logger log = Logger.getLogger(XmlToAppData.class);

    private Database database;
    private Table currTable;
    private Column currColumn;
    private ForeignKey currFK;
    private Index currIndex;
    private Unique currUnique;

    private boolean firstPass;
    private boolean isExternalSchema;
    private String currentPackage;
    private String currentXmlFile;
    private String defaultPackage;

    private static SAXParserFactory saxFactory;

    /** remember all files we have already parsed to detect looping. */
    private Vector alreadyReadFiles;

    /** this is the stack to store parsing data */
    private Stack parsingStack = new Stack();

    static
    {
        saxFactory = SAXParserFactory.newInstance();
        saxFactory.setValidating(true);
    }

    /**
     * Creates a new instance for the specified database type.
     *
     * @param databaseType The type of database for the application.
     */
    public XmlToAppData(String databaseType)
    {
        database = new Database(databaseType);
        firstPass = true;
    }

    /**
     * Creates a new instance for the specified database type.
     *
     * @param databaseType The type of database for the application.
     * @param defaultPackage the default java package used for the om
     */
    public XmlToAppData(String databaseType, String defaultPackage)
    {
        database = new Database(databaseType);
        this.defaultPackage = defaultPackage;
        firstPass = true;
    }

    /**
     * Parses a XML input file and returns a newly created and
     * populated Database structure.
     *
     * @param xmlFile The input file to parse.
     * @return Database populated by <code>xmlFile</code>.
     */
    public Database parseFile(String xmlFile)
            throws EngineException
    {
        try
        {
            // in case I am missing something, make it obvious
            if (!firstPass)
            {
                throw new Error("No more double pass");
            }
            // check to see if we alread have parsed the file
            if ((alreadyReadFiles != null)
                    && alreadyReadFiles.contains(xmlFile))
            {
                return database;
            }
            else if (alreadyReadFiles == null)
            {
                alreadyReadFiles = new Vector(3, 1);
            }

            // remember the file to avoid looping
            alreadyReadFiles.add(xmlFile);

            currentXmlFile = xmlFile;

            SAXParser parser = saxFactory.newSAXParser();

            FileReader fr = null;
            try
            {
                fr = new FileReader(xmlFile);
            }
            catch (FileNotFoundException fnfe)
            {
                throw new FileNotFoundException
                    (new File(xmlFile).getAbsolutePath());
            }
            BufferedReader br = new BufferedReader(fr);
            try
            {
                log.info("Parsing file: '"
                        + (new File(xmlFile)).getName() + "'");
                InputSource is = new InputSource(br);
                parser.parse(is, this);
            }
            finally
            {
                br.close();
            }
        }
        catch (Exception e)
        {
            throw new EngineException(e);
        }
        if (!isExternalSchema)
        {
            firstPass = false;
        }
        database.doFinalInitialization();
        return database;
    }

    /**
     * EntityResolver implementation. Called by the XML parser
     *
     * @param publicId The public identifier of the external entity
     * @param systemId The system identifier of the external entity
     * @return an InputSource for the database.dtd file
     * @see DTDResolver#resolveEntity(String, String)
     */
    public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException
    {
        try
        {
            //return new DTDResolver().resolveEntity(publicId, systemId);
            return null;
        }
        catch (Exception e)
        {
            throw new SAXException(e);
        }
    }

    /**
     * Handles opening elements of the xml file.
     *
     * @param uri
     * @param localName The local name (without prefix), or the empty string if
     *         Namespace processing is not being performed.
     * @param rawName The qualified name (with prefix), or the empty string if
     *         qualified names are not available.
     * @param attributes The specified or defaulted attributes
     */
    public void startElement(String uri, String localName, String rawName,
                             Attributes attributes)
            throws SAXException
    {
        try
        {
            if (rawName.equals("database"))
            {
                if (isExternalSchema)
                {
                    currentPackage = attributes.getValue("package");
                    if (currentPackage == null)
                    {
                        currentPackage = defaultPackage;
                    }
                }
                else
                {
                    database.loadFromXML(attributes);
                    if (database.getPackage() == null)
                    {
                        database.setPackage(defaultPackage);
                    }
                }
            }
            else if (rawName.equals("external-schema"))
            {
                String xmlFile = attributes.getValue("filename");
                if (xmlFile.charAt(0) != '/')
                {
                    File f = new File(currentXmlFile);
                    xmlFile = new File(f.getParent(), xmlFile).getPath();
                }

                // put current state onto the stack
                ParseStackElement.pushState(this);

                isExternalSchema = true;

                parseFile(xmlFile);
                // get the last state from the stack
                ParseStackElement.popState(this);
            }
            else if (rawName.equals("domain"))
            {
                Domain domain = new Domain();
                domain.loadFromXML(attributes, database.getPlatform());
                database.addDomain(domain);
            }
            else if (rawName.equals("table"))
            {
                currTable = database.addTable(attributes);
                if (isExternalSchema)
                {
                    currTable.setForReferenceOnly(true);
                    currTable.setPackage(currentPackage);
                }
            }
            else if (rawName.equals("column"))
            {
                currColumn = currTable.addColumn(attributes);
            }
            else if (rawName.equals("inheritance"))
            {
                currColumn.addInheritance(attributes);
            }
            else if (rawName.equals("foreign-key"))
            {
                currFK = currTable.addForeignKey(attributes);
            }
            else if (rawName.equals("reference"))
            {
                currFK.addReference(attributes);
            }
            else if (rawName.equals("index"))
            {
                currIndex = currTable.addIndex(attributes);
            }
            else if (rawName.equals("index-column"))
            {
                currIndex.addColumn(attributes);
            }
            else if (rawName.equals("unique"))
            {
                currUnique = currTable.addUnique(attributes);
            }
            else if (rawName.equals("unique-column"))
            {
                currUnique.addColumn(attributes);
            }
            else if (rawName.equals("id-method-parameter"))
            {
                currTable.addIdMethodParameter(attributes);
            }
        }
        catch (Exception e)
        {
            throw new SAXException(e);
        }
    }

    /**
     * Handles closing elements of the xml file.
     *
     * @param uri
     * @param localName The local name (without prefix), or the empty string if
     *         Namespace processing is not being performed.
     * @param rawName The qualified name (with prefix), or the empty string if
     *         qualified names are not available.
     */
    public void endElement(String uri, String localName, String rawName)
    {
        if (log.isDebugEnabled())
        {
            log.debug("endElement(" + uri + ", " + localName + ", "
                    + rawName + ") called");
        }
    }

    /**
     * When parsing multiple files that use nested <external-schema> tags we
     * need to use a stack to remember some values.
     */
    private static class ParseStackElement
    {
        private boolean isExternalSchema;
        private String currentPackage;
        private String currentXmlFile;
        private boolean firstPass;

        /**
         *
         * @param parser
         */
        public ParseStackElement(XmlToAppData parser)
        {
            // remember current state of parent object
            isExternalSchema = parser.isExternalSchema;
            currentPackage = parser.currentPackage;
            currentXmlFile = parser.currentXmlFile;
            firstPass = parser.firstPass;

            // push the state onto the stack
            parser.parsingStack.push(this);
        }

        /**
         * Removes the top element from the stack and activates the stored state
         *
         * @param parser
         */
        public static void popState(XmlToAppData parser)
        {
            if (!parser.parsingStack.isEmpty())
            {
                ParseStackElement elem = (ParseStackElement)
                        parser.parsingStack.pop();

                // activate stored state
                parser.isExternalSchema = elem.isExternalSchema;
                parser.currentPackage = elem.currentPackage;
                parser.currentXmlFile = elem.currentXmlFile;
                parser.firstPass = elem.firstPass;
            }
        }

        /**
         * Stores the current state on the top of the stack.
         *
         * @param parser
         */
        public static void pushState(XmlToAppData parser)
        {
            new ParseStackElement(parser);
        }
    }
}
