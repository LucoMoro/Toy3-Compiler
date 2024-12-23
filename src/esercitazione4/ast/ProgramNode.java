package esercitazione4.ast;

import esercitazione4.ast.StatOp.StatOpNode;
import esercitazione4.ast.VarDeclOp.VarDeclNode;
import esercitazione4.visitor.Visitor;
import esercitazione4.visitor.symbolTable.SymbolTable;

import java.util.ArrayList;

public class ProgramNode implements Node{

    public ProgramNode(Object decls, Object vars, Object stats){
        this.decls = (ArrayList<DeclOpNode>) decls;
        this.vars = (ArrayList<VarDeclNode>) vars;
        this.stats = (ArrayList<StatOpNode>) stats;
    }

    public ArrayList<DeclOpNode> getDecls() {
        return decls;
    }

    public ArrayList<VarDeclNode> getVars() {
        return vars;
    }

    public ArrayList<StatOpNode> getStats() {
        return stats;
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
                + "list of declarations: '" + decls + "'; "
                + "list of variables: '" + vars + "'; "
                + "list of statements: '" + stats + "'"
                + "}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private ArrayList<DeclOpNode> decls;
    private ArrayList<VarDeclNode> vars;
    private ArrayList<StatOpNode> stats;

    private SymbolTable table;
}
