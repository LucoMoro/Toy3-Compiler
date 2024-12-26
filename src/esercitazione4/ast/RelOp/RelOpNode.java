package esercitazione4.ast.RelOp;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.ast.Type;
import esercitazione4.visitor.Visitor;

public interface RelOpNode extends ExprOpNode {

    Object accept(Visitor v);

    Type getReturnType();
    void setReturnType(Type returnType);
}
