package com.shebdev.sclermont.myfirstapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.shebdev.sclermont.myfirstapp.db.MessageContract;
import com.shebdev.sclermont.myfirstapp.db.MessageDbHelper;

import java.util.ArrayList;


public class MyActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    public static final String PREFS_NAME = "MyPrefsFile";

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

        editNom.setText(settings.getString("editNom",""));
        editFinMsg.setText(settings.getString("editFinMsg", ""));

    }

    /** Called when the user clicks the Send button */
    public void genererMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        //EditText editAccueil = (EditText) findViewById(R.id.edit_accueil);
        EditText editNom = (EditText) findViewById(R.id.edit_nom);
        EditText editFinMsg = (EditText) findViewById(R.id.edit_fin_message);
        ArrayList<String> message = new ArrayList<String>();
        //message.add(editAccueil.getText().toString());
        message.add(editNom.getText().toString());
        message.add(editFinMsg.getText().toString());
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

        MessageDbHelper dbHelper = new MessageDbHelper(getBaseContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                MessageContract.MessagePart._ID,
                MessageContract.MessagePart.COLUMN_NAME_PART_ID,
                MessageContract.MessagePart.COLUMN_NAME_TXT
        };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                MessageContract.MessagePart.COLUMN_NAME_TXT + " DESC";

        Cursor c = db.query(
                MessageContract.MessagePart.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        c.moveToFirst();
        long itemId = c.getLong(
                c.getColumnIndexOrThrow(MessageContract.MessagePart._ID)
        );

        String txt = c.getString( c.getColumnIndexOrThrow(MessageContract.MessagePart.COLUMN_NAME_TXT));
    }

    public void chargerMessage(View view) {
        Intent intent = new Intent(this, MessageSelect.class);
        startActivityForResult(intent, 666); // TODO: Mettre une constante
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 666) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String message = data.getStringExtra("message");;
                EditText editFinMsg = (EditText) findViewById(R.id.edit_fin_message);
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("editFinMsg", message);

                // Commit the edits!
                editor.commit();
            }
        }
    }

    public void sauvegarderMessage(View view) {

        MessageDbHelper dbHelper = new MessageDbHelper(getBaseContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MessageContract.MessagePart.COLUMN_NAME_PART_ID, System.currentTimeMillis());


        EditText editFinMsg = (EditText) findViewById(R.id.edit_fin_message);
        values.put(MessageContract.MessagePart.COLUMN_NAME_TXT, editFinMsg.getText().toString());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                MessageContract.MessagePart.TABLE_NAME,
                null,
                values);
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
