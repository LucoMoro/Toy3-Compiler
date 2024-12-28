package esercitazione4.visitor.symbolTable;

import esercitazione4.ast.Type;
import java.util.ArrayList;

public class FunctionType implements Firm {

    public FunctionType(ArrayList<Type> input_types, Type return_type){
        this.input_types = input_types;
        this.return_type = return_type;
    }

    public FunctionType(ArrayList<Type> input_types){
        this.input_types = input_types;
        this.return_type = null;
    }

    public FunctionType(Type return_type){
        this.input_types = null;
        this.return_type = return_type;
    }

    public ArrayList<Type> getInput_types() {
        return input_types;
    }

    public void setInput_types(ArrayList<Type> input_types) {
        this.input_types = input_types;
    }

    public Type getReturn_type() {
        return return_type;
    }

    public void setReturn_type(Type return_type) {
        this.return_type = return_type;
    }

    @Override
    public String toString(){
        return "" + this.input_types + "->" + this.return_type;
    }

    public Type getSingleType(){
        return return_type;
    }

    public ArrayList<Type> getListOfTypes(){
        return input_types;
    }

    private ArrayList<Type> input_types;
    private Type return_type;
}
