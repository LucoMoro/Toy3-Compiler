package esercitazione4;

import esercitazione4.ast.ProgramNode;
import esercitazione4.visitor.ScopeVisitor;
import esercitazione4.visitor.TranslationVisitor;
import esercitazione4.visitor.TreeVisitor;
import esercitazione4.visitor.TypeCheckerVisitor;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
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

        String output_folder = "test_files" + File.separator + "/c_out";
        Path inputPath = Paths.get(args[0]);
        String inputFileName = inputPath.getFileName().toString();
        String cInputFileName = FilenameUtils.removeExtension(inputFileName) + ".c";

        //String baseName = FilenameUtils.removeExtension(filePath);
        //String fileName = baseName + ".c";

        try {
            //FileReader fileReader = new FileReader(filePath);
            FileReader fileReader = new FileReader(inputPath.toString());
            parser p = new parser(new esercitazione4.Yylex(fileReader));
            FileWriter output_file = new FileWriter("output.xml");

            ProgramNode program = (ProgramNode) p.parse().value;
            TreeVisitor visitor = new TreeVisitor(output_file);
            program.accept(visitor);

            ScopeVisitor scope = new ScopeVisitor();
            program.accept(scope);

            //System.out.println("/*********************************** Type Checking *************************************************/");
            TypeCheckerVisitor typeChecker = new TypeCheckerVisitor();
            program.accept(typeChecker);

            //System.out.println("/*********************************** Translating in C *************************************************/");
            FileWriter translationFile = new FileWriter(output_folder + File.separator + cInputFileName);
            TranslationVisitor translator = new TranslationVisitor();
            String outputCode = (String) program.accept(translator);
            translationFile.append(outputCode);

            output_file.close();
            translationFile.close();
        }
        catch(FileNotFoundException e){
            System.err.println("Error: File not found - " + inputPath.toString());
        }
        catch(Exception e){
            System.out.println(e);
        }

    }
}
