package esercitazione4.ast.StatOp;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.visitor.Visitor;

public class ReturnOpNode implements StatOpNode {

    public ReturnOpNode(Object expression){
        this.expression = (ExprOpNode) expression;
    }

    public ExprOpNode getExpression() {
        return expression;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + "{"
                + "expression: '" + expression + "'"
                + "}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private ExprOpNode expression;
}
