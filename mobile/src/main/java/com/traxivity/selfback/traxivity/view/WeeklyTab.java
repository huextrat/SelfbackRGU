package com.traxivity.selfback.traxivity.view;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.traxivity.selfback.traxivity.R;
import com.traxivity.selfback.traxivity.database.activity.ActivityManager;
import com.traxivity.selfback.traxivity.database.goal.DbGoal;
import com.traxivity.selfback.traxivity.database.goal.GoalManager;
import com.traxivity.selfback.traxivity.model.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huextrat.
 */

public class WeeklyTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.weekly_tab,container,false);

        Date currentDate = new Date();

        BarChart graphChart = (BarChart) v.findViewById(R.id.barChart);
        graphChart.setScaleEnabled(false);
        graphChart.setDragEnabled(false);
        graphChart.setPinchZoom(false);
        graphChart.getDescription().setEnabled(false);

        XAxis xAxis = graphChart.getXAxis();
        xAxis.setAxisMaximum(7f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis yAxisR = graphChart.getAxisRight();
        yAxisR.setDrawLabels(false); // no axis labels
        yAxisR.setDrawAxisLine(false); // no axis line
        yAxisR.setDrawGridLines(false); // no grid lines
        yAxisR.setDrawZeroLine(true); // draw a zero line

        ActivityManager managerActivity = new ActivityManager();
        GoalManager managerGoal = new GoalManager();

        final DbGoal weeklyGoalSteps = managerGoal.goalStepsWeekly(currentDate);
        final DbGoal weeklyGoalDuration = managerGoal.goalDurationWeekly(currentDate);

        if (weeklyGoalSteps != null) {
            float stepsNumber = (float) weeklyGoalSteps.getStepsNumber();
            LimitLine limitLine = new LimitLine(stepsNumber, "Steps Objective");
            limitLine.setLineColor(Color.CYAN);
            yAxisR.addLimitLine(limitLine);
            yAxisR.setAxisMaximum(stepsNumber*1.5f);
        }
        else {
            yAxisR.setAxisMaximum(10000f);
        }

        List<BarEntry> entries = new ArrayList<>();
        Date dateImpl = currentDate;
        int nbsteps;
        for(int i=0;i<7;i++) {
            nbsteps = managerActivity.getTotalStepsDay(dateImpl);
            entries.add(new BarEntry((float) i, (float) nbsteps));
            dateImpl = DateUtil.addDays(dateImpl,1);
        }

        BarDataSet set = new BarDataSet(entries, "Activity");
        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        graphChart.setData(data);
        graphChart.setFitBars(true); // make the x-axis fit exactly all bars
        graphChart.invalidate();

        return v;
    }
}
