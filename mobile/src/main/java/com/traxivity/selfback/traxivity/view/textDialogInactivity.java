package com.traxivity.selfback.traxivity.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.traxivity.selfback.traxivity.MainActivity;
import com.traxivity.selfback.traxivity.R;
import com.traxivity.selfback.traxivity.database.inactivity.DbInactivity;
import com.traxivity.selfback.traxivity.database.inactivity.InactivityManager;

/**
 * Created by jbjourget on 09/05/2017.
 */

public class textDialogInactivity extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.text_dialog, null);
        TextView tv1 = (TextView) view.findViewById(R.id.textView);
        String fullmessage= "You choosed to set your maximum inactivity to "+InactivityGoalInput.nbHours+" Hours and "+InactivityGoalInput.nbMin+" Min" ;

        builder.setView(view)
                .setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        InactivityManager managerInactivity = new InactivityManager();
                        DbInactivity myInactivity = new DbInactivity(InactivityGoalInput.dateDeb, InactivityGoalInput.nbHours, InactivityGoalInput.nbMin);
                        managerInactivity.insertInactivity(myInactivity);

                        InactivityGoalInput.dateDeb = null;
                        InactivityGoalInput.nbHours = null;
                        InactivityGoalInput.nbMin = null;
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        InactivityGoalInput.nbHours = null;
                        InactivityGoalInput.nbMin = null;
                    }
                });
        tv1.setText(fullmessage);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}