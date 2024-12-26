package esercitazione4.ast.Constants;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.ast.Type;
import esercitazione4.visitor.Visitor;

public interface ConstantNode extends ExprOpNode {
    Object accept(Visitor v);

    Type getReturnType();
    void setReturnType(Type returnType);
}
