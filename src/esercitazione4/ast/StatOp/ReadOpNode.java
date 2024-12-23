package esercitazione4.ast.StatOp;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.visitor.Visitor;

import java.util.ArrayList;

public class ReadOpNode implements StatOpNode {

    public ReadOpNode(Object identifiers){
        this.identifiers = (ArrayList<ExprOpNode>) identifiers;
    }

    public ArrayList<ExprOpNode> getIdentifiers() {
        return identifiers;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + "{"
                + "list of identifiers: '" + identifiers + "'"
                + "}";
    }

    public Object accept(Visitor v) {
        return v.visit(this);
    }

    private ArrayList<ExprOpNode> identifiers;
}
