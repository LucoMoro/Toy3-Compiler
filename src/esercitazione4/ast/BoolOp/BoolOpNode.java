package esercitazione4.ast.BoolOp;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.ast.Type;
import esercitazione4.visitor.Visitor;

public interface BoolOpNode extends ExprOpNode {

    Object accept(Visitor v);

    Type getReturnType();
    void setReturnType(Type returnType);
}
