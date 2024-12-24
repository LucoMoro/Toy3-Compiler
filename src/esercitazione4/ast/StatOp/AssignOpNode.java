package esercitazione4.ast.StatOp;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.ast.IdNode;
import esercitazione4.visitor.Visitor;
import esercitazione4.visitor.symbolTable.SymbolTable;

import java.util.ArrayList;

public class AssignOpNode implements StatOpNode {

    public AssignOpNode(Object identifiers, Object expressions){
        this.identifiers = (ArrayList<IdNode>) identifiers;
        this.expressions = (ArrayList<ExprOpNode>) expressions;
    }

    public ArrayList<IdNode> getIdentifiers() {
        return identifiers;
    }

    public ArrayList<ExprOpNode> getExpressions() {
        return expressions;
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
                + "list of identifiers: '" + identifiers + "'; "
                + "list of expressions: '" + expressions + "'"
                + "}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private ArrayList<IdNode> identifiers;
    private ArrayList<ExprOpNode> expressions;
    private SymbolTable table;
}
