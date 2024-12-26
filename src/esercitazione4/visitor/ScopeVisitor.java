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
import esercitazione4.visitor.symbolTable.*;

import java.util.ArrayList;
import java.util.Stack;

public class ScopeVisitor implements Visitor{
    private SymbolTable table;
    private Stack<SymbolTable> typeEnvironment = new Stack<>();
    private String bodyName=""; //used to give a name to the body table based on his parent

    /* Program */
    @Override
    public Object visit(ProgramNode node) {

        SymbolTable programTable = new SymbolTable("ProgramTable");
        typeEnvironment.add(programTable);

        ArrayList<DeclOpNode> decls = node.getDecls();

        //iterates on each declaration and adds the element as a row of the scoping table of Program
        for (DeclOpNode decl : decls){
            String name;
            String kind;
            Firm type; //variable used to indicate the firm of the method or the type of the function
            Type return_type = null;
            ArrayList<Type> inputs_type = new ArrayList<>(); //variable used to temporarily contain the parameters' type of the function
            if(decl instanceof DefDeclNode){
                name = ((DefDeclNode) decl).getName().getValue(); //name will be equal to the function name
                if(((DefDeclNode) decl).getType() == null){ //checks if the function has a return type
                    kind = "procedure";
                }else {
                    kind = "function";
                    return_type = ((DefDeclNode) decl).getType();
                }
                if(((DefDeclNode) decl).getParams() == null){ //checks if the function has parameters
                    inputs_type = null;
                }else {
                    for(ParDeclNode par : ((DefDeclNode) decl).getParams()){ //gets the type of each parameter
                        inputs_type.add(0, par.getRight());
                    }
                }

                type = new FunctionType(inputs_type, return_type);

                SymbolTableRow row = new SymbolTableRow(name, kind, type);
                programTable.addRow(row);

            } else if (decl instanceof VarDeclNode) {
                for(VarOptInitNode var : ((VarDeclNode) decl).getVars()){ //there could be defined multiple variables together
                    name = var.getIdentifier().getValue();
                    kind = "variable";

                    if(((VarDeclNode) decl).getType() == null){ //used to check if the variable is initialized with a constant or a type
                        boolean check = checkVarDecl((VarDeclNode) decl); //checks if there is only one unititialized variable
                        if(check) {
                            type = new VariableType(((VarDeclNode) decl).getConstant());
                        } else {
                            throw new RuntimeException("Incorrect variables declaration in " + node );
                        }
                    } else {
                        type = new VariableType(((VarDeclNode) decl).getType());
                    }

                    SymbolTableRow row = new SymbolTableRow(name, kind, type);
                    programTable.addRow(row);
                }
            }
            decl.accept(this); //propagates the accept() to the declarations contained in program-begin body
        }

        node.setProgramTable(programTable);
        System.out.println(programTable);

        //Start of the begin-end body
        ArrayList<VarDeclNode> vars = node.getVars();


        SymbolTable beginEndTable = new SymbolTable("Begin-End");
        typeEnvironment.add(beginEndTable);

        if(vars != null){
            for(VarDeclNode var : vars){
                String name;
                String kind;
                Firm type;
                for(VarOptInitNode optVar : var.getVars()){//there could be defined multiple variables together
                    name = optVar.getIdentifier().getValue();
                    kind = "variable";

                    if(var.getType() == null){ //used to check if the variable is initialized with a constant or a type
                        boolean check = checkVarDecl(var); //checks if there is only one unititialized variable
                        if(check) {
                            type = new VariableType(var.getConstant());
                        } else {
                            throw new RuntimeException("Incorrect variables declaration in " + node );
                        }
                    } else {
                        type = new VariableType(var.getType());
                    }

                    SymbolTableRow row = new SymbolTableRow(name, kind, type); //creates a new row (one for each variable defined).
                                                                               // Multiple definitions on the same line are counted as separated rows
                    beginEndTable.addRow(row);
                }

                var.accept(this);
            }
        }

        ArrayList<StatOpNode> stats = node.getStats();
        if(stats != null){
            for(StatOpNode stat : stats){
                stat.accept(this);
            }
        }

        node.setBegindEndTable(beginEndTable);
        System.out.println(beginEndTable);

        typeEnvironment.pop();
        typeEnvironment.pop();

        return node;
    }

