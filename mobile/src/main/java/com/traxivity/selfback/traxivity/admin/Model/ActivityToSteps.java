package com.traxivity.selfback.traxivity.admin.Model;

/**
 * Created by Alexandre on 17/05/2017.
 */

public class ActivityToSteps {
    private String activityName;

    private int numberStepsPerMinute;

    public ActivityToSteps(String activityName, int numberStepsPerMinute) {
        this.activityName = activityName;
        this.numberStepsPerMinute = numberStepsPerMinute;
    }

    public int getSteps(int minuteNumber){
        return numberStepsPerMinute * minuteNumber;
    }

    public String getActivityName() {
        return activityName;
    }

    public int getNumberStepsPerMinute() {
        return numberStepsPerMinute;
    }
}

