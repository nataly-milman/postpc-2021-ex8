package exercise.android.nami.coolcalculator;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class CalculatorWorker extends Worker {
    public CalculatorWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        setProgressAsync(new Data.Builder().putInt("progress", 0).build());
    }

    @NonNull
    @Override
    public Result doWork() {
        // todo find roots
        System.out.println("Got work");
        return null;
    }
}
