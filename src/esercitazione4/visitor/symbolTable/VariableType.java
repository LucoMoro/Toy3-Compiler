package esercitazione4.visitor.symbolTable;

import esercitazione4.ast.Constants.*;
import esercitazione4.ast.Type;

public class VariableType implements Firm{

    public VariableType(Type type){
        this.type = type;
    }

    public VariableType(ConstantNode constant){
        this.constant = constant;
        if(this.constant instanceof BoolNode){
            this.type = Type.BOOL;
        } else if (this.constant instanceof CharNode){
            this.type = Type.CHAR;
        } else if (this.constant instanceof DoubleNode){
            this.type = Type.DOUBLE;
        } else if (this.constant instanceof IntNode){
            this.type = Type.INT;
        } else {
            this.type = Type.STRING;
        }
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
    private ConstantNode constant;
}
