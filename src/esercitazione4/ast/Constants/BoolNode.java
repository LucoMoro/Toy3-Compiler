package esercitazione4.ast.Constants;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.visitor.Visitor;

public class BoolNode implements ConstantNode, ExprOpNode {

    public BoolNode(Boolean value){
        this.value = value;
    }

    public Boolean getValue(){
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

    private Boolean value;
}
