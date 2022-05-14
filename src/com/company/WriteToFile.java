package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class WriteToFile {

    public static void loadingCelsFromFile(String[][] values, String[][] formulas, Path path) throws IOException {
        if (Files.notExists(path) || Files.readAllLines(path).isEmpty()){
            File file = new File("D:\\saved_cells.txt");
            PrintWriter valuesWriter = new PrintWriter(file);
            for (int i = 0; i < 31; i++) {
                for (int j = 0; j < 27; j++) {
                    formulas[i][j] ="";
                    values[i][j] = "";
                    valuesWriter.printf("%d %d %s %s%n",i, j, values[i][j], formulas[i][j]);
                }
            }
            valuesWriter.close();
        }else{
            List<String> allLines = Files.readAllLines(Path.of("D:\\saved_cells.txt"));
            for (int i = 0; i < allLines.size(); i++) {
                String[] input = allLines.get(i).split(" ");
                String row = input[0];
                String col = input[1];
                if (input.length>2){
                    String value = input[2];
                    values[Integer.parseInt(row)][Integer.parseInt(col)] = value;
                }else {
                    values[Integer.parseInt(row)][Integer.parseInt(col)] = "";
                }
                if (input.length>3){
                    String formula = input[3];
                    formulas[Integer.parseInt(row)][Integer.parseInt(col)] = formula;
                }else {
                    formulas[Integer.parseInt(row)][Integer.parseInt(col)] = "";
                }
                if (input.length>4){
                    String text = "";
                    for (int j = 2; j < input.length; j++) {
                        text += String.format("%s ", input[j]);
                    }
                    values[Integer.parseInt(row)][Integer.parseInt(col)] = text.trim();
                }
            }

        }
    }

//-------------------------------UPDATING THE FILE----------------------------------------------------------------------
    public static void writeToFile (String[][] values, String[][] formulas) throws FileNotFoundException {
        Path path = Paths.get("D:\\saved_cells.txt");
        PrintWriter writer = new PrintWriter("D:\\saved_cells.txt");
        for (int i = 0; i < 31; i++) {
            for (int j = 0; j < 27; j++) {
                String row = String.valueOf(i);
                String col = String.valueOf(j);
                writer.printf(String.format("%s %s %s %s", row, col, values[i][j],formulas[i][j]));
                writer.println();
            }
        }
        writer.close();
    }
}
