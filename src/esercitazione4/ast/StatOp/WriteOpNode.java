package esercitazione4.ast.StatOp;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.ast.Type;
import esercitazione4.visitor.Visitor;
import esercitazione4.visitor.symbolTable.SymbolTable;

import java.util.ArrayList;

//Todo class should be renamed. Only interfaceses should have "Op" in their name
public class WriteOpNode implements StatOpNode{

    public WriteOpNode(Object expressions){
        this.expressions = (ArrayList<ExprOpNode>) expressions;
        this.isNewLine = false;
    }

    public WriteOpNode(Object expressions, Boolean newLine){
        this.expressions = (ArrayList<ExprOpNode>) expressions;
        this.isNewLine = newLine;
    }

    public Boolean getIsNewLine() {
        return isNewLine;
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

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + "{"
                + "list of expressions: '" + expressions + "'; "
                + "new line: '" + isNewLine + "'"
                + "}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private Boolean isNewLine;
    private ArrayList<ExprOpNode> expressions;
    private SymbolTable table;

    private Type returnType;
}
