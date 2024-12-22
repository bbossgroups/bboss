package bboss.org.apache.velocity.app.event.implement;

import org.apache.commons.lang3.StringEscapeUtils;

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

/**
 * <p>Escape all HTML entities.</p>
 * <p>Warning: escaping references this way, without knowing if they land inside plain text, inside an attribute value or elsewhere, is not usable in production.</p>
 *
 * @see <a href="http://commons.apache.org/proper/commons-lang/javadocs/api-release/org/apache/commons/lang3/StringEscapeUtils.html#escapeHtml4%28java.lang.String%29">StringEscapeUtils</a>
 * @author wglass
 * @since 1.5
 * @deprecated impractical use
 */
@Deprecated
public class EscapeHtmlReference extends EscapeReference
{

    /**
     * Escape all HTML entities.
     *
     * @param text
     * @return An escaped String.
     * @see <a href="http://commons.apache.org/proper/commons-lang/javadocs/api-release/org/apache/commons/lang3/StringEscapeUtils.html#escapeHtml4%28java.lang.String%29">StringEscapeUtils</a>
     */
    @Override
    protected String escape(Object text)
    {
        return StringEscapeUtils.escapeHtml4(text.toString());
    }

    /**
     * @return attribute "eventhandler.escape.html.match"
     */
    @Override
    protected String getMatchAttribute()
    {
        return "eventhandler.escape.html.match";
    }

}
