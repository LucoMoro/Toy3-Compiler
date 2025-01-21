package esercitazione4.visitor.symbolTable;

import esercitazione4.ast.Constants.*;
import esercitazione4.ast.Type;

import java.util.ArrayList;

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
    public Type getSingleType() {
        return type;
    }

    @Override
    public ArrayList<Type> getListOfTypes() {
        return null;
    }

    @Override
    public String toString(){
        return "" + this.type;
    }

    public Boolean compareTypes(Type newType){
        Boolean result = false;
        if(this.type == newType){
            return true;
        }
        return  result;
    }

    private Type type;
    private ConstantNode constant;
}
