package exercise.android.nami.coolcalculator;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

public class MyApp extends Application {
    public ArrayList<CalculationDetails> calculationDetails;
    SharedPreferences sp;
    Context context;

    public MyApp(Context context) {
        super.onCreate();
        this.context = context;
        this.sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        calculationDetails = new ArrayList<>();
    }

}
