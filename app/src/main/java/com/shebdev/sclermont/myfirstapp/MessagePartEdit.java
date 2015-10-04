package com.shebdev.sclermont.myfirstapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;


public class MessagePartEdit extends ActionBarActivity implements MediaPlayer.OnCompletionListener {

    private int mPosition;
    private int playerFileIterator;

    private MediaRecorder mRecorder = null;
    private MediaPlayer   mPlayer = null;
    private static String mFileName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message_part_edit);

        Intent intent = getIntent();
        String messagePart = intent.getStringExtra(MyActivity.EXTRA_MESSAGE_PART);
        mPosition = intent.getIntExtra(MyActivity.EXTRA_MESSAGE_PART_POSITION, -1);

        if (messagePart != null) {
            EditText messagePartEdit = (EditText) findViewById(R.id.message_part_edit);
            messagePartEdit.setText(messagePart);
        }

        mFileName = intent.getStringExtra(MyActivity.EXTRA_MESSAGE_PART_AUDIO_FILE);
        if (mFileName == null || mFileName.length() == 0) {
            String assemblyId = intent.getStringExtra(MyActivity.EXTRA_MESSAGE_PART_ASSEMBLY_ID);
            String messagePartOrder = intent.getStringExtra(MyActivity.EXTRA_MESSAGE_PART_ORDER);
            mFileName = (assemblyId==null?0:assemblyId) + "_" +
                    (messagePartOrder==null?0:messagePartOrder) +
                    "_" + System.currentTimeMillis() + ".3gp";
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message_part_edit, menu);
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

    public void annulerMessageEdit(View view) {
        finish();
    }

    public void okMessageEdit(View view) {
        EditText messagePart = (EditText) findViewById(R.id.message_part_edit);
        Intent returnIntent = new Intent();
        returnIntent.putExtra(MyActivity.EXTRA_MESSAGE_PART, messagePart.getText().toString());
        returnIntent.putExtra(MyActivity.EXTRA_MESSAGE_PART_POSITION, mPosition);
        if (new File(getFilesDir() + "/" + mFileName).exists()) {
            returnIntent.putExtra(MyActivity.EXTRA_MESSAGE_PART_AUDIO_FILE, mFileName);
        }
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //mPlayer.stop();

        if (playerFileIterator < 4) {
            // TODO: Voir à bien positionner le repertoire de travail
            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
            String mFileNameEnd;

            try {
                // TODO: Marche pas.  voir si ca pourrait faire la job
                // http://developer.android.com/reference/android/media/SoundPool.html
                mFileNameEnd = mFileName + "/" + playerFileIterator++ + ".3gp";
                mp.reset();
                mp.setDataSource(mFileNameEnd);
                mp.prepare();
                mp.start();
            } catch (IOException e) {
                Log.e("TATATAT", "prepare() failed");
            }
        }
        else {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void startRecording(View view) {

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(getFilesDir().getPath() + "/" + mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("TAGGGG", "prepare() failed");
        }

        mRecorder.start();
    }

    public void stopRecording(View view) {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }



}
