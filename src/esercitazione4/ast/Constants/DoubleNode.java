package esercitazione4.ast.Constants;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.ast.Type;
import esercitazione4.visitor.Visitor;
import esercitazione4.visitor.symbolTable.SymbolTable;

public class DoubleNode implements ConstantNode, ExprOpNode {

    public DoubleNode(double value){
        this.value=value;
    }

    public double getValue() {
        return value;
    }

    public SymbolTable getTable() {
        return table;
    }

    public void setTable(SymbolTable table) {
        this.table = table;
    }

    @Override
    public Type getReturnType() {
        return returnType;
    }

    @Override
    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName()+"{"
                + "value: '"
                + value +"'}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private double value;

    private SymbolTable table;
    private Type returnType;
}
