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
