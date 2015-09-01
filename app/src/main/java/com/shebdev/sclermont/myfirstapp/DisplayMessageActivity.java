package com.shebdev.sclermont.myfirstapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shebdev.sclermont.myfirstapp.db.MessageContract;
import com.shebdev.sclermont.myfirstapp.db.MessageDbHelper;

import java.util.ArrayList;
import java.util.GregorianCalendar;


public class DisplayMessageActivity extends ActionBarActivity {


    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            // Get the message from the intent
        StringBuilder sb = new StringBuilder();
        Intent intent = getIntent();
        ArrayList<String> message = intent.getStringArrayListExtra(MyActivity.EXTRA_MESSAGE);
        boolean addDateToMessage = intent.getBooleanExtra(MyActivity.EXTRA_ADD_DATE, false);

        sb.append(message.get(0) + " " + message.get(1));

        if (addDateToMessage) {
            GregorianCalendar greg = new GregorianCalendar();

            String[] dayOWeek = {"dimanche", "lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi"};
            String[] month = {"janvier", "février", "mars", "avril", "mai", "juin", "juillet", "août", "septembre", "octobre", "novembre","décembre"};

            sb.append(", aujourd'hui " + dayOWeek[greg.get(GregorianCalendar.DAY_OF_WEEK) - 1]);
            sb.append(" le " + (greg.get(GregorianCalendar.DAY_OF_MONTH)));
            sb.append(" " + month[greg.get(GregorianCalendar.MONTH)]);
        }

        sb.append(" " + message.get(2));

        text = sb.toString();

       // Set the text view as the activity layout
        setContentView(R.layout.activity_display_message);

        //Toast.makeText(this, "Message généré", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onPause() {
        super.onPause();

        MyApplication myapp = (MyApplication) getApplication();
        myapp.bus.post("STOP");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up ImageView, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onResume() {
        super.onResume();

        TextView txtContenuMsg = (TextView) findViewById(R.id.contenu_message);
        txtContenuMsg.setText(text);

        ImageView btn = (ImageView) findViewById(R.id.button_play);
        btn.setEnabled(true);
    }

    private void convertTextToSpeech(String text) {

        MyApplication myapp = (MyApplication) getApplication();
        myapp.bus.post(text);

    }



    public void arreterMessage(View view) {
        ImageView btnArreter = (ImageView) findViewById(R.id.button_stop);
        btnArreter.setEnabled(false);
        btnArreter.setAlpha(0.5f);
        ImageView btnJouer = (ImageView) findViewById(R.id.button_play);
        btnJouer.setEnabled(true);
        btnJouer.setAlpha(1f);
        MyApplication myapp = (MyApplication) getApplication();
        myapp.bus.post("STOP");
    }

    public void jouerMessage(View view) {
        ImageView btnArreter = (ImageView) findViewById(R.id.button_stop);
        btnArreter.setEnabled(true);
        btnArreter.setAlpha(1f);
        ImageView btnJouer = (ImageView) findViewById(R.id.button_play);
        btnJouer.setEnabled(false);
        btnJouer.setAlpha(0.5f);
        convertTextToSpeech(text);
    }
}
