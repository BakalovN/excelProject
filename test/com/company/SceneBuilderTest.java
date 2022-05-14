package com.company;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SceneBuilderTest {

    @Test
    public void test(){
        SceneBuilder test = new SceneBuilder();
        String patternToCalculate = "( 15 + 4 ) / 2 - ( 4 + 5 ) / 4 - 1 / 4 + 4 * ( 2 + 4 - ( 7 - 2 ) )";
        String textAfterBrackets = Calculations.bracketsCalculations(patternToCalculate);
        String textAfterMultiplication = Calculations.multiplicationCalculation(textAfterBrackets).trim();
        String textAfterDivision = Calculations.divisionCalculation(textAfterMultiplication).trim();
        String textAfterSubstraction = Calculations.substractionCalculation(textAfterDivision);
        String textAfterAddition = Calculations.additionCalculation(textAfterSubstraction);
        boolean isPassed = textAfterAddition.equals("11.0");
        System.out.println(isPassed);
    }
}