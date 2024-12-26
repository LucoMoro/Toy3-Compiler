package esercitazione4.ast.VarDeclOp;

import esercitazione4.ast.*;
import esercitazione4.visitor.Visitor;
import esercitazione4.visitor.symbolTable.SymbolTable;

public class VarOptInitNode implements DeclOpNode {

    public VarOptInitNode(Object identifier){
        this.identifier = (IdNode) identifier;
        this.expression = null;
    }

    public VarOptInitNode(Object identifier, Object expression){
        this.identifier = (IdNode) identifier;
        this.expression = (ExprOpNode) expression;
    }

    public IdNode getIdentifier() {
        return identifier;
    }

    public ExprOpNode getExpression() {
        return expression;
    }

    public SymbolTable getTable() {
        return table;
    }

    public void setTable(SymbolTable symbolTable) {
        this.table = symbolTable;
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
                + "identifier: '" + identifier + "'; "
                + "expression: '" + expression + "'"
                + "}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private IdNode identifier;
    private ExprOpNode expression;
    private SymbolTable table;

    private Type returnType;
}
