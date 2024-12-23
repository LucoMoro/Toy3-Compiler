package esercitazione4.visitor.symbolTable;

import esercitazione4.ast.Node;

/**
 * Class that represents the single row of a symbol table
 */
public class SymbolTableRow {

    private String id;
    private Node node;
    private String kind;
    Firm type;

    public SymbolTableRow(String id, Node node, String kind, Firm type){
            this.id = id;
            this.node = node;
            this.kind = kind;
            this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
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
}
