package esercitazione4.visitor.symbolTable;

import esercitazione4.ast.Node;
import esercitazione4.ast.Type;

/**
 * Class that represents the single row of a symbol table
 */
public class SymbolTableRow {

    private String symbol;
    private String kind;
    Firm type;
    private Object properties;

    public SymbolTableRow(String symbol, String kind, Firm type){
            this.symbol = symbol;
            this.kind = kind;
            this.type = type;
    }

    public SymbolTableRow(String symbol, String kind, Firm type, Object properties){
        this.symbol = symbol;
        this.kind = kind;
        this.type = type;
        this.properties = properties;
    }

    public SymbolTableRow(String symbol, String kind){
        this.symbol = symbol;
        this.kind = kind;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Firm getType() {
        return type;
    }

    public void setType(Firm type) {
        this.type = type;
    }

    public Object getProperties() {
        return properties;
    }

    public void setProperties(Object properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object obj) { //needed to throw the DuplicateSymbolException
        SymbolTableRow row = (SymbolTableRow) obj;
        return this.symbol.equals(row.getSymbol()) && this.kind.equals(row.getKind());
    }

    @Override
    public String toString(){
        String output="";
        if(this.properties == null) {
            output = getClass().getSimpleName() + "{"
                    + "id: '" + this.symbol + "'; "
                    + "kind: '" + this.kind + "'; "
                    + "type: '" + this.type + "'"
                    + "}";
        } else {
            output = getClass().getSimpleName() + "{"
                    + "id: '" + this.symbol + "'; "
                    + "kind: '" + this.kind + "'; "
                    + "type: '" + this.type + "'; "
                    + "properties: '" + this.properties + "'"
                    + "}";
        }
        return output;
    }
}
