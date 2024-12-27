package esercitazione4.visitor.symbolTable;

import java.util.ArrayList;

public class SymbolTable {

    private ArrayList<SymbolTableRow> rows;
    private SymbolTable parent;
    private String tableName;

    public SymbolTable(String name){
        this.tableName = name;
        this.rows = new ArrayList<SymbolTableRow>();
    }

    public SymbolTable(String name, ArrayList<SymbolTableRow> rows){
        this.tableName = name;
        this.rows = rows;
    }

    public SymbolTable getParent() {
        return parent;
    }

    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public ArrayList<SymbolTableRow> getRows() {
        return rows;
    }

    public void setRows(ArrayList<SymbolTableRow> rows) {
        this.rows = rows;
    }

    public Object addRow(SymbolTableRow row) throws DuplicateSymbolException {
        if(this.rows.contains(row)){
            throw new DuplicateSymbolException("Error: multiple declaration on: " + row.getSymbol() +", "+ row.getKind());
        }else {
            this.rows.add(row);
        }
        return this;
    }

    @Override
    public String toString(){
        return this.tableName + "{"
                + " rows: " + this.rows
                + "}";
    }
}
