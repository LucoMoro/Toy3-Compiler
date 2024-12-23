package esercitazione4.ast.ArithOp;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.visitor.Visitor;

public class AddNode implements ExprOpNode, ArithOpNode {
    public AddNode(Object left, Object right){
        this.operation = "PLUS";
        this.left = (ExprOpNode) left;
        this.right = (ExprOpNode) right;
    }

    public Object getLeft(){
        return this.left;
    }

    public Object getRight(){
        return this.right;
    }

    public String getOperation() {
        return operation;
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
