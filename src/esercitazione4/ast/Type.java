package esercitazione4.ast;

import esercitazione4.ast.Constants.*;

public enum Type {

    INT,
    BOOL,
    DOUBLE,
    STRING,
    CHAR,
    NOTYPE;

    /**
     * Function that converts the constant into the corresponding type
     */
    public static Type convertType(ConstantNode constant){
        Type convertedType = null;

        if(constant instanceof BoolNode){
            convertedType = Type.BOOL;
        } else if (constant instanceof IntNode){
            convertedType = Type.INT;
        } else if (constant instanceof DoubleNode){
            convertedType = Type.DOUBLE;
        } else if (constant instanceof CharNode){
            convertedType = Type.CHAR;
        } else if (constant instanceof StringNode){
            convertedType = Type.STRING;
        }

        return convertedType;
    }
}
