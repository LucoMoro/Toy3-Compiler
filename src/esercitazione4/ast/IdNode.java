package esercitazione4.ast;

import esercitazione4.visitor.Visitor;
import esercitazione4.visitor.symbolTable.SymbolTable;

public class IdNode implements ExprOpNode {

    public IdNode(Object name){
        this.name = (String) name;
    }

    public String getValue() {
        return name;
    }

    public SymbolTable getTable() {
        return table;
    }

    public void setTable(SymbolTable table) {
        this.table = table;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{"
                + "name: '" + name + "'"
                + "}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private String name;

    private SymbolTable table;
}
