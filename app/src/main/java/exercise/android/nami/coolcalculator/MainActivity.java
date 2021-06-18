package exercise.android.nami.coolcalculator;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    EditText numberText;
    FloatingActionButton calculateButton;
    MyApp app;
    CalculatorHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = new MyApp(this);
        holder = new CalculatorHolder();
        holder.calculations = app.calculationDetails;
        for (CalculationDetails calculationDetails : holder.calculations) {
            if (calculationDetails.status.equals("in progress")) {
                runCalculationWorker(calculationDetails);
            }
        }

        numberText = findViewById(R.id.numberText);
        calculateButton = findViewById(R.id.calculateButton);

        calculateButton.setOnClickListener(view -> {
            try {
                long number = Long.parseLong(numberText.getText().toString());
                if (holder.findNumberInCalculationsList(number)) {
                    Toast.makeText(this, "Seen this number before", Toast.LENGTH_SHORT).show();
                } else {
                    CalculationDetails calculationDetails = new CalculationDetails(number);
                    runCalculationWorker(calculationDetails);
                    holder.addCalculation(calculationDetails);
                }
                numberText.setText("");
                Toast.makeText(this, Long.toString(number), Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number (maybe too big)", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void runCalculationWorker(CalculationDetails calculationDetails) {
        Data.Builder dataBuilder = new Data.Builder();
        dataBuilder.putString("id", calculationDetails.id);
        dataBuilder.putLong("number", calculationDetails.number);
        dataBuilder.putLong("currentNumber", calculationDetails.currentNumber);

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(CalculatorWorker.class)
                .setInputData(dataBuilder.build()).build();

        WorkManager.getInstance(this).enqueue(workRequest);
        calculationDetails.workId = workRequest.getId().toString();

        LiveData<WorkInfo> workInfo = WorkManager.getInstance(getApplicationContext())
                .getWorkInfoByIdLiveData(workRequest.getId());

        workInfo.observeForever(workInfoObs -> observe(workInfoObs, calculationDetails));
    }

    private void observe(WorkInfo workInfoObs, CalculationDetails calculationDetails) {
        if (workInfoObs != null) {
            WorkInfo.State state = workInfoObs.getState();
            if (state == WorkInfo.State.SUCCEEDED) {
                Data output = workInfoObs.getOutputData();
                System.out.println(output);
                System.out.println(output.getLong("root1", 0));
                System.out.println(output.getLong("root2", 0));
                //TODO handle success
                this.holder.markDone(output.getString("id"), "done");


            } else if (state == WorkInfo.State.FAILED) {
                Data output = workInfoObs.getOutputData();
                if (output.getBoolean("notDone", false)) {
                    calculationDetails.currentNumber = output.getLong("currentNumber", 2);
                    runCalculationWorker(calculationDetails);
                } else {
                    // TODO handle failure
                    this.holder.markDone(output.getString("id"), "no roots");

                }
            }
        }
    }
}