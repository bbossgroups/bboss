package bboss.org.apache.velocity.runtime.parser.node;

import bboss.org.apache.velocity.context.InternalContextAdapter;
import bboss.org.apache.velocity.exception.MathException;
import bboss.org.apache.velocity.exception.MethodInvocationException;
import bboss.org.apache.velocity.exception.TemplateInitException;
import bboss.org.apache.velocity.runtime.RuntimeConstants;
import bboss.org.apache.velocity.runtime.parser.Parser;
import bboss.org.apache.velocity.util.DuckType;

public class ASTNegateNode extends SimpleNode
{
    protected boolean strictMode = false;

    public ASTNegateNode(int i)
    {
        super(i);
    }

    public ASTNegateNode(Parser p, int i)
    {
        super(p, i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object init(InternalContextAdapter context, Object data) throws TemplateInitException
    {
        super.init(context, data);
        /* save a literal image now (needed in case of error) */
        strictMode = rsvc.getBoolean(RuntimeConstants.STRICT_MATH, false);
        cleanupParserAndTokens();
        return data;
    }

    /**
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#jjtAccept(bboss.org.apache.velocity.runtime.parser.node.StandardParserVisitor, java.lang.Object)
     */
    @Override
    public Object jjtAccept(StandardParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

    /**
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#evaluate(bboss.org.apache.velocity.context.InternalContextAdapter)
     */
    @Override
    public boolean evaluate(InternalContextAdapter context)
            throws MethodInvocationException
    {
        return jjtGetChild(0).evaluate(context);
    }

    /**
     * @see bboss.org.apache.velocity.runtime.parser.node.SimpleNode#value(bboss.org.apache.velocity.context.InternalContextAdapter)
     */
    @Override
    public Object value(InternalContextAdapter context)
            throws MethodInvocationException
    {
        Object value = jjtGetChild(0).value( context );
        try
        {
            value = DuckType.asNumber(value);
        }
        catch (NumberFormatException nfe) {}
        if (!(value instanceof Number))
        {
            String msg = "Argument of unary negate (" +
                    jjtGetChild(0).literal() +
                    ") " +
                    (value == null ? "has a null value." : "is not a Number.");
            if (strictMode)
            {
                log.error(msg);
                throw new MathException(msg, rsvc.getLogContext().getStackTrace());
            }
            else
            {
                log.debug(msg);
                return null;
            }
        }
        return MathUtils.negate((Number) value);
    }
    @Override
    public String literal()
    {
        return "-" + jjtGetChild(0).literal();
    }
}
