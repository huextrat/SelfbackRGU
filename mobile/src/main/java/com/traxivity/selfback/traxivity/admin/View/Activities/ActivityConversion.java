package com.traxivity.selfback.traxivity.admin.View.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.traxivity.selfback.traxivity.R;
import com.traxivity.selfback.traxivity.admin.Controller.ActivityConversionDAO;
import com.traxivity.selfback.traxivity.admin.Model.ActivityToSteps;

public class ActivityConversion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);

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
}
