package jvillarreal.a15;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Main Activity runs onCreate, Sets up listadapter and handlers and notification channel, they work!
 * Loads exercises from shared preferences (Bug) where it saves data twice when adding from
 * AddWorkout.java . I should have used a database to store data. Notifications resume activity
 * and timer runs in the background.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final int NOTIFICATION_ID = 0;
    private NotificationManager mNotifyManager;

    private ListView listView;
    private Exercise curExercise;
    private static ArrayList<Exercise> exercises = new ArrayList<>();
    private ArrayAdapter<Exercise> exercisesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        //creating shared pref to save arraylist of exercises
        final SharedPreferences sharedPreferences = getSharedPreferences("USER",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Set", "");

        //I believe the bug is here that adds exercise twice. On launch it loads the data that is
        //saved in the array list. But called again when we come back from
        // StartWorkout which already adds the object to the list
        if (json.equals("[]")) {
            Toast.makeText(MainActivity.this,"Empty list, Add a Workout!",Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<Exercise>>() {
            }.getType();
            List<Exercise> arrPackageData = gson.fromJson(json, type);
            if(arrPackageData != null){
                for (Exercise data : arrPackageData) {
                    exercises.add(data);
                }
            }

        }
        listView = findViewById(R.id.lvExcercises);
        exercisesAdapter = new ArrayAdapter<>(this, R.layout.list_exercise_layout, exercises);
        listView.setAdapter(exercisesAdapter);

        setupClickHandler();
        setupLongClickHandler(sharedPreferences);
        updateList(sharedPreferences);
        createNotificationChannel();

        exercisesAdapter.notifyDataSetChanged();

    }

    /**
     * Create notification channel for notifications
     */
    public void createNotificationChannel() {
        mNotifyManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager
                    .IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);

            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);

        }

    }

    /**
     * Update the exercise list when receiving from AddWorkout and Save to shared preferences
     * @param sharedPreferences The current shared preferences to save data
     */
    private void updateList(SharedPreferences sharedPreferences) {
        Intent i = getIntent();
        Gson gson = new Gson();
        //test single object passing data
        curExercise = (Exercise)i.getSerializableExtra("exrObject");
        //Update the list when we get an intent back from StartWorkout.class and save it.
        if (curExercise != null){
            Log.d(TAG, curExercise.toString());
            exercises.add(curExercise);
            String json = gson.toJson(exercises);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Set",json);
            editor.commit();
            exercisesAdapter.notifyDataSetChanged();
        }
        exercisesAdapter.notifyDataSetChanged();

    }

    /**
     * Handler for starting a specific workout in a list. Sent to the Start Workout activity
     */
    private void setupClickHandler(){
        //Listener for the list view. Handles short click and sends to Start Workout
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d(TAG, "Start Workout");
            Intent intent = new Intent(this, StartWorkout.class);
            Exercise tmpItem = exercises.get(position);
            intent.putExtra("exrObjectStart", tmpItem);
            startActivity(intent);
        });
    }

    /**
     * Remove an exercise from the list by long press. Also modifies data that is stored.
     * @param sharedPreferences To remove from saved data
     */
    private void setupLongClickHandler(SharedPreferences sharedPreferences){
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Log.d(TAG, "listener");
            Exercise tmpItem = exercises.remove(position);
            exercisesAdapter.notifyDataSetChanged();
            Gson gson = new Gson();
            String json = gson.toJson(exercises);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Set",json);
            editor.commit();
            displayToast("Exercise Removed", Toast.LENGTH_SHORT);
            return true;
        });
    }

    /**
     * Display a specifc toast
     * @param msg The message we want to display
     * @param length
     */
    public void displayToast(String msg, int length){
        Toast toast = Toast.makeText(this, msg, length);
        toast.setGravity(Gravity.CENTER, 0 , -100);
        toast.show();
    }

    /**
     * Handle when click add workout button
     * @param view The view that calls this handler
     */
    public void onBtnClick(View view) {
        Intent intent = new Intent(this, AddWorkout.class);
        startActivity(intent);
    }

}