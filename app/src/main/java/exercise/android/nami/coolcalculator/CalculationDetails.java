package exercise.android.nami.coolcalculator;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

public class CalculationDetails implements Serializable, Comparable<CalculationDetails> {
    public long number;
    public long currentNumber;
    public long root1;
    public long root2;
    public String id;
    public String workId = "";
    public String status = "";
    public int progressPerc;

    public CalculationDetails(long number) {
        id = UUID.randomUUID().toString();
        workId = "";
        this.number = number;
        root1 = -1;
        root2 = -1;
        currentNumber = 2;
        status = "in progress";
    }

    @Override
    public int compareTo(CalculationDetails o) {
        if (this.status.equals("in progress")) {
            if (!o.status.equals("in progress") || (this.number <= o.number)) {
                return -1;
            } else {
                return 1;
            }
        } else if (o.status.equals("in progress") && !this.status.equals("in progress") ) {
                return 1;
        } else if (this.number > o.number) {
            return 1;
        } else {
            return -1;
        }
    }
}
