package com.fanny.traxivity.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.fanny.traxivity.R;

import java.util.Calendar;

/**
 * Created by extra on 30/05/2017.
 */

public class datePickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        TextView tv_date = (TextView)getActivity().findViewById(R.id.tv_date);

        tv_date.setText(Html.fromHtml("Date : <font color='#000000'>" + Integer.toString(dayOfMonth) + "/" + Integer.toString(month) + "/" + Integer.toString(year) + "</font>"));
    }
}
