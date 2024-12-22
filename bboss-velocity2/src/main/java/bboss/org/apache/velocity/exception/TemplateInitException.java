package bboss.org.apache.velocity.exception;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import bboss.org.apache.velocity.runtime.parser.ParseException;

/**
 * Exception generated to indicate parse errors caught during
 * directive initialization (e.g. wrong number of arguments)
 *
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain</a>
 * @version $Id$
 * @since 1.5
 */
public class TemplateInitException extends VelocityException
        implements ExtendedParseException
{
    private final String templateName;
    private final int col;
    private final int line;

    /**
     * Version Id for serializable
     */
    private static final long serialVersionUID = -4985224672336070621L;

    /**
     * @param msg
     * @param templateName
     * @param col
     * @param line
     */
    public TemplateInitException(final String msg,
            final String templateName, final int col, final int line)
    {
        super(msg);
        this.templateName = templateName;
        this.col = col;
        this.line = line;
    }

    /**
     *
     * @param msg
     * @param parseException
     * @param templateName
     * @param col
     * @param line
     */
    public TemplateInitException(final String msg, ParseException parseException,
            final String templateName, final int col, final int line)
    {
        super(msg,parseException);
        this.templateName = templateName;
        this.col = col;
        this.line = line;
    }

    /**
     *
     * @param msg
     * @param parseException
     * @param stacktrace
     * @param templateName
     * @param col
     * @param line
     * @since 2.2
     */
    public TemplateInitException(final String msg, ParseException parseException, String[] stacktrace,
                                 final String templateName, final int col, final int line)
    {
        super(msg,parseException, stacktrace);
        this.templateName = templateName;
        this.col = col;
        this.line = line;
    }

    /**
     * Returns the Template name where this exception occurred.
     * @return the template name
     */
    @Override
    public String getTemplateName()
    {
        return templateName;
    }

    /**
     * Returns the line number where this exception occurred.
     * @return the line number
     */
    @Override
    public int getLineNumber()
    {
        return line;
    }

    /**
     * Returns the column number where this exception occurred.
     * @return the line number
     */
    @Override
    public int getColumnNumber()
    {
        return col;
    }




}
