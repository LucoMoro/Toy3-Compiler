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

        body.setReturnType(Type.NOTYPE); //todo remove when BodyNode will be implemented
        if(body.getReturnType() != Type.NOTYPE){
            throw  new RuntimeException("Type system error: " + getClass().getSimpleName());
        }

        checkReturnType(node); //could be implemented after the accept()

        ArrayList<StatOpNode> stats = node.getBody().getRight();
        if(stats != null){
            for(StatOpNode stat : stats){
                stat.accept(this);
            }
        }

        typeEnvironment.pop();
        node.setReturnType(Type.NOTYPE);

        return Type.NOTYPE;
    }

    /* VarDecls */
    @Override
    public Object visit(VarOptInitNode node) {

        typeEnvironment.add(node.getTable());

        IdNode id = node.getIdentifier();
        id.accept(this);

        ExprOpNode expr = node.getExpression();
        if(expr != null){
            expr.accept(this);
        }

        typeEnvironment.pop();
        node.setReturnType(Type.NOTYPE); //todo change in node.setReturnType(expr.getReturnType()); when the method will be implemented

        return node.getReturnType();
    }
    @Override
    public Object visit(VarDeclNode node) {

        typeEnvironment.add(node.getTable());

        ArrayList<VarOptInitNode> optVars = node.getVars();

        if(optVars != null){
            for(VarOptInitNode optVar : optVars){
                Type optVarType = (Type) optVar.accept(this);
                if(optVar.getExpression() != null){ //further checks are useless if the variable has not been initialized
                    if(node.getType() != null){ //VarDecl could be initialized with a type or a constant, so a check is needed
                        if(optVarType != node.getType()){
                            throw new RuntimeException("The variable '" + optVar.getIdentifier().getValue() +
                                    "' initialized with: " + optVar.getExpression() + " does not match the declaration type: " + node.getType());
                        }
                    } else { //this case should be always covered by the ScopeVisitor, since it is not possible to have something like var = "test" : "a";
                        Type initializationConstantType = Type.convertType(node.getConstant()); //could be used the return value of constant.accept() but the code would become less clear
                        if(optVarType != initializationConstantType){
                            throw new RuntimeException("The variable '" + optVar.getIdentifier().getValue() +
                                    "' initialized with: " + optVar.getExpression() + " does not match the declaration type: " + initializationConstantType);
                        }
                    }
                }
            }
        }

        ConstantNode constant = node.getConstant();
        if(constant != null){
            constant.accept(this);
        }

        typeEnvironment.pop();
        node.setReturnType(Type.NOTYPE);

        return node.getReturnType();
    }

    /* PVar */
    @Override
    public Object visit(PVarNode node) {

        typeEnvironment.add(node.getTable());

        IdNode id = node.getVariable();
        id.accept(this);

        typeEnvironment.pop();
        node.setReturnType(id.getReturnType());

        return id.getReturnType();
    }

    @Override
    public Object visit(ParDeclNode node) {
        return null;
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
    public Object visit(BodyNode node) {
        return null;
    }

    /**
    * checks if the function is a procedure or a function and, in case it is a function,
     * if it has at least one return statement
    */
    public void checkReturnType(DefDeclNode node){
        boolean returnFlag = false;

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
                        if(tmpStat.getReturnType() == null && (tmpStat.getExpression() instanceof ConstantNode)){ //todo modify when implementing visitors on arithmetic operators
                            tmpType = Type.convertType((ConstantNode) tmpStat.getExpression()); //also VariableType could be used since it does the same
                                                                                                //if a constant is provided
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
    }

    public Type singleExpressionOperation(String operation, ExprOpNode expr1){
        Type type = null;

        if (operation.equals("MINUS") && expr1.getReturnType() == Type.INT ){
            type = Type.INT;
        } else if (operation.equals("MINUS") && expr1.getReturnType() == Type.DOUBLE){
            type = Type.DOUBLE;
        } else if (operation.equals("NOT") && expr1.getReturnType() == Type.BOOL){
            type = Type.BOOL;
        }
        return type;
    }

    public Type doubleExpressionOperation(String operation, ExprOpNode expr1, ExprOpNode expr2){
        Type type = null;

        boolean arithOpCheck = operation.equals("PLUS") || operation.equals("TIMES") || operation.equals("MINUS") || operation.equals("DIV");
        boolean boolOpCheck = operation.equals("AND") || operation.equals("OR");
        boolean relOpCheck = operation.equals("GT") || operation.equals("GE") || operation.equals("LT") || operation.equals("LE") || operation.equals("EQ") || operation.equals("NE");

        if( arithOpCheck && expr1.getReturnType() == Type.INT && expr2.getReturnType() == Type.INT) {
            type = Type.INT;
        } else if( arithOpCheck && expr1.getReturnType() == Type.INT && expr2.getReturnType() == Type.DOUBLE) {
            type = Type.DOUBLE;
        } else if (arithOpCheck && expr1.getReturnType() == Type.DOUBLE && expr2.getReturnType() == Type.INT) {
            type = Type.DOUBLE;
        } else if ( arithOpCheck && expr1.getReturnType() == Type.DOUBLE && expr2.getReturnType() == Type.DOUBLE) {
            type = Type.DOUBLE;
        } else if (operation.equals("PLUS") && expr1.getReturnType() == Type.STRING && expr2.getReturnType() == Type.STRING) {
            type = Type.STRING;
        } else if ( boolOpCheck && expr1.getReturnType() == Type.BOOL && expr2.getReturnType() == Type.BOOL) {
            type = Type.BOOL;
        } else if ( relOpCheck && expr1.getReturnType() == Type.INT && expr2.getReturnType() == Type.INT) {
            type = Type.BOOL;
        } else if ( relOpCheck && expr1.getReturnType() == Type.DOUBLE && expr2.getReturnType() == Type.INT) {
            type = Type.BOOL;
        } else if ( relOpCheck && expr1.getReturnType() == Type.INT && expr2.getReturnType() == Type.DOUBLE) {
            type = Type.BOOL;
        } else if ( relOpCheck && expr1.getReturnType() == Type.DOUBLE && expr2.getReturnType() == Type.DOUBLE) {
            type = Type.BOOL;
        }

        return type;
    }

}
