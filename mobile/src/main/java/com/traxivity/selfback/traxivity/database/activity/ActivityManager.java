package com.traxivity.selfback.traxivity.database.activity;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * Created by huextrat.
 */

public class ActivityManager {
    private Realm realm;

    public void insertActivity(DbActivity myActivity) {
        List<DbActivity> lastAddedActivityList = new ArrayList<>();
        DbActivity lastAddedActivity;

        realm = Realm.getDefaultInstance();

        if(!getAllActivityDay(new Date()).isEmpty()){
            lastAddedActivityList = getAllActivityDay(new Date());
        }
        else {
            lastAddedActivityList.add(new DbActivity(new Date(),ActivityType.Unknown,0));
        }
        lastAddedActivity = lastAddedActivityList.get(0);

        if(!myActivity.getActivity().equals(lastAddedActivity.getActivity())) {
            DbActivity newActivity = new DbActivity(myActivity.getStartTime(), ActivityType.valueOf(myActivity.getActivity()),myActivity.getNbSteps());

            realm.beginTransaction();
            DbActivity realmActivity = realm.copyToRealm(newActivity);
            realm.commitTransaction();
        }
        else if(myActivity.getStartTime().getHours() != lastAddedActivity.getEndTime().getHours() || myActivity.getStartTime().getHours() != lastAddedActivity.getStartTime().getHours()){
            Calendar cal = Calendar.getInstance();
            cal.setTime(lastAddedActivity.getStartTime());
            cal.add(Calendar.HOUR, 1);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            Date endTime = cal.getTime();
            cal.clear();

            lastAddedActivity.setEndTime(endTime);
            lastAddedActivity.setDuration((endTime.getTime()-lastAddedActivity.getStartTime().getTime())/1000);

            realm.beginTransaction();
            removeLastActivity();
            DbActivity realmActivityLast = realm.copyToRealm(lastAddedActivity);
            realm.commitTransaction();

            DbActivity newActivity = new DbActivity(myActivity.getStartTime(), ActivityType.valueOf(myActivity.getActivity()),myActivity.getNbSteps());

            newActivity.setStartTime(endTime);
            newActivity.setDuration((newActivity.getEndTime().getTime()-endTime.getTime())/1000);

            realm.beginTransaction();
            DbActivity realmActivity = realm.copyToRealm(newActivity);
            realm.commitTransaction();
        }
        else {
            DbActivity updateActivity = new DbActivity(lastAddedActivity.getStartTime(), ActivityType.valueOf(myActivity.getActivity()),lastAddedActivity.getNbSteps()+myActivity.getNbSteps());

            updateActivity.setEndTime(myActivity.getEndTime());
            updateActivity.setDuration((myActivity.getEndTime().getTime()-lastAddedActivity.getStartTime().getTime())/1000);

            realm.beginTransaction();
            removeLastActivity();
            DbActivity realmActivity = realm.copyToRealm(updateActivity);
            realm.commitTransaction();
        }
    }

    /**
     *
     * @param wantedDate the date of the day we want to restore data
     * @return
     */
    public List<DbActivity> getAllActivityDay(Date wantedDate) {
        realm = Realm.getDefaultInstance();
        RealmResults<DbActivity> results = realm.where(DbActivity.class).equalTo("id",wantedDate.getDate()).findAllSorted("startTime", Sort.DESCENDING);
        return realm.copyFromRealm(results);
    }

    public Map<Integer, DbActivity> getAllActivityDayByHours(Date wantedDate) {
        Map<Integer, DbActivity> activityByHours = new HashMap<>();
        realm = Realm.getDefaultInstance();
        List<DbActivity> results = getAllActivityDay(wantedDate);
        for(DbActivity activity : results){
            activityByHours.put(activity.getHoursRange(),activity);
        }
        return activityByHours;
    }

    /**
     *
     * @param beginningDate From this date
     * @param endDate To this date
     * @return
     */
    public List<DbActivity> getAllActivityRangeDay(Date beginningDate, Date endDate) {
        realm = Realm.getDefaultInstance();
        RealmResults<DbActivity> results = realm.where(DbActivity.class).greaterThanOrEqualTo("id", beginningDate.getDate()).lessThanOrEqualTo("id",endDate.getDate()).findAllSorted("startTime", Sort.ASCENDING);
        return realm.copyFromRealm(results);
    }

    /**
     *
     * @param date The date of the day we want to restore the total inactivity
     * @return
     */
    public double getTotalInactivityDay(Date date){
        double result = 0;
        realm = Realm.getDefaultInstance();
        List<DbActivity> listDayActivity = getAllActivityDay(date);
        for(DbActivity activity : listDayActivity){
            if(activity.getActivity().equals(ActivityType.Inactive)){
                result = result + activity.getDuration();
            }
        }
        return result;
    }

    /**
     *
     * @param date The date of the day we want to restore the total activity
     * @return
     */
    public double getTotalActivityDay(Date date){
        double result = 0;
        realm = Realm.getDefaultInstance();
        List<DbActivity> listDayActivity = getAllActivityDay(date);
        for(DbActivity activity : listDayActivity){
            if(!activity.getActivity().equals(ActivityType.Inactive)){
                result = result + activity.getDuration();
            }
        }
        return result;
    }

    public int getTotalStepsDay(Date date){
        int result = 0;
        realm = Realm.getDefaultInstance();
        List<DbActivity> listDayActivity = getAllActivityDay(date);
        for(DbActivity activity : listDayActivity){
            if(!activity.getActivity().equals(ActivityType.Inactive)){
                result = result + activity.getNbSteps();
            }
        }
        return result;
    }

    public Map<Integer, Integer> getTotalStepsDayByHours(Date wantedDate) {
        Map<Integer, Integer> stepsDayByHours = new HashMap<>();
        realm = Realm.getDefaultInstance();
        List<DbActivity> results = getAllActivityDay(wantedDate);
        for(DbActivity activity : results){
            if(!stepsDayByHours.containsKey(activity.getHoursRange())) {
                stepsDayByHours.put(activity.getHoursRange(), activity.getNbSteps());
            }
            else {
                stepsDayByHours.put(activity.getHoursRange(), stepsDayByHours.get(activity.getHoursRange()) + activity.getNbSteps());
            }
        }
        return stepsDayByHours;
    }

    /**
     *
     * @param date The date of the day we want to restore the data
     * @param hoursRange Hours range like "1213" = 12h to 13h. Format 24h
     * @return
     */
    public List<DbActivity> getActivityThisHours(Date date, int hoursRange){
        List<DbActivity> activityThisHours = new ArrayList<>();
        realm = Realm.getDefaultInstance();
        List<DbActivity> listDayActivity = getAllActivityDay(date);
        for(DbActivity activity : listDayActivity){
            if(activity.getHoursRange() != hoursRange){
                activityThisHours.add(activity);
            }
        }
        return activityThisHours;
    }

    /**
     * Remove the latest activity stored
     */
    public void removeLastActivity(){
        realm.where(DbActivity.class).findAllSorted("startTime",Sort.ASCENDING).deleteFirstFromRealm();
    }
}
