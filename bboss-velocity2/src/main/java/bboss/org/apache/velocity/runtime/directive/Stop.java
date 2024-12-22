package bboss.org.apache.velocity.runtime.directive;

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

import bboss.org.apache.velocity.context.InternalContextAdapter;
import bboss.org.apache.velocity.runtime.RuntimeServices;
import bboss.org.apache.velocity.runtime.parser.ParseException;
import bboss.org.apache.velocity.runtime.parser.Token;
import bboss.org.apache.velocity.runtime.parser.node.Node;

import java.io.Writer;
import java.util.ArrayList;

/**
 * This class implements the #stop directive which allows
 * a user to stop the merging and rendering process. The #stop directive
 * will accept a single message argument with info about the reason for
 * stopping.
 */
public class Stop extends Directive
{
    private static final StopCommand STOP_ALL = new StopCommand("StopCommand to exit merging") {
        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    };

    private boolean hasMessage = false;

    /**
     * Return name of this directive.
     * @return The name of this directive.
     */
    @Override
    public String getName()
    {
        return "stop";
    }

    /**
     * Return type of this directive.
     * @return The type of this directive.
     */
    @Override
    public int getType()
    {
        return LINE;
    }

    /**
     * Since there is no processing of content,
     * there is never a need for an internal scope.
     */
    @Override
    public boolean isScopeProvided()
    {
        return false;
    }

    @Override
    public void init(RuntimeServices rs, InternalContextAdapter context, Node node)
    {
        super.init(rs, context, node);

        hasMessage = (node.jjtGetNumChildren() == 1);
    }

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node)
    {
        if (!hasMessage)
        {
            throw STOP_ALL;
        }

        Object argument = node.jjtGetChild(0).value(context);

        // stop all and use specified message
        throw new StopCommand(String.valueOf(argument));
    }

    /**
     * Called by the parser to check the argument types
     */
    @Override
    public void checkArgs(ArrayList<Integer> argtypes, Token t, String templateName)
      throws ParseException
    {
        int kids = argtypes.size();
        if (kids > 1)
        {
            throw new MacroParseException("The #stop directive only accepts a single message parameter",
               templateName, t);
        }
    }

}
