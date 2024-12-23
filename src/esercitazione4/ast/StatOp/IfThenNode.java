package esercitazione4.ast.StatOp;

import esercitazione4.ast.BodyNode;
import esercitazione4.ast.ExprOpNode;
import esercitazione4.visitor.Visitor;

public class IfThenNode implements StatOpNode {

    public IfThenNode(Object expr, Object body){
        this.left = (ExprOpNode) expr;
        this.right = (BodyNode) body;
    }

    public ExprOpNode getLeft() {
        return left;
    }

    public BodyNode getRight() {
        return right;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + "{"
                + "expr: '" + left + "'; "
                + "if-then body: '" + right + "'"
                + "}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private ExprOpNode left;
    private BodyNode right;
}
