package com.fanny.traxivity.database.stepsManagerBeta;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * Created by huextrat.
 */

public class StepsManager {
    private Realm realm;

    public void insertActivity(DbSteps mySteps) {
        List<DbSteps> lastAddedActivityList = new ArrayList<>();
        DbSteps lastAddedActivity;

        realm = Realm.getDefaultInstance();

        if(!getAllStepsDay(new Date()).isEmpty()){
            lastAddedActivityList = getAllStepsDay(new Date());
        }
        else {
            lastAddedActivityList.add(new DbSteps(new Date(),0));
        }
        lastAddedActivity = lastAddedActivityList.get(0);

        if(mySteps.getHoursRange() == lastAddedActivity.getHoursRange()) {

            DbSteps updateSteps = new DbSteps(lastAddedActivity.getStartTime(),lastAddedActivity.getNbSteps()+mySteps.getNbSteps());

            realm.beginTransaction();
            removeLastActivity();
            DbSteps realmActivity = realm.copyToRealm(updateSteps);
            realm.commitTransaction();
        }
        else if(mySteps.getHoursRange() != lastAddedActivity.getHoursRange()){
            Calendar cal = Calendar.getInstance();
            cal.setTime(lastAddedActivity.getStartTime());
            cal.add(Calendar.HOUR, 1);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            Date endTime = cal.getTime();
            cal.clear();

            DbSteps newSteps = new DbSteps(mySteps.getStartTime(),mySteps.getNbSteps());

            newSteps.setStartTime(endTime);

            realm.beginTransaction();
            DbSteps realmActivity = realm.copyToRealm(newSteps);
            realm.commitTransaction();
        }
        else {
            return;
        }
    }

    /**
     *
     * @param wantedDate the date of the day we want to restore data
     * @return
     */
    public List<DbSteps> getAllStepsDay(Date wantedDate) {
        realm = Realm.getDefaultInstance();
        RealmResults<DbSteps> results = realm.where(DbSteps.class).equalTo("id",wantedDate.getDate()).findAllSorted("startTime", Sort.DESCENDING);
        return realm.copyFromRealm(results);
    }

    public int getTotalStepsDay(Date date){
        int result = 0;
        realm = Realm.getDefaultInstance();
        List<DbSteps> listDaySteps = getAllStepsDay(date);
        for(DbSteps steps : listDaySteps){
            result = result + steps.getNbSteps();
        }
        return result;
    }

    public Map<Integer, Integer> getTotalStepsDayByHours(Date wantedDate) {
        Map<Integer, Integer> stepsDayByHours = new HashMap<>();

        List<DbSteps> results = getAllStepsDay(wantedDate);

        int lastActivityHoursRange = 999;
        int totalNbSteps = 0;
        int listSize = results.size();

        if(results.size() == 1){
            stepsDayByHours.put(results.get(0).getHoursRange(), results.get(0).getNbSteps());
        }
        else {
            for (DbSteps myActivity : results) {
                if (lastActivityHoursRange == myActivity.getHoursRange() || lastActivityHoursRange == 999) {
                    totalNbSteps = totalNbSteps + myActivity.getNbSteps();
                    lastActivityHoursRange = myActivity.getHoursRange();
                    listSize = listSize - 1;
                    if(listSize == 0){
                        stepsDayByHours.put(lastActivityHoursRange, totalNbSteps);
                    }
                } else {
                    stepsDayByHours.put(lastActivityHoursRange, totalNbSteps);
                    lastActivityHoursRange = myActivity.getHoursRange();
                    totalNbSteps = myActivity.getNbSteps();
                    listSize = listSize - 1;
                    if(listSize == 0){
                        stepsDayByHours.put(lastActivityHoursRange, totalNbSteps);
                    }
                }
            }
        }
        return stepsDayByHours;
    }

    /**
     * Remove the latest activity stored
     */
    public void removeLastActivity(){
        realm.where(DbSteps.class).findAllSorted("startTime",Sort.DESCENDING).deleteFirstFromRealm();
    }
}
