package com.traxivity.selfback.traxivity.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.fitness.data.Goal;
import com.traxivity.selfback.traxivity.R;
import com.traxivity.selfback.traxivity.database.activity.ActivityManager;
import com.traxivity.selfback.traxivity.database.activity.DbActivity;
import com.traxivity.selfback.traxivity.database.goal.DbGoal;
import com.traxivity.selfback.traxivity.database.goal.GoalManager;
import com.traxivity.selfback.traxivity.database.inactivity.DbInactivity;
import com.traxivity.selfback.traxivity.model.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import at.grabner.circleprogress.AnimationState;
import at.grabner.circleprogress.AnimationStateChangedListener;
import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;
import at.grabner.circleprogress.UnitPosition;
import io.realm.Realm;

/**
 * Created by huextrat.
 */

public class DailyTab extends Fragment {
    private CircleProgressView dailyCircle;
    private ActivityManager managerActivity;
    private GoalManager managerGoal;
    private Date currentDate;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.daily_tab,container,false);

        //DonutProgress dailyCircle = (DonutProgress) v.findViewById(R.id.circle_progress_day);
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
        xAxis.setAxisMaximum(24f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis yAxisR = graphChart.getAxisRight();
        yAxisR.setDrawLabels(false); // no axis labels
        yAxisR.setDrawAxisLine(false); // no axis line
        yAxisR.setDrawGridLines(false); // no grid lines
        yAxisR.setDrawZeroLine(true); // draw a zero line

        managerActivity = new ActivityManager();
        managerGoal = new GoalManager();

        final DbGoal dailyGoalSteps = managerGoal.goalStepsDaily(currentDate);
        final DbGoal dailyGoalDuration = managerGoal.goalDurationDaily(currentDate);

         if (dailyGoalSteps != null) {
             dailyCircle.setValueAnimated(0,managerGoal.goalStatusStepsDaily(currentDate, managerActivity.getTotalStepsDay(currentDate)),2000);
             dailyCircle.setTextMode(TextMode.TEXT);
             dailyCircle.setText(managerActivity.getTotalStepsDay(currentDate)+" steps");
             dailyCircle.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(dailyCircle.getUnit().equals("%")){
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
                         dailyCircle.setUnit("%");
                         dailyCircle.setUnitColor(getResources().getColor(R.color.colorPrimary));
                         dailyCircle.setUnitVisible(true);
                         dailyCircle.setUnitScale(1);
                         dailyCircle.setUnitPosition(UnitPosition.RIGHT_TOP);
                         dailyCircle.setText(String.valueOf(managerGoal.goalStatusStepsDaily(currentDate, managerActivity.getTotalStepsDay(currentDate))));
                     }
                 }
             });
             dailyGoalTv.setText(dailyGoalSteps.getStepsNumber() + " steps");
         }
         else if(dailyGoalDuration != null){
             dailyCircle.setValueAnimated(0,managerGoal.goalStatusStepsDaily(currentDate, managerActivity.getTotalStepsDay(currentDate)),2000);
             dailyCircle.setTextMode(TextMode.TEXT);
             dailyCircle.setText(managerActivity.getTotalStepsDay(currentDate)+" steps");
             dailyCircle.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(dailyCircle.getUnit().equals("%")){
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
                         dailyCircle.setUnit("%");
                         dailyCircle.setUnitColor(getResources().getColor(R.color.colorPrimary));
                         dailyCircle.setUnitVisible(true);
                         dailyCircle.setUnitScale(1);
                         dailyCircle.setUnitPosition(UnitPosition.RIGHT_TOP);
                         dailyCircle.setText(String.valueOf(managerGoal.goalStatusDurationDaily(currentDate, managerActivity.getTotalActivityDay(currentDate))));
                     }
                 }
             });
             dailyGoalTv.setText(dailyGoalSteps.getStepsNumber() + " seconds");
         }
         else {
             dailyGoalTv.setText("No goal set");
             yAxisR.setAxisMaximum(10000f);
         }

         clearDb.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            clearDb();
        }
        });

        List<BarEntry> entries = new ArrayList<>();
        //readDB to find nb steps per hour
        Date dateImpl = currentDate;
        Integer nbsteps;
        Map<Integer, Integer> mapStepsDayByHour = managerActivity.getTotalStepsDayByHours(currentDate);
        //PROBLEM HERE
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


        BarDataSet set = new BarDataSet(entries, "Activity");
         BarData data = new BarData(set);
         data.setBarWidth(0.9f); // set custom bar width
         graphChart.setData(data);
         graphChart.setFitBars(true); // make the x-axis fit exactly all bars
         graphChart.invalidate();

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
