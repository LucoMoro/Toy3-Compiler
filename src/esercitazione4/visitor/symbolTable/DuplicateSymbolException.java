package esercitazione4.visitor.symbolTable;

public class DuplicateSymbolException extends RuntimeException {
    public DuplicateSymbolException(String message) {
        super(message);
    }
}
