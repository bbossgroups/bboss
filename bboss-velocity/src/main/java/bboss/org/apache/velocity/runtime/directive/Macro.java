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
import bboss.org.apache.velocity.exception.TemplateInitException;
import bboss.org.apache.velocity.runtime.RuntimeServices;
import bboss.org.apache.velocity.runtime.parser.ParseException;
import bboss.org.apache.velocity.runtime.parser.Token;
import bboss.org.apache.velocity.runtime.parser.node.Node;
import bboss.org.apache.velocity.runtime.parser.node.StandardParserTreeConstants;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 *  Macro implements the macro definition directive of VTL.
 *
 *  example:
 *
 *  #macro( isnull $i )
 *     #if( $i )
 *         $i
 *      #end
 *  #end
 *
 *  This object is used at parse time to mainly process and register the
 *  macro.  It is used inline in the parser when processing a directive.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author <a href="hps@intermeta.de">Henning P. Schmiedehausen</a>
 * @version $Id$
 */
public class Macro extends Directive
{
    private static  boolean debugMode = false;

    /**
     * Return name of this directive.
     * @return The name of this directive.
     */
    @Override
    public String getName()
    {
        return "macro";
    }

    /**
     * Return type of this directive.
     * @return The type of this directive.
     */
    @Override
    public int getType()
    {
        return BLOCK;
    }

    /**
     * Since this class does no processing of content,
     * there is never a need for an internal scope.
     */
    @Override
    public boolean isScopeProvided()
    {
        return false;
    }

    /**
     *   render() doesn't do anything in the final output rendering.
     *   There is no output from a #macro() directive.
     * @param context
     * @param writer
     * @param node
     * @return True if the directive rendered successfully.
     * @throws IOException
     */
    @Override
    public boolean render(InternalContextAdapter context,
                          Writer writer, Node node)
        throws IOException
    {
        /*
         *  do nothing: We never render.  The VelocimacroProxy object does that
         */

        return true;
    }

    /**
     * @see bboss.org.apache.velocity.runtime.directive.Directive#init(bboss.org.apache.velocity.runtime.RuntimeServices, bboss.org.apache.velocity.context.InternalContextAdapter, bboss.org.apache.velocity.runtime.parser.node.Node)
     */
    @Override
    public void init(RuntimeServices rs, InternalContextAdapter context,
                     Node node)
       throws TemplateInitException
    {
        super.init(rs, context, node);


        // Add this macro to the VelocimacroManager now that it has been initialized.
        List<MacroArg> macroArgs = getArgArray(node, rsvc);
        int numArgs = node.jjtGetNumChildren();
        rsvc.addVelocimacro(macroArgs.get(0).name, node.jjtGetChild(numArgs - 1),
            macroArgs, node.getTemplate());
    }

    /**
     * Check the argument types of a macro call, called by the parser to do validation
     */
    @Override
    public void checkArgs(ArrayList<Integer> argtypes, Token t, String templateName)
        throws ParseException
    {
        if (argtypes.size() < 1)
        {
            throw new MacroParseException("A macro definition requires at least a macro name"
                , templateName, t);
        }

        /*
         *  lets make sure that the first arg is an ASTWord
         */
        if(argtypes.get(0) != StandardParserTreeConstants.JJTWORD)
        {
            throw new MacroParseException("Macro argument 1"
                    + " must be a token without surrounding \' or \""
                    , templateName, t);
        }


        // We use this to flag if the default arguments are out of order. such as
        // #macro($a $b=1 $c).  We enforce that all default parameters must be
        // specified consecutively, and at the end of the argument list.
        boolean consecutive = false;

        // All arguments other then the first must be either a reference
        // or a directiveassign followed by a reference in the case a default
        // value is specified.
        for (int argPos = 1; argPos < argtypes.size(); argPos++)
        {
            if (argtypes.get(argPos) == StandardParserTreeConstants.JJTDIRECTIVEASSIGN)
            {
               // Absorb next argument type since parser enforces that these are in
               // pairs, and we don't need to check the type of the second
               // arg because it is done by the parser.
               argPos++;
               consecutive = true;
            }
            else if (argtypes.get(argPos) != StandardParserTreeConstants.JJTREFERENCE)
            {
                throw new MacroParseException("Macro argument " + (argPos + 1)
                  + " must be a reference", templateName, t);
            }
            else if (consecutive)
            {
                // We have already found a default parameter e.g.; $x = 2, but
                // the next parameter was not a reference.
                throw new MacroParseException("Macro non-default argument follows a default argument at "
                  , templateName, t);
            }
        }
    }

