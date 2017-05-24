package com.traxivity.selfback.traxivity.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.traxivity.selfback.traxivity.R;
import com.traxivity.selfback.traxivity.database.goal.DbGoal;
import com.traxivity.selfback.traxivity.database.goal.GoalManager;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by jbjourget on 02/05/2017.
 */

public class textDialogAccept extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        String fullmessage= "You planned to do " ;
        final View view = inflater.inflate(R.layout.text_dialog, null);
        TextView tv1 = (TextView) view.findViewById(R.id.textView);
        if(GoalInputActivity.nbSteps != null){
            if(GoalInputActivity.isWeek){
                fullmessage = fullmessage+ GoalInputActivity.nbSteps+" steps for a week do you agree to commit to the set objective ?";
            }else{
                fullmessage = fullmessage+ GoalInputActivity.nbSteps+" steps for a day do you agree to commit to the set objective ?";
            }
        }else{
            if(!GoalInputActivity.isWeek){
                fullmessage = fullmessage+ GoalInputActivity.nbHours+" h and "+ GoalInputActivity.nbMin+" min for a day do you agree to commit to the set objective ?";
            }else{
                fullmessage = fullmessage+ GoalInputActivity.nbHours+" h and "+ GoalInputActivity.nbMin+" min for a week do you agree to commit to the set objective ?";
            }
        }

        builder.setView(view)
                .setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        GoalManager manager = new GoalManager();
                        DbGoal newGoal = new DbGoal();

                        if(GoalInputActivity.nbSteps == null){
                            if(GoalInputActivity.isWeek){
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(GoalInputActivity.dateD);
                                cal.add(Calendar.DAY_OF_WEEK, 7);
                                Date endTime = cal.getTime();

                                manager.insertGoal(newGoal.withDuration(GoalInputActivity.dateD,endTime,GoalInputActivity.nbHours*3600+GoalInputActivity.nbMin*60));
                            }
                            else {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(GoalInputActivity.dateD);
                                cal.add(Calendar.DAY_OF_WEEK, 1);
                                Date endTime = cal.getTime();

                                manager.insertGoal(newGoal.withDuration(GoalInputActivity.dateD,endTime,GoalInputActivity.nbHours*3600+GoalInputActivity.nbMin*60));
                            }
                        }
                        else {
                            if(GoalInputActivity.isWeek){
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(GoalInputActivity.dateD);
                                cal.add(Calendar.DAY_OF_WEEK, 7);
                                Date endTime = cal.getTime();

                                manager.insertGoal(newGoal.withSteps(GoalInputActivity.dateD,endTime,GoalInputActivity.nbSteps));
                            }
                            else {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(GoalInputActivity.dateD);
                                cal.add(Calendar.DAY_OF_WEEK, 1);
                                Date endTime = cal.getTime();

                                manager.insertGoal(newGoal.withSteps(GoalInputActivity.dateD,endTime,GoalInputActivity.nbSteps));
                            }
                        }

                        InactivityGoalInput.dateDeb = GoalInputActivity.dateD;
                        GoalInputActivity.dateD = null;
                        GoalInputActivity.nbMin = null;
                        GoalInputActivity.nbHours = null;
                        GoalInputActivity.nbSteps = null;
                        Intent intent = new Intent(getActivity(), InactivityGoalInput.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        GoalInputActivity.dateD = null;
                        GoalInputActivity.nbMin = null;
                        GoalInputActivity.nbHours = null;
                        GoalInputActivity.nbSteps = null;
                    }
                });
        tv1.setText(fullmessage);
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
