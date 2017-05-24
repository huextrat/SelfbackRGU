package com.traxivity.selfback.traxivity.model;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.traxivity.selfback.traxivity.database.activity.ActivityManager;
import com.traxivity.selfback.traxivity.database.activity.ActivityType;
import com.traxivity.selfback.traxivity.database.activity.DbActivity;

import java.util.Date;

/**
 * Created by Sadiq on 01/03/2017.
 */

public class ActivityRecogniserService extends IntentService {

    private int steps = 1000;
    private Handler handler;
    private long lastDateTime = new Date().getTime();

    public ActivityRecogniserService(){
        super("ActivityRecogniserService");
    }

    public void onCreate(){
        super.onCreate();

        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("Handling Intent");
        if (ActivityRecognitionResult.hasResult(intent)){
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            DetectedActivity activity = result.getMostProbableActivity();
            String strActivity = getActivityName(activity.getType());

            if(activity.getConfidence() > 70) {
                System.out.println("Activity: " + strActivity);

                //--------------------------------------------------------------------------
                ActivityManager manager = new ActivityManager();
                if (!strActivity.equals("Unknown")) {
                    Date d = new Date();
                    DbActivity myActivity = new DbActivity(d, ActivityType.valueOf(strActivity), steps);
                    manager.insertActivity(myActivity);
                }
                //--------------------------------------------------------------------------

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                DisplayToast toast = new DisplayToast(context, strActivity, duration);
                handler.post(toast);
            }
        }

    }

    private String getActivityName(int activity){
        switch(activity){
            //case DetectedActivity.IN_VEHICLE: return "In Vehicle";
            //case DetectedActivity.ON_BICYCLE: return "Cycling";
            case DetectedActivity.RUNNING: return "Running";
            case DetectedActivity.WALKING: return "Walking";
            case DetectedActivity.STILL: return "Inactive";
            case DetectedActivity.ON_FOOT: return "OnFoot";
            default: return "Unknown";
        }
    }
}
