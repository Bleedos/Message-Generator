package com.shebdev.sclermont.myfirstapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;


public class MyActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
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
        editFinMsg.setText(settings.getString("editFinMsg",""));
    }

    /** Called when the user clicks the Send button */
    public void playMessage(View view) {
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
