package exercise.android.nami.coolcalculator;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    ConstraintLayout calculationRow;
    TextView showNumberText;
    ImageView deleteButton;
    ProgressBar progressBar;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        this.calculationRow = itemView.findViewById(R.id.calculationRow);
        this.showNumberText = itemView.findViewById(R.id.showNumberText);
        this.deleteButton = itemView.findViewById(R.id.deleteButton);
        this.progressBar = itemView.findViewById(R.id.progressBar);
    }

    public void setCompleteView(CalculationDetails calculationDetails) {
        showNumberText.setText(calculationDetails.toString());
    }

}