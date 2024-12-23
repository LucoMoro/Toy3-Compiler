package esercitazione4.ast.RelOp;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.visitor.Visitor;

public class LTNode implements ExprOpNode, RelOpNode {

    public LTNode(Object left, Object right){
        this.operation = "LT";
        this.left = (ExprOpNode) left;
        this.right = (ExprOpNode) right;
    }

    public String getOperation() {
        return operation;
    }

    public ExprOpNode getLeft() {
        return left;
    }

    public ExprOpNode getRight() {
        return right;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName()+"{"
                + "left: '" + left + "';"
                + " operation: '" + operation + "';"
                + " right: '" + right + "'}";
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }

    private String operation;
    private ExprOpNode left, right;
}
