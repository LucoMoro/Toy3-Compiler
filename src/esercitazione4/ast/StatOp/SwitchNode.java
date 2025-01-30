package esercitazione4.ast.StatOp;

import esercitazione4.ast.IdNode;
import esercitazione4.ast.SwitchStatNode;
import esercitazione4.ast.Type;
import esercitazione4.visitor.Visitor;
import esercitazione4.visitor.symbolTable.SymbolTable;

import java.util.ArrayList;

public class SwitchNode implements StatOpNode{

    public SwitchNode(Object id, ArrayList<SwitchStatNode> switchStats) {
        this.id = (IdNode) id;
        this.switchStats = switchStats;
    }

    public IdNode getId() {
        return id;
    }

    public void setId(IdNode id) {
        this.id = id;
    }

    public ArrayList<SwitchStatNode> getSwitchStats() {
        return switchStats;
    }

    public void setSwitchStats(ArrayList<SwitchStatNode> switchStats) {
        this.switchStats = switchStats;
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
    public String toString() {
        return getClass().getSimpleName() + "{"
                + "id: '" + id + "'; "
                + "switchStats: '" + switchStats + "'"
                + "}";
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }

    private IdNode id;
    private ArrayList<SwitchStatNode> switchStats;

    private SymbolTable table;
    private Type returnType;
}
