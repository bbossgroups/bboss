package bboss.org.apache.velocity.runtime.parser.node;

import bboss.org.apache.velocity.context.InternalContextAdapter;
import bboss.org.apache.velocity.exception.TemplateInitException;
import bboss.org.apache.velocity.runtime.parser.Parser;

public abstract class ASTLogicalOperator extends ASTBinaryOperator
{
    public ASTLogicalOperator(int id)
    {
        super(id);
    }

    public ASTLogicalOperator(Parser p, int id)
    {
        super(p, id);
    }

    /**
     * @throws TemplateInitException
     * @see bboss.org.apache.velocity.runtime.parser.node.Node#init(bboss.org.apache.velocity.context.InternalContextAdapter, java.lang.Object)
     */
    @Override
    public Object init(InternalContextAdapter context, Object data) throws TemplateInitException
    {
        Object obj = super.init(context, data);
        cleanupParserAndTokens(); // drop reference to Parser and all JavaCC Tokens
        return obj;
    }
}
