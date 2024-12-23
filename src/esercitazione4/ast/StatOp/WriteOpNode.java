package esercitazione4.ast.StatOp;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.visitor.Visitor;

import java.util.ArrayList;

//Todo class should be renamed. Only interfaceses whould have "Op" in their name
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
}
