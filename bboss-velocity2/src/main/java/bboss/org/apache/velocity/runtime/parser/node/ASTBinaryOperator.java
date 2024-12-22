package bboss.org.apache.velocity.runtime.parser.node;

import bboss.org.apache.velocity.context.InternalContextAdapter;
import bboss.org.apache.velocity.exception.TemplateInitException;
import bboss.org.apache.velocity.runtime.parser.Parser;

public abstract class ASTBinaryOperator extends SimpleNode
{
    public ASTBinaryOperator(int id)
    {
        super(id);
    }

    public ASTBinaryOperator(Parser p, int id)
    {
        super(p, id);
    }

    /**
     * @throws TemplateInitException
     * @see bboss.org.apache.velocity.runtime.parser.node.Node#init(org.apache.velocity.context.InternalContextAdapter, java.lang.Object)
     */
    @Override
    public Object init(InternalContextAdapter context, Object data) throws TemplateInitException
    {
        Object obj = super.init(context, data);
        cleanupParserAndTokens(); // drop reference to Parser and all JavaCC Tokens
        return obj;
    }

    /**
     * get the string representing the mathematical operator
     * @return operator string
     */
    public abstract String getLiteralOperator();

    @Override
    public String literal()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(jjtGetChild(0).literal());
        builder.append(' ');
        builder.append(getLiteralOperator());
        builder.append(' ');
        return builder.toString();
    }
}
