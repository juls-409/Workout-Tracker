package jvillarreal.a15;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Class to start a workout on short click. Sets up a timer/sets/name
 * based on Exercise object attributes. Notifications are working, they are pushed when rest
 * period is done. They resume app with no bugs. Notification built here.
 * @author Julian Villarreal
 */
public class StartWorkout extends AppCompatActivity {
    //for debugging
    private static final String TAG = StartWorkout.class.getName();

    //for notifications
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final int NOTIFICATION_ID = 0;
    private NotificationManager mNotifyManager;

    //for views
    private TextView txtViewName;
    private TextView txtViewCurrentSet;
    private TextView txtViewTimer;

    //create object with attributes from one that was clicked in list view
    private Exercise curExercise;

    //For Timer
    private CountDownTimer timer;
    private int curExerciseTime;
    private boolean started = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_workout);
        createNotificationChannel();
        //Get intent from main activity to grab obj data from the one that was clicked on
        Intent i = getIntent();

        curExercise = new Exercise();

        Log.d(TAG, ""+curExercise.getCurrentSet());
        //to change text in timer
        txtViewTimer = findViewById(R.id.txtViewTimer);
        //copy obj attributes from one that was passed in
        curExercise = (Exercise)i.getSerializableExtra("exrObjectStart");
        //set up view with obj attb
        txtViewName = findViewById(R.id.txtViewName);
        txtViewCurrentSet = findViewById(R.id.txtViewCurrentSet);
        txtViewName.setText(curExercise.getExerciseName());

        String currentSet = Integer.toString(curExercise.getCurrentSet());
        txtViewCurrentSet.setText(currentSet);
        curExerciseTime = curExercise.getRestSeconds();
        //displayed when a user hasnt finished a set
        txtViewTimer.setText("Finish a set to start the timer!");

    }

    /**
     * display a toast
     * @param msg message we want to display
     * @param length the length of time to display
     */
    public void displayToast(String msg, int length){
        Toast toast = Toast.makeText(this, msg, length);
        toast.setGravity(Gravity.BOTTOM, 0 , 0);
        toast.show();
    }

    /**
     * Handler for Reset and Increment
     * @param view the button that calls the handler
     */
    public void onBtnClickStartW(View view) {
        switch (view.getId()){
            //reset to 0
            case R.id.btnReset:
                //cancel the current ongoing timer
                timerCancel();
                curExercise.resetCurrentSets();
                txtViewCurrentSet.setText(Integer.toString(curExercise.getCurrentSet()));
                break;
            case R.id.btnIncrease:
                //starts rest timer
                timerStart();
                displayToast("Background Timer Started", Toast.LENGTH_SHORT);
                curExercise.incrementCurrentSet();
                Log.d(TAG, ""+curExercise.getCurrentSet());
                txtViewCurrentSet.setText(Integer.toString(curExercise.getCurrentSet()));
                break;
        }
    }

    /**
     * Helper method to cancel timer
     */
    private void timerCancel(){
        if(started) {
            timer.cancel();
            txtViewTimer.setText("Finish a set to start the timer!");
        }
    }

    /**
     * Starts a timer that can run in the background. When finished, sends a push notification
     */
    public void timerStart() {
        if(started)
            timer.cancel();
        timer = new CountDownTimer(curExerciseTime*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                txtViewTimer.setText("Seconds remaining: " + millisUntilFinished / 1000+"s");
            }

            public void onFinish() {
                txtViewTimer.setText("Rest period over\nStart your next set!");
                sendNotification();

            }
        }.start();
        timer.start();
        started = true;
    }

    /**
     * Builder for notification
     * @return a built notification
     */
    private NotificationCompat.Builder getNotificationBuilder(){
        Intent notificationIntent = new Intent(this, StartWorkout.class);
        //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                //NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int)System.currentTimeMillis(), notificationIntent, 0);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Rest Interval Done!")
                .setContentText("Start your next set.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_android);
        return notifyBuilder;

    }

    /**
     * Send a notification method
     */
    public void sendNotification(){
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    /**
     * To create the notification channel
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

}