    /* DefDecl */
    //todo change the "ref" position from scoping table of the method to firm of the method
    @Override
    public Object visit(DefDeclNode node) {

        SymbolTable defDeclTable = new SymbolTable("DefDecl(" +node.getName().getValue()+")");
        typeEnvironment.add(defDeclTable);

        IdNode id = node.getName();
        id.accept(this);

        String name;
        String kind;
        Firm type;
        ArrayList<ParDeclNode> pars = node.getParams();

        if(pars != null){
           for(ParDeclNode par : pars){
               ArrayList<PVarNode> pvars = par.getLeft();
               for(PVarNode pvar : pvars){
                   name = pvar.getVariable().getValue();
                   kind = "variable";
                   type = new VariableType(par.getRight());

                   SymbolTableRow row = new SymbolTableRow(name, kind, type, "ref: " +pvar.getHasRef());
                   defDeclTable.addRow(row);
               }
               par.accept(this);
           }
        }

        ArrayList<VarDeclNode> vars = node.getBody().getLeft();
        if(vars != null){
            for(VarDeclNode var : vars){
                ArrayList<VarOptInitNode> optvars = var.getVars();
                for(VarOptInitNode optvar : optvars){
                    name = optvar.getIdentifier().getValue();
                    kind = "variable";

                    if(var.getType() == null){ //used to check if the variable is initialized with a constant or a type
                        boolean check = checkVarDecl(var); //checks if there is only one unititialized variable
                        if(check) {
                            type = new VariableType(var.getConstant());
                        } else {
                            throw new RuntimeException("Incorrect variables declaration in " + node );
                        }
                    } else {
                        type = new VariableType(var.getType());
                    }

                    SymbolTableRow row = new SymbolTableRow(name, kind, type);
                    defDeclTable.addRow(row);
                }
                var.accept(this);
            }
        }

        ArrayList<StatOpNode> stats = node.getBody().getRight();
        if(stats != null) {
            for(StatOpNode stat : stats){
                stat.accept(this);
            }
        }

        node.setTable(defDeclTable);
        System.out.println(defDeclTable);
        typeEnvironment.pop();

        return node;
    }

    /* VarDecls */
    @Override
    public Object visit(VarOptInitNode node) {

        node.setTable(typeEnvironment.peek());

        IdNode id = node.getIdentifier();
        id.accept(this);

        ExprOpNode expr = node.getExpression();
        if(expr != null){
            expr.accept(this);
        }
        return node;
    }
    @Override
    public Object visit(VarDeclNode node) {

        node.setTable(typeEnvironment.peek());

        ArrayList<VarOptInitNode> optVars = node.getVars();
        if(optVars != null){
            for(VarOptInitNode optVar: optVars){
                optVar.accept(this);
            }
        }

        ConstantNode constant = node.getConstant();
        if(constant != null){
            constant.accept(this);
        }

        return node;
    }

    /* PVar */
    @Override
    public Object visit(PVarNode node) {

        node.setTable(typeEnvironment.peek());

        IdNode id = node.getVariable();
        id.accept(this);
        return node;
    }
    @Override
    public Object visit(ParDeclNode node) {

        node.setTable(typeEnvironment.peek());

        ArrayList<PVarNode> pvars = node.getLeft();
        if(pvars != null){
            for(PVarNode pvar : pvars){
                pvar.accept(this);
            }
        }

        return node;
    }

