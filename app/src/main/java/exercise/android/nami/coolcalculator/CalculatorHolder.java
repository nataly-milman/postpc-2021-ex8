package exercise.android.nami.coolcalculator;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Collections;

public class CalculatorHolder extends Activity {

    public ArrayList<CalculationDetails> calculations;

    public CalculatorHolder() {
        calculations = new ArrayList<>();
    }

    public void addCalculation(CalculationDetails calculationDetails) {
        calculations.add(calculationDetails);
        Collections.sort(calculations);
    }

    public int indexOf(CalculationDetails calculationDetails){
        return calculations.indexOf(calculationDetails);
    }

    public void markDone(String id, String status) {
        for (int i = 0; i < calculations.size(); i++) {
            if (calculations.get(i).id.equals(id)) {
                calculations.get(i).status = status;
                calculations.get(i).progressPerc = 100;
                Collections.sort(calculations);
            }
        }
    }

    public void deleteCalculation(String id) {
        for (int i = 0; i < calculations.size(); i++) {
            if (calculations.get(i).id.equals(id)) {
                calculations.remove(calculations.get(i));
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