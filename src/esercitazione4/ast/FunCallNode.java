package esercitazione4.ast;

import esercitazione4.ast.StatOp.StatOpNode;
import esercitazione4.visitor.Visitor;

import java.util.ArrayList;

public class FunCallNode implements ExprOpNode, StatOpNode {

    public FunCallNode(Object name){
        this.name = (IdNode) name;
    }

    public FunCallNode(Object name, Object parameters){
        this.name = (IdNode) name;
        this.parameters = (ArrayList<ExprOpNode>) parameters;
    }

    public IdNode getName() {
        return name;
    }

    public ArrayList<ExprOpNode> getParameters() {
        return parameters;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + "{"
                + "name: '" + name + "'"
                + "parameters: '" + parameters + "'"
                + "}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private IdNode name;
    private ArrayList<ExprOpNode> parameters = new ArrayList<>();
}