    /* Body */
    @Override
    public Object visit(BodyNode node) {

        //add peek to change the name based on the called instruction
        SymbolTable bodyTable = new SymbolTable(bodyName);
        typeEnvironment.add(bodyTable);

        String name;
        String kind;
        Firm type;

        ArrayList<VarDeclNode> vars = node.getLeft();
        if(vars != null){
            for(VarDeclNode var: vars){ //todo refactor and extract this method
                ArrayList<VarOptInitNode> optVars = var.getVars();
                for(VarOptInitNode optVar : optVars){
                    name = optVar.getIdentifier().getValue();
                    kind = "variable";

                    if(var.getType() == null){ //used to check if the variable is initialized with a constant or a type
                        boolean check = checkVarDecl(var); //checks if there is only one unititialized variable
                        if(check) {
                            type = new VariableType(var.getConstant());
                        } else {
                            throw new RuntimeException("Incorrect variables declaration in " + node );
                        }
                    } else {
                        type = new VariableType(var.getType());
                    }

                    SymbolTableRow row = new SymbolTableRow(name, kind, type);
                    bodyTable.addRow(row);

                }

                var.accept(this);
            }
        }

        ArrayList<StatOpNode> stats = node.getRight();
        if(stats != null){
            for(StatOpNode stat: stats){
                stat.accept(this);
            }
        }

        node.setTable(bodyTable);
        System.out.println(bodyTable);
        typeEnvironment.pop();

        return node;
    }

    /* Statements */
    @Override
    public Object visit(ReadOpNode node) {

        node.setTable(typeEnvironment.peek());
        ArrayList<ExprOpNode> exprs = node.getIdentifiers();
        if(exprs != null){
            for(ExprOpNode expr: exprs){
                expr.accept(this);
            }
        }

        return node;
    }
    @Override
    public Object visit(WriteOpNode node) {

        node.setTable(typeEnvironment.peek());

        ArrayList<ExprOpNode> exprs = node.getExpressions();
        if(exprs != null){
            for(ExprOpNode expr: exprs){
                expr.accept(this);
            }
        }
        return node;
    }
    @Override
    public Object visit(AssignOpNode node) {

        node.setTable(typeEnvironment.peek());

        ArrayList<IdNode> ids = node.getIdentifiers();
        for(IdNode id: ids){
            id.accept(this);
        }

        ArrayList<ExprOpNode> exprs = node.getExpressions();
        for(ExprOpNode expr: exprs){
            expr.accept(this);
        }

        return node;
    }
    @Override
    public Object visit(ReturnOpNode node) {

        node.setTable(typeEnvironment.peek());

        ExprOpNode expr = node.getExpression();
        expr.accept(this);
        return node;
    }
    @Override
    public Object visit(IfThenElseNode node) {

        SymbolTable ifThenElseTable = new SymbolTable("IfThenElse");
        typeEnvironment.add(ifThenElseTable);

        ExprOpNode expr = node.getLeft();
        expr.accept(this);

        BodyNode ifThenBody = node.getMid();
        bodyName = "ifThenBody";
        ifThenBody.accept(this);


        BodyNode elseBody = node.getRight();
        bodyName = "ElseBody";
        elseBody.accept(this);

        System.out.println(ifThenElseTable);
        node.setTable(ifThenElseTable);
        typeEnvironment.pop();

        return node;
    }
    @Override
    public Object visit(IfThenNode node) {

        SymbolTable ifThenTable = new SymbolTable("IfThen");
        typeEnvironment.add(ifThenTable);

        ExprOpNode expr = node.getLeft();
        expr.accept(this);

        BodyNode body = node.getRight();
        bodyName = "IfThenBody";
        body.accept(this);

        System.out.println(ifThenTable);
        node.setTable(ifThenTable);
        typeEnvironment.pop();

        return node;
    }
    @Override
    public Object visit(WhileNode node) {

        SymbolTable whileTable = new SymbolTable("While");
        typeEnvironment.add(whileTable);

        ExprOpNode expr = node.getLeft();
        expr.accept(this);

        BodyNode body = node.getRight();
        bodyName = "WhileBody";
        body.accept(this);

        System.out.println(whileTable);
        node.setTable(whileTable);
        typeEnvironment.pop();

        return node;
    }
    @Override
    public Object visit(FunCallNode node) {

        node.setTable(typeEnvironment.peek());

        IdNode id = node.getName();
        id.accept(this);

        ArrayList<ExprOpNode> exprs = node.getParameters();
        if(exprs != null){
            for(ExprOpNode expr : exprs){
                expr.accept(this);
            }
        }

        return node;
    }

