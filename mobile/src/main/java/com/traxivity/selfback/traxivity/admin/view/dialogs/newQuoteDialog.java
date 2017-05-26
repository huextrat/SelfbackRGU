package com.traxivity.selfback.traxivity.admin.view.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.traxivity.selfback.traxivity.R;
import com.traxivity.selfback.traxivity.admin.model.Quote;
import com.traxivity.selfback.traxivity.admin.view.activities.NewMessage;

/**
 * Created by Alexandre on 02/05/2017.
 */

public class newQuoteDialog extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.new_quote_dialog, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText et_qauthor = (EditText)view.findViewById(R.id.et_qauthor);
                        EditText et_qcontent = (EditText)view.findViewById(R.id.et_qcontent);
                        if(NewMessage.newMessage != null && !et_qauthor.getText().toString().equals("") && !et_qcontent.getText().toString().equals("")){
                            NewMessage.newMessage.setQuote(new Quote(et_qauthor.getText().toString(), et_qcontent.getText().toString()));
                            NewMessage.bt_removeQuote.setVisibility(View.VISIBLE);
                            if(NewMessage.tv_quote != null)
                                NewMessage.tv_quote.setText(Html.fromHtml(NewMessage.newMessage.getQuote().toString()));
                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        newQuoteDialog.this.getDialog().cancel();
                    }
                });

        builder.setTitle("Edit quote");

        return builder.create();
    }
}

