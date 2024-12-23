package esercitazione4.visitor.symbolTable;

import esercitazione4.ast.Type;

public class VariableType implements Firm{

    public VariableType(Type type){
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString(){
        return "" + this.type;
    }

    private Type type;
}
