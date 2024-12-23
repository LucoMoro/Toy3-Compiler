package esercitazione4.ast;

import esercitazione4.visitor.Visitor;

public interface DeclOpNode extends Node{

    Object accept(Visitor v);
}
