package com.shebdev.sclermont.myfirstapp;

import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class StaticAudioRecordingsActivity extends AppCompatActivity {

    GridView daysOfWeekGridView;
    GridView daysGridView;
    GridView monthsGridView;
    private MediaRecorder mRecorder = null;
    private static final SimpleDateFormat sdfMonths = new SimpleDateFormat("MMMM", Locale.CANADA_FRENCH);
    private static final SimpleDateFormat sdfDaysOfWeek = new SimpleDateFormat("EEEE", Locale.CANADA_FRENCH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_audio_recordings);

        daysOfWeekGridView = (GridView) findViewById(R.id.gridView3);
        daysGridView = (GridView) findViewById(R.id.gridView);
        monthsGridView = (GridView) findViewById(R.id.gridView2);

        String[] daysOfWeek = new String[7];
        GregorianCalendar greg = new GregorianCalendar();
        for (int h = 0; h < 7; h++) {
            greg.set(GregorianCalendar.DAY_OF_WEEK, h);
            daysOfWeek[h] = sdfDaysOfWeek.format(greg.getTime());
        }

        ArrayAdapter<String> daysOfWeekAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, daysOfWeek);

        daysOfWeekGridView.setAdapter(daysOfWeekAdapter);

        daysOfWeekGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if (v.isActivated()) {
                    v.setBackgroundColor(0xFF303030);
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

        ArrayAdapter<String> daysAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, monthDays);

        daysGridView.setAdapter(daysAdapter);

        daysGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if (v.isActivated()) {
                    v.setBackgroundColor(0xFF303030);
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

        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, months);

        monthsGridView.setAdapter(monthsAdapter);

        monthsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if (v.isActivated()) {
                    v.setBackgroundColor(0xFF303030);
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

    }

    public void recordTodayVoiceSegment(View view) {

    }

    public void recordLeVoiceSegment(View view) {

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
