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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = new MyApp(this);

        numberText = findViewById(R.id.numberText);
        calculateButton = findViewById(R.id.calculateButton);

        calculateButton.setOnClickListener(view -> {
            try {
                long number = Long.parseLong(numberText.getText().toString());
                runCalculationWorker(new Calculation(number));
                numberText.setText("");
                Toast.makeText(this, Long.toString(number), Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number (maybe roo big)", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void runCalculationWorker(Calculation calculation) {
        Data.Builder dataBuilder = new Data.Builder();
        dataBuilder.putString("id", calculation.id);
        dataBuilder.putLong("number", calculation.number);
        dataBuilder.putLong("currentNumber", calculation.currentNumber);

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(CalculatorWorker.class)
                .setInputData(dataBuilder.build()).build();

        WorkManager.getInstance(this).enqueue(workRequest);
        calculation.workId = workRequest.getId().toString();

        LiveData<WorkInfo> workInfo = WorkManager.getInstance(getApplicationContext())
                .getWorkInfoByIdLiveData(workRequest.getId());
        workInfo.observeForever(workInfoObs -> {
            if (workInfoObs != null) {
                WorkInfo.State state = workInfoObs.getState();
                if (state == WorkInfo.State.SUCCEEDED) {
                    Data output = workInfoObs.getOutputData();
                    //TODO handle success
                } else if (state == WorkInfo.State.FAILED) {
                    Data output = workInfoObs.getOutputData();
                    // TODO handle failure
                }
                // TODO hadle progress bar
            }
        });
    }
}