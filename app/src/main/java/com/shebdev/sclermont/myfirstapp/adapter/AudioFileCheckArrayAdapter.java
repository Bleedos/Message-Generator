package com.shebdev.sclermont.myfirstapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.shebdev.sclermont.myfirstapp.MyApplication;

import java.io.File;

/**
 * Created by sclermont on 20/11/15.
 */
public class AudioFileCheckArrayAdapter<T> extends ArrayAdapter {

    private String audioFileNameStart;

    public AudioFileCheckArrayAdapter(Context context, int resource, T[] objects, String audioFileNameStart) {
        super(context, resource, objects);
        this.audioFileNameStart = audioFileNameStart;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        setBaseColor(view, audioFileNameStart + position + MyApplication.AUDIO_FILE_EXTENSION,
                MyApplication.FILE_EXIST_BACKGROUND_COLOR, MyApplication.FILE_NOT_EXIST_GRID_BACKGROUND_COLOR);
        return view;
    }

    private void setBaseColor(View view, String audioFileName, int baseColorExists, int baseColorDoesntExist) {
        File audioFile = new File(getContext().getFilesDir() + "/" + audioFileName);
        if (audioFile.exists()) {
            view.setBackgroundColor(baseColorExists);
        }
        else {
            view.setBackgroundColor(baseColorDoesntExist);
        }
    }
}
