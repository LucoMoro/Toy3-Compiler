package esercitazione4.ast.ArithOp;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.visitor.Visitor;

public class MulNode implements ExprOpNode, ArithOpNode {

    public MulNode(Object left, Object right){
        this.operation = "TIMES";
        this.left = (ExprOpNode) left;
        this.right = (ExprOpNode) right;
    }

    public String getOperation() {
        return operation;
    }

    public Object getLeft(){
        return this.left;
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
