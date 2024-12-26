package esercitazione4.ast.DefDeclOp;

import esercitazione4.ast.BodyNode;
import esercitazione4.ast.DeclOpNode;
import esercitazione4.ast.IdNode;
import esercitazione4.ast.ParDeclOp.ParDeclNode;
import esercitazione4.ast.Type;
import esercitazione4.visitor.Visitor;
import esercitazione4.visitor.symbolTable.SymbolTable;

import java.util.ArrayList;

public class DefDeclNode implements DeclOpNode {

    public DefDeclNode(Object name, Object type, Object body){
        this.name = (IdNode) name;
        this.params = null;
        this.type = (Type) type;
        this.body = (BodyNode) body;
    }

    public DefDeclNode(Object name, Object params, Object type, Object body){
        this.name = (IdNode) name;
        this.params = (ArrayList<ParDeclNode>) params;
        this.type = (Type) type;
        this.body = (BodyNode) body;
    }

    public IdNode getName() {
        return name;
    }

    public ArrayList<ParDeclNode> getParams() {
        return params;
    }

    public Type getType() {
        return type;
    }

    public BodyNode getBody() {
        return body;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + "{"
                + "name: '" + name + "'; "
                + "list of parameters: '" + params + "'; "
                + "type: '" + type + "'; "
                + "body: '" + body +"'"
                + "}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
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

    private IdNode name;
    private ArrayList<ParDeclNode> params;
    private Type type;
    private BodyNode body;

    private SymbolTable table;
    private Type returnType;
}
