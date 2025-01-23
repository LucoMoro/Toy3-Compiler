package esercitazione4;

import esercitazione4.*;
import org.junit.jupiter.api.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

public class parserTest {

    private static final String SRC_TEST_RESOURCES = "C:\\Users\\morel\\Desktop\\Unisa\\Progetti attivi\\Morelli_es4TP\\test_files\\";

    private void parseFile(String fileName) {
        String filePath = SRC_TEST_RESOURCES + fileName;
        try (Reader fileReader = new BufferedReader(new FileReader(filePath))) {
            parser parser = new parser(new Yylex(fileReader));
            Object result = parser.parse(); // Esegui il parsing
            System.out.println("Frase corretta!");
        } catch (Exception e) {
            fail("Error while parsing file '" + filePath + "': " + e.getMessage());
        }
    }

    @Test
    public void testFile1() {
        parseFile("test_1.txt");
    }

    @Test
    public void testFile2() {
        parseFile("test_2.txt");
    }

    @Test
    public void testFile3() {
        parseFile("test_3.txt");
    }

    @Test
    public void testFile4() {
        parseFile("test_4.txt");
    }

    @Test
    public void testFile5() {
        parseFile("test_5.txt");
    }

    @Test
    public void testFile7() {
        parseFile("test_7.txt");
    }

    @Test
    public void testFile8() {
        parseFile("test_8.txt");
    }

    @Test
    public void testFile9() {
        parseFile("test_9.txt");
    }

    @Test
    public void testFile10() {
        parseFile("test_10.txt");
    }

    @Test
    public void testFile11() {
        parseFile("test_11.txt");
    }

    @Test
    public void testFile12() {
        parseFile("test_12.txt");
    }

    @Test
    public void testFile13() {
        parseFile("test_13.txt");
    }

    @Test
    public void testFileProf1() {
        parseFile("test_example.txt");
    }

    @Test
    public void testFileProf2() {
        parseFile("personal_valid1.txt");
    }

    @Test
    public void testFileProf3() {
        parseFile("personal_valid2.txt");
    }

    @Test
    public void testFileProf4() {
        parseFile("personal_valid3.txt");
    }


}