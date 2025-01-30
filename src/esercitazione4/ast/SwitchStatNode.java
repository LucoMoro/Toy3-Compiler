package esercitazione4.ast;

import esercitazione4.ast.Constants.ConstantNode;
import esercitazione4.ast.StatOp.StatOpNode;
import esercitazione4.visitor.Visitor;
import esercitazione4.visitor.symbolTable.SymbolTable;

import java.util.ArrayList;

public class SwitchStatNode implements StatOpNode {

    public SwitchStatNode(Object constant, Object stats) {
        this.constant = (ConstantNode) constant;
        this.stats = (ArrayList<StatOpNode>) stats;
    }

    public ConstantNode getConstant() {
        return constant;
    }

    public void setConstant(ConstantNode constant) {
        this.constant = constant;
    }

    public ArrayList<StatOpNode> getStats() {
        return stats;
    }

    public void setStats(ArrayList<StatOpNode> stats) {
        this.stats = stats;
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
                + "constant: '" + constant + "'; "
                + "stats: '" + stats + "'"
                + "}";
    }

    @Override
    public Object accept(Visitor v) {
        return v.visit(this);
    }

    private ConstantNode constant;
    private ArrayList<StatOpNode> stats;

    private SymbolTable table;
    private Type returnType;
}
