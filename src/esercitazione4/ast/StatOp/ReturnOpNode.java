package esercitazione4.ast.StatOp;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.visitor.Visitor;
import esercitazione4.visitor.symbolTable.SymbolTable;

public class ReturnOpNode implements StatOpNode {

    public ReturnOpNode(Object expression){
        this.expression = (ExprOpNode) expression;
    }

    public ExprOpNode getExpression() {
        return expression;
    }

    public SymbolTable getTable() {
        return table;
    }

    public void setTable(SymbolTable table) {
        this.table = table;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + "{"
                + "expression: '" + expression + "'"
                + "}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private ExprOpNode expression;
    private SymbolTable table;
}
