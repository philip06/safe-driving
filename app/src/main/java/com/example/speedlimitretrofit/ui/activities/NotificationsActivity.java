package com.example.speedlimitretrofit.ui.activities;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;

import com.example.speedlimitretrofit.R;

import java.util.Locale;

public class NotificationsActivity extends AppCompatActivity {

    private TextToSpeech mTTS;
    private TextView maxSpeedTextView;
    private TextView currentSpeedTextView;
    private boolean vibrationSwitch;
    private boolean audioSwitch;
    private SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(this);

    //private int delay;
    public void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("notifications error", "Language not supported");
                    }
                }
                else {
                    Log.e("notifications error", "Initialization failed");
                }
            }
        });
    }

    protected void vibrate() {
        vibrationSwitch = spref.getBoolean("Vibration_Preference", false);
        if (vibrationSwitch) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(750);
        }
    }

    protected void textToSpeech(String textWarning) {
        audioSwitch = spref.getBoolean("Audio_Preference",false);
        if (audioSwitch) {
            mTTS.speak(textWarning, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    // If the person goes over 9mph. It will vibrate and send a voice notification.
    protected void warning() {
        MainActivity data = new MainActivity();
        maxSpeedTextView = data.getTextViewMaxSpeed();
        currentSpeedTextView = data.getTextViewCurrentSpeed();
        int maxSpeed;
        int currentSpeed;
        try {
            maxSpeed = Integer.parseInt(maxSpeedTextView.getText().toString());
            currentSpeed = Integer.parseInt(currentSpeedTextView.getText().toString());
            if (currentSpeed >= maxSpeed + 10) {
                textToSpeech("Slow down");
                vibrate();
            }
        }
        catch (Exception e)
        {
            Log.e("notifications warning", "Error speed");
        }
    }
}
