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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Locale;


public class DisplayMessageActivity extends ActionBarActivity {

    ArrayList<String> audioFileNameList;
    String text;
    LinkedList<MediaPlayer> mediaPlayerList = new LinkedList<>();
    SimpleDateFormat sdfStart = new SimpleDateFormat("EEEE", Locale.CANADA_FRENCH);
    SimpleDateFormat sdfFinish = new SimpleDateFormat("d MMMM", Locale.CANADA_FRENCH);
    boolean addDateToMessage;
    MediaPlayer firstPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            // Get the message from the intent
        StringBuilder sb = new StringBuilder();
        Intent intent = getIntent();
        ArrayList<String> message = intent.getStringArrayListExtra(MyActivity.EXTRA_GENERATED_MESSAGE);
        audioFileNameList = intent.getStringArrayListExtra(MyActivity.EXTRA_AUDIO_FILE_NAME_LIST);

        addDateToMessage = intent.getBooleanExtra(MyActivity.EXTRA_ADD_DATE, false);

        sb.append(message.get(0) + " " + message.get(1));

        if (addDateToMessage) {
            GregorianCalendar greg = new GregorianCalendar();
            sb.append(", aujourd'hui ");
            sb.append(sdfStart.format(greg.getTime()));
            sb.append(" le ");
            sb.append(sdfFinish.format(greg.getTime()));
        }

        sb.append(", " + message.get(2));

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

        if (firstPlayer == null) {
            MediaPlayer mp;
            File audioFile;
            int insertPosition = 1;

            // TODO: utiliser la méthode privée pour insérer dans la liste et chainer les player?
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

                    if (firstPlayer == null) {
                        firstPlayer = mp;
                    }
                    mediaPlayerList.add(mp);
                }
            }

            insertPosition = insertAudioFileAt(insertPosition, MyApplication.NAME_FILE_NAME + MyApplication.AUDIO_FILE_EXTENSION);

            if (addDateToMessage) {
                GregorianCalendar greg = new GregorianCalendar();
                insertPosition = insertAudioFileAt(insertPosition, MyApplication.TODAY_FILE_NAME + MyApplication.AUDIO_FILE_EXTENSION);
                insertPosition = insertAudioFileAt(insertPosition, MyApplication.DAY_OF_WEEK_FILE_NAME_START + greg.get(GregorianCalendar.DAY_OF_WEEK) + MyApplication.AUDIO_FILE_EXTENSION);
                insertPosition = insertAudioFileAt(insertPosition, MyApplication.FRENCH_LE_FILE_NAME + MyApplication.AUDIO_FILE_EXTENSION);
                insertPosition = insertAudioFileAt(insertPosition, MyApplication.DAY_OF_MONTH_FILE_NAME_START + greg.get(GregorianCalendar.DAY_OF_MONTH) + MyApplication.AUDIO_FILE_EXTENSION);
                insertPosition = insertAudioFileAt(insertPosition, MyApplication.MONTH_FILE_NAME_START + greg.get(GregorianCalendar.MONTH) + MyApplication.AUDIO_FILE_EXTENSION);
            }

            MediaPlayer mpItem1 = null;
            MediaPlayer mpItem2 = null;
            for (int i = 0; i < mediaPlayerList.size(); i++) {
                mpItem1 = mediaPlayerList.get(i);
                if ((i + 1) < mediaPlayerList.size()) {
                    mpItem2 = mediaPlayerList.get(i + 1);
                }
                if (i == 0) {
                    firstPlayer.setNextMediaPlayer(mpItem2);
                } else {
                    mpItem1.setNextMediaPlayer(mpItem2);
                }
                mpItem2 = null;
            }

            firstPlayer.start();
        }
        else {
            stopVoiceRecordings(null);
        }
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
        firstPlayer = null;
    }

    private int insertAudioFileAt(int position, String audioFileName) {
        File audioFile = new File(getFilesDir() + "/" + audioFileName);
        if (audioFile.exists()) {
            MediaPlayer newMediaPlayer = new MediaPlayer();
            try {
                newMediaPlayer.setDataSource(getFilesDir().getPath() + "/" + audioFileName);
                newMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayerList.add(position, newMediaPlayer);
            // TODO: gerer ça en dehors d'ici ça pourrait être n'importe quoi le prochain index
            if (mediaPlayerList.size() > position) {
                position++;
            }

        }
        return position;
    }
}
