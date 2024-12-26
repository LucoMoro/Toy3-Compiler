package esercitazione4.ast;

import esercitazione4.visitor.Visitor;

public interface Node {
    Object accept(Visitor v);

    //todo define setTable & getTable
}
