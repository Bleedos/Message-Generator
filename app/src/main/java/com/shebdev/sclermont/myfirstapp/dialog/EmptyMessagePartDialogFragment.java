package com.shebdev.sclermont.myfirstapp.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.shebdev.sclermont.myfirstapp.R;

/**
 * Created by sclermont on 04/08/15.
 */
public class EmptyMessagePartDialogFragment extends DialogFragment {

    // TODO: Avoir un seul dialog et setter la chain a afficher en fonction de l'erreur rencontree
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.empty_message_part)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
