package exercise.android.nami.coolcalculator;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

public class MyApp extends Application {
    public ArrayList<CalculationDetails> calculationDetails;
    SharedPreferences sp;
    Context context;

    public MyApp(Context context) {
        super.onCreate();
        this.context = context;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        calculationDetails = new ArrayList<>();
        calculationsFromSaved();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        calculationDetails = new ArrayList<>();
    }

    public void updateCalculations(ArrayList<CalculationDetails> calculationDetails){
        this.calculationDetails = calculationDetails;
        String itemsJson = new Gson().toJson(calculationDetails);
        sp.edit().putString("calculationDetails", itemsJson).apply();
    }

    public void calculationsFromSaved() {
        String itemsJson = sp.getString("calculationDetails", "");

        if (itemsJson.length() != 0) {
            Type listType = new TypeToken<ArrayList<CalculationDetails>>(){}.getType();
            calculationDetails = new Gson().fromJson(itemsJson, listType);
        }
    }

}
