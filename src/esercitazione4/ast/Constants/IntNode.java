package esercitazione4.ast.Constants;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.visitor.Visitor;

public class IntNode implements ConstantNode, ExprOpNode {

    public IntNode(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName()+"{"
                + "value: '"
                + value + "'}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private int value;
}
