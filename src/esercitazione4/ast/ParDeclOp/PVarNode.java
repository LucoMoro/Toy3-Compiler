package esercitazione4.ast.ParDeclOp;

import esercitazione4.ast.IdNode;
import esercitazione4.visitor.Visitor;

public class PVarNode implements ParDeclOpNode {

    public PVarNode(Object variable){
        this.variable = (IdNode) variable;
        this.hasRef = false;
    }

    public PVarNode(Object variable, Boolean hasRef){
        this.variable = (IdNode) variable;
        this.hasRef = hasRef;
    }

    public IdNode getVariable() {
        return variable;
    }

    public Boolean getHasRef() {
        return hasRef;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + "{"
                + "ref: '" + hasRef + "'; "
                + "variable: '" + variable + "'"
                + "}";
    }

    public Object accept(Visitor v){
        return v.visit(this);
    }

    private IdNode variable;
    private Boolean hasRef;
}
