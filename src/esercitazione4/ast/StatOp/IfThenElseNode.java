package esercitazione4.ast.StatOp;

import esercitazione4.ast.BodyNode;
import esercitazione4.ast.ExprOpNode;
import esercitazione4.visitor.Visitor;

public class IfThenElseNode implements StatOpNode {

    public IfThenElseNode(Object expr, Object body1, Object body2){
        this.left = (ExprOpNode) expr;
        this.mid = (BodyNode) body1;
        this.right = (BodyNode) body2;
    }

    public ExprOpNode getLeft() {
        return left;
    }

    public BodyNode getMid() {
        return mid;
    }

    public BodyNode getRight() {
        return right;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + "{"
                + "expr: '" + left + "'; "
                + "if-then body: '" + mid + "'; "
                + "else body: '" + right + "'"
                + "}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private ExprOpNode left;
    private BodyNode mid;
    private BodyNode right;
}
