package esercitazione4.visitor;

import esercitazione4.ast.*;
import esercitazione4.ast.ArithOp.*;
import esercitazione4.ast.BoolOp.*;
import esercitazione4.ast.Constants.*;
import esercitazione4.ast.DefDeclOp.DefDeclNode;
import esercitazione4.ast.ParDeclOp.*;
import esercitazione4.ast.RelOp.*;
import esercitazione4.ast.StatOp.*;
import esercitazione4.ast.VarDeclOp.*;

public interface Visitor {
    /* Identifier  */
    Object visit(IdNode node);

    /* Constants */
    Object visit(BoolNode node);
    Object visit(CharNode node);
    Object visit(IntNode node);
    Object visit(DoubleNode node);
    Object visit(StringNode node);

    /* Arithmetic Operators */
    Object visit(AddNode node);
    Object visit(DiffNode node);
    Object visit(MulNode node);
    Object visit(DivNode node);
    Object visit(UMinusNode node);

    /* Boolean Operators */
    Object visit(AndNode node);
    Object visit(OrNode node);
    Object visit(NotNode node);

    /* Relational Operators */
    Object visit(GTNode node);
    Object visit(GENode node);
    Object visit(LTNode node);
    Object visit(LENode node);
    Object visit(EQNode node);
    Object visit(NENode node);

    Object visit(FunCallNode node);

    /* Statements */
    Object visit(ReadOpNode node);
    Object visit(WriteOpNode node);
    Object visit(AssignOpNode node);
    Object visit(ReturnOpNode node);
    Object visit(IfThenElseNode node);
    Object visit(IfThenNode node);
    Object visit(WhileNode node);
    Object visit(SwitchStatNode node);
    Object visit(SwitchNode node);

    /* VarDecls */
    Object visit(VarOptInitNode node);
    Object visit(VarDeclNode node);

    /* Body */
    Object visit(BodyNode node);

    /* PVar */
    Object visit(PVarNode node);
    Object visit(ParDeclNode node);

    /* DefDecl */
    Object visit(DefDeclNode node);

    /* Program */
    Object visit(ProgramNode node);
}
