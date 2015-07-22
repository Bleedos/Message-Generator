package com.shebdev.sclermont.myfirstapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
        Intent intent = getIntent();
        ArrayList<String> message = intent.getStringArrayListExtra(MyActivity.EXTRA_MESSAGE);

        GregorianCalendar greg = new GregorianCalendar();
        StringBuilder sb = new StringBuilder();

        String[] dayOWeek = {"dimanche","lundi","mardi","mercredi","jeudi","vendredi","samedi"};
        String[] month = {"janvier", "février", "mars", "avril", "mai", "juin", "juillet", "août"};

        sb.append("Bonjour, ici " + message.get(0));
        sb.append(", aujourd'hui " + dayOWeek[greg.get(GregorianCalendar.DAY_OF_WEEK)-1] );
        sb.append(" le " + (greg.get(GregorianCalendar.DAY_OF_MONTH)));
        sb.append(" " + month[greg.get(GregorianCalendar.MONTH)]);
        sb.append(" " + message.get(1));

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
        // automatically handle clicks on the Home/Up button, so long
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

        Button btn = (Button) findViewById(R.id.bouton_jouer);
        btn.setEnabled(true);
    }

    private void convertTextToSpeech(String text) {

        MyApplication myapp = (MyApplication) getApplication();
        myapp.bus.post(text);

    }



    public void arreterMessage(View view) {
        Button btnArreter = (Button) findViewById(R.id.bouton_arreter);
        btnArreter.setEnabled(false);
        Button btnJouer = (Button) findViewById(R.id.bouton_jouer);
        btnJouer.setEnabled(true);
        MyApplication myapp = (MyApplication) getApplication();
        myapp.bus.post("STOP");
    }

    public void jouerMessage(View view) {
        Button btnArreter = (Button) findViewById(R.id.bouton_arreter);
        btnArreter.setEnabled(true);
        Button btnJouer = (Button) findViewById(R.id.bouton_jouer);
        btnJouer.setEnabled(false);
        convertTextToSpeech(text);
    }
}
