package com.fanny.traxivity.view;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fanny.traxivity.database.activity.ActivityManager;
import com.fanny.traxivity.database.goal.DbGoal;
import com.fanny.traxivity.database.goal.GoalManager;
import com.fanny.traxivity.model.DateUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.fanny.traxivity.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by huextrat.
 */

public class WeeklyTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.weekly_tab,container,false);

        // DonutProgress weeklyCircle = (DonutProgress) v.findViewById(R.id.circle_progress_week);
        //TextView weeklyGoalTv = (TextView) v.findViewById(R.id.goal_weekly);

        Date currentDate = new Date();

        HorizontalBarChart graphChart = (HorizontalBarChart) v.findViewById(R.id.barChart);
        graphChart.setScaleEnabled(false);
        graphChart.setDragEnabled(false);
        graphChart.setPinchZoom(false);
        graphChart.getDescription().setEnabled(false);

        XAxis xAxis = graphChart.getXAxis();
        xAxis.setAxisMaximum(6f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        graphChart.getAxisLeft().setDrawGridLines(false);
        YAxis yAxisR = graphChart.getAxisRight();
        yAxisR.setDrawLabels(false); // no axis labels
        yAxisR.setDrawAxisLine(false); // no axis line
        yAxisR.setDrawGridLines(false); // no grid lines
        yAxisR.setDrawZeroLine(true); // draw a zero line

        ActivityManager managerActivity = new ActivityManager();
        GoalManager managerGoal = new GoalManager();

        final DbGoal dailyGoalSteps = managerGoal.goalStepsDaily(currentDate);
        final DbGoal dailyGoalDuration = managerGoal.goalDurationDaily(currentDate);

        List<BarEntry> entries = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date dateImpl = c.getTime();
        BarDataSet set;

        if (dailyGoalSteps != null) {
            float stepsNumber = (float) dailyGoalSteps.getStepsNumber();
           /* weeklyGoalTv.setText(Integer.toString(weeklyGoalSteps.getStepsNumber()) + " steps");
            weeklyCircle.setProgress(managerGoal.goalStatusStepsWeekly(currentDate, managerActivity.getTotalStepsDay(currentDate)));*/
            LimitLine limitLine = new LimitLine(stepsNumber, "Steps Objective");
            limitLine.setLineColor(Color.CYAN);
            yAxisR.addLimitLine(limitLine);
            yAxisR.setAxisMaximum(stepsNumber*1.5f);
            int nbsteps;
            for(int i=0;i<7;i++) {
                nbsteps = managerActivity.getTotalStepsDay(dateImpl);
                entries.add(new BarEntry((float) i, (float) nbsteps));
                dateImpl = DateUtil.addDays(dateImpl,1);
            }
            set = new BarDataSet(entries, "Steps");
        }
        else if(dailyGoalDuration != null){
            float timeDuration = (float) dailyGoalDuration.getDuration();
            timeDuration = timeDuration/3600f;
           /* weeklyGoalTv.setText(Double.toString(weeklyGoalDuration.getDuration()) + " seconds");
            weeklyCircle.setProgress(managerGoal.goalStatusDurationWeekly(currentDate, managerActivity.getTotalActivityDay(currentDate)));*/
            LimitLine limitLine = new LimitLine(timeDuration, "Time Objective");
            limitLine.setLineColor(Color.CYAN);
            yAxisR.addLimitLine(limitLine);
            yAxisR.setAxisMaximum(timeDuration*1.5f);
            float duration;
            for(int i=0;i<7;i++) {
                duration = (float) managerActivity.getTotalActivityDay(dateImpl);
                duration = duration/3600f; //TO get Hours of activity
                entries.add(new BarEntry((float) i, duration));
                dateImpl = DateUtil.addDays(dateImpl,1);
            }
            set = new BarDataSet(entries, "Time");
        }
        else {
            // weeklyGoalTv.setText("No goal set");
            set = new BarDataSet(entries, "Activity");
            yAxisR.setAxisMaximum(50000f);
        }

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        graphChart.setData(data);
        graphChart.invalidate();

        return v;
    }
}