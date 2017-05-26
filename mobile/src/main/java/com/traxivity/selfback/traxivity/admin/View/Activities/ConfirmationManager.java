package com.traxivity.selfback.traxivity.admin.View.Activities;

import android.app.DialogFragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.traxivity.selfback.traxivity.R;
import com.traxivity.selfback.traxivity.admin.Controller.ConfirmationDAO;
import com.traxivity.selfback.traxivity.admin.Model.Confirmation;
import com.traxivity.selfback.traxivity.admin.View.ConfirmationAdapter;
import com.traxivity.selfback.traxivity.admin.View.Dialogs.elementDeleteDialog;
import com.traxivity.selfback.traxivity.admin.View.Dialogs.newConfirmationDialog;
import com.traxivity.selfback.traxivity.database.activity.ActivityManager;

import java.util.ArrayList;

public class ConfirmationManager extends AppCompatActivity {

    private static final String TAG = MessagesManager.class.getSimpleName();

    public static Context context;

    private TaskCompletionSource<ArrayList<Confirmation>> getConfirmationTask;
    private Task getConfirmationTaskWaiter;

    protected ArrayList<Confirmation> confirmationList;
    protected Confirmation confirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_manager);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        context = this;

        confirmationList = new ArrayList<>();
        confirmation = new Confirmation("","");

        Button bt_addConf = (Button)findViewById(R.id.bt_addConf);
        Button bt_searchConf = (Button)findViewById(R.id.bt_searchConf);
        final ListView lv_conf = (ListView)findViewById(R.id.lv_confirmations);
        final ProgressBar pb_conf = (ProgressBar)findViewById(R.id.pb_conf);
        pb_conf.setVisibility(View.GONE);
        final RadioGroup rg_confType = (RadioGroup)findViewById(R.id.rg_confType);
        final TextView tv_noneConf = (TextView) findViewById(R.id.tv_noneConf);
        final TextView tv_listConf = (TextView) findViewById(R.id.tv_listConf);
        tv_noneConf.setVisibility(View.GONE);
        tv_listConf.setVisibility(View.GONE);


        final ConfirmationAdapter adapter = new ConfirmationAdapter(this, confirmationList);
        lv_conf.setAdapter(adapter);

        bt_addConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newConfirmation = new newConfirmationDialog();
                newConfirmation.show(getFragmentManager(),TAG);
            }
        });

        bt_searchConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getConfirmationTask = new TaskCompletionSource<>();
                getConfirmationTaskWaiter = getConfirmationTask.getTask();

                getConfirmationTaskWaiter.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            tv_listConf.setVisibility(View.VISIBLE);
                            Log.d("task", "success");
                            confirmationList.clear();
                            confirmationList.addAll((ArrayList<Confirmation>)task.getResult());

                            if(confirmationList.size() == 0){
                                tv_noneConf.setVisibility(View.VISIBLE);
                                tv_noneConf.setText("None");
                            }
                            else
                                tv_noneConf.setVisibility(View.GONE);
                            pb_conf.setIndeterminate(false);
                            pb_conf.setVisibility(View.GONE);
                            Toast.makeText(context, Integer.toString(confirmationList.size()) + " confirmations found", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                        }
                        else{
                            Exception e = task.getException();
                            System.out.println(e.getMessage());
                        }
                    }
                });

                //TODO : POURQUOI LES ATTRIBUTS DES INSTANCES RECUPEREES SONT VIDES

                int rb_index = rg_confType.getCheckedRadioButtonId();
                String type = "";
                if(rb_index == R.id.rb_activityConf){
                    type = "Activity";
                }else{
                    if(rb_index == R.id.rb_stepsConf)
                        type = "Steps";
                    else
                    if(rb_index == R.id.rb_inactivityConf)
                        type = "Inactivity";
                }

                pb_conf.setVisibility(View.VISIBLE);
                pb_conf.setIndeterminate(true);
                ConfirmationDAO.getInstance().getConfirmation(type, getConfirmationTask);
            }
        });

        lv_conf.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Confirmation confClicked = adapter.getItem(position);

                DialogFragment confDelete = elementDeleteDialog.newInstance(confClicked);
                confDelete.show(getFragmentManager(),TAG);

                return true;
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
