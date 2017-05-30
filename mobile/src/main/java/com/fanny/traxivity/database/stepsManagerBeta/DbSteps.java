package com.fanny.traxivity.database.stepsManagerBeta;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by huextrat.
 */

public class DbSteps extends RealmObject {

    private int id;
    private Date startTime;
    private int hoursRange;
    private int nbSteps;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getHoursRange() {
        return hoursRange;
    }
    public void setHoursRange(int nbSteps) {
        this.hoursRange = hoursRange;
    }

    public int getNbSteps() {
        return nbSteps;
    }
    public void setNbSteps(int nbSteps) {
        this.nbSteps = nbSteps;
    }

    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public DbSteps(Date startTime, int nbSteps) {
        this.id = startTime.getDate();
        this.hoursRange = startTime.getHours();
        this.startTime = startTime;
        this.nbSteps = nbSteps;
    }

    public DbSteps(){

    }
}