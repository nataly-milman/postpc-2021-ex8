package exercise.android.nami.coolcalculator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CalculatorAdapter extends RecyclerView.Adapter<CalculatorAdapter.ViewHolder> {
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
    public CalculatorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_calculation_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CalculatorAdapter.ViewHolder holder, int position) {
        CalculationDetails calculationDetails = this.holder.calculations.get(holder.getLayoutPosition());
        holder.TextCalc.setText(holder.CalcToString(calculationDetails));
        holder.deleteButton.setOnClickListener(view -> {
            if (calculationDetails.status.equals("in progress")) {
                workManager.cancelWorkById(UUID.fromString(calculationDetails.workId));
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout calculationRow;
        TextView TextCalc;
        ImageView deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.calculationRow = itemView.findViewById(R.id.calculationRow);
            this.TextCalc = itemView.findViewById(R.id.showNumberText);
            this.deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void SetViewsToComplete(CalculationDetails calculationDetails) {
            TextCalc.setText(CalcToString(calculationDetails));
        }

        public String CalcToString(CalculationDetails calculationDetails) {
            String res = "";
            if (calculationDetails.status.equals("in progress")) {
                res = "Calculating roots for " + calculationDetails.number + "(" + calculationDetails.progressPerc + "%)";
            } else if (calculationDetails.status.equals("done")) {
                res = "Roots for " + calculationDetails.number + " = " + calculationDetails.root1 + "*" + calculationDetails.root2;
            } else if (calculationDetails.status.equals("prime")) {
                res = "No roots for " + calculationDetails.number + ", it's prime!";
            }
            return res;
        }

    }
}