package esercitazione4.ast.ParDeclOp;

import esercitazione4.ast.Type;
import esercitazione4.visitor.Visitor;

import java.util.ArrayList;

public class ParDeclNode implements  ParDeclOpNode {

    public ParDeclNode(Object pvars, Object type){
        this.left = (ArrayList<PVarNode>) pvars;
        this.right = (Type) type;
    }

    public ArrayList<PVarNode> getLeft() {
        return left;
    }

    public Type getRight() {
        return right;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + "{"
                + "list of parameters: '" + left + "'; "
                + "type: '" + right + "'"
                + "}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private ArrayList<PVarNode> left;
    private Type right;
}
