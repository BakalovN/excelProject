package com.company;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Calculations {

//----------------RETURNING THE FORMULA FROM THE CELL WITH NUMBERS INSTEAD WITH CELL COORDINATES------------------------
    public static String formulaWithNums(String textFromCell, List<HBox> hBoxes, String[][] values) throws IOException {
        textFromCell = textFromCell.replaceAll("=", "");
        char[] input = textFromCell.toCharArray();
        String textWithCells = "";
        for (int i = 0; i < input.length; i++) {
            if (input[i] == '+'){
                textWithCells += " + ";
            }else if (input[i] == '-'){
                textWithCells += " - ";
            }else if (input[i] == '('){
                textWithCells += "( ";
            }else if (input[i] == ')'){
                textWithCells += " )";
            }else if (input[i] == '*'){
                textWithCells += " * ";
            }else if (input[i] == '/'){
                textWithCells += " / ";
            }else if (Character.isDigit(input[i])){
                textWithCells += String.valueOf(input[i]);
                while (true){
                    i++;
                    if (i<input.length){
                        if (!Character.isDigit(input[i])){
                            break;
                        }
                    }else {
                        // i--;
                        break;
                    }
                    textWithCells += String.valueOf(input[i]);
                }
                i--;
            }else if (Character.isLetter(input[i])){
                int col = input[i] - 64;
                int row = 0;
                String rowHelper = "";
                while (true){
                    i++;
                    if (i<input.length){
                        if (!Character.isDigit(input[i])){
                            break;
                        }
                    }else {
                        //i--;
                        break;
                    }

                    rowHelper += String.valueOf(input[i]);
                }
                i--;
                row = Integer.parseInt(rowHelper);
                TextField t = (TextField) hBoxes.get(row).getChildren().get(col);
                values[row][col] = t.getText();
                textWithCells += t.getText();
            }
        }
        return textWithCells;
    }

    public static String bracketsCalculations (String textWithNumbers){
        textWithNumbers = textWithNumbers.trim();
        String[] helper = textWithNumbers.split(" ");
        String toCalculate = "";
        List<String> brackets = new ArrayList<>();
        List<Integer> indexesOfBrackets = new ArrayList<>();
        List<String> afterBrackets = new ArrayList<>(Arrays.asList(helper));
        for (int i = 0; i < afterBrackets.size(); i++) {
            if (afterBrackets.get(i).equals("(") || afterBrackets.get(i).equals(")")){
                brackets.add(afterBrackets.get(i));
                indexesOfBrackets.add(i);
            }
        }
        String bracketsTemplate = "";
        int start = 0;
        int stop = 0;
        for (int i = 0; i < brackets.size(); i++) {
            if (brackets.get(i).equals("(") && brackets.get(i+1).equals(")")){
                start = indexesOfBrackets.get(i);
                stop = indexesOfBrackets.get(i+1);
                brackets.remove(i);
                brackets.remove(i);
                indexesOfBrackets.remove(i);
                indexesOfBrackets.remove(i);
                for (int j = start+1; j < stop; j++) {
                    bracketsTemplate += String.format("%s ",afterBrackets.get(j));
                }
                bracketsTemplate = bracketsTemplate.trim();
                String calculated = additionCalculation(substractionCalculation(divisionCalculation(multiplicationCalculation(bracketsTemplate))));
                bracketsTemplate = "";
                for (int j = start; j <= stop; j++) {
                    afterBrackets.remove(start);
                    for (int k = 0; k < indexesOfBrackets.size(); k++) {
                        if (indexesOfBrackets.get(k)>start && j!=stop){
                            indexesOfBrackets.set(k, indexesOfBrackets.get(k)-1);
                        }
                    }
                }
                afterBrackets.add(start, calculated);
                i=-1;
            }

        }
        String templateToReturn = "";
        for (int i = 0; i < afterBrackets.size(); i++) {
            templateToReturn += String.format("%s ", afterBrackets.get(i));
        }
        return templateToReturn.trim();
    }
    public static String multiplicationCalculation(String templateToCalculate){
        templateToCalculate = templateToCalculate.trim();
        String[] helper = templateToCalculate.split(" ");
        List<String> afterMultiplication = new ArrayList<>(Arrays.asList(helper));
        for (int i = 0; i < afterMultiplication.size(); i++) {
            if (afterMultiplication.get(i).equals("*")){
                double n1 = Double.parseDouble(afterMultiplication.get(i-1));
                double n2 = Double.parseDouble(afterMultiplication.get(i+1));
                double multiplication = n1*n2;
                for (int j = 0; j < 3; j++) {
                    afterMultiplication.remove(i-1);
                }
                afterMultiplication.add(i-1,String.valueOf(multiplication));
                i--;
            }
        }
        String templateToReturn = "";
        for (int i = 0; i < afterMultiplication.size(); i++) {
            templateToReturn += String.format("%s ", afterMultiplication.get(i));
        }
        return templateToReturn.trim();
    }

    public static String divisionCalculation(String templateToCalculate){
        templateToCalculate = templateToCalculate.trim();
        String[] helper = templateToCalculate.split(" ");
        List<String> afterDivision = new ArrayList<>(Arrays.asList(helper));
        for (int i = 0; i < afterDivision.size(); i++) {
            if (afterDivision.get(i).equals("/")){
                double n1 = Double.parseDouble(afterDivision.get(i-1));
                double n2 = Double.parseDouble(afterDivision.get(i+1));
                double multiplication = n1/n2;
                for (int j = 0; j < 3; j++) {
                    afterDivision.remove(i-1);
                }
                afterDivision.add(i-1,String.valueOf(multiplication));
                i--;
            }
        }
        String templateToReturn = "";
        for (int i = 0; i < afterDivision.size(); i++) {
            templateToReturn += String.format("%s ", afterDivision.get(i));
        }
        return templateToReturn.trim();
    }

    public static String additionCalculation(String templateToCalculate){
        templateToCalculate = templateToCalculate.trim();
        String[] helper = templateToCalculate.split(" ");
        List<String> afterAddition = new ArrayList<>(Arrays.asList(helper));
        for (int i = 0; i < afterAddition.size(); i++) {
            if (afterAddition.get(i).equals("+")){
                double n1 = Double.parseDouble(afterAddition.get(i-1));
                double n2 = Double.parseDouble(afterAddition.get(i+1));
                double addition = n1+n2;
                for (int j = 0; j < 3; j++) {
                    afterAddition.remove(i-1);
                }
                afterAddition.add(i-1,String.valueOf(addition));
                i--;
            }
        }
        String templateToReturn = "";
        for (int i = 0; i < afterAddition.size(); i++) {
            templateToReturn += String.format("%s ", afterAddition.get(i));
        }
        return templateToReturn.trim();
    }

    public static String substractionCalculation(String templateToCalculate){
        templateToCalculate = templateToCalculate.trim();
        String[] helper = templateToCalculate.split(" ");
        List<String> afterSubstraction = new ArrayList<>(Arrays.asList(helper));
        for (int i = 0; i < afterSubstraction.size(); i++) {
            if (afterSubstraction.get(i).equals("-")){
                double n1 = Double.parseDouble(afterSubstraction.get(i-1));
                double n2 = Double.parseDouble(afterSubstraction.get(i+1));
                double substraction = n1-n2;
                for (int j = 0; j < 3; j++) {
                    afterSubstraction.remove(i-1);
                }
                afterSubstraction.add(i-1,String.valueOf(substraction));
                i--;
            }
        }
        String templateToReturn = "";
        for (int i = 0; i < afterSubstraction.size(); i++) {
            templateToReturn += String.format("%s ", afterSubstraction.get(i));
        }
        return templateToReturn.trim();
    }
}
