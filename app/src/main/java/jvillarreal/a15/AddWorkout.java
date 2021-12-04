package jvillarreal.a15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Add custom workout class
 * @author Julian Villarreal
 */
public class AddWorkout extends AppCompatActivity {
    private static final String TAG = AddWorkout.class.getName();
    //views
    EditText txtName;
    EditText txtSets;
    EditText txtRest;
    //create exercise obj
    Exercise exerciseTest = new Exercise();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
        txtName = findViewById(R.id.exrName);
        txtSets = findViewById(R.id.exrSets);
        txtRest = findViewById(R.id.exrRest);
        exerciseTest = new Exercise();

    }

    /**
     * On save, send exercise object back to main activity and save it in shared preferences.
     * @param view The button that called this handler
     */
    public void onBtnClickSave(View view) {

        if(!(txtRest.getText().toString().isEmpty() || txtSets.getText().toString().isEmpty()||
        txtName.getText().toString().isEmpty())){
            Log.d(TAG,""+Integer.parseInt(txtSets.getText().toString()));
            exerciseTest.setExerciseName(txtName.getText().toString());
            exerciseTest.setTotalSets(Integer.parseInt(txtSets.getText().toString()));
            exerciseTest.setRestSeconds(Integer.parseInt(txtRest.getText().toString()));
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("exrObject", exerciseTest);
            startActivity(intent);
        }

    }
}