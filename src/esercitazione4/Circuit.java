package esercitazione4;

import esercitazione4.ast.ProgramNode;
import esercitazione4.visitor.ScopeVisitor;
import esercitazione4.visitor.TreeVisitor;
import esercitazione4.visitor.TypeCheckerVisitor;

import java.io.*;

public class Circuit {
    public static void main(String[] args) throws Exception {
        /*String res = "corretta";
        System.out.println("Type in circuit, hit Return, then Cmd-D (in MacOs) o Ctrl-D (in Windows)");
        String filePath = args[0];

        try {
            FileReader fileReader = new FileReader(filePath);
            parser p = new parser(new esercitazione4.Yylex(fileReader));

            p.parse(); // l'uso di p.debug_parse() al posto di p.parse() produce tutte le azioni del parser durante il riconoscimento
        }
        catch(FileNotFoundException e){
            System.err.println("Error: File not found - " + filePath);
        }
        catch(Exception e){
            res = "errata!!";
            System.out.println(e);
        }

        System.out.println("Frase " + res);*/


        String filePath = args[0];

        try {
            FileReader fileReader = new FileReader(filePath);
            parser p = new parser(new esercitazione4.Yylex(fileReader));
            FileWriter output_file = new FileWriter("output.xml");

            ProgramNode program = (ProgramNode) p.parse().value;
            TreeVisitor visitor = new TreeVisitor(output_file);
            program.accept(visitor);

            ScopeVisitor scope = new ScopeVisitor();
            program.accept(scope);

            TypeCheckerVisitor typeChecker = new TypeCheckerVisitor();
            program.accept(typeChecker);

            output_file.close();
        }
        catch(FileNotFoundException e){
            System.err.println("Error: File not found - " + filePath);
        }
        catch(Exception e){
            System.out.println(e);
        }

    }
}
