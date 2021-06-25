package exercise.android.nami.coolcalculator;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

public class CalculationDetails implements Serializable, Comparable<CalculationDetails> {
    public long number;
    public long currentNumber;
    public long root1;
    public long root2;
    public String id;
    public UUID workId;
    public String status = "in progress";
    public double progressPerc;

    public CalculationDetails(long number) {
        id = UUID.randomUUID().toString();
        this.number = number;
        root1 = -1;
        root2 = -1;
        currentNumber = 2;
    }

    @Override
    public int compareTo(CalculationDetails o) {
        if (this.status.equals("in progress")) {
            return  (!o.status.equals("in progress") || (this.number <= o.number))?  -1 : 1;
        } else if (o.status.equals("in progress") && !this.status.equals("in progress") ) {
            return 1;
        } else return (this.number > o.number) ? 1 : -1;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        String res = "";
        if (this.status.equals("in progress")) {
            res = "Calculating roots for " + this.number + "(" + this.progressPerc + "%)";
        } else if (this.status.equals("done")) {
            res = "Roots for " + this.number + " = " + this.root1 + "*" + this.root2;
        } else if (this.status.equals("prime")) {
            res = "No roots for " + this.number + ", it's prime!";
        }
        return res;
    }
}
