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
                    type = new VariableType(((VarDeclNode) decl).getType());

                    SymbolTableRow row = new SymbolTableRow(name, kind, type);
                    programTable.addRow(row);
                }
            }
            decl.accept(this);
        }

        System.out.println(programTable);

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

        node.setTable(programTable);
        return node;
    }

    @Override
    public Object visit(DefDeclNode node) {

        return node;
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

}
