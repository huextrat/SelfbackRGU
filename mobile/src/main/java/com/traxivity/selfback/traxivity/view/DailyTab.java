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

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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

import io.realm.Realm;

/**
 * Created by extra on 22/05/2017.
 */

public class DailyTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.daily_tab,container,false);

        DonutProgress dailyCircle = (DonutProgress) v.findViewById(R.id.circle_progress_day);
        Button clearDb = (Button) v.findViewById(R.id.button_clear);
        TextView dailyGoalTv = (TextView) v.findViewById(R.id.goal_daily);

        Date testDate = new Date(2017,05,24);
        Date currentDate = new Date();

        ActivityManager managerActivity = new ActivityManager();
        List<DbActivity> a = managerActivity.getAllActivityRangeDay(testDate,currentDate);
        for(DbActivity activity : a){
            Log.d("test",activity.getActivity()+" - "+activity.getStartTime() + " - "+ activity.getEndTime() + " - "+activity.getDuration());
        }
        GoalManager managerGoal = new GoalManager();

        final DbGoal dailyGoalSteps = managerGoal.goalStepsDaily(currentDate);
        final DbGoal dailyGoalDuration = managerGoal.goalDurationDaily(currentDate);

         if (dailyGoalSteps != null) {
            dailyGoalTv.setText(Integer.toString(dailyGoalSteps.getStepsNumber()) + " steps");
            dailyCircle.setProgress(managerGoal.goalStatusStepsDaily(currentDate, managerActivity.getTotalStepsDay(currentDate)));
         }
         else if(dailyGoalDuration != null){
            dailyGoalTv.setText(Double.toString(dailyGoalDuration.getDuration()) + " seconds");
            dailyCircle.setProgress(managerGoal.goalStatusDurationDaily(currentDate, managerActivity.getTotalActivityDay(currentDate)));
         }
         else {
            dailyGoalTv.setText("No goal set");
         }

         clearDb.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            clearDb();
        }
        });

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
         LimitLine limitLine = new LimitLine(6000f, "Steps Objective");
         limitLine.setLineColor(Color.CYAN);
         yAxisR.addLimitLine(limitLine);
         yAxisR.setAxisMaximum(10000f);

         List<BarEntry> entries = new ArrayList<>();
         //readDB to find nb steps per hour
         Date dateImpl = currentDate;
         int nbsteps;
         for(int i=0;i<7;i++) {
         nbsteps = managerActivity.getTotalStepsDay(dateImpl);
         entries.add(new BarEntry((float) i, (float) nbsteps));
         dateImpl = DateUtil.addDays(dateImpl,1);
         }

         for(int i=0; i<24; i++){
         /**dayMap.get(i);
         hours.add(i,);**/
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
