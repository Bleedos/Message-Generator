package com.shebdev.sclermont.myfirstapp;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class DisplayMessageActivity extends ActionBarActivity {

    TextToSpeech tts;
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



        // Create the text view
        /*TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(sb.toString());*/

        text = sb.toString();

       // Set the text view as the activity layout
        //setContentView(textView);
        setContentView(R.layout.activity_display_message);

        tts=new TextToSpeech(DisplayMessageActivity.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.FRANCE);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }
                    else{
                        tts.setPitch(0.6f);
                        //convertTextToSpeech(text);
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });


        Log.e("info", "ggnennenn   nenenenn!");

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
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if(tts != null) {

            tts.stop();
            tts.shutdown();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView txtContenuMsg = (TextView) findViewById(R.id.contenu_message);
        txtContenuMsg.setText(text);
    }

    private void convertTextToSpeech(String text) {
        if(text==null||"".equals(text)) {
            text = "Content not available";
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }

    }

    public void jouerMessage(View view) {
        convertTextToSpeech(text);
    }
}