    /* Identifier */
    @Override
    public Object visit(IdNode node) {

        node.setTable(typeEnvironment.peek());

        return node;
    }

    /* Constants */
    @Override
    public Object visit(BoolNode node) {

        node.setTable(typeEnvironment.peek());

        return node;
    }
    @Override
    public Object visit(CharNode node) {

        node.setTable(typeEnvironment.peek());
        return node;
    }
    @Override
    public Object visit(IntNode node) {

        node.setTable(typeEnvironment.peek());
        return node;
    }
    @Override
    public Object visit(DoubleNode node) {

        node.setTable(typeEnvironment.peek());
        return node;
    }
    @Override
    public Object visit(StringNode node) {

        node.setTable(typeEnvironment.peek());
        return node;
    }

    /* Arithmetic Operators */
    @Override
    public Object visit(AddNode node) {

        node.setTable(typeEnvironment.peek());

        ExprOpNode expr1 = (ExprOpNode) node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = (ExprOpNode) node.getRight();
        expr2.accept(this);

        return node;
    }
    @Override
    public Object visit(DiffNode node) {

        node.setTable(typeEnvironment.peek());

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        return node;
    }
    @Override
    public Object visit(MulNode node) {

        node.setTable(typeEnvironment.peek());

        ExprOpNode expr1 = (ExprOpNode) node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        return node;
    }
    @Override
    public Object visit(DivNode node) {

        node.setTable(typeEnvironment.peek());

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        return node;
    }
    @Override
    public Object visit(UMinusNode node) {

        node.setTable(typeEnvironment.peek());

        ExprOpNode expr = node.getLeft();
        expr.accept(this);

        return node;
    }

    /* Boolean Operators */
    @Override
    public Object visit(AndNode node) {

        node.setTable(typeEnvironment.peek());

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        return node;
    }
    @Override
    public Object visit(OrNode node) {

        node.setTable(typeEnvironment.peek());

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        return node;
    }
    @Override
    public Object visit(NotNode node) {

        node.setTable(typeEnvironment.peek());

        ExprOpNode expr = node.getLeft();
        expr.accept(this);

        return node;
    }

    /* Relational Operators */
    @Override
    public Object visit(GTNode node) {

        node.setTable(typeEnvironment.peek());

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        return node;
    }
    @Override
    public Object visit(GENode node) {

        node.setTable(typeEnvironment.peek());

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        return node;
    }
    @Override
    public Object visit(LTNode node) {

        node.setTable(typeEnvironment.peek());

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        return node;
    }
    @Override
    public Object visit(LENode node) {

        node.setTable(typeEnvironment.peek());

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        return node;
    }
    @Override
    public Object visit(EQNode node) {

        node.setTable(typeEnvironment.peek());

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        return node;
    }
    @Override
    public Object visit(NENode node) {

        node.setTable(typeEnvironment.peek());

        ExprOpNode expr1 = node.getLeft();
        expr1.accept(this);

        ExprOpNode expr2 = node.getRight();
        expr2.accept(this);

        return node;
    }


    /**
     * Checks if there is only one variable that is not initialized. This rule
     * applies only when the variable is defined using a constant, not a type.
     *
     *
     * Args:
     *     var (VarDeclNode): The variable declaration node, representing the
     *                        list of variables initialized with a constant.
     */
    //todo check if it is necessary to modify it in order to catch declarations such as 6*7
    public boolean checkVarDecl(VarDeclNode var){

        if(var.getVars().size() == 1){
            VarOptInitNode varOpt  = var.getVars().get(0);
            if(varOpt.getExpression() == null) {
                return true; //correct
            } else {
                return false;
            }
        }
        return false;
    }
}
