package esercitazione4.ast;

import esercitazione4.visitor.Visitor;

public interface ExprOpNode extends Node {

    Object accept(Visitor v);

    Type getReturnType();
    void setReturnType(Type returnType);
}
