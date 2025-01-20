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
import esercitazione4.visitor.symbolTable.FunctionType;
import esercitazione4.visitor.symbolTable.SymbolTable;
import esercitazione4.visitor.symbolTable.SymbolTableRow;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;


public class TranslationVisitor implements Visitor{

    HashMap<String, ArrayList<Boolean>> firms = new HashMap<>();

    @Override
    public Object visit(ProgramNode node) {
        StringBuilder builder = new StringBuilder();
        setupFirms(node); //adds elements to the HashMap firms

        builder.append(buildHeader());

        ArrayList<DeclOpNode> decls = node.getDecls();

        //needed for the definition fo the prototypes
        if(decls != null){
            for(DeclOpNode decl : decls){
                if(decl instanceof DefDeclNode){
                    builder.append(createPrototype((DefDeclNode) decl));
                }
            }
        }

        builder.append("\n\n");

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
                if(stat instanceof FunCallNode){
                    builder.append(";").append("\n");
                }
            }
        }

        builder.append("}\n");
        return builder.toString();
    }

    @Override
    public Object visit(DefDeclNode node) {
        StringBuilder builder = new StringBuilder();

        builder.append(createFirm(node));

        BodyNode body = node.getBody();
        builder.append(body.accept(this)); //in this case, differently from the others visitors I demand the workload to the body

        builder.append("\n");
        return builder.toString();
    }

    @Override
    public Object visit(ParDeclNode node) {
        StringBuilder builder = new StringBuilder();

        ArrayList<PVarNode> pVars = node.getLeft();

        if(pVars != null){
            for(int i = pVars.size() -1 ; i >= 0; i--){ //needed to reverse the parameters take in input
                PVarNode pVar =  pVars.get(i);
                builder.append(getCType(node.getRight())).append(" ");
                if(pVar.getHasRef() && node.getRight() != Type.STRING){ //needed in order to avoid strings such as "char* * message"
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

        builder.append(node.getVariable().accept(this));

        return builder.toString();
    }


    @Override
    public Object visit(VarDeclNode node) {
        StringBuilder builder = new StringBuilder();

        ArrayList<VarOptInitNode> optVars = node.getVars();
        if(optVars != null){

        if(node.getType() == null){ //checks if the variable has a constant or a type
                Type tmpType = Type.convertType(node.getConstant());
                builder.append(getCType(tmpType)).append(" ");
                builder.append(optVars.get(0).accept(this)); //if there is a constant, the declaration can have only 1 variable so a loop is useless
                builder.append(" = ");
                builder.append(node.getConstant().accept(this));
        }else {
             builder.append(getCType(node.getType())).append(" ");
             for(int i = optVars.size()-1; i>= 0; i--){
                 VarOptInitNode optVar = optVars.get(i);
                 if(i == 0){ //needed in order to avoid the symbol "," on the last variable
                     builder.append(optVar.accept(this));
                 } else {
                     builder.append(optVar.accept(this)).append(", ");
                        }
                    }
                }
        }

        builder.append(";").append("\n");
        return builder.toString();
    }

    @Override
    public Object visit(VarOptInitNode node) {
        StringBuilder builder = new StringBuilder();

        builder.append(node.getIdentifier().accept(this));

        ExprOpNode expr = node.getExpression();
        if(expr != null) {
            builder.append(" = ");
            builder.append(expr.accept(this));
        }

        return builder.toString();
    }

    @Override
    public Object visit(IdNode node) {
        StringBuilder builder = new StringBuilder();

        builder.append(node.getValue());

        return builder.toString();
    }

    @Override
    public Object visit(BodyNode node) {
        StringBuilder builder = new StringBuilder();

        builder.append(" {").append("\n");

        ArrayList<VarDeclNode> vars = node.getLeft();
        if(vars != null){
            for(VarDeclNode var : vars ){
                builder.append(var.accept(this));
            }
        }

        ArrayList<StatOpNode> stats = node.getRight();
        if(stats != null){
            for(StatOpNode stat : stats){
                builder.append(stat.accept(this));
                if(stat instanceof FunCallNode){ //needed since FunCallNode can be used as both parameter or statement itself
                    builder.append(";").append("\n");
                }
            }
        }

        builder.append("}").append("\n");
        return builder.toString();
    }

    @Override
    public Object visit(ReadOpNode node) {
        StringBuilder builder = new StringBuilder();

        for(ExprOpNode expr:  node.getIdentifiers()) {
            if(expr instanceof IdNode){
                IdNode id = (IdNode) expr;

                Type type = id.getReturnType();
                String variableName = (String) id.accept(this);

                if(type.name().equalsIgnoreCase(Type.STRING.name())) {

                    String buffer = "buffer = (char*) malloc((1024*5)*sizeof(char));\n";
                    builder.append(buffer);
                    builder.append("scanf(\"%s\", buffer);\n");
                    String alloc = variableName + "= (char*) malloc((strlen(buffer) + 1) *sizeof(char));\n";
                    alloc = alloc + "strcpy(" + variableName + ",buffer);\nfree(buffer);\n";
                    builder.append(alloc);
                }

                if(type.name().equalsIgnoreCase(Type.BOOL.name())
                        || type.name().equalsIgnoreCase(Type.INT.name())) {
                    builder.append("scanf(\"%d\", ").append("&").append(variableName).append(");");
                }

                if(type.name().equalsIgnoreCase(Type.DOUBLE.name())) {
                    builder.append("scanf(\"%f\", ").append("&").append(variableName).append(");");
                }

                if(type.name().equalsIgnoreCase(Type.CHAR.name())) {
                    builder.append("scanf(\"%c\", ").append("&").append(variableName).append(");");
                }
            }

        }

        builder.append("\n");
        return builder.toString();
    }


    @Override
    public Object visit(WriteOpNode node) {
        StringBuilder builderPrint = new StringBuilder();
        StringBuilder builderArgs = new StringBuilder();

        builderPrint.append("printf(\"");
        builderArgs.append("");

        ArrayList<ExprOpNode> exprs = node.getExpressions();
        for(int i = exprs.size()-1; i>= 0; i-- ){
            ExprOpNode expr = exprs.get(i);

            String exprString = (String) expr.accept(this);

            Type type = expr.getReturnType();
            String specifier = getPrintSpecifier(type);

            builderPrint.append(specifier);//builds the string
            builderArgs.append(exprString).append(", "); //builds the list of arguments
        }

        builderArgs.deleteCharAt(builderArgs.length() - 1);
        builderArgs.deleteCharAt(builderArgs.length() - 1);

        if(node.getIsNewLine()){
            builderPrint.append("\\n");
        }

        builderPrint.append("\", ").append(builderArgs).append(")").append(";").append("\n");
        return builderPrint.toString();
    }

    @Override
    public Object visit(IfThenNode node) {
        StringBuilder builder = new StringBuilder();

        builder.append("if(");
        ExprOpNode expr = node.getLeft();
        builder.append(expr.accept(this));

        builder.append("} else {\n");
        BodyNode bodyElse = node.getRight();
        builder.append(bodyElse.accept(this));
        builder.append("}\n");

        return builder.toString();
    }

    @Override
    public Object visit(IfThenElseNode node) {
        StringBuilder builder = new StringBuilder();

        builder.append("if(");
        ExprOpNode expr = node.getLeft();
        builder.append(expr.accept(this));

        builder.append(") {\n");
        BodyNode bodyIfThen = node.getMid();
        builder.append(bodyIfThen.accept(this));

        builder.append("} else {\n");
        BodyNode bodyElse = node.getRight();
        builder.append(bodyElse.accept(this));
        builder.append("}\n");

        return builder.toString();
    }

    @Override
    public Object visit(WhileNode node) {
        StringBuilder builder = new StringBuilder();

        builder.append("while (");
        ExprOpNode expr = node.getLeft();
        builder.append(expr.accept(this));

        builder.append(") {\n");
        BodyNode body = node.getRight();
        builder.append(body.accept(this));
        builder.append("}\n");

        return builder.toString();
    }

    @Override
    public Object visit(BoolNode node) {
        StringBuilder builder = new StringBuilder();

        if(node.getValue() == true) {
            builder.append("1");
        } else {
            builder.append("0");
        }

        return builder.toString();
    }

    @Override
    public Object visit(CharNode node) {
        StringBuilder builder = new StringBuilder();

        builder.append("\'");
        builder.append(node.getValue());
        builder.append("\'");

        return builder.toString();
    }

    @Override
    public Object visit(IntNode node) {
        StringBuilder builder = new StringBuilder();

        builder.append(node.getValue());

        return builder.toString();
    }

    @Override
    public Object visit(DoubleNode node) {
        StringBuilder builder = new StringBuilder();

        builder.append(node.getValue());

        return builder.toString();
    }

    @Override
    public Object visit(StringNode node) {
        StringBuilder builder = new StringBuilder();

        builder.append("\"");
        builder.append(node.getValue());
        builder.append("\"");

        return builder.toString();
    }

    @Override
    public Object visit(ReturnOpNode node) {
        StringBuilder builder = new StringBuilder();

        ExprOpNode expr = node.getExpression();
        builder.append("return ");
        builder.append(expr.accept(this));

        builder.append(";").append("\n");
        return builder.toString();
    }

    @Override
    public Object visit(FunCallNode node) {
        StringBuilder builder = new StringBuilder();

        IdNode id = node.getName();
        builder.append(id.accept(this));

        ArrayList<Boolean> references = firms.get(id.getValue());

        builder.append("(");

        ArrayList<ExprOpNode> exprs = node.getParameters();

        if(exprs != null){
            for(int i = exprs.size() -1; i >= 0; i--){
                ExprOpNode expr = exprs.get(i);
                String exprContent = (String) expr.accept(this);
                if(references.get(i) == true && expr.getReturnType() != Type.STRING){
                    builder.append("&");
                }
                builder.append(exprContent);
                if(i != 0){
                    builder.append(", ");
                }
            }
        }

        builder.append(")");
        return builder.toString();
    }

    @Override
    public Object visit(AssignOpNode node) {
        StringBuilder builder = new StringBuilder();

        ArrayList<IdNode> ids = node.getIdentifiers();
        ArrayList<ExprOpNode> exprs = node.getExpressions();

        for(int i = ids.size() -1; i>= 0; i--){
            IdNode id = ids.get(i);
            ExprOpNode expr = exprs.get(i);
            builder.append(id.accept(this));
            builder.append("=");
            builder.append(expr.accept(this));
            builder.append(";").append("\n");
        }

        return builder.toString();
    }

    @Override
    public Object visit(AddNode node) {
        StringBuilder builder = new StringBuilder();

        ExprOpNode expr1 = (ExprOpNode) node.getLeft();
        //String stringExpr1 = (String) expr1.accept(this);
        ExprOpNode expr2 = (ExprOpNode) node.getRight();
        //String stringExpr2 = (String) expr2.accept(this);

        //builder.append(doubleExpressionOperation("PLUS", expr1, expr2, stringExpr1, stringExpr2));
        builder.append(doubleExpressionOperation("PLUS", expr1, expr2));

        return builder.toString();
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
                output = "double";
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
            char* buffer;
            
            """);

        header.append("""
            
            char* string_concat(char* s1, char* s2)
            {
                char* ns = malloc(strlen(s1) + strlen(s2) + 1);
                strcpy(ns, s1);
                strcat(ns, s2);
                return ns;
            }
            
            """);

        header.append("""
            char* int2str(int n)
            {
                char buffer[BUFFER_SIZE];
                int len = sprintf(buffer,"%d",n);
                char *ns = malloc(len + 1);
                sprintf(ns,"%d",n);
                return ns;
            }
            
            """);


        header.append("""
            char* char2str(char c)
            {
                char *ns = malloc(2);
                sprintf(ns, "%c", c);
                return ns;
            }
            
            """);

        header.append("""
            char* double2str(double f)
            {
                char buffer[BUFFER_SIZE];
                int len = sprintf(buffer,"%f", f);
                char *ns = malloc(len + 1);
                sprintf(ns, "%f", f);
                return ns;
            }
        
        """);

        header.append("""
            char* bool2str(int b)
            {
                char* ns = NULL;
                if(b)
                {
                    ns = malloc(5);
                    strcpy(ns, "true");
                }
                else
                {
                    ns = malloc(6);
                    strcpy(ns, "false");
                }
                return ns;
            }
            
            """);

        return header.toString();
    }

    public String createPrototype(DefDeclNode node){
        StringBuilder buildPrototype = new StringBuilder();

        buildPrototype.append(createFirm(node));

        buildPrototype.append(";").append("\n");
        return buildPrototype.toString();
    }

    public String createFirm(DefDeclNode node){

        StringBuilder buildFirm = new StringBuilder();
        if (node.getType() == null) {
            buildFirm.append("void ");
        }
        else if (node.getType() == Type.STRING) {
            buildFirm.append("char* ");
        }
        else {
            buildFirm.append(getCType(node.getType())).append(" ");
        }
        buildFirm.append(node.getName().accept(this)).append("_fun (");

        ArrayList<ParDeclNode> params = node.getParams();
        if(params != null){
            for(int i = params.size() -1; i >= 0; i--) {//iterates in reverse on each parDecl which may be composed by multiple pVars
                buildFirm.append(params.get(i).accept(this));
            }
            if (!buildFirm.isEmpty()) { //instead of calculating the number of parameters, I simply delete the last "," and the last " "
                buildFirm.deleteCharAt(buildFirm.length() - 1);
                buildFirm.deleteCharAt(buildFirm.length() - 1);
            }
        }
        buildFirm.append(")");

        return buildFirm.toString();
    }

    private String getPrintSpecifier(Type type) {

        switch (type) {
            case INT:
                return "%d";
            case DOUBLE:
                return "%f";
            case BOOL, STRING:
                return "%s";
            case CHAR:
                return "%c";
            default:
                return "";
        }
    }

    private void setupFirms (Node node){
        ProgramNode programNode = (ProgramNode) node;

        //code needed for FunctionCall
        SymbolTable table = programNode.getProgramTable();
        ArrayList<SymbolTableRow> rows = table.getRows();

        if(rows != null){ //can be null
            for(SymbolTableRow row : rows){
                if(row.getType() instanceof FunctionType){ //needed for filtering all the functions
                    String name = row.getSymbol();
                    ArrayList<Boolean> refs = ((FunctionType) row.getType()).getReferences();
                    firms.put(name, refs); //a reverse list is not needed here since it will be done already in FunCallNode
                }
            }
        }
    }

    public String doubleExpressionOperation(String operation, ExprOpNode expr1, ExprOpNode expr2){
        StringBuilder builder = new StringBuilder();
        Type type;

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
            builder.append("string_concat(");
            builder.append(objectToCString((String) expr1.accept(this), expr1.getReturnType())).append(", ");
            builder.append(objectToCString((String) expr2.accept(this), expr1.getReturnType())).append(")");
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

        return builder.toString();
    }

    private String objectToCString(String expression, Type type) {
        switch (type) {
            case INT: return "int2str(" + expression + ")";
            case CHAR: return "char2str(" + expression +  ")";
            case DOUBLE: return "double2str(" + expression +  ")";
            case BOOL: return "bool2str(" + expression +  ")";
            case STRING: return expression;
            default: return "";

        }
    }

}
