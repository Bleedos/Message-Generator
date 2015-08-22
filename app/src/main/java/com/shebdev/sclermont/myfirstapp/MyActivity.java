package com.shebdev.sclermont.myfirstapp;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.shebdev.sclermont.myfirstapp.adapter.MessagePartRecyclerAdapter;
import com.shebdev.sclermont.myfirstapp.db.MessageAssemblyLinkData;
import com.shebdev.sclermont.myfirstapp.db.MessageContract;
import com.shebdev.sclermont.myfirstapp.db.MessageDbHelper;
import com.shebdev.sclermont.myfirstapp.db.MessagePartData;
import com.shebdev.sclermont.myfirstapp.dialog.EmptyMessagePartDialogFragment;
import com.shebdev.sclermont.myfirstapp.dialog.EmptyNameDialogFragment;

import java.util.ArrayList;
import java.util.List;


public class MyActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    public static final String PREFS_NAME = "MyPrefsFile";

    private RecyclerView mRecyclerView;
    private MessagePartRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);



        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.phone);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                openSearch();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        EditText editNom = (EditText) findViewById(R.id.edit_nom);
        EditText editFinMsg = (EditText) findViewById(R.id.edit_fin_message);

        editor.putString("editNom", editNom.getText().toString());
        editor.putString("editFinMsg", editFinMsg.getText().toString());

        // Commit the edits!
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        EditText editNom = (EditText) findViewById(R.id.edit_nom);
        EditText editFinMsg = (EditText) findViewById(R.id.edit_fin_message);

        editNom.setText(settings.getString("editNom", ""));
        editFinMsg.setText(settings.getString("editFinMsg", ""));
        long assemblyId = settings.getLong("assemblyId", -1l);
        assemblyId = 4l;  // TODO: Retirer et conserver la valeur chargee a partir des prefs

        //Toast.makeText(this, "AssId" + assemblyId, Toast.LENGTH_LONG).show();

        if (mAdapter == null) {

            //Toast.makeText(this, "adapter null", Toast.LENGTH_LONG).show();


            mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            if (assemblyId < 0) {

                ArrayList<StringBuilder> dset = new ArrayList<StringBuilder>();
                dset.add(new StringBuilder("1"));
                dset.add(new StringBuilder("2"));
                dset.add(new StringBuilder("3"));
                dset.add(new StringBuilder("4"));
                mAdapter = new MessagePartRecyclerAdapter(dset, -1l);
            }
            else {
                //Toast.makeText(this, "load existing assembly", Toast.LENGTH_LONG).show();
                ArrayList<StringBuilder> dset = loadAssemblyLinkData(assemblyId);
                mAdapter = new MessagePartRecyclerAdapter(dset, assemblyId);
            }

            mRecyclerView.setAdapter(mAdapter);
        }

    }

    /** Called when the user clicks the Send button */
    public void genererMessage(View view) {

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        //EditText editAccueil = (EditText) findViewById(R.id.edit_accueil);
        EditText editNom = (EditText) findViewById(R.id.edit_nom);
        EditText editFinMsg = (EditText) findViewById(R.id.edit_fin_message);

        if (editNom == null || editNom.length() == 0) {
            DialogFragment newFragment = new EmptyNameDialogFragment();
            newFragment.show(getFragmentManager(), "emptyname");
            return;
        }

        if (editFinMsg == null || editFinMsg.length() == 0) {
            DialogFragment newFragment = new EmptyMessagePartDialogFragment();
            newFragment.show(getFragmentManager(), "emptymessagepart");
            return;
        }

        ArrayList<StringBuilder> dataSet = mAdapter.getMDataset();
        StringBuilder sbd = new StringBuilder();

        ArrayList<String> message = new ArrayList<String>();
        //message.add(editAccueil.getText().toString());
        message.add(editNom.getText().toString());
        for (StringBuilder sb : dataSet) {
            sbd.append(sb.toString());
            sbd.append(" ");
        }
        message.add(sbd.toString());
        //message.add(editFinMsg.getText().toString());
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }

    public void chargerMessage(View view) {
        Intent intent = new Intent(this, MessageSelect.class);
        startActivityForResult(intent, 666); // TODO: Mettre une constante
    }

    public void chargerAssembly(View view) {
        Intent intent = new Intent(this, MessageAssemblyListActivity.class);
        startActivityForResult(intent, 444); // TODO: Mettre une constante
    }

    public void ajouterLigne(View view) {
        Intent intent = new Intent(this, MessagePartEdit.class);
        startActivityForResult(intent, 555);    // TODO: Mettre une constante
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 666) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String message = data.getStringExtra("message");
                EditText editFinMsg = (EditText) findViewById(R.id.edit_fin_message);
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("editFinMsg", message);

                // Commit the edits!
                editor.commit();
            }
        }
        else if (requestCode == 555) {
            if (resultCode == RESULT_OK) {
                String messagePart = data.getStringExtra("messagePart");
                mAdapter.addLine(new StringBuilder(messagePart));
                mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, mAdapter.getItemCount());
            }
        }
        else if (requestCode == 444) {
            long assemblyId = data.getLongExtra("assemblyId", -1l);
            Toast.makeText(getBaseContext(), "assid 444::"+assemblyId, Toast.LENGTH_LONG).show();
            if (assemblyId >= 0) {
                mAdapter.changeMDataSet(loadAssemblyLinkData(assemblyId));
            }

        }
        else {
            long assemblyId = data.getLongExtra("assemblyId", -1l);
            Toast.makeText(getBaseContext(), "assid inconnu::"+assemblyId, Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<StringBuilder> loadAssemblyLinkData(long assemblyId) {
        ArrayList<StringBuilder> dset = new ArrayList<StringBuilder>();
        MessageDbHelper dbHelper = new MessageDbHelper(getBaseContext());
        ArrayList<MessageAssemblyLinkData> dataSet = dbHelper.getAssemblyLinkData(assemblyId);

        for (MessageAssemblyLinkData mald : dataSet) {
            //Toast.makeText(this, "mpd::"+mald.getId(), Toast.LENGTH_LONG).show();
            MessagePartData mpd = dbHelper.getMessagePart(Long.parseLong(mald.getPartId()));

            dset.add(new StringBuilder(mpd.getText()));
        }

        return dset;
    }

    public void sauvegarderMessage(View view) {

        //EditText editFinMsg = (EditText) findViewById(R.id.edit_fin_message);
        MessageDbHelper dbHelper = new MessageDbHelper(getBaseContext());
        long mPartId;

        ArrayList<StringBuilder> dataSet = mAdapter.getMDataset();

        long ts = System.currentTimeMillis();
        long assemblyId = dbHelper.createPartAssembly("title "+ts, "desc "+ts);
        long po = 1l;

        for (StringBuilder sb : dataSet) {
            mPartId = dbHelper.createMessagePart(sb.toString());
            dbHelper.createPartAssemblyLink(assemblyId, mPartId, po++);
        }
    }

    public void savePrefs(View view) {
//        Intent intent = new Intent(this, DisplayMessageActivity.class);
//        //EditText editAccueil = (EditText) findViewById(R.id.edit_accueil);
//        EditText editNom = (EditText) findViewById(R.id.edit_nom);
//        EditText editFinMsg = (EditText) findViewById(R.id.edit_fin_message);
//        ArrayList<String> message = new ArrayList<String>();
//        //message.add(editAccueil.getText().toString());
//        message.add(editNom.getText().toString());
//        message.add(editFinMsg.getText().toString());


    }

    public void openSearch() {
        System.out.println("Open search");
    }

    public void openSettings() {
        System.out.println("Open settings");
    }

}
