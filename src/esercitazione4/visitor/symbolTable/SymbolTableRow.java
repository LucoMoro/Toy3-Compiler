package esercitazione4.visitor.symbolTable;

import esercitazione4.ast.Node;

/**
 * Class that represents the single row of a symbol table
 */
public class SymbolTableRow {

    private String symbol;
    private String kind;
    Firm type;

    public SymbolTableRow(String symbol, String kind, Firm type){
            this.symbol = symbol;
            this.kind = kind;
            this.type = type;
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

    @Override
    public String toString(){
        return getClass().getSimpleName() + "{"
                + "id: '" + this.symbol + "'; "
                + "kind: '" + this.kind + "'; "
                + "type: '" + this.type + "'"
                + "}";
    }
}
