package esercitazione4.ast.Constants;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.visitor.Visitor;

public class DoubleNode implements ConstantNode, ExprOpNode {

    public DoubleNode(double value){
        this.value=value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName()+"{"
                + "value: '"
                + value +"'}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private double value;
}
