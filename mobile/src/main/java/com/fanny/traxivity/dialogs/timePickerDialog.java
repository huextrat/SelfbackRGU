package com.fanny.traxivity.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fanny.traxivity.R;

import java.util.Calendar;

/**
 * Created by extra on 30/05/2017.
 */

public class timePickerDialog extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView tv_date = (TextView)getActivity().findViewById(R.id.tv_time);

        tv_date.setText(Html.fromHtml("Start time : <font color='#000000'>" + Integer.toString(hourOfDay) + ":" + Integer.toString(minute)+ "</font>"));
    }
}
