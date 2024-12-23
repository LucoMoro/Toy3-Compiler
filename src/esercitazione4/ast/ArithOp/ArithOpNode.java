package esercitazione4.ast.ArithOp;

import esercitazione4.ast.ExprOpNode;
import esercitazione4.visitor.Visitor;

public interface ArithOpNode extends ExprOpNode {

    Object accept(Visitor v);
}
