package esercitazione4.ast.RelOp;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.visitor.Visitor;

public class EQNode implements ExprOpNode, RelOpNode {

    public EQNode(Object left, Object right){
        this.operation = "EQ";
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

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private String operation;
    private ExprOpNode left, right;
}
