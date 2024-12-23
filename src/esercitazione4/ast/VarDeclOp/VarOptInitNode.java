package esercitazione4.ast.VarDeclOp;

import esercitazione4.ast.DeclOpNode;
import esercitazione4.ast.ExprOpNode;
import esercitazione4.ast.IdNode;
import esercitazione4.ast.Node;
import esercitazione4.visitor.Visitor;

public class VarOptInitNode implements DeclOpNode {

    public VarOptInitNode(Object identifier){
        this.identifier = (IdNode) identifier;
        this.expression = null;
    }

    public VarOptInitNode(Object identifier, Object expression){
        this.identifier = (IdNode) identifier;
        this.expression = (ExprOpNode) expression;
    }

    public IdNode getIdentifier() {
        return identifier;
    }

    public ExprOpNode getExpression() {
        return expression;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + "{"
                + "identifier: '" + identifier + "'; "
                + "expression: '" + expression + "'"
                + "}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private IdNode identifier;
    private ExprOpNode expression;
}
