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

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.*;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Map;

public class TreeVisitor implements Visitor{

    public TreeVisitor(FileWriter file){
        this.file = file;
    }

    /* Program */
    public Object visit(ProgramNode node){
        try{
            file.append("<Program>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;

        ArrayList<DeclOpNode> decls = node.getDecls();

        for (DeclOpNode decl : decls){
            decl.accept(this);
        }

        ArrayList<VarDeclNode> vars = node.getVars();
        ArrayList<StatOpNode> stats = node.getStats();

        if(vars != null){
            for(VarDeclNode var : vars){
                var.accept(this);
            }
        }

        if(stats != null){
            for(StatOpNode stat : stats){
                stat.accept(this);
            }
        }


        depth--;

        try{
            file.append("</Program>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }

    /* DefDecl */
    public Object visit(DefDeclNode node){
        String open_tag = "<DefDecl name='" + node.getName().getValue() + "' "
                          + "type='" + node.getType() + "'>";

        try{

            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        ArrayList<ParDeclNode> pars = node.getParams();
        if(pars != null){
            for(ParDeclNode par : pars){
                par.accept(this);
            }
        }

        node.getBody().accept(this);

        depth--;

        try{
            String indent = space.repeat(depth);
            file.append(indent).append("</DefDecl>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }

    /* VarDecls */
    public Object visit(VarOptInitNode node){
        String open_tag= "<VarOptInit name='" + node.getIdentifier().getValue() + "'>";

        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        ExprOpNode expression = node.getExpression();
        if(expression != null){
            expression.accept(this);
        }

        depth--;
        try{
            String indent = space.repeat(depth);
            file.append(indent).append("</VarOptInit>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }
    public Object visit(VarDeclNode node){
        String open_tag="";

        if(node.getType()!=null) {
            open_tag = "<VarDecl type='" + node.getType() + "'>";
        } else {
            open_tag = "<VarDecl>";
        }

        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        ArrayList<VarOptInitNode> vars = node.getVars();
        if(vars != null){
            for(VarOptInitNode var : vars){
                var.accept(this);
            }
        }

        ConstantNode constant = node.getConstant();
        if (constant != null){
            constant.accept(this);
        }


        depth--;

        try{
            String indent = space.repeat(depth);
            file.append(indent).append("</VarDecl>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }

    /* PVar */
    public Object visit(PVarNode node){
        String open_tag;

        IdNode variable = node.getVariable();

        open_tag = "<PVar hasRef='" + node.getHasRef() + "' "
                   + " name='" + variable.getValue() + "'>";

        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try{
            String indent = space.repeat(depth);
            file.append(indent).append("</PVar>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }
    public Object visit(ParDeclNode node){
        String open_tag = "<ParDecl type='" + node.getRight() +"'>";

        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        ArrayList<PVarNode> pvars = node.getLeft();
        if(pvars != null){
            for(PVarNode pvar : pvars){
                pvar.accept(this);
            }
        }

        depth--;

        try{
            String indent = space.repeat(depth);
            file.append(indent).append("</ParDecl>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }

    /* Body */
    public Object visit(BodyNode node){
        String open_tag;

        try {
            String indent = space.repeat(depth);
            open_tag="<Body>";
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        ArrayList<VarDeclNode> vars = node.getLeft();
        if(vars != null){
            for(VarDeclNode var : vars){
                var.accept(this);
            }
        }

        ArrayList<StatOpNode> stats = node.getRight();
        if(stats != null){
            for(StatOpNode stat : stats){
                stat.accept(this);
            }
        }

        depth--;
        try{
            String indent = space.repeat(depth);
            file.append(indent).append("</Body>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }

    /* Statements */
    public Object visit(ReadOpNode node){
        String open_tag;

        open_tag = "<ReadNode>";

        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;

        ArrayList<ExprOpNode> identifiers = node.getIdentifiers();
        for(ExprOpNode expr : identifiers){
            expr.accept(this);
        }

        depth--;

        try {
            String indent = space.repeat(depth);
            file.append(indent).append("</ReadNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }
    public Object visit(WriteOpNode node){
        String open_tag;

        open_tag = "<WriteNode isNewLine='" + node.getIsNewLine() +"'>";

        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;

        ArrayList<ExprOpNode> expressions = node.getExpressions();
        for(ExprOpNode expr : expressions){
            expr.accept(this);
        }

        depth--;

        try{
            String indent = space.repeat(depth);
            file.append(indent).append("</WriteNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }
    public Object visit(AssignOpNode node){
        String open_tag;

        open_tag="<AssignNode>";

        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;

        ArrayList<IdNode> identifiers = node.getIdentifiers();
        for(IdNode identifier : identifiers){
            identifier.accept(this);
        }

        ArrayList<ExprOpNode> expressions = node.getExpressions();
        for(ExprOpNode expression : expressions){
            expression.accept(this);
        }

        depth--;

        try{
            String indent = space.repeat(depth);
            file.append(indent).append("</AssignNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }
    public Object visit(ReturnOpNode node){
        String open_tag;

        open_tag = "<ReturnNode>";

        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;

        ExprOpNode expr = node.getExpression();
        expr.accept(this);

        depth--;

        try {
            String indent = space.repeat(depth);
            file.append(indent).append("</ReturnNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }
    public Object visit(IfThenElseNode node){
        String open_tag;

        open_tag = "<IfThenElseNode>";
        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        ExprOpNode expr = node.getLeft();
        expr.accept(this);

        BodyNode ifThenBody = node.getMid();
        ifThenBody.accept(this);

        BodyNode elseBody = node.getRight();
        elseBody.accept(this);

        depth--;
        try {
            String indent = space.repeat(depth);
            file.append(indent).append("</IfThenElseNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }
    public Object visit(IfThenNode node){
        String open_tag;

        open_tag = "<IfThenNode>";

        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;

        ExprOpNode expr = node.getLeft();
        expr.accept(this);

        BodyNode ifThenBody = node.getRight();
        ifThenBody.accept(this);

        depth--;
        try {
            String indent = space.repeat(depth);
            file.append(indent).append("</IfThenNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }
    public Object visit(WhileNode node){
        String open_tag;

        open_tag = "<WhileNode>";

        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;

        ExprOpNode expr = node.getLeft();
        expr.accept(this);

        BodyNode body = node.getRight();
        body.accept(this);

        depth--;

        try{
            String indent = space.repeat(depth);
            file.append(indent).append("</WhileNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }
    public Object visit(FunCallNode node){
        String open_tag;

        open_tag = "<FunCallNode name='" + node.getName().getValue()
                   + "'>";

        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;

        ArrayList<ExprOpNode> exprs = node.getParameters();
        if(exprs != null){
            for(ExprOpNode expr : exprs){
                expr.accept(this);
            }
        }

        depth--;

        try {
            String indent = space.repeat(depth);
            file.append(indent).append("</FunCallNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }

    @Override
    public Object visit(SwitchStatNode node) {
        String open_tag;

        open_tag = "<SwitchStatNode>";

        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;

        ConstantNode constant = node.getConstant();
        constant.accept(this);

        ArrayList<StatOpNode> stats = node.getStats();
        if(stats != null) {
            for(StatOpNode stat : stats) {
                stat.accept(this);
            }
        }

        depth--;

        try {
            String indent = space.repeat(depth);
            file.append(indent).append("</SwitchStatNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }

    @Override
    public Object visit(SwitchNode node) {
        String open_tag;

        open_tag = "<SwitchNode>";

        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;

        IdNode id = node.getId();
        id.accept(this);

        ArrayList<SwitchStatNode> switchStats = node.getSwitchStats();
        if(switchStats != null) {
            for(SwitchStatNode switchStat : switchStats) {
                switchStat.accept(this);
            }
        }

        depth--;

        try {
            String indent = space.repeat(depth);
            file.append(indent).append("</SwitchNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }

    /* Identifier */
    public Object visit(IdNode node){
        String open_tag;

        open_tag = "<IdNode>";

        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag);
            file.append(node.getValue());
            file.append("</IdNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }

    /* Constants */
    public Object visit(BoolNode node){
        String open_tag;

        open_tag = "<BoolNode>";

        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag);
            file.append(node.getValue().toString());
            file.append("</BoolNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }
    public Object visit(CharNode node){
        String open_tag;

        open_tag = "<CharNode>";
        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag);
            file.append(node.getValue());
            file.append("</CharNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return node;
    }
    public Object visit(IntNode node){
        String open_tag;

        open_tag = "<IntNode>";
        try {
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag);
            file.append(""+node.getValue());
            file.append("</IntNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return node;
    }
    public Object visit(DoubleNode node){
        String open_tag;

        open_tag = "<DoubleNode>";
        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag);
            file.append(""+node.getValue());
            file.append("</DoubleNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return node;
    }
    public Object visit(StringNode node){
        String open_tag;

        open_tag = "<StringNode>";
        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag);
            file.append(node.getValue());
            file.append("</StringNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return node;
    }

    /* Arithmetic Operators */
    public Object visit(AddNode node){
        String open_tag;

        open_tag = "<AddNode>";
        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        ExprOpNode e1 = (ExprOpNode) node.getLeft();
        e1.accept(this);

        ExprOpNode e2 = (ExprOpNode) node.getRight();
        e2.accept(this);
        depth--;

        try{
            String indent = space.repeat(depth);
            file.append(indent).append("</AddNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return node;
    }
    public Object visit(DiffNode node){
        String open_tag;

        open_tag = "<DiffNode>";

        try {
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        ExprOpNode e1 = node.getLeft();
        e1.accept(this);
        ExprOpNode e2 = node.getRight();
        e2.accept(this);
        depth--;

        try {
            String indent = space.repeat(depth);
            file.append(indent).append("</DiffNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return node;
    }
    public Object visit(MulNode node){
        String open_tag;

        open_tag = "<MulNode>";
        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        //todo uniform the classes
        ExprOpNode e1 = (ExprOpNode) node.getLeft();
        e1.accept(this);
        ExprOpNode e2 = (ExprOpNode) node.getRight();
        e2.accept(this);
        depth--;

        try {
            String indent = space.repeat(depth);
            file.append(indent).append("</MulNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return node;
    }
    public Object visit(DivNode node){
        String open_tag;

        open_tag = "<DivNode>";
        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        ExprOpNode e1 = node.getLeft();
        e1.accept(this);
        ExprOpNode e2 = node.getRight();
        e2.accept(this);
        depth--;

        try{
            String indent = space.repeat(depth);
            file.append(indent).append("</DivNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return node;
    }
    public Object visit(UMinusNode node){
        String open_tag;

        open_tag = "<UMinusNode>";
        try{
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        ExprOpNode expr = node.getLeft();
        expr.accept(this);
        depth--;

        try {
            String indent = space.repeat(depth);
            file.append(indent).append("</UMinusNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }

    /* Boolean Operators */
    public Object visit(AndNode node) {
        String open_tag;

        open_tag = "<AndNode>";

        try {
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        ExprOpNode e1 = node.getLeft();
        e1.accept(this);
        ExprOpNode e2 = node.getRight();
        e2.accept(this);
        depth--;

        try {
            String indent = space.repeat(depth);
            file.append(indent).append("</AndNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return node;
    }
    public Object visit(OrNode node) {
        String open_tag;

        open_tag = "<OrNode>";

        try {
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        ExprOpNode e1 = node.getLeft();
        e1.accept(this);
        ExprOpNode e2 = node.getRight();
        e2.accept(this);
        depth--;

        try {
            String indent = space.repeat(depth);
            file.append(indent).append("</OrNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return node;
    }
    public Object visit(NotNode node) {
        String open_tag;

        open_tag = "<NotNode>";

        try {
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        ExprOpNode expr = node.getLeft();
        expr.accept(this);
        depth--;

        try {
            String indent = space.repeat(depth);
            file.append(indent).append("</NotNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return node;
    }

    /* Relational Operators */
    public Object visit(GTNode node) {
        String open_tag;

        open_tag="<GTNode>";

        try {
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        ExprOpNode e1 = node.getLeft();
        e1.accept(this);
        ExprOpNode e2 = node.getRight();
        e2.accept(this);
        depth--;

        try{
            String indent = space.repeat(depth);
            file.append(indent).append("</GTNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return node;
    }
    public Object visit(GENode node) {
        String open_tag;

        open_tag = "<GENode>";

        try {
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        ExprOpNode e1 = node.getLeft();
        e1.accept(this);
        ExprOpNode e2 = node.getRight();
        e2.accept(this);
        depth--;

        try {
            String indent = space.repeat(depth);
            file.append(indent).append("</GENode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return node;
    }
    public Object visit(LTNode node) {
        String open_tag;

        open_tag = "<LTNode>";
        try {
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        ExprOpNode e1 = node.getLeft();
        e1.accept(this);
        ExprOpNode e2 = node.getRight();
        e2.accept(this);
        depth--;

        try {
            String indent = space.repeat(depth);
            file.append(indent).append("</LTNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return node;
    }
    public Object visit(LENode node) {
        String open_tag;

        open_tag = "<LENode>";
        try {
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        ExprOpNode e1 = node.getLeft();
        e1.accept(this);
        ExprOpNode e2 = node.getRight();
        e2.accept(this);
        depth--;

        try {
            String indent = space.repeat(depth);
            file.append(indent).append("</LENode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return node;
    }
    public Object visit(EQNode node) {
        String open_tag;

        open_tag = "<EQNode>";
        try {
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        ExprOpNode e1 = node.getLeft();
        e1.accept(this);
        ExprOpNode e2 = node.getRight();
        e2.accept(this);
        depth--;

        try {
            String indent = space.repeat(depth);
            file.append(indent).append("</EQNode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return node;
    }
    public Object visit(NENode node) {
        String open_tag;

        open_tag = "<NENode>";
        try {
            String indent = space.repeat(depth);
            file.append(indent).append(open_tag).append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        depth++;
        ExprOpNode e1 = node.getLeft();
        e1.accept(this);
        ExprOpNode e2 = node.getRight();
        e2.accept(this);
        depth--;

        try {
            String indent = space.repeat(depth);
            file.append(indent).append("</NENode>").append("\n");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return node;
    }


    private final FileWriter file;
    private int depth = 0;
    private String space="  ";
}
