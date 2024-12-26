package esercitazione4.visitor;

import esercitazione4.ast.*;
import esercitazione4.ast.ArithOp.*;
import esercitazione4.ast.BoolOp.AndNode;
import esercitazione4.ast.BoolOp.NotNode;
import esercitazione4.ast.BoolOp.OrNode;
import esercitazione4.ast.Constants.*;
import esercitazione4.ast.DefDeclOp.DefDeclNode;
import esercitazione4.ast.ParDeclOp.PVarNode;
import esercitazione4.ast.ParDeclOp.ParDeclNode;
import esercitazione4.ast.RelOp.*;
import esercitazione4.ast.StatOp.*;
import esercitazione4.ast.VarDeclOp.VarDeclNode;
import esercitazione4.ast.VarDeclOp.VarOptInitNode;
import esercitazione4.visitor.symbolTable.SymbolTable;

import java.util.ArrayList;
import java.util.Stack;

public class TypeCheckerVisitor implements  Visitor{

    private Stack<SymbolTable> typeEnvironment = new Stack<>();

    /* Program */
    @Override
    public Object visit(ProgramNode node) {

        typeEnvironment.add(node.getProgramTable());

        ArrayList<DeclOpNode> decls = node.getDecls();
        if(decls != null){
            for(DeclOpNode decl : decls){
                decl.accept(this);
            }
        }

        typeEnvironment.add(node.getBegindEndTable());

        ArrayList<VarDeclNode> vars = node.getVars();
        if(vars != null){
            for(VarDeclNode var : vars){
                var.accept(this);
            }
        }

        ArrayList<StatOpNode> stats = node.getStats();
        if(stats != null){
            for(StatOpNode stat : stats){
                stat.accept(this);
            }
        }

        typeEnvironment.pop(); //beginEndTable pop
        typeEnvironment.pop(); //ProgramTable pop
        node.setReturnType(Type.NOTYPE);

        return node.getReturnType();
    }

    /* DefDecl */
    @Override
    public Object visit(DefDeclNode node) {

        boolean returnFlag = false;

        typeEnvironment.add(node.getTable());

        IdNode id = node.getName();
        id.accept(this);

        ArrayList<ParDeclNode> pars = node.getParams();
        if(pars != null){
            for(ParDeclNode par : pars){
                par.accept(this);
            }
        }

        BodyNode body = node.getBody();
        body.accept(this);

        body.setReturnType(Type.NOTYPE); //to remove when BodyNode will be implemented
        if(body.getReturnType() != Type.NOTYPE){
            throw  new RuntimeException("Type system error: " + getClass().getSimpleName());
        }

        ArrayList<StatOpNode> stats = node.getBody().getRight();

        Type functionType = node.getType();
        if(functionType == null){
            if(stats != null){
                for(StatOpNode stat : stats){
                    if(stat instanceof ReturnOpNode){
                        throw new RuntimeException("The procedure " + node.getName().getValue() + " should not contain a return statement");
                    }
                }
            }
        } else {
            if(stats != null){
                for(StatOpNode stat : stats){
                    if(stat instanceof ReturnOpNode){ //searches for the return statement
                        returnFlag = true;
                        ReturnOpNode tmpStat = (ReturnOpNode) stat; //if the return statement is found I create a temporary statemnt
                        Type tmpType = null;
                        if(tmpStat.getReturnType() == null && (tmpStat.getExpression() instanceof ConstantNode)){
                            tmpType = convertType((ConstantNode) tmpStat.getExpression());
                        }
                        if(tmpType != functionType){
                            throw new RuntimeException("The return type for " + node.getName().getValue() +" is incorrect");
                        }
                    }
                }
                if (returnFlag == false) {
                    throw new RuntimeException("The function " + node.getName().getValue() +" must contain at least one return statement");
                }
            }
        }

        typeEnvironment.pop();
        node.setReturnType(Type.NOTYPE);

        return Type.NOTYPE;
    }

    @Override
    public Object visit(IdNode node) {
        return null;
    }

    @Override
    public Object visit(BoolNode node) {
        return null;
    }

    @Override
    public Object visit(CharNode node) {
        return null;
    }

    @Override
    public Object visit(IntNode node) {
        return null;
    }

    @Override
    public Object visit(DoubleNode node) {
        return null;
    }

    @Override
    public Object visit(StringNode node) {
        return null;
    }

    @Override
    public Object visit(AddNode node) {
        return null;
    }

    @Override
    public Object visit(DiffNode node) {
        return null;
    }

    @Override
    public Object visit(MulNode node) {
        return null;
    }

    @Override
    public Object visit(DivNode node) {
        return null;
    }

    @Override
    public Object visit(UMinusNode node) {
        return null;
    }

    @Override
    public Object visit(AndNode node) {
        return null;
    }

    @Override
    public Object visit(OrNode node) {
        return null;
    }

    @Override
    public Object visit(NotNode node) {
        return null;
    }

    @Override
    public Object visit(GTNode node) {
        return null;
    }

    @Override
    public Object visit(GENode node) {
        return null;
    }

    @Override
    public Object visit(LTNode node) {
        return null;
    }

    @Override
    public Object visit(LENode node) {
        return null;
    }

    @Override
    public Object visit(EQNode node) {
        return null;
    }

    @Override
    public Object visit(NENode node) {
        return null;
    }

    @Override
    public Object visit(FunCallNode node) {
        return null;
    }

    @Override
    public Object visit(ReadOpNode node) {
        return null;
    }

    @Override
    public Object visit(WriteOpNode node) {
        return null;
    }

    @Override
    public Object visit(AssignOpNode node) {
        return null;
    }

    @Override
    public Object visit(ReturnOpNode node) {
        return null;
    }

    @Override
    public Object visit(IfThenElseNode node) {
        return null;
    }

    @Override
    public Object visit(IfThenNode node) {
        return null;
    }

    @Override
    public Object visit(WhileNode node) {
        return null;
    }

    @Override
    public Object visit(VarOptInitNode node) {
        return null;
    }

    @Override
    public Object visit(VarDeclNode node) {
        return null;
    }

    @Override
    public Object visit(BodyNode node) {
        return null;
    }

    @Override
    public Object visit(PVarNode node) {
        return null;
    }

    @Override
    public Object visit(ParDeclNode node) {
        return null;
    }

    /**
     * Function that converts the constant into the corresponding type
     */
    public Type convertType(ConstantNode constant){
        Type convertedType = null;

        if(constant instanceof BodyNode){
            convertedType = Type.BOOL;
        } else if (constant instanceof IntNode){
            convertedType = Type.INT;
        } else if (constant instanceof DoubleNode){
            convertedType = Type.DOUBLE;
        } else if (constant instanceof CharNode){
            convertedType = Type.CHAR;
        } else if (constant instanceof StringNode){
            convertedType = Type.STRING;
        }

        return convertedType;
    }

}
