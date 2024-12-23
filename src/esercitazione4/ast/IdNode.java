package esercitazione4.ast;

import esercitazione4.visitor.Visitor;

public class IdNode implements ExprOpNode {

    public IdNode(Object name){
        this.name = (String) name;
    }

    public String getValue() {
        return name;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{"
                + "name: '" + name + "'"
                + "}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private String name;
}
