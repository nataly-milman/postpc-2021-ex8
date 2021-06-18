package exercise.android.nami.coolcalculator;

import java.util.Random;
import java.util.UUID;

public class Calculation {
    public long number;
    public long currentNumber;
    public long root1;
    public long root2;
    public String id;
    public String workId = "";
    public int progressPerc;

    public Calculation(long number){
        id = UUID.randomUUID().toString();
        workId="";
        this.number = number;
        root1=-1;
        root2=-1;
        currentNumber=2;
    }

    public void calculateRoots()
    {
        for(long i = 2; i <= (number / 2); i++)
        {
            if (number % i == 0)
            {
                root1 = i;
                root2 = (this.number / i);
                progressPerc = 1;
                return;
            }

            progressPerc = (int)(i / this.number);
        }
    }
}
