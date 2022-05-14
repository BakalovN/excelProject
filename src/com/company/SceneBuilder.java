package com.company;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SceneBuilder extends Application {

    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws Exception {
//----------------ARRAYS THAT STORE FORMULAS AND VALUES-----------------------------------------------------------------
        Path path = Paths.get("D:\\saved_cells.txt");
        String[][] formulas = new String[31][27];
        String[][] values = new String[31][27];
//----------------------------------------------------------------------------------------------------------------------
        WriteToFile.loadingCelsFromFile(values,formulas,path);//setting the arrays with saved data from txt file

        VBox vBox = new VBox();//               --------------------VERTICAL BOX--------------------
        vBox.setMaxWidth(1000);//               --HORIZONTAL BOX WITH TEXT FIELDS                 --
        vBox.setMaxHeight(800);//               --HORIZONTAL BOX WITH TEXT FIELDS                 --
        List<HBox> hBoxes = new ArrayList<>();//--HORIZONTAL BOX WITH TEXT FIELDS                 --

//-----------------------------BUILDING THE FRONT VIEW------------------------------------------------------------------
        for (int i = 0; i < 31; i++) {
            TextField textField = new TextField();

            if (i==0){
                HBox hbox = new HBox();//setting the first cell
                textField.setAlignment(Pos.CENTER);
                textField.setPromptText("e/v");//setting the prompt text to e/v e -> edit v -> view
                textField.setEditable(true);
                hbox.getChildren().add(textField);
                vBox.getChildren().add(hbox);
                hBoxes.add(hbox);
            }else {
                HBox hbox = new HBox();
                textField.setAlignment(Pos.CENTER);
                textField.setText(String.valueOf(i)+".");//setting the numbers of the rows
                textField.setEditable(false);
                hbox.getChildren().add(textField);
                vBox.getChildren().add(hbox);
                hBoxes.add(hbox);
            }
        }
        for (int i = 1; i < 27; i++) {
            TextField textField = new TextField();
            textField.setAlignment(Pos.CENTER);
            char ch = (char)(64+i);//setting the letters for the columns
            textField.setText(String.valueOf(ch));
            textField.setEditable(false);
            hBoxes.get(0).getChildren().add(textField);
        }
        for (int i = 1; i < 31; i++) {
            for (int j = 1; j < 27; j++) {
                TextField textField = new TextField();
                hBoxes.get(i).getChildren().add(textField);
                textField.setText(values[i][j]);//setting the working sheet with values from previous sessions
            }
        }

//-----------------------------FUNCTIONALITIES OF THE CELLS-------------------------------------------------------------
        for (int i = 1; i < 31; i++) {
            for (int j = 1; j < 27; j++) {
                TextField t = (TextField)hBoxes.get(i).getChildren().get(j);
                int finalI = i;
                int finalJ = j;
                t.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        t.setPrefWidth(300);
                        TextField edit = (TextField) hBoxes.get(0).getChildren().get(0);
                        if (formulas[finalI][finalJ].contains("=") &&edit.getText().equals("e")){
                            t.setText(formulas[finalI][finalJ]);
                        }
                    }
                });
                t.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        t.setPrefWidth(149);
                        TextField edit = (TextField) hBoxes.get(0).getChildren().get(0);
                        if (!values[finalI][finalJ].equals("") && edit.getText().equals("v")){
                            t.setText(values[finalI][finalJ]);
                        }

                    }
                });
            }

        }

        for (int i = 1; i < 31; i++) {
            for (int j = 1; j < 27; j++) {
                TextField t = (TextField)hBoxes.get(i).getChildren().get(j);
                int finalI = i;
                int finalJ = j;
                t.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        try {
                            if (t.getText().contains("=")){
                                String textFromCell = t.getText();
                                formulas[finalI][finalJ] = textFromCell;
                                String textWithNumbers = Calculations.formulaWithNums(textFromCell, hBoxes, values);
                                String textAfterBrackets = Calculations.bracketsCalculations(textWithNumbers);
                                String textAfterMultiplication = Calculations.multiplicationCalculation(textAfterBrackets).trim();
                                String textAfterDivision = Calculations.divisionCalculation(textAfterMultiplication).trim();
                                String textAfterSubstraction = Calculations.substractionCalculation(textAfterDivision);
                                String textAfterAddition = Calculations.additionCalculation(textAfterSubstraction);
                                values[finalI][finalJ] = textAfterAddition;
                                WriteToFile.writeToFile(values, formulas);
                                t.setText(textAfterAddition);
                            }else{
                                String textFromCell = t.getText();
                                values[finalI][finalJ] = textFromCell;
                                formulas[finalI][finalJ] = "";
                                String col = String.valueOf((char)(finalJ+64));
                                String row = String.valueOf(finalI);
                                String excelCol = col+row;
                                if (!values[finalI][finalJ].equals("")){
                                    for (int k = 0; k < 31; k++) {
                                        for (int l = 0; l < 27; l++) {
                                            if (formulas[k][l].contains(excelCol)){
                                                values[k][l] = Calculations.substractionCalculation(Calculations.additionCalculation(Calculations.divisionCalculation(Calculations.multiplicationCalculation(Calculations.bracketsCalculations(Calculations.formulaWithNums(formulas[k][l], hBoxes, values))))));
                                                TextField t1 = (TextField) hBoxes.get(k).getChildren().get(l);
                                                t1.setText(values[k][l]);
                                            }
                                        }
                                    }
                                }
                                WriteToFile.writeToFile(values,formulas);
                            }

                        } catch (Exception e) {
                            t.setPromptText("Invalid data inserted");
                        }

                    }
                });
            }
        }
//---------------------------SCENE CONFIGURATION------------------------------------------------------------------------
        Scene scene = new Scene(vBox, 1000, 800);
        vBox.prefWidthProperty().bind(scene.widthProperty().multiply(0.80));
        vBox.prefHeightProperty().bind(scene.widthProperty().multiply(0.80));
        stage.setScene(scene);
        stage.show();
    }

}
