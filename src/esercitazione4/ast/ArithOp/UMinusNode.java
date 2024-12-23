package esercitazione4.ast.ArithOp;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.visitor.Visitor;

public class UMinusNode implements ExprOpNode, ArithOpNode {

    public UMinusNode(Object left){
        this.operation = "UMINUS";
        this.left = (ExprOpNode) left;
    }

    public String getOperation() {
        return operation;
    }

    public ExprOpNode getLeft() {
        return left;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName()+"{"
                + "operation: '" + operation +"';"
                + " child: '" + left +  "'}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private String operation;
    private ExprOpNode left;
}
