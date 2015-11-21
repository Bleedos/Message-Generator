package com.shebdev.sclermont.myfirstapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.io.File;

/**
 * Created by sclermont on 20/11/15.
 */
public class AudioFileCheckArrayAdapter<T> extends ArrayAdapter {

    private static final String FILE_EXT = ".3gp";

    private String audioFileNameStart;

    public AudioFileCheckArrayAdapter(Context context, int resource, T[] objects, String audioFileNameStart) {
        super(context, resource, objects);
        this.audioFileNameStart = audioFileNameStart;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        setBaseColor(view, audioFileNameStart + position + FILE_EXT,0xFF00AA00,0xFF303030);
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
