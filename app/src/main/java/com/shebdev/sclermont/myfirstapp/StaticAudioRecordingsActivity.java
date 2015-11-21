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

        setBaseColor(findViewById(R.id.btn_record_name), "name.3gp", 0xFF00AA00, 0xFF5A595B);
        setBaseColor(findViewById(R.id.btn_record_today), "today.3gp", 0xFF00AA00, 0xFF5A595B);
        setBaseColor(findViewById(R.id.btn_record_french_le), "le.3gp", 0xFF00AA00, 0xFF5A595B);


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
                android.R.layout.simple_list_item_1, daysOfWeek, "day_of_week_");
        dayOfWeekGridView.setAdapter(daysOfWeekAdapter);

        dayOfWeekGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if (v.isActivated()) {
                    v.setBackgroundColor(0xFF00AA00);
                    v.setActivated(false);
                    for (int i = 0; i < 7; i++) {
                        if (i != position) {
                            parent.getChildAt(i).setEnabled(true);
                            parent.getChildAt(i).setClickable(false);
                        }
                    }
                    stopRecording();
                } else {
                    v.setBackgroundColor(0xFFFF0000);
                    v.setActivated(true);
                    for (int i = 0; i < 7; i++) {
                        if (i != position) {
                            parent.getChildAt(i).setEnabled(false);
                            parent.getChildAt(i).setClickable(true);
                        }
                    }
                    startRecording("day_of_week_" + position + ".3gp");
                }
            }
        });

        String[] monthDays = new String[31];

        for (int i = 0; i < 31; i++) {
            monthDays[i] = String.valueOf(i+1);
        }

        AudioFileCheckArrayAdapter<String> daysAdapter = new AudioFileCheckArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, monthDays, "day_");

        dayOfMonthGridView.setAdapter(daysAdapter);

        dayOfMonthGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if (v.isActivated()) {
                    v.setBackgroundColor(0xFF00AA00);
                    v.setActivated(false);
                    for (int i = 0; i < 31; i++) {
                        if (i != position) {
                            parent.getChildAt(i).setEnabled(true);
                            parent.getChildAt(i).setClickable(false);
                        }
                    }
                    stopRecording();
                } else {
                    v.setBackgroundColor(0xFFFF0000);
                    v.setActivated(true);
                    for (int i = 0; i < 31; i++) {
                        if (i != position) {
                            parent.getChildAt(i).setEnabled(false);
                            parent.getChildAt(i).setClickable(true);
                        }
                    }
                    startRecording("day_" + (position + 1) + ".3gp");
                }
            }
        });

        String[] months = new String[12];
        for (int j = 0; j < 12; j++) {
            greg.set(GregorianCalendar.MONTH, j);
            months[j] = sdfMonths.format(greg.getTime());
        }

        AudioFileCheckArrayAdapter<String> monthsAdapter = new AudioFileCheckArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, months, "month_");

        monthGridView.setAdapter(monthsAdapter);

        monthGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if (v.isActivated()) {
                    v.setBackgroundColor(0xFF00AA00);
                    v.setActivated(false);
                    for (int i = 0; i < 12; i++) {
                        if (i != position) {
                            parent.getChildAt(i).setEnabled(true);
                            parent.getChildAt(i).setClickable(false);
                        }
                    }
                    stopRecording();
                } else {
                    v.setBackgroundColor(0xFFFF0000);
                    v.setActivated(true);
                    for (int i = 0; i < 12; i++) {
                        if (i != position) {
                            parent.getChildAt(i).setEnabled(false);
                            parent.getChildAt(i).setClickable(true);
                        }
                    }
                    startRecording("month_" + position + ".3gp");
                }
            }
        });
    }

//    @Override
//    public void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        // TODO: voir à peut-être modifier setBaseColor pour différencier cette view... aller en
//        // debug pour voir quelle view on passe
//        dayOfWeekGridView = (GridView) findViewById(R.id.dayOfWeekGridView);
//        for (int h = 0; h < 7; h++) {
//            setBaseColor(dayOfWeekGridView.getChildAt(h), "day_of_week_" + h + ".3gp", 0xFF00AA00, 0xFF5A595B);
//        }
//    }

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
            startRecording("name.3gp");
        }
        toggleButtonColor(view);
    }

    public void recordTodayVoiceSegment(View view) {
        if (view.isActivated()) {
            stopRecording();
        }
        else {
            startRecording("today.3gp");
        }
        toggleButtonColor(view);
    }

    public void recordLeVoiceSegment(View view) {
        if (view.isActivated()) {
            stopRecording();
        }
        else {
            startRecording("le.3gp");
        }
        toggleButtonColor(view);
    }

    // TODO: Centraliser
    private void toggleButtonColor(View view) {
        if (view.isActivated()) {
            view.getBackground().setColorFilter(0xFF5A595B, PorterDuff.Mode.MULTIPLY);
            view.setActivated(false);
        }
        else {
            view.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
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
