package com.shebdev.sclermont.myfirstapp;

import android.graphics.PorterDuff;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.shebdev.sclermont.myfirstapp.adapter.AudioFileCheckArrayAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class StaticAudioRecordingsActivity extends AppCompatActivity {

    GridView dayOfWeekGridView;
    GridView dayOfMonthGridView;
    GridView monthGridView;
    private MediaRecorder mRecorder = null;
    private static final SimpleDateFormat sdfMonths = new SimpleDateFormat("MMMM", Locale.CANADA_FRENCH);
    private static final SimpleDateFormat sdfDaysOfWeek = new SimpleDateFormat("EEEE", Locale.CANADA_FRENCH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_audio_recordings);

        setBaseColor(findViewById(R.id.btn_record_name), MyApplication.NAME_FILE_NAME + MyApplication.AUDIO_FILE_EXTENSION, MyApplication.FILE_EXIST_BACKGROUND_COLOR, MyApplication.FILE_NOT_EXIST_BUTTON_BACKGROUND_COLOR);
        setBaseColor(findViewById(R.id.btn_record_today), MyApplication.TODAY_FILE_NAME + MyApplication.AUDIO_FILE_EXTENSION, MyApplication.FILE_EXIST_BACKGROUND_COLOR, MyApplication.FILE_NOT_EXIST_BUTTON_BACKGROUND_COLOR);
        setBaseColor(findViewById(R.id.btn_record_french_le), MyApplication.FRENCH_LE_FILE_NAME + MyApplication.AUDIO_FILE_EXTENSION, MyApplication.FILE_EXIST_BACKGROUND_COLOR, MyApplication.FILE_NOT_EXIST_BUTTON_BACKGROUND_COLOR);


        dayOfWeekGridView = (GridView) findViewById(R.id.dayOfWeekGridView);
        dayOfMonthGridView = (GridView) findViewById(R.id.dayOfMonthGridView);
        monthGridView = (GridView) findViewById(R.id.monthGridView);

        String[] daysOfWeek = new String[7];
        GregorianCalendar greg = new GregorianCalendar();
        for (int h = 0; h < 7; h++) {
            greg.set(GregorianCalendar.DAY_OF_WEEK, h);
            daysOfWeek[h] = sdfDaysOfWeek.format(greg.getTime());
        }

        AudioFileCheckArrayAdapter<String> daysOfWeekAdapter = new AudioFileCheckArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, daysOfWeek, MyApplication.DAY_OF_WEEK_FILE_NAME_START);
        dayOfWeekGridView.setAdapter(daysOfWeekAdapter);

        dayOfWeekGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if (v.isActivated()) {
                    v.setBackgroundColor(MyApplication.FILE_EXIST_BACKGROUND_COLOR);
                    v.setActivated(false);
                    for (int i = 0; i < 7; i++) {
                        if (i != position) {
                            parent.getChildAt(i).setEnabled(true);
                            parent.getChildAt(i).setClickable(false);
                        }
                    }
                    stopRecording();
                } else {
                    v.setBackgroundColor(MyApplication.RECORDING_BACKGROUND_COLOR);
                    v.setActivated(true);
                    for (int i = 0; i < 7; i++) {
                        if (i != position) {
                            parent.getChildAt(i).setEnabled(false);
                            parent.getChildAt(i).setClickable(true);
                        }
                    }
                    startRecording(MyApplication.DAY_OF_WEEK_FILE_NAME_START + position + MyApplication.AUDIO_FILE_EXTENSION);
                }
            }
        });

        String[] monthDays = new String[31];

        for (int i = 0; i < 31; i++) {
            monthDays[i] = String.valueOf(i+1);
        }

        AudioFileCheckArrayAdapter<String> daysAdapter = new AudioFileCheckArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, monthDays, MyApplication.DAY_OF_MONTH_FILE_NAME_START);

        dayOfMonthGridView.setAdapter(daysAdapter);

        dayOfMonthGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if (v.isActivated()) {
                    v.setBackgroundColor(MyApplication.FILE_EXIST_BACKGROUND_COLOR);
                    v.setActivated(false);
                    for (int i = 0; i < 31; i++) {
                        if (i != position) {
                            parent.getChildAt(i).setEnabled(true);
                            parent.getChildAt(i).setClickable(false);
                        }
                    }
                    stopRecording();
                } else {
                    v.setBackgroundColor(MyApplication.RECORDING_BACKGROUND_COLOR);
                    v.setActivated(true);
                    for (int i = 0; i < 31; i++) {
                        if (i != position) {
                            parent.getChildAt(i).setEnabled(false);
                            parent.getChildAt(i).setClickable(true);
                        }
                    }
                    startRecording(MyApplication.DAY_OF_MONTH_FILE_NAME_START + (position + 1) + MyApplication.AUDIO_FILE_EXTENSION);
                }
            }
        });

        String[] months = new String[12];
        for (int j = 0; j < 12; j++) {
            greg.set(GregorianCalendar.MONTH, j);
            months[j] = sdfMonths.format(greg.getTime());
        }

        AudioFileCheckArrayAdapter<String> monthsAdapter = new AudioFileCheckArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, months, MyApplication.MONTH_FILE_NAME_START);

        monthGridView.setAdapter(monthsAdapter);

        monthGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if (v.isActivated()) {
                    v.setBackgroundColor(MyApplication.FILE_EXIST_BACKGROUND_COLOR);
                    v.setActivated(false);
                    for (int i = 0; i < 12; i++) {
                        if (i != position) {
                            parent.getChildAt(i).setEnabled(true);
                            parent.getChildAt(i).setClickable(false);
                        }
                    }
                    stopRecording();
                } else {
                    v.setBackgroundColor(MyApplication.RECORDING_BACKGROUND_COLOR);
                    v.setActivated(true);
                    for (int i = 0; i < 12; i++) {
                        if (i != position) {
                            parent.getChildAt(i).setEnabled(false);
                            parent.getChildAt(i).setClickable(true);
                        }
                    }
                    startRecording(MyApplication.MONTH_FILE_NAME_START + position + MyApplication.AUDIO_FILE_EXTENSION);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_static_audio_recordings, menu);
        return true;
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
        super.onPause();

        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }

    public void recordNameVoiceSegment(View view) {
        if (view.isActivated()) {
            stopRecording();
        }
        else {
            startRecording(MyApplication.NAME_FILE_NAME + MyApplication.AUDIO_FILE_EXTENSION);
        }
        toggleButtonColor(view);
    }

    public void recordTodayVoiceSegment(View view) {
        if (view.isActivated()) {
            stopRecording();
        }
        else {
            startRecording(MyApplication.TODAY_FILE_NAME + MyApplication.AUDIO_FILE_EXTENSION);
        }
        toggleButtonColor(view);
    }

    public void recordLeVoiceSegment(View view) {
        if (view.isActivated()) {
            stopRecording();
        }
        else {
            startRecording(MyApplication.FRENCH_LE_FILE_NAME + MyApplication.AUDIO_FILE_EXTENSION);
        }
        toggleButtonColor(view);
    }

    // TODO: Centraliser
    private void toggleButtonColor(View view) {
        if (view.isActivated()) {
            view.getBackground().setColorFilter(MyApplication.FILE_EXIST_BACKGROUND_COLOR, PorterDuff.Mode.MULTIPLY);
            view.setActivated(false);
        }
        else {
            view.getBackground().setColorFilter(MyApplication.RECORDING_BACKGROUND_COLOR, PorterDuff.Mode.MULTIPLY);
            view.setActivated(true);
        }
    }

    // TODO: Centraliser et voir dans simplegridviewadapter
    private void setBaseColor(View view, String audioFileName, int baseColorExists, int baseColorDoesntExist) {
        File audioFile = new File(getFilesDir() + "/" + audioFileName);
        if (audioFile.exists()) {
            view.getBackground().setColorFilter(baseColorExists, PorterDuff.Mode.MULTIPLY);
        }
        else {
            view.getBackground().setColorFilter(baseColorDoesntExist, PorterDuff.Mode.MULTIPLY);
        }
    }

    private void startRecording(String fileName) {

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(getFilesDir().getPath() + "/" + fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("TAGGGG", "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }
}
