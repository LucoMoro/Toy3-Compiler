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

import java.io.FileWriter;
import java.util.ArrayList;

public class TranslationVisitor implements Visitor{

    @Override
    public Object visit(ProgramNode node) {
        StringBuilder builder = new StringBuilder();

        builder.append(buildHeader());

        ArrayList<DeclOpNode> decls = node.getDecls();

        String prototype;
        //needed for the definition fo the prototypes
        if(decls != null){
            for(DeclOpNode decl : decls){
                if(decl instanceof DefDeclNode){
                    builder.append(createPrototype((DefDeclNode) decl));
                }
            }
        }

        //needed for the declarations
        if(decls != null){
            for(DeclOpNode decl : decls){
                builder.append(decl.accept(this));
            }
        }

        builder.append("\n\nint main() {\n");

        ArrayList<VarDeclNode> vars = node.getVars();
        if(vars != null){
            for(VarDeclNode var : vars){
                builder.append(var.accept(this));
            }
        }

        ArrayList<StatOpNode> stats = node.getStats();
        if(stats != null){
            for(StatOpNode stat: stats){
                builder.append(stat.accept(this));
            }
        }

        builder.append("}\n");

        return builder.toString();
    }

    @Override
    public Object visit(DefDeclNode node) {
        return null;
    }

    @Override
    public Object visit(ParDeclNode node) {
        StringBuilder builder = new StringBuilder();

        ArrayList<PVarNode> pVars = node.getLeft();

        if(pVars != null){
            for(int i = pVars.size() -1 ; i >= 0; i--){ //needed to reverse the parameters take in input
                PVarNode pVar =  pVars.get(i);
                builder.append(getCType(node.getRight())).append(" ");
                if(pVar.getHasRef()){
                    builder.append("*");
                }
                builder.append(pVar.accept(this)).append(", ");
            }
        }

        return builder.toString();
    }

    @Override
    public Object visit(PVarNode node) {
        StringBuilder builder = new StringBuilder();

        builder.append(node.getVariable().getValue());

        return builder.toString();
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

    /**
     * Converts a custom Type enumeration to its equivalent C type as a string.
     *
     * @param type the Type enumeration value to convert. If null, the method returns "void".
     *             Possible Type values include:
     *             - STRING: maps to "char*"
     *             - CHAR: maps to "char"
     *             - DOUBLE: maps to "float"
     *             - INT and BOOL: both map to "int"
     *             - Any other type returns an empty string.
     * @return a string representing the corresponding C type for the given Type enumeration.
     */
    public String getCType(Type type) {
        String output = "";

        if(type == null){
            return "void";
        }

        switch(type){
            case STRING:
                output = "char*";
                break;

            case CHAR:
                output = "char";
                break;

            case DOUBLE:
                output = "float";
                break;

            case INT, BOOL:
                output = "int";
                break;

            default:
                output = "";
                break;
        }

        return output;
    }

    /**
     * Builds a standard C header string that includes common standard library headers
     * and defines a macro for buffer size.
     *
     * The header includes:
     * - Standard I/O functions (`#include <stdio.h>`)
     * - Memory management functions (`#include <stdlib.h>`)
     * - String manipulation functions (`#include <string.h>`)
     * - Mathematical functions (`#include <math.h>`)
     *
     * Additionally, a `BUFFER_SIZE` macro is defined as `1024*4`.
     *
     * @return a string containing the generated C header.
     */
    public String buildHeader(){
        StringBuilder header = new StringBuilder();

        header.append("""
            #include <stdio.h>
            #include <stdlib.h>
            #include <string.h>
            #include <math.h>
            
            #define BUFFER_SIZE  1024*4
        
            """);

        return header.toString();
    }


    public String createPrototype(DefDeclNode node){
        StringBuilder buildPrototype = new StringBuilder();

        if (node.getType() == null) {
            buildPrototype.append("void ");
        }
        else if (node.getType() == Type.STRING) {
            buildPrototype.append("char* ");
        }
        else {
            buildPrototype.append(getCType(node.getType())).append(" ");
        }
        buildPrototype.append(node.getName().getValue()).append("_fun (");

        ArrayList<ParDeclNode> params = node.getParams();
        if(params != null){
            for(int i = params.size() -1; i >= 0; i--) {//iterates in reverse on each parDecl which may be composed by multiple pVars
                buildPrototype.append(params.get(i).accept(this));
            }
            if (!buildPrototype.isEmpty()) { //instead of calculating the number of parameters, I simply delete the last "," and the last " "
                buildPrototype.deleteCharAt(buildPrototype.length() - 1);
                buildPrototype.deleteCharAt(buildPrototype.length() - 1);
            }
        }

        buildPrototype.append(")");

        buildPrototype.append(";").append("\n");
        return buildPrototype.toString();
    }
}
