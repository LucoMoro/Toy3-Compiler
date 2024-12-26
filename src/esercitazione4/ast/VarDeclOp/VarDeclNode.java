package esercitazione4.ast.VarDeclOp;

import esercitazione4.ast.Constants.ConstantNode;
import esercitazione4.ast.DeclOpNode;
import esercitazione4.ast.Type;
import esercitazione4.visitor.Visitor;
import esercitazione4.visitor.symbolTable.SymbolTable;

import java.util.ArrayList;

public class VarDeclNode implements DeclOpNode {

    public VarDeclNode(Object vars, Object typeOrConstant){
        if(typeOrConstant instanceof Type){
            this.type = (Type) typeOrConstant;
            this.constant = null;
        }
        else {
            this.constant = (ConstantNode) typeOrConstant;
            this.type = null;
        }
        this.vars = (ArrayList<VarOptInitNode>) vars;
    }

    public ConstantNode getConstant() {
        return constant;
    }

    public Type getType() {
        return type;
    }

    public ArrayList<VarOptInitNode> getVars() {
        return vars;
    }

    public SymbolTable getTable() {
        return table;
    }

    public void setTable(SymbolTable table) {
        this.table = table;
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    @Override
    public String toString(){
        String result="";
        if(type==null){
            result = getClass().getSimpleName() + "{"
                    + "list of variables: '" + vars + "'; "
                    + "constant: '" + constant + "'"
                    + "}";
        }
        else {
            result = getClass().getSimpleName() + "{"
                    + "list of variables: '" + vars + "'; "
                    + "type: '" + type + "'"
                    + "}";
        }
        return result;
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private Type type;
    private ConstantNode constant;
    private ArrayList<VarOptInitNode> vars;
    private SymbolTable table;

    private Type returnType;
}
