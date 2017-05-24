package com.traxivity.selfback.traxivity.database.goal;

import android.util.Log;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by huextrat.
 */

public class GoalManager {
    private Realm realm;

    public void insertGoal(DbGoal myGoal) {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        DbGoal realmGoal = realm.copyToRealm(myGoal);
        realm.commitTransaction();
    }

    public List<DbGoal> getGoal(Date beginningDate) {
        realm = Realm.getDefaultInstance();
        RealmResults<DbGoal> results = realm.where(DbGoal.class).equalTo("id",beginningDate.getDate()).findAllSorted("beginningDate", Sort.ASCENDING);
        return realm.copyFromRealm(results);
    }


    public DbGoal goalStepsDaily(Date beginningDate){
        List<DbGoal> listGoal = getGoal(beginningDate);
        for(DbGoal myGoal : listGoal){
            if(myGoal.getType().equals("Steps") && (myGoal.getEndingDate().getDate()-myGoal.getBeginningDate().getDate()) == 1){
                return myGoal;
            }
        }
        return null;
    }

    public DbGoal goalStepsWeekly(Date beginningDate){
        List<DbGoal> listGoal = getGoal(beginningDate);
        for(DbGoal myGoal : listGoal){
            if(myGoal.getType().equals("Steps") && (myGoal.getEndingDate().getDate()-myGoal.getBeginningDate().getDate()) == 7){
                return myGoal;
            }
        }
        return null;
    }

    public int goalStatusStepsDaily(Date beginningDate, int steps){
        List<DbGoal> listGoal = getGoal(beginningDate);
        for(DbGoal myGoal : listGoal){
            if(myGoal.getType().equals("Steps") && (myGoal.getEndingDate().getDate()-myGoal.getBeginningDate().getDate()) == 1){
                return (int) (((double)steps/myGoal.getStepsNumber())*100);
            }
        }
        return 0;
    }

    public int goalStatusStepsWeekly(Date beginningDate, int steps){
        List<DbGoal> listGoal = getGoal(beginningDate);
        for(DbGoal myGoal : listGoal){
            if(myGoal.getType().equals("Steps") && (myGoal.getEndingDate().getDate()-myGoal.getBeginningDate().getDate()) == 7){
                return (int) (((double)steps/myGoal.getStepsNumber())*100);
            }
        }
        return 0;
    }

    public int goalStatusDurationDaily(Date beginningDate, double duration){
        List<DbGoal> listGoal = getGoal(beginningDate);
        for(DbGoal myGoal : listGoal){
            if(myGoal.getType().equals("Duration") && (myGoal.getEndingDate().getDate()-myGoal.getBeginningDate().getDate()) == 1){
                return (int) ((duration/myGoal.getStepsNumber())*100);
            }
        }
        return 0;
    }

    public int goalStatusDurationWeekly(Date beginningDate, double duration){
        List<DbGoal> listGoal = getGoal(beginningDate);
        for(DbGoal myGoal : listGoal){
            if(myGoal.getType().equals("Duration") && (myGoal.getEndingDate().getDate()-myGoal.getBeginningDate().getDate()) == 7){
                return (int) ((duration/myGoal.getStepsNumber())*100);
            }
        }
        return 0;
    }

    public DbGoal goalDurationDaily(Date beginningDate){
        List<DbGoal> listGoal = getGoal(beginningDate);
        for(DbGoal myGoal : listGoal){
            if(myGoal.getType().equals("Duration") && (myGoal.getEndingDate().getDate()-myGoal.getBeginningDate().getDate()) == 1){
                return myGoal;
            }
        }
        return null;
    }

    public DbGoal goalDurationWeekly(Date beginningDate){
        List<DbGoal> listGoal = getGoal(beginningDate);
        for(DbGoal myGoal : listGoal){
            if(myGoal.getType().equals("Duration") && (myGoal.getEndingDate().getDate()-myGoal.getBeginningDate().getDate()) == 7){
                return myGoal;
            }
        }
        return null;
    }
}
