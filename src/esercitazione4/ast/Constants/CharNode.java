package esercitazione4.ast.Constants;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.visitor.Visitor;

public class CharNode implements ConstantNode, ExprOpNode {

    public CharNode(char c){
        this.c=c;
    }

    public char getValue(){
        return c;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName()+"{"
                + "value: '"
                + c + "'}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private char c;
}