    /**
     * Creates an array containing the literal text from the macro
     * argument(s) (including the macro's name as the first arg).
     *
     * @param node The parse node from which to grok the argument
     * list.  It's expected to include the block node tree (for the
     * macro body).
     * @param rsvc For debugging purposes only.
     * @return array of arguments
     */
    private static List<MacroArg> getArgArray(Node node, RuntimeServices rsvc)
    {
        /*
         * Get the number of arguments for the macro, excluding the
         * last child node which is the block tree containing the
         * macro body.
         */
        int numArgs = node.jjtGetNumChildren();
        numArgs--;  // avoid the block tree...

        ArrayList<MacroArg> macroArgs = new ArrayList<>();

        for (int i = 0; i < numArgs; i++)
        {
            Node curnode = node.jjtGetChild(i);
            MacroArg macroArg = new MacroArg();
            if (curnode.getType() == StandardParserTreeConstants.JJTDIRECTIVEASSIGN)
            {
                // This is an argument with a default value
            	macroArg.name = curnode.getFirstTokenImage();

                // Inforced by the parser there will be an argument here.
                i++;
                curnode = node.jjtGetChild(i);
                macroArg.defaultVal = curnode;
            }
            else
            {
                // An argument without a default value
               	macroArg.name = curnode.getFirstTokenImage();
            }

            // trim off the leading $ for the args after the macro name.
            // saves everyone else from having to do it
            if (i > 0 && macroArg.name.startsWith(String.valueOf(rsvc.getParserConfiguration().getDollarChar())))
            {
                macroArg.name = macroArg.name.substring(1);
            }

            macroArgs.add(macroArg);
        }

        if (debugMode)
        {
            StringBuilder msg = new StringBuilder("Macro.getArgArray(): nbrArgs=");
            msg.append(numArgs).append(": ");
            macroToString(msg, macroArgs, rsvc);
            rsvc.getLog("macro").debug(msg.toString());
        }

        return macroArgs;
    }

    /**
     * MacroArgs holds the information for a single argument in a
     * macro definition.  The arguments for a macro are passed around as a
     * list of these objects.
     */
    public static class MacroArg
    {
       /**
        * Name of the argument with '$' stripped off
        */
        public String name = null;

        /**
         * If the argument was given a default value, then this contains
         * the base of the AST tree of the value. Otherwise it is null.
         */
        public Node defaultVal = null;
    }

    /**
     * For debugging purposes.  Formats the arguments from
     * <code>argArray</code> and appends them to <code>buf</code>.
     *
     * @param buf A StringBuilder. If null, a new StringBuilder is allocated.
     * @param macroArgs  Array of macro arguments, containing the
     *        #macro() arguments and default values.  the 0th is the name.
     * @return A StringBuilder containing the formatted arguments. If a StringBuilder
     *         has passed in as buf, this method returns it.
     * @since 1.5
     */
    public static StringBuilder macroToString(final StringBuilder buf, List<MacroArg> macroArgs, RuntimeServices rsvc)
    {
        StringBuilder ret = (buf == null) ? new StringBuilder() : buf;

        ret.append(rsvc.getParserConfiguration().getHashChar()).append(macroArgs.get(0).name).append("( ");
        for (MacroArg marg : macroArgs)
        {
            ret.append(rsvc.getParserConfiguration().getDollarChar()).append(marg.name);
            if (marg.defaultVal != null)
            {
              ret.append("=").append(marg.defaultVal);
            }
            ret.append(' ');
        }
        ret.append(" )");
        return ret;
    }

}
