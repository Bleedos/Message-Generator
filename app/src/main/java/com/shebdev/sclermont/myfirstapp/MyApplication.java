package com.shebdev.sclermont.myfirstapp;

import android.app.Application;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import java.util.Locale;

/**
 * Created by sclermont on 12/07/15.
 */
public class MyApplication extends Application {

    public static Bus bus = new Bus(ThreadEnforcer.MAIN);
    TextToSpeech tts;

    @Override
    public void onCreate() {
        super.onCreate();

        tts=new TextToSpeech(MyApplication.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.CANADA_FRENCH);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        //Log.e("error", "This Language is not supported");
                    }
                    else{
                        tts.setPitch(0.7f);
                        //convertTextToSpeech(text);
                    }
                }
                else {
                    //Log.e("error", "Initilization Failed!");
                }
            }
        });

        //Toast.makeText(this, "App started", Toast.LENGTH_LONG).show();

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
