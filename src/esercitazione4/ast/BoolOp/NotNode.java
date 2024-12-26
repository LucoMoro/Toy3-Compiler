package esercitazione4.ast.BoolOp;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.ast.Type;
import esercitazione4.visitor.Visitor;
import esercitazione4.visitor.symbolTable.SymbolTable;

public class NotNode implements ExprOpNode, BoolOpNode {

    public NotNode(Object left){
        this.operation = "NOT";
        this.left = (ExprOpNode) left;
    }

    public String getOperation() {
        return operation;
    }

    public ExprOpNode getLeft() {
        return left;
    }

    public SymbolTable getTable() {
        return table;
    }

    public void setTable(SymbolTable table) {
        this.table = table;
    }

    @Override
    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    @Override
    public Type getReturnType() {
        return returnType;
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

    private SymbolTable table;
    private Type returnType;
}
