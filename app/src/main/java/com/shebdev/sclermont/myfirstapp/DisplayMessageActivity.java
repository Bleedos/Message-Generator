package com.shebdev.sclermont.myfirstapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;


public class DisplayMessageActivity extends ActionBarActivity {

    ArrayList<String> audioFileNameList;
    String text;
    LinkedList<MediaPlayer> mediaPlayerList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            // Get the message from the intent
        StringBuilder sb = new StringBuilder();
        Intent intent = getIntent();
        ArrayList<String> message = intent.getStringArrayListExtra(MyActivity.EXTRA_GENERATED_MESSAGE);
        audioFileNameList = intent.getStringArrayListExtra(MyActivity.EXTRA_AUDIO_FILE_NAME_LIST);

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

        stopVoiceRecordings(null);
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

    public void playVoiceRecordings(View view) {

        MediaPlayer mp;
        MediaPlayer firstPlayer = null;
        MediaPlayer previousPlayer = null;
        File audioFile;
        for (String audioFileName : audioFileNameList) {

            audioFile = new File(getFilesDir() + "/" + audioFileName);

            if (audioFile.exists()) {

                mp = new MediaPlayer();
                try {
                    mp.setDataSource(getFilesDir().getPath() + "/" + audioFileName);
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (previousPlayer == null) {
                    firstPlayer = mp;
                } else {
                    previousPlayer.setNextMediaPlayer(mp);
                }
                previousPlayer = mp;
                mediaPlayerList.add(mp);
            }
        }

        firstPlayer.start();
    }

    public void stopVoiceRecordings(View view) {
        MediaPlayer mp;
        if (mediaPlayerList.size() > 0) {
            while (mediaPlayerList.size() > 0) {
                mp = mediaPlayerList.getFirst();
                mp.release();
                mp = null;
                mediaPlayerList.remove();
            }
        }
    }
}
