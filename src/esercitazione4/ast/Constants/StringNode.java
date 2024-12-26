package esercitazione4.ast.Constants;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.ast.Type;
import esercitazione4.visitor.Visitor;
import esercitazione4.visitor.symbolTable.SymbolTable;

public class StringNode implements ConstantNode, ExprOpNode {

    public StringNode(String value){
        this.value = value;
    }

    public String getValue() {
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

    private String value;

    private SymbolTable table;
    private Type returnType;
}
