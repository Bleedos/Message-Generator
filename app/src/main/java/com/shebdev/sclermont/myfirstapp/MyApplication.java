package com.shebdev.sclermont.myfirstapp;

import android.app.Application;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by sclermont on 12/07/15.
 */
public class MyApplication extends Application {

    public static Bus bus = new Bus(ThreadEnforcer.MAIN);
    TextToSpeech tts;

    public static float RECYCLER_VIEW_TEXT_SIZE = 20.0f;
    public static int EVEN_BACKGROUND_COLOR = 0xFF212121;
    public static int ODD_BACKGROUND_COLOR = 0xFF424242;
    public static int FILE_EXIST_BACKGROUND_COLOR = 0xFF00AA00;
    public static int FILE_NOT_EXIST_GRID_BACKGROUND_COLOR = 0xFF303030;
    public static int FILE_NOT_EXIST_BUTTON_BACKGROUND_COLOR = 0xFF5A595B;
    public static int RECORDING_BACKGROUND_COLOR = 0xFFFF0000;

    public static String DAY_OF_WEEK_FILE_NAME_START = "day_of_week_";
    public static String DAY_OF_MONTH_FILE_NAME_START = "day_";
    public static String MONTH_FILE_NAME_START = "month_";
    public static String NAME_FILE_NAME = "name";
    public static String TODAY_FILE_NAME = "today";
    public static String FRENCH_LE_FILE_NAME = "le";
    public static String AUDIO_FILE_EXTENSION = ".3gp";

    @Override
    public void onCreate() {
        super.onCreate();

        tts=new TextToSpeech(MyApplication.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.CANADA_FRENCH);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        //Log.e("error", "This Language is not supported");
                    }
                    else{
                        tts.setPitch(0.85f);
                        //convertTextToSpeech(text);
                    }
                }
                else {
                    //Log.e("error", "Initilization Failed!");
                }
            }
        });

        bus.register(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        if(tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    @Subscribe
    public void getMessage(String text) {

        if ("STOP".equals(text)) {
            //Toast.makeText(this, "Message stop", Toast.LENGTH_LONG).show();
            if (tts != null && tts.isSpeaking()) {
                tts.stop();
            }
        }
        else {
            if (text == null || "".equals(text)) {
                text = "Content not available";
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                //Toast.makeText(this, "Message playback", Toast.LENGTH_LONG).show();
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        }
    }
}
