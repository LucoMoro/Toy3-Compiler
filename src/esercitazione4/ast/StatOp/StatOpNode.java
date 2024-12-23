package esercitazione4.ast.StatOp;

import esercitazione4.ast.Node;
import esercitazione4.visitor.Visitor;

public interface StatOpNode extends Node {

    Object accept(Visitor v);
}
