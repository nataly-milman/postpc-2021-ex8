package exercise.android.nami.coolcalculator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import org.jetbrains.annotations.NotNull;

public class CalculatorAdapter extends RecyclerView.Adapter<ViewHolder> {
    public WorkManager workManager;
    public CalculatorHolder holder;
    MyApp app;

    public CalculatorAdapter(CalculatorHolder calcHolder, WorkManager workManager, MyApp app) {
        this.workManager = workManager;
        this.holder = calcHolder;
        this.app = app;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_calculation_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        CalculationDetails calculationDetails = this.holder.calculations.get(holder.getLayoutPosition());
        holder.showNumberText.setText(calculationDetails.toString());
        holder.deleteButton.setOnClickListener(view -> {
            if (calculationDetails.status.equals("in progress")) {
                workManager.cancelWorkById(calculationDetails.workId);
            }
            this.holder.deleteCalculation(calculationDetails.id);
            app.updateCalculations(this.holder.calculations);
            notifyItemRangeRemoved(holder.getLayoutPosition(), 1);
        });
    }

    @Override
    public int getItemCount() {
        return holder.calculations.size();
    }


}