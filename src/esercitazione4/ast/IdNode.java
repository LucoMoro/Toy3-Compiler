package esercitazione4.ast;

import esercitazione4.visitor.Visitor;
import esercitazione4.visitor.symbolTable.Firm;
import esercitazione4.visitor.symbolTable.SymbolTable;

public class IdNode implements ExprOpNode {

    public IdNode(Object name){
        this.name = (String) name;
        this.hasRef = false;
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
    public Type getReturnType() {
        return returnType;
    }

    @Override
    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public Boolean getHasRef() {
        return hasRef;
    }

    public void setHasRef(Boolean hasRef) {
        this.hasRef = hasRef;
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
    private Type returnType;
    private Boolean hasRef;
}
