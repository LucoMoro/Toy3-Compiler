package esercitazione4.ast;

import esercitazione4.ast.StatOp.StatOpNode;
import esercitazione4.ast.VarDeclOp.VarDeclNode;
import esercitazione4.visitor.Visitor;
import esercitazione4.visitor.symbolTable.SymbolTable;

import java.util.ArrayList;

public class BodyNode implements Node{

    public BodyNode(Object varDecls, Object statements){
        this.left = (ArrayList<VarDeclNode>) varDecls;
        this.right = (ArrayList<StatOpNode>) statements;
    }

    public ArrayList<VarDeclNode> getLeft() {
        return left;
    }

    public ArrayList<StatOpNode> getRight() {
        return right;
    }

    public SymbolTable getTable() {
        return table;
    }

    public void setTable(SymbolTable table) {
        this.table = table;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + "{"
                + "list of variables: '" + left + "'; "
                + "list of statements: '" + right + "'"
                + "}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private ArrayList<VarDeclNode> left;
    private ArrayList<StatOpNode> right;

    private SymbolTable table;

}
