package exercise.android.nami.coolcalculator;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class CalculatorWorker extends Worker {
    public static final int MAX_TIME = 900000;  // 15 minutes = 900000 ms

    public CalculatorWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        setProgressAsync(new Data.Builder().putInt("progress", 0).build());
    }

    @NonNull
    @Override
    public Result doWork() {
        Data.Builder dataBuilder = new Data.Builder();

        long startTime = System.currentTimeMillis();
        String id = getInputData().getString("id");
        long number = getInputData().getLong("number", 0);
        long currentNumber = getInputData().getLong("currentNumber", 2);

        dataBuilder.putString("id", id);
        dataBuilder.putLong("number", number);

        System.out.println("Got work on " + number);
        for (long i = currentNumber; i <= (number / 2); i++) {
            if (number % i == 0) {
                dataBuilder.putLong("root1", i);
                dataBuilder.putLong("root2",number / i);
                return Result.success(dataBuilder.build());
            }
            if((System.currentTimeMillis() - startTime) >= MAX_TIME) {
                dataBuilder.putLong("currentNumber", i);
                dataBuilder.putBoolean("notDone", true);
                return Result.failure(dataBuilder.build());
            }
        }
        dataBuilder.putBoolean("notDone", false);
        return Result.failure(dataBuilder.build());
    }


}
