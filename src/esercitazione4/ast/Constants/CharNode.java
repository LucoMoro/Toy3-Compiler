package esercitazione4.ast.Constants;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.ast.Type;
import esercitazione4.visitor.Visitor;
import esercitazione4.visitor.symbolTable.SymbolTable;

public class CharNode implements ConstantNode, ExprOpNode {

    public CharNode(char c){
        this.c=c;
    }

    public char getValue(){
        return c;
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
                + c + "'}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private char c;

    private SymbolTable table;
    private Type returnType;
}
