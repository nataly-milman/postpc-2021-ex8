package exercise.android.nami.coolcalculator;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity {
    EditText numberText;
    FloatingActionButton calculateButton;
    MyApp app;
    CalculatorHolder holder;
    RecyclerView recyclerRoots;
    CalculatorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = new MyApp(this);
        holder = new CalculatorHolder();
        if (app.calculationDetails != null) {
            holder.calculations = app.calculationDetails;
        }
        adapter = new CalculatorAdapter(holder, WorkManager.getInstance(MainActivity.this),app);


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
                    adapter.notifyItemInserted(holder.indexOf(calculationDetails));
                }
                numberText.setText("");
                Toast.makeText(this, Long.toString(number), Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number (maybe too big)", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerRoots = findViewById(R.id.recyclerRoots);
        recyclerRoots.setAdapter(adapter);
        recyclerRoots.setLayoutManager(new LinearLayoutManager(this));
        recyclerRoots.addItemDecoration(new DividerItemDecoration(this, VERTICAL));

        for (CalculationDetails calculationDetails : holder.calculations) {
            if (calculationDetails.status.equals("in progress")) {
                runCalculationWorker(calculationDetails);
            }
        }

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
            Data output = workInfoObs.getOutputData();
            if (state == WorkInfo.State.SUCCEEDED) {
                calculationDetails.root1 =  output.getLong("root1", 0);
                calculationDetails.root2 = output.getLong("root2", 0);
                holder.markDone(calculationDetails.id,"done");
                holder.markDone(output.getString("id"), "done");
                adapter.notifyDataSetChanged();
                ViewHolder viewHolder = (ViewHolder) recyclerRoots.
                        findViewHolderForLayoutPosition(holder.indexOf(calculationDetails));
                if (viewHolder != null) {
                    // fails if asserted
                    viewHolder.setCompleteView(calculationDetails);
                }
            } else if (state == WorkInfo.State.FAILED) {
                if (output.getBoolean("notDone", false)) {
                    calculationDetails.currentNumber = output.getLong("currentNumber", 2);
                    runCalculationWorker(calculationDetails);
                } else {
                    holder.markDone(output.getString("id"), "no roots");
                    calculationDetails.status = "prime";
                    holder.markDone(calculationDetails.id,"prime");
                    adapter.notifyDataSetChanged();
                    adapter.notifyDataSetChanged();
                    ViewHolder viewHolder = (ViewHolder) recyclerRoots.
                            findViewHolderForLayoutPosition(holder.indexOf(calculationDetails));
                    if (viewHolder != null) {
                        // fails if asserted
                        viewHolder.setCompleteView(calculationDetails);
                    }
                }
            }

            // TODO add progress
        }
    }
}