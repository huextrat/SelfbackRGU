package com.traxivity.selfback.traxivity.view;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.traxivity.selfback.traxivity.MainActivity;
import com.traxivity.selfback.traxivity.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by jbjourget on 02/05/2017.
 */



public class GoalInputActivity extends AppCompatActivity {

    DateFormat df;
    RelativeLayout relative1;
    EditText stepsEtext;
    EditText hoursEtext;
    EditText minEtext;
    RadioButton rdbSteps;
    RadioButton rdbTime;
    Button confirm;
    Editable stepsEditable;
    Editable minEditable;
    Editable hoursEditable;
    /* Button dayButton;
     Button weekButton;*/
    TextView hintsText;
    TextInputLayout tipSteps;
    TextInputLayout tipHours;
    TextInputLayout tipMin;
    TextView textDate;
    Boolean launched = false;
    // public static Boolean isWeek = false;
    public static Integer nbSteps = null;
    public static Integer nbMin = null;
    public static Integer nbHours = null;
    public static Date dateD =null;
    public static final long NB_STEPS_FOR_DAY= 10000;
    public static final long NB_HOUR_FOR_DAY = 1;
    public static final long NB_MIN_FOR_DAY = 20;
   /* public static final long NB_STEPS_FOR_WEEK= NB_STEPS_FOR_DAY*7;
    public static final long NB_HOUR_FOR_WEEK = NB_HOUR_FOR_DAY*7+NB_MIN_FOR_DAY*7/60;
    public static final long NB_MIN_FOR_WEEK = NB_MIN_FOR_DAY*7%60; */

    public void refreshDate(){
        textDate.setText(df.format(dateD));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_input);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dateD = new Date();
        relative1 = (RelativeLayout) findViewById(R.id.relativeLayout);
        stepsEtext = (EditText) findViewById(R.id.editText);
        hoursEtext = (EditText) findViewById(R.id.editText5);
        minEtext = (EditText) findViewById(R.id.editText4);
        rdbSteps = (RadioButton) findViewById(R.id.radioButton5);
        rdbTime = (RadioButton) findViewById(R.id.radioButton4);
        confirm = (Button) findViewById(R.id.appCompatButton3);
       /* dayButton = (Button) findViewById(R.id.appCompatButton);
        weekButton = (Button) findViewById(R.id.appCompatButton2);*/
        hintsText = (TextView) findViewById(R.id.textView3);
        hintsText.setText("The average number of steps for a day is "+NB_STEPS_FOR_DAY);
        tipSteps = (TextInputLayout) findViewById(R.id.textInputSteps);
        tipHours = (TextInputLayout) findViewById(R.id.textInputHours);
        tipMin = (TextInputLayout) findViewById(R.id.textInputMin);
        textDate = (TextView) findViewById(R.id.textView10);
        launched = true;
        df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        rdbSteps.setChecked(true);
        relative1.setVisibility(View.INVISIBLE);

        textDate.setText(df.format(dateD));

        textDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DialogFragment newFragment = new CalendarInput();
                newFragment.show(fm, "ActivityAccept");
            }
        });

        /*
        dayButton.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                isWeek = false;
                dayButton.setEnabled(false);
                dayButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark,null));
                weekButton.setEnabled(true);
                weekButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary,null));
                if(rdbSteps.isChecked()){
                    hintsText.setText("The average number of steps for a day is "+NB_STEPS_FOR_DAY);

                }else{
                    hintsText.setText("The average duration of training for a day is "+NB_HOUR_FOR_DAY +" h "+NB_MIN_FOR_DAY+" min");

                }

            }
        });

        weekButton.setOnClickListener(new View.OnClickListener(){

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                isWeek = true;
                weekButton.setEnabled(false);
                weekButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark,null));
                dayButton.setEnabled(true);
                dayButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary,null));
                if(rdbSteps.isChecked()){
                    hintsText.setText("The average number of steps for a week is "+NB_STEPS_FOR_WEEK);
                }else{
                    hintsText.setText("The average duration of training for a week is "+NB_HOUR_FOR_WEEK +" h "+NB_MIN_FOR_WEEK+" min");
                }
            }
        });*/

        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    buttonConfirmClick(v);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onStepsTicked(View view){
        stepsEtext.setVisibility(View.VISIBLE);
        tipHours.setVisibility(View.INVISIBLE);
        tipMin.setVisibility(View.INVISIBLE);
        tipSteps.setVisibility(View.VISIBLE);
        rdbTime.setChecked(false);
        relative1.setVisibility(View.INVISIBLE);
       /* if(isWeek){
            hintsText.setText("The average number of steps for a week is "+NB_STEPS_FOR_WEEK);
        }else{*/
        hintsText.setText("The average number of steps for a day is "+NB_STEPS_FOR_DAY);
        //}

    }

    public void onTimeTicked(View view){
        relative1.setVisibility(View.VISIBLE);
        tipSteps.setVisibility(View.INVISIBLE);
        tipHours.setVisibility(View.VISIBLE);
        tipMin.setVisibility(View.VISIBLE);
        rdbSteps.setChecked(false);
        stepsEtext.setVisibility(View.INVISIBLE);
       /* if(isWeek){
            hintsText.setText("The average  duration of training for a week is "+NB_HOUR_FOR_WEEK+" h "+NB_MIN_FOR_WEEK+" min");
        }else{*/
        hintsText.setText("The average duration of training for a day is "+NB_HOUR_FOR_DAY +" h "+NB_MIN_FOR_DAY+" min");
        //}
    }



    public void buttonConfirmClick(View view) throws ParseException {
        if(rdbSteps.isChecked()){
            stepsEditable = stepsEtext.getText();



            if(!(String.valueOf(stepsEditable).equals(""))) {
                nbSteps = Integer.parseInt(String.valueOf(stepsEditable));
                android.app.FragmentManager fm = getFragmentManager();
                DialogFragment newFragment = new textDialogAccept();
                newFragment.show(fm, "ActivityAccept");
            }
        }
        else{
            minEditable = minEtext.getText();
            hoursEditable = hoursEtext.getText();
            if (!(String.valueOf(hoursEditable).equals("")) && !(String.valueOf(minEditable).equals(""))){
                nbMin = Integer.parseInt(String.valueOf(minEditable));
                nbHours = Integer.parseInt(String.valueOf(hoursEditable));
                android.app.FragmentManager fm = getFragmentManager();
                DialogFragment newFragment = new textDialogAccept();
                newFragment.show(fm, "ActivityAccept");
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

}