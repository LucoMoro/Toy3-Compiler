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
import esercitazione4.visitor.symbolTable.Firm;
import esercitazione4.visitor.symbolTable.SymbolTable;
import esercitazione4.visitor.symbolTable.SymbolTableRow;
import esercitazione4.visitor.symbolTable.VariableType;

import java.util.ArrayList;
import java.util.Stack;

public class TypeCheckerVisitor implements  Visitor{

    private Stack<SymbolTable> typeEnvironment = new Stack<>();

    /* Program */
    @Override
    public Object visit(ProgramNode node) {

        typeEnvironment.add(node.getProgramTable());
        System.out.println("ProgramNode: " + node.getProgramTable());

        ArrayList<DeclOpNode> decls = node.getDecls();
        if(decls != null){
            for(DeclOpNode decl : decls){
                decl.accept(this);
            }
        }

        typeEnvironment.add(node.getBegindEndTable());
        System.out.println("BeginEndTable: " + node.getBegindEndTable());

        ArrayList<VarDeclNode> vars = node.getVars();
        if(vars != null){
            for(VarDeclNode var : vars){
                var.accept(this);
            }
        }

        /*ArrayList<StatOpNode> stats = node.getStats(); //todo add check for list of statements
        if(stats != null){
            for(StatOpNode stat : stats){
                stat.accept(this);
            }
        }*/

        ArrayList<StatOpNode> stats = node.getStats();
        if(stats != null){
            for(StatOpNode stat : stats){ //checks for each statement if his type is NOTYPE
                Type tmpStatType = (Type) stat.accept(this); //temporary variable that contains the type of stat
                tmpStatType = Type.NOTYPE; //todo remove when all the StatOpNode will be implemented
                if(tmpStatType != Type.NOTYPE){
                    throw new RuntimeException("The current statement: " + stat + "has a wrong type"); //this code should be not reachable since if there is an error
                    //it would be caught before arriving to BodyNode
                }
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
        System.out.println("DefDecl: " + node.getTable());

        ArrayList<ParDeclNode> pars = node.getParams();
        if(pars != null){
            for(ParDeclNode par : pars){
                par.accept(this);
            }
        }

        checkReturnType(node); //could be implemented after the accept()

        //function body
        BodyNode body = node.getBody();
        ArrayList<VarDeclNode> vars = body.getLeft();
        if(vars != null){
            for(VarDeclNode var : vars){
                var.accept(this);
            }
        }

        ArrayList<StatOpNode> stats = body.getRight();

        if(stats != null){
            for(StatOpNode stat : stats){ //checks for each statement if his type is NOTYPE
                Type tmpStatType = (Type) stat.accept(this); //temporary variable that contains the type of stat
                tmpStatType = Type.NOTYPE; //todo remove when all the StatOpNode will be implemented
                if(tmpStatType != Type.NOTYPE){
                    throw new RuntimeException("The current statement: " + stat + "has a wrong type"); //this code should be not reachable since if there is an error
                    //it would be caught before arriving to BodyNode
                }
            }
        }

        body.setReturnType(Type.NOTYPE);

        typeEnvironment.pop();
        node.setReturnType(Type.NOTYPE);

        return Type.NOTYPE;
    }

    /* VarDecls */
    @Override
    public Object visit(VarOptInitNode node) {

        //IdNode id = node.getIdentifier();
        //id.accept(this);

        ExprOpNode expr = node.getExpression();
        if(expr != null){
            expr.accept(this);
            node.setReturnType(expr.getReturnType()); //todo change in node.setReturnType(expr.getReturnType()); when the method will be implemented
        } else {
            node.setReturnType(null); //in this case it has to be null since there is no expression to check
        }

        return node.getReturnType();
    }
    @Override
    public Object visit(VarDeclNode node) {

        ArrayList<VarOptInitNode> optVars = node.getVars();

        if(optVars != null){
            for(VarOptInitNode optVar : optVars){
                Type optVarType = (Type) optVar.accept(this);
                //System.out.println("Check on optVarType The variable " + optVar.getIdentifier().getValue() + " has type " + optVarType);
                if(optVar.getExpression() != null){ //further checks are useless if the variable has not been initialized
                    if(node.getType() != null){ //VarDecl could be initialized with a type or a constant, so a check is needed
                        if(optVarType != node.getType()){
                            throw new RuntimeException("The variable '" + optVar.getIdentifier().getValue() +
                                    "' initialized with: " + optVar.getExpression() + " (" + optVar.getExpression().getReturnType() + ") does not match the declaration type: " + node.getType());
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

        node.setReturnType(Type.NOTYPE);

        return node.getReturnType();
    }

    /* PVar */
    @Override
    public Object visit(PVarNode node) {

        IdNode id = node.getVariable();
        //id.accept(this);

        node.setReturnType(id.getReturnType());

        return id.getReturnType();
    }
    @Override
    public Object visit(ParDeclNode node) {

        ArrayList<PVarNode> pVars = node.getLeft();
        if(pVars != null){
            for(PVarNode pVar : pVars) {
                pVar.accept(this);
            }
        }

        node.setReturnType(node.getRight());

        return node.getRight();
    }

    /* Body */
    @Override
    public Object visit(BodyNode node) {

        typeEnvironment.add(node.getTable());

        ArrayList<VarDeclNode> vars = node.getLeft();
        if(vars != null){
            for(VarDeclNode var : vars){
                var.accept(this);
            }
        }

        ArrayList<StatOpNode> stats = node.getRight();

        if(stats != null){
            for(StatOpNode stat : stats){
                Type tmpStatType = (Type) stat.accept(this); //temporary variable that contains the type of stat
                tmpStatType = Type.NOTYPE; //todo remove when all the StatOpNode will be implemented
                if(tmpStatType != Type.NOTYPE){
                    throw new RuntimeException("The current statement: " + stat + "has a wrong type"); //this code should be not reachable since if there is an error
                                                                                                       //it would be caught before arriving to BodyNode
                }
            }
        }

        typeEnvironment.pop();
        node.setReturnType(Type.NOTYPE);

        return node.getReturnType();
    }

    @Override
    public Object visit(IdNode node) {

        Type tmpType = lookUpVariable(typeEnvironment, node);
        node.setReturnType(tmpType);
        if(node.getReturnType() == null) {
            throw new RuntimeException("The variable: '" + node.getValue() + "' has not been declared");
        }

        return node.getReturnType();
    }

    /* Arithmetic Operators */
    @Override
    public Object visit(AddNode node) {

        ExprOpNode expr1 = (ExprOpNode) node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = (ExprOpNode) node.getRight();
        expr2.accept(this);

        Type exprType = this.doubleExpressionOperation("PLUS", expr1, expr2);

        node.setReturnType(exprType);

        return exprType;
    }
    @Override
    public Object visit(DiffNode node) {

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        Type exprType = this.doubleExpressionOperation("MINUS", expr1, expr2);

        node.setReturnType(exprType);

        return exprType;
    }
    @Override
    public Object visit(MulNode node) {

        ExprOpNode expr1 = (ExprOpNode) node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        Type exprType = this.doubleExpressionOperation("TIMES", expr1, expr2);

        node.setReturnType(exprType);

        return exprType;
    }
    @Override
    public Object visit(DivNode node) {

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        Type exprType = this.doubleExpressionOperation("DIV", expr1, expr2);

        node.setReturnType(exprType);

        return exprType;
    }
    @Override
    public Object visit(UMinusNode node) {

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        Type exprType = this.singleExpressionOperation("UMINUS", expr1);

        node.setReturnType(exprType);

        return exprType;
    }

    /* Boolean Operators */
    @Override
    public Object visit(AndNode node) {

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        Type exprType = this.doubleExpressionOperation("AND", expr1, expr2);

        node.setReturnType(exprType); //change

        return exprType;
    }
    @Override
    public Object visit(OrNode node) {

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        Type exprType = this.doubleExpressionOperation("OR", expr1, expr2);

        node.setReturnType(exprType);

        return exprType;
    }
    @Override
    public Object visit(NotNode node) {

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        Type exprType = this.singleExpressionOperation("NOT", expr1);

        node.setReturnType(exprType);

        return exprType;
    }

    /* Relational Operators */
    @Override
    public Object visit(GTNode node) {

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        Type exprType = this.doubleExpressionOperation("GT", expr1, expr2);

        node.setReturnType(exprType);

        return exprType;
    }
    @Override
    public Object visit(GENode node) {

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        Type exprType = this.doubleExpressionOperation("GE", expr1, expr2);

        node.setReturnType(exprType);

        return exprType;
    }
    @Override
    public Object visit(LTNode node) {

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        Type exprType = this.doubleExpressionOperation("LT", expr1, expr2);

        node.setReturnType(exprType);

        return exprType;
    }
    @Override
    public Object visit(LENode node) {

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        Type exprType = this.doubleExpressionOperation("LE", expr1, expr2);

        node.setReturnType(exprType);

        return exprType;
    }
    @Override
    public Object visit(EQNode node) {

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        Type exprType = this.doubleExpressionOperation("EQ", expr1, expr2);

        node.setReturnType(exprType);

        return exprType;
    }
    @Override
    public Object visit(NENode node) {

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        Type exprType = this.doubleExpressionOperation("NE", expr1, expr2);

        node.setReturnType(exprType);

        return exprType;
    }


    /* Constants */
    @Override
    public Object visit(BoolNode node) {

        node.setReturnType(Type.BOOL);

        return node.getReturnType();
    }
    @Override
    public Object visit(CharNode node) {

        node.setReturnType(Type.CHAR);

        return node.getReturnType();
    }
    @Override
    public Object visit(IntNode node) {

        node.setReturnType(Type.INT);

        return node.getReturnType();
    }
    @Override
    public Object visit(DoubleNode node) {

        node.setReturnType(Type.DOUBLE);

        return node.getReturnType();
    }
    @Override
    public Object visit(StringNode node) {

        node.setReturnType(Type.STRING);

        return node.getReturnType();
    }

    @Override
    public Object visit(FunCallNode node) {

        //x() x.accept() -> lookup(x kind = "function/ procedure")
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

    /**
    * checks if the function is a procedure or a function and, in case it is a function,
     * if it has at least one return statement
    */
    public void checkReturnType(DefDeclNode node){
        boolean returnFlag = false;

        ArrayList<StatOpNode> stats = node.getBody().getRight();

        Type functionType = node.getType();
        if(functionType == null){ //procedure
            if(stats != null){
                for(StatOpNode stat : stats){
                    if(stat instanceof ReturnOpNode){
                        throw new RuntimeException("The procedure " + node.getName().getValue() + " should not contain a return statement");
                    }
                }
            }
        } else { //function
            if(stats != null){
                for(StatOpNode stat : stats){
                    if(stat instanceof ReturnOpNode){ //searches for the return statement
                        returnFlag = true;
                        ReturnOpNode tmpStat = (ReturnOpNode) stat; //if the return statement is found I create a temporary statemnt
                        Type tmpType = null;
                        if(tmpStat.getReturnType() == null){//checks if the returnType is null todo check, after the implementation of ReturnOpNode, if this check is usefull
                            tmpType = (Type) tmpStat.getExpression().accept(this);
                        }
                        if(tmpType != functionType){
                            throw new RuntimeException("The return type for '" + node.getName().getValue() +"' is incorrect: expected: " + functionType + " and got " + tmpType);
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

        if (operation.equals("UMINUS") && expr1.getReturnType() == Type.INT ){
            type = Type.INT;
        } else if (operation.equals("UMINUS") && expr1.getReturnType() == Type.DOUBLE){
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
        } else {
            throw new RuntimeException("The expressions: '" + expr1 + "' and '" + expr2 + "' do not have a match in the table");
        }

        return type;
    }

    public Type lookUpVariable(Stack<SymbolTable> typeEnvironment, IdNode node){
        Type variableType=null;

        Stack<SymbolTable> clonedTypeEnvironment;
        clonedTypeEnvironment = cloneTypeEnvironment(typeEnvironment);

            if(clonedTypeEnvironment != null){
                for(SymbolTable clonedSymbolTable : clonedTypeEnvironment){
                    if(clonedSymbolTable.contains(node, "variable")){ //todo can be refactored
                        SymbolTableRow row = clonedSymbolTable.getRow(node, "variable");
                            variableType = row.getType().getSingleType();
                        return variableType;
                    }
                }
            }

        return variableType;
    }

    public Stack<SymbolTable> cloneTypeEnvironment(Stack<SymbolTable> typeEnvironment){
        Stack<SymbolTable> clonedStack = new Stack<SymbolTable>();

        for(SymbolTable currSymbolTable: typeEnvironment){
            clonedStack.push(currSymbolTable);
        }

        return clonedStack;
    }

}
