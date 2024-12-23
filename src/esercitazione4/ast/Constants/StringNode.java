package esercitazione4.ast.Constants;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.visitor.Visitor;

public class StringNode implements ConstantNode, ExprOpNode {

    public StringNode(String value){
        this.value = value;
    }

    public String getValue() {
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

    private String value;
}
