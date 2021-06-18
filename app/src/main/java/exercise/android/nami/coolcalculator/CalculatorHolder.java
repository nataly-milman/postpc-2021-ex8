package exercise.android.nami.coolcalculator;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Collections;

public class CalculatorHolder extends Activity {

    ArrayList<CalculationDetails> calculations;

    public CalculatorHolder() {
        calculations = new ArrayList<>();
    }

    public void addCalculation(CalculationDetails calculationDetails) {
        calculations.add(calculationDetails);
        Collections.sort(calculations);
    }

    public void markDone(String id, String status) {
        for (CalculationDetails calc : calculations) {
            if (calc.id.equals(id)) {
                calc.status = status;
                calc.progressPerc = 100;
                Collections.sort(calculations);
            }
        }
    }

    public void deleteCalculation(String id) {
        for (CalculationDetails calc : calculations) {
            if (calc.id.equals(id)) {
                calculations.remove(calc);
                Collections.sort(calculations);
            }
        }
    }

    public boolean findNumberInCalculationsList(long number) {
        for (CalculationDetails calc : calculations) {
            if (calc.number == number) {
                return true;
            }
        }
        return false;
    }
}