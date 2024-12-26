package esercitazione4.ast.ParDeclOp;

import esercitazione4.ast.IdNode;
import esercitazione4.ast.Type;
import esercitazione4.visitor.Visitor;
import esercitazione4.visitor.symbolTable.SymbolTable;

public class PVarNode implements ParDeclOpNode {

    public PVarNode(Object variable){
        this.variable = (IdNode) variable;
        this.hasRef = false;
    }

    public PVarNode(Object variable, Boolean hasRef){
        this.variable = (IdNode) variable;
        this.hasRef = hasRef;
    }

    public IdNode getVariable() {
        return variable;
    }

    public Boolean getHasRef() {
        return hasRef;
    }

    public SymbolTable getTable() {
        return table;
    }

    public void setTable(SymbolTable table) {
        this.table = table;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + "{"
                + "ref: '" + hasRef + "'; "
                + "variable: '" + variable + "'"
                + "}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private IdNode variable;
    private Boolean hasRef;

    private SymbolTable table;
    private Type returnType;
}
