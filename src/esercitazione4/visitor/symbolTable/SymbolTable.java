package esercitazione4.visitor.symbolTable;

import java.util.ArrayList;

public class SymbolTable {

    private ArrayList<SymbolTableRow> rows;
    private SymbolTable parent;

    public SymbolTable(){
        this.rows = new ArrayList<SymbolTableRow>();
    }

    public SymbolTable getParent() {
        return parent;
    }

    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }
}
