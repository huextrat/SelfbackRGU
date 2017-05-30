package com.fanny.traxivity.admin.view.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fanny.traxivity.R;
import com.fanny.traxivity.admin.controller.ActivityConversionDAO;
import com.fanny.traxivity.admin.model.ActivityToSteps;

/**
 * Created by extra on 30/05/2017.
 */

public class NewConversion extends AppCompatActivity {
    private static final String TAG = MessagesManager.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_conversion);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        final EditText et_activityName = (EditText)findViewById(R.id.et_activityName);
        final EditText et_stepsPerMinute = (EditText)findViewById(R.id.et_stepsPerMinute);
        Button bt_addConversion = (Button)findViewById(R.id.bt_addConversion);

        bt_addConversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activityName = et_activityName.getText().toString();
                int stepsPerMinute = Integer.valueOf(et_stepsPerMinute.getText().toString());

                ActivityToSteps newConversion = new ActivityToSteps(activityName, stepsPerMinute);

                ActivityConversionDAO.getInstance().addActivityConversion(newConversion);
                Toast.makeText(MainMenu.context, "New activity conversion added", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }

    @Override
    public void finish(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        super.finish();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
