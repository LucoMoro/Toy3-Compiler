package esercitazione4.visitor.symbolTable;

import esercitazione4.ast.Type;

import java.util.ArrayList;

/**
 * interface used to simulate the type of a variable or a firm of a method
 */
public interface Firm {

    Type getSingleType();

   ArrayList<Type> getListOfTypes();

}
