package com.shebdev.sclermont.myfirstapp.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.shebdev.sclermont.myfirstapp.MyActivity;
import com.shebdev.sclermont.myfirstapp.R;

/**
 * Created by sclermont on 04/08/15.
 */
public class ErrorDialogFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = this.getArguments();
        String myValue = bundle.getString(MyActivity.ERROR_MESSAGE);

        builder.setIcon(R.drawable.ic_error_outline_white_24dp);
        builder.setTitle(getString(R.string.error_dialog_title));
        builder.setMessage(myValue)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
