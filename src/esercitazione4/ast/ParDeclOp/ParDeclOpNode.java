package esercitazione4.ast.ParDeclOp;

import esercitazione4.ast.DeclOpNode;
import esercitazione4.visitor.Visitor;

public interface ParDeclOpNode extends DeclOpNode {

    Object accept(Visitor v);
}
