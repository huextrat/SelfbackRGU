package com.fanny.traxivity.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fanny.traxivity.database.activity.ActivityManager;
import com.fanny.traxivity.database.activity.DbActivity;
import com.fanny.traxivity.database.goal.DbGoal;
import com.fanny.traxivity.database.goal.GoalManager;
import com.fanny.traxivity.database.inactivity.DbInactivity;
import com.fanny.traxivity.model.DateUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.fanny.traxivity.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;
import at.grabner.circleprogress.UnitPosition;
import io.realm.Realm;

/**
 * Created by huextrat.
 */

public class DailyTab extends Fragment {
    private BarDataSet set;
    private CircleProgressView dailyCircle;
    private ActivityManager managerActivity;
    private GoalManager managerGoal;
    private Date currentDate;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.daily_tab,container,false);

        dailyCircle = (CircleProgressView)v.findViewById(R.id.circleView);
        dailyCircle.setBarColor(getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorPrimary));

        Button clearDb = (Button) v.findViewById(R.id.button_clear);
        TextView dailyGoalTv = (TextView) v.findViewById(R.id.goal_daily);

        currentDate = new Date();

        BarChart graphChart = (BarChart) v.findViewById(R.id.barChart);
        graphChart.setScaleEnabled(false);
        graphChart.setDragEnabled(false);
        graphChart.setPinchZoom(false);
        graphChart.getDescription().setEnabled(false);

        XAxis xAxis = graphChart.getXAxis();
        xAxis.setAxisMaximum(23f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        graphChart.getAxisLeft().setDrawGridLines(false);
        YAxis yAxisR = graphChart.getAxisRight();
        yAxisR.setDrawLabels(false); // no axis labels
        yAxisR.setDrawAxisLine(false); // no axis line
        yAxisR.setDrawGridLines(false); // no grid lines
        yAxisR.setDrawZeroLine(true); // draw a zero line

        managerActivity = new ActivityManager();
        managerGoal = new GoalManager();

        final DbGoal dailyGoalSteps = managerGoal.goalStepsDaily(currentDate);
        final DbGoal dailyGoalDuration = managerGoal.goalDurationDaily(currentDate);

        List<BarEntry> entries = new ArrayList<>();

         if (dailyGoalSteps != null) {
             dailyCircle.setValueAnimated(0,managerGoal.goalStatusStepsDaily(currentDate, managerActivity.getTotalStepsDay(currentDate)),2000);
             dailyCircle.setTextMode(TextMode.TEXT);
             dailyCircle.setText(managerActivity.getTotalStepsDay(currentDate)+" steps");
             dailyCircle.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(dailyCircle.getUnit().equals(" %")){
                         dailyCircle.setValueAnimated(0,managerGoal.goalStatusStepsDaily(currentDate, managerActivity.getTotalStepsDay(currentDate)),2000);
                         dailyCircle.setUnit("");
                         dailyCircle.setAutoTextSize(true);
                         dailyCircle.setUnitVisible(false);
                         dailyCircle.setValueAnimated(0,managerGoal.goalStatusStepsDaily(currentDate, managerActivity.getTotalStepsDay(currentDate)),2000);
                         dailyCircle.setTextMode(TextMode.TEXT);
                         dailyCircle.setText(managerActivity.getTotalStepsDay(currentDate)+" steps");
                     }
                     else {
                         dailyCircle.setValueAnimated(0,managerGoal.goalStatusStepsDaily(currentDate, managerActivity.getTotalStepsDay(currentDate)),2000);
                         dailyCircle.setTextMode(TextMode.PERCENT);
                         dailyCircle.setUnitSize(200);
                         dailyCircle.setAutoTextSize(true);
                         dailyCircle.setUnit(" %");
                         dailyCircle.setUnitColor(getResources().getColor(R.color.colorPrimary));
                         dailyCircle.setUnitVisible(true);
                         dailyCircle.setUnitScale(1);
                         dailyCircle.setUnitPosition(UnitPosition.RIGHT_TOP);
                         dailyCircle.setText(String.valueOf(managerGoal.goalStatusStepsDaily(currentDate, managerActivity.getTotalStepsDay(currentDate))));
                     }
                 }
             });
             dailyGoalTv.setText(dailyGoalSteps.getStepsNumber() + " steps");
             yAxisR.setAxisMaximum(dailyGoalSteps.getStepsNumber()*2f);

             Integer nbsteps;
             Map<Integer, Integer> mapStepsDayByHour = managerActivity.getTotalStepsDayByHours(currentDate);
             for(Integer i=0;i<24;i++) {
                 nbsteps = mapStepsDayByHour.get(i);

                 if(nbsteps == null) {
                     entries.add(new BarEntry((float) i, 0f));
                     Log.w("Nbsteps",i.toString());

                 }else {
                     entries.add(new BarEntry((float) i, (float) nbsteps));
                     Log.w("Nbsteps",nbsteps.toString());
                 }
             }
             set = new BarDataSet(entries, "Steps");
         }
         else if(dailyGoalDuration != null){
             dailyCircle.setValueAnimated(0,managerGoal.goalStatusStepsDaily(currentDate, managerActivity.getTotalStepsDay(currentDate)),2000);
             dailyCircle.setTextMode(TextMode.TEXT);
             dailyCircle.setText(managerActivity.getTotalStepsDay(currentDate)+" steps");
             dailyCircle.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(dailyCircle.getUnit().equals(" %")){
                         dailyCircle.setValueAnimated(0,managerGoal.goalStatusDurationDaily(currentDate, managerActivity.getTotalActivityDay(currentDate)),2000);
                         dailyCircle.setUnit("");
                         dailyCircle.setTextSize(100);
                         dailyCircle.setUnitVisible(false);
                         dailyCircle.setValueAnimated(0,managerGoal.goalStatusDurationDaily(currentDate, managerActivity.getTotalActivityDay(currentDate)),2000);
                         dailyCircle.setTextMode(TextMode.TEXT);
                         dailyCircle.setText(managerActivity.getTotalStepsDay(currentDate)+" seconds");
                     }
                     else {
                         dailyCircle.setValueAnimated(0,managerGoal.goalStatusDurationDaily(currentDate, managerActivity.getTotalActivityDay(currentDate)),2000);
                         dailyCircle.setTextMode(TextMode.PERCENT);
                         dailyCircle.setUnitSize(200);
                         dailyCircle.setAutoTextSize(true);
                         dailyCircle.setUnit(" %");
                         dailyCircle.setUnitColor(getResources().getColor(R.color.colorPrimary));
                         dailyCircle.setUnitVisible(true);
                         dailyCircle.setUnitScale(1);
                         dailyCircle.setUnitPosition(UnitPosition.RIGHT_TOP);
                         dailyCircle.setText(String.valueOf(managerGoal.goalStatusDurationDaily(currentDate, managerActivity.getTotalActivityDay(currentDate))));
                     }
                 }
             });
             dailyGoalTv.setText(dailyGoalSteps.getStepsNumber() + " seconds");

             yAxisR.setAxisMaximum((float)dailyGoalDuration.getDuration()*2f);

             Integer activityTime;
             Map<Integer, Integer> mapTimeDayByHour = managerActivity.getTotalTimeDayByHours(currentDate);
             for(Integer i=0;i<24;i++) {
                 activityTime = mapTimeDayByHour.get(i);

                 if(activityTime == null) {
                     entries.add(new BarEntry((float) i, 0f));
                     Log.w("Nbsteps",i.toString());

                 }else {
                     entries.add(new BarEntry((float) i, (float) activityTime));
                     Log.w("Nbsteps",activityTime.toString());
                 }
             }

             set = new BarDataSet(entries, "Time");
         }
         else {
             dailyGoalTv.setText("No goal set");
             set = new BarDataSet(entries, "Activity");
             yAxisR.setAxisMaximum(10000f);
         }


        clearDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearDb();
            }
        });

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        graphChart.setData(data);
        graphChart.setDrawGridBackground(false);
        graphChart.invalidate();

        List<DbActivity> list = managerActivity.getAllActivityDay(currentDate);
        for(DbActivity activity : list){
            Log.d("testActivity", activity.getActivity()+" - "+activity.getStartTime()+ " - "+ activity.getEndTime());
        }

        List<DbGoal> listGoal = managerGoal.getAllGoal();
        for(DbGoal goal : listGoal){
            Log.d("testGoal", goal.getBeginningDate()+" - "+goal.getStepsNumber());
        }

        return v;
    }

    public void clearDb() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(DbActivity.class);
        realm.delete(DbGoal.class);
        realm.delete(DbInactivity.class);
        realm.commitTransaction();
    }
}